"""Read-only checks for the framework naming, grouping and suite inventory."""
from pathlib import Path
from collections import Counter
import re
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / 'src/test/java'
methods = []
ids = []
classes = {}
for path in SOURCE.rglob('*.java'):
    source = path.read_text()
    package = re.search(r'^package ([\w.]+);', source, re.M).group(1)
    assert package == package.lower(), f'Package must be lowercase: {package}'
    assert path.parent == SOURCE / package.replace('.', '/'), path
    classes[package + '.' + path.stem] = path
    if package in {'a_testbase', 'b_utilities', 'd_loginpage'}:
        continue
    if path.stem.endswith('TestCase'):
        assert re.fullmatch(r'[A-Z][A-Za-z]*[1-9][0-9]*TestCase', path.stem), path
        assert 'extends BaseClass' in source, path
    else:
        assert re.fullmatch(r'[A-Z][A-Za-z]*PageObjects[1-9][0-9]*', path.stem), path
        assert 'extends BasePage' in source, path
    if path.stem.endswith('TestCase'):
        case_count = len(re.findall(r'@Test\(', source))
        assert '@BeforeMethod' not in source, (path, 'navigation must be explicit in tests')
        if package in {'documenteditor', 'para'}:
            assert source.count('ReusableMethods.loginAndSearchFileClickEdit();') == case_count, path
            prefix, number = re.fullmatch(r'(.*?)([0-9]+)TestCase', path.stem).groups()
            assert source.count('new ' + prefix + 'PageObjects' + number + '(getDriver())') == case_count, path
    else:
        assert all(kind == 'xpath' for kind in re.findall(r'@FindBy\(\s*(\w+)\s*=', source)), path
        assert '.switchTo().frame(' not in source and '.switchTo().defaultContent(' not in source, path
    constants = dict(re.findall(r'private static final String (\w+) = "([^"]*)";', source))
    clean = re.sub(r'/\*.*?\*/', '', source, flags=re.S)
    for annotation, method in re.findall(r'@Test\((.*?)\)\s*public void (\w+)\(', clean, re.S):
        assert re.fullmatch(r'verify[A-Z][A-Za-z0-9]*', method), method
        assert f'logger.info(STARTING_TEST_MSG, "{method}")' in source, (method, 'missing start log')
        assert f'logger.info(FINISHED_TEST_MSG, "{method}")' in source, (method, 'missing finish log')

        assert re.search(r'enabled\s*=\s*true', annotation), (method, 'enabled=true must be explicit')
        assert not re.search(r'retryAnalyzer\s*=|\bpriority\s*=|enabled\s*=\s*false', annotation), method
        group_values = re.search(r'groups\s*=\s*\{(.*?)\}', annotation, re.S).group(1)
        groups = [constants.get(g.strip(), g.strip().strip('"')) for g in group_values.split(',')]
        module = re.fullmatch(r'([A-Z]\w*?)\d+TestCase', path.stem).group(1)
        assert len(groups) == 2 and groups[0] == module, (method, groups)
        assert re.fullmatch(r'P[1-4]-' + module, groups[1]), (method, groups)
        case_id = re.search(r'description\s*=\s*"(TC_\d+) \|', annotation).group(1)
        methods.append(method)
        ids.append(case_id)
catalog = (ROOT / 'docs/test-case-catalog.md').read_text().split('## Archived sample template')[0]
expected_ids = set(re.findall(r'^\| (TC_\d+) \|', catalog, re.M))
assert set(ids) == expected_ids, (set(ids) - expected_ids, expected_ids - set(ids))
assert len(set(methods)) == len(methods), Counter(methods)
assert len(set(ids)) == len(ids)
for suite in ROOT.glob('*.xml'):
    if suite.name == 'pom.xml':
        continue
    tree = ET.parse(suite)
    assert all(t.get('name') == 'RetryMultipleFailedTestcases' for t in tree.iter('test')), suite
    for node in tree.iter('class'):
        assert node.get('name') in classes, (suite, node.attrib)
        owner = classes[node.get('name')].read_text()
        owner_methods = {method for _, method in re.findall(
            r'@Test\((.*?)\)\s*public void (\w+)\(', owner, re.S)}
        for included in node.findall('methods/include'):
            assert included.get('name') in owner_methods, (suite, node.get('name'), included.attrib)
    for node in tree.findall('.//methods/include'):
        assert node.get('name') in methods, (suite, node.attrib)
for suite in ['testng.xml', 'Master.xml']:
    selected = {c.get('name') for c in ET.parse(ROOT / suite).iter('class')}
    expected_classes = {name for name in classes if name.endswith('TestCase')}
    assert selected == expected_classes, (suite, selected, expected_classes)
loggers = {node.get('name'): node.get('level') for node in ET.parse(ROOT / 'src/test/resources/log4j2.xml').iter('Logger')}
assert all(loggers.get(name) == 'debug' for name in ['documenteditor', 'para', 'portal', 'd_loginpage'])
print(f'PASS: naming, groups, suite references, {len(methods)} enabled catalog cases with start/finish logs, and DEBUG package configuration.')
