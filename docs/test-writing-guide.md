# Guide for writing automation test cases

Use this guide for every future request to write or extend Selenium, Java and TestNG automation tests in this hybrid framework. Apply the naming and grouping rules below to new code and retain the Page Object Model (POM), Log4j2 logging and verification practices in this guide.

Source: **Automation Framework: Naming and Grouping Standard**, dated **23 October 2026**, supplied as `Automation Framework Naming and Grouping Standard (1).pdf` (4 pages). Sections 1–5 of that standard are recorded below. Logging, frame handling, cleanup and SonarQube practices are supplementary guidance from the previously supplied code sample and repository; the PDF does not define those practices.

The PDF rules supersede the earlier naming/grouping recommendations in this guide, including `Xx_<Screen>TC`, separate mixed-case test/page packages, class-name/High groups and permission to commit `enabled = false`. The implementation has since been migrated to these rules; see `docs/framework-restructuring.md`.

## 1. Structure and responsibilities

| Component | Responsibility |
| --- | --- |
| Test class extending `BaseClass` | Describe the scenario, call page actions, assert expected results and log each step. |
| Page class extending `BasePage` | Declare locators, initialise through `super(driver)`, perform actions, wait for UI state and expose checks. |
| Reusable navigation | Log in, select the project, locate the file and open Edit. Run this once per test. |
| Configuration | Store changing URLs, credentials, test text and timeouts in `config.properties`; never commit credentials. |
| TestNG suite | Select the required tests and browser; retain the framework listeners. |
| Test case catalog | Record ID, name, automation method, objective and expected result in `docs/test-case-catalog.md`. |

Keep locators and WebDriver interaction in page objects. Build the page object locally for each test; do not share it or a driver across parallel tests. Use descriptive names such as `paragraphPage`, `clickTargetParagraph` and `isNewParagraphDisplayedInDocumentStructure`. Use correct spelling in new methods; do not copy `Dispalyed` from the sample.

### Package and class rules — PDF pages 1–2

- Package names are all lowercase, one per module/feature: `para`, `list`, `section`, `trackchange`, `undo_redo`, `documenteditor`. Use an underscore only to separate words.
- The shared framework packages in the standard are `a_testbase`, `b_utilities` and `d_loginpage`. Reuse them; do not create new framework packages.
- Test classes use `<Module><Number>TestCase`, extend `BaseClass`, and start the next number at about 50 tests: `Para1TestCase`, `Para2TestCase`, `DocumentEditor1TestCase`.
- Page objects use `<Module>PageObjects<Number>` and extend `BasePage`: `ParaPageObjects1`, `DocumentEditorPageObjects1`.
- Put the test class and page object in the same module package. New names such as `Ab_DocumentEditorTC`, `Ab_DocumentEditorPage`, `EditorPO`, `d_pageObjects` and `e_testCases` do not follow the standard.
- Keep login, project selection and file-list navigation in `HomePage` and `ReusableMethods`. Do not create another login page.

```text
src/test/java/
├── a_testbase/       BaseClass.java
├── b_utilities/      ReusableMethods.java, ExtentReportManager.java, RetryAnalyzer.java
├── d_loginpage/      BasePage.java, HomePage.java
├── para/             Para1TestCase.java, ParaPageObjects1.java
└── documenteditor/   DocumentEditor1TestCase.java, DocumentEditorPageObjects1.java
```

### Methods, fields and constants — PDF page 2

| Item | Required convention | Example |
| --- | --- | --- |
| Test method | Unique across the entire project; lowerCamelCase beginning with `verify` | `verifyClickingEnterMiddleOfPara` |
| Page action | Verb plus element | `clickOnFirstParaVe()`, `inputFileName(String name)` |
| Boolean page check | `is<Something>` | `isNewParaDisplayedInDs()` |
| Page value read | `get<Something>` | `getDocumentTitle()` |
| Locator field | lowerCamelCase with `@FindBy` | `firstPara`, `loginButton` |
| Constant | `private static final`, UPPER_SNAKE_CASE | `FRAME_0`, `STARTING_TEST_MSG` |

The test method name is the **Automation ID**; write it in the manual test case sheet. Any text used **three or more times** becomes a constant. Search the whole project before choosing a method name, not just its class.

### Exactly two groups — PDF page 3

Every `@Test` has exactly two groups: the module name in PascalCase and the manual-sheet priority mapped to that module.

| Manual sheet priority | Priority group | Document Editor example |
| --- | --- | --- |
| Highest / Critical | `P1-<Module>` | `P1-DocumentEditor` |
| High | `P2-<Module>` | `P2-DocumentEditor` |
| Medium | `P3-<Module>` | `P3-DocumentEditor` |
| Low | `P4-<Module>` | `P4-DocumentEditor` |

```java
@Test(enabled = true, groups = { "Para", "P2-Para" })
public void verifyClickingEnterMiddleOfPara() { /* scenario */ }
```

Other module groups include `List`, `Section` and `DocumentEditor`. Do not use a class name as the module group, bare `High`/`Medium` groups, extra smoke/regression groups, `retryAnalyzer` on the annotation, or TestNG `priority =`. The framework handles retry. **Do not commit tests with `enabled = false`.** Keep the existing report-compatible manual ID in `description`; it supplements the two required groups.

### Suite integration — PDF pages 3–4

In the standard project's `testng.xml`, add the module group includes and class **inside the existing** `<test name="RetryMultipleFailedTestcases">` block. Do not rename that block or create another one with the same name. Merge entries with the existing groups/classes and preserve listeners and parameters.

```xml
<test name="RetryMultipleFailedTestcases">
    <groups>
        <run>
            <!-- Document Editor -->
            <include name="DocumentEditor" />
            <include name="P1-DocumentEditor" />
            <include name="P2-DocumentEditor" />
            <include name="P3-DocumentEditor" />
            <include name="P4-DocumentEditor" />
        </run>
    </groups>
    <classes>
        <!-- Document Editor -->
        <class name="documenteditor.DocumentEditor1TestCase" />
    </classes>
</test>
```

To run only P1 cases, keep only the relevant `P1-<Module>` include lines for that run. TestNG group includes are alternatives: leaving `DocumentEditor` included also selects P2/P3/P4 cases. Preserve the normal suite configuration when preparing a temporary filtered run.

### Current repository implementation

The repository now uses `a_testbase.BaseClass`, `d_loginpage.BasePage`, and `d_loginpage.HomePage`. Shared login, project selection and file-list navigation live in `HomePage`; `ReusableMethods.loginAndSearchFileClickEdit()` opens the editor once per editor test.

Editor cases are in `documenteditor.DocumentEditor1TestCase` with `DocumentEditorPageObjects1` in the same package. Portal journey cases are in `portal.Portal1TestCase` and reuse the shared HomePage instead of adding another login page. Every test has its module group and mapped priority group. All 29 application cases are enabled.

`testng.xml` is the default suite with the `RetryMultipleFailedTestcases` block. `Master.xml` is an equivalent compatibility suite. Focused suites retain their existing command names and use standard class/group references. Sample starter cases are archived under `docs/templates` as noncompiled references.

The sample's broader paragraph scenario checks the Visual Editor (VE), Document Structure (DS), accepting a tracked change and Code View (CV). Existing TC_19 checks only that Enter increases the section's paragraph count by one, then undoes the split. Accept Change and Code View checks require their own confirmed locators and expected behaviour before implementation.

## 2. Design the case first

Before writing code, record:

1. Unique test ID, readable name and automation method name. The catalog currently ends at TC_29; allocate the next unused ID and check global method-name uniqueness. Choose the standard module package, numbered class pair and the two required groups from the manual-sheet priority.
2. Preconditions: project, document allocation, permissions, original text and tracked-change state.
3. Exact interaction: single click, double click, caret placement, Enter or toolbar action.
4. Observable expected result and where to verify it: VE, DS, right panel or CV.
5. Explicit wait condition, then a meaningful assertion with a failure message.
6. Cleanup that reverses only this test's changes. Do not save, reset the file or accept other users' changes as generic cleanup.

For “Enter in the middle of a paragraph,” a click alone does not establish the split position. Place and verify the caret at the required text boundary, press Enter in the editable context, and wait for the expected change. For count verification, record the initial count in the target section and expect `initialCount + 1`; the whole document already contains other paragraphs.

If the requirement also includes accepting the inserted paragraph, scope Accept Change to that insertion and verify its agreed post-accept state. An accepted change may need different cleanup from Undo. Never add Accept Change merely to match the sample's shape.

## 3. Logging requirements

Use this sequence for each meaningful step:

| Level | When | Example |
| --- | --- | --- |
| INFO | Test starts | `Starting test: verifyParagraphSelectionShowsParagraphType` |
| DEBUG | Before navigation, frame changes, actions and assertions | `Clicking plain text in the target paragraph` |
| DEBUG | After a completed action or frame change | `Clicked target paragraph` |
| INFO | Observed result after the wait | `Selected Element after paragraph click: p` |
| INFO | Successful end, after assertions and required cleanup | `Finished test: ... - PASSED` |
| ERROR | A failure handled by a shared listener or boundary | Include context and the exception; let the test fail. |

Use parameterised messages: `logger.info("Paragraph count: expected={}, actual={}", expected, actual);`. Avoid string concatenation, `System.out.println`, `printStackTrace`, and logging the same exception in several layers. Do not log passwords, tokens, cookies or full sensitive page content. A log line is not an assertion.

Read a result once after waiting, store it in a local variable, then use that value in both the log and assertion. The sample calls `isNewParaDispalyedInVe()` twice; avoid that because the UI may change between calls and each call may perform another wait.

The current `src/test/resources/log4j2.xml` uses root level `info`, so `logger.debug(...)` messages are hidden. To capture detailed steps, add package loggers inside `<Loggers>` before `<Root>`:

```xml
<Logger name="documenteditor" level="debug" />
<Logger name="para" level="debug" />
```

Use the actual module package(s) containing the new tests and pages. Add a `portal` logger when diagnosing portal cases. These inherit the existing console and file appenders. Logs are written to `logs/Automation.log`. `debug=yes` in `config.properties` controls the existing BasePage debug helper; it does not enable Log4j2 DEBUG messages. No logging configuration is changed by this guide.

## 4. Test method template using existing editor actions

The following is a reference version of TC_17 with the standard class and groups. It assumes members of `documenteditor.DocumentEditor1TestCase extends BaseClass`, a matching `DocumentEditorPageObjects1` with the existing editor actions, and setup that opens the editor once. These classes now exist; import `a_testbase.BaseClass` and use the existing navigation setup. Do not add another TC_17 or duplicate this method (it already exists globally); adapt the pattern with a new unique method for a new scenario. Add the Log4j2 imports if missing.

```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

private static final Logger LOGGER = LogManager.getLogger(DocumentEditor1TestCase.class);
private static final String STARTING_TEST_MSG = "Starting test: {}";
private static final String FINISHED_TEST_MSG = "Finished test: {} - PASSED";
private static final String CLICKING_HEADING_MSG = "Clicking plain text in the target heading";
private static final String CLICKING_PARAGRAPH_MSG = "Clicking plain text in the target paragraph";

@Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" },
        description = "TC_17 | Verify selecting a paragraph shows p")
public void verifyParagraphSelectionShowsParagraphType() {
    final String testName = "verifyParagraphSelectionShowsParagraphType";
    LOGGER.info(STARTING_TEST_MSG, testName);
    LOGGER.debug("Initialising document editor page object");
    DocumentEditorPageObjects1 page = new DocumentEditorPageObjects1(getDriver());

    LOGGER.debug(CLICKING_HEADING_MSG);
    page.clickTargetHeading();
    LOGGER.debug("Clicked heading; waiting for Selected Element to show heading");
    page.waitForSelectedElementType("heading");

    LOGGER.debug(CLICKING_PARAGRAPH_MSG);
    page.clickTargetParagraph();
    LOGGER.debug("Clicked paragraph; waiting for Selected Element to show p");
    page.waitForSelectedElementType("p");

    String actualType = page.getSelectedElementType();
    LOGGER.info("Selected Element after paragraph click: {}", actualType);
    LOGGER.debug("Asserting Selected Element is p");
    Assert.assertEquals(actualType, "p",
            "Selected Element should change from heading to p after clicking the paragraph");
    LOGGER.info(FINISHED_TEST_MSG, testName);
}
```

Log prerequisite navigation in the existing setup method or navigation page methods so it appears before the scenario steps. Do not log that login occurred unless the login action actually completed. BaseClass and the report listeners own browser teardown and failure reporting.

## 5. Page Object template

This reference illustrates the existing standard-named page object and shared `d_loginpage.BasePage` helpers. Add members to that class; do not create a duplicate page implementation.

```java
package documenteditor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import d_loginpage.BasePage;

public class DocumentEditorPageObjects1 extends BasePage {
    private static final Logger LOGGER = LogManager.getLogger(DocumentEditorPageObjects1.class);
    private static final String WAITING_FOR_TYPE_MSG = "Waiting for Selected Element: {}";

    @FindBy(id = "selectedElmName")
    private WebElement selectedElementName;

    public DocumentEditorPageObjects1(WebDriver driver) {
        super(driver); // BasePage initialises PageFactory, waits and Actions.
    }

    public void waitForSelectedType(String expectedType) {
        LOGGER.debug(WAITING_FOR_TYPE_MSG, expectedType);
        mywait.until(driver -> selectedElementName.isDisplayed()
                && expectedType.equals(selectedElementName.getText().trim()));
        LOGGER.debug("Selected Element reached expected value: {}", expectedType);
    }

    public String getSelectedType() {
        waitForElementToBeVisible(selectedElementName);
        return selectedElementName.getText().trim();
    }
}
```

Call these methods in the main-page context. The real editor page already switches there before reading this control. Page methods perform actions and waits; TestNG assertions belong in the test class. Do not add unused fields, constants, getters or unrelated methods from a larger sample page.

### Locators and frame handling

- Prefer stable IDs or semantic attributes scoped to the correct document/section. Use `@FindBy` for fixed locators and `By` for dynamic or re-rendered targets.
- Do not copy generated `inno_ref='55' or '56'`, CKEditor numeric IDs or positional `(//li[@name='p'])[2]` unless that identity/order is an explicit requirement. Re-rendering and inserted paragraphs can change them.
- Multiple `data-element='p'` nodes are normal. Locate the intended paragraph using configured text and scope. Account for nested formatting spans and invisible editor markers using the existing helpers.
- Wait for the editor to be ready. Prefer the known CKEditor frame locator to frame index `0`; do not assume the first frame is always the editor.
- Keep frame entry and restoration together, with `defaultContent()` in `finally` when leaving the operation. Log before and after both switches.
- Switching to default content does not reliably preserve keyboard focus. Send Enter to the confirmed editable/caret context; do not copy the sample's switch-out-then-Enter sequence without checking what `enterKey()` does.
- Use native input for user interactions. Use selection helpers when exact caret positioning is required; do not set a dropdown label or fabricate DOM results to make an assertion pass.

## 6. SonarQube and code quality

Apply these checks before review:

- Remove unused imports, fields, constants, parameters, dead code and commented-out test bodies from newly written code. Do not commit `enabled = false`; the PDF prohibits it. Do not bypass this rule with commented-out test bodies or permanently excluded cases to hide failures. Report blocked cases clearly.
- Use consistent Java naming, private locator fields and `private static final` loggers/constants. Avoid unnecessary public WebElement getters; expose meaningful actions and checks.
- Keep methods focused. Extract repeated navigation, frame handling, waits and actions into the appropriate existing helpers. Use constants for repeated messages and configuration for variable test data.
- Use parameterised logs and avoid expensive WebDriver calls inside log arguments. Read actual results once.
- Add clear assertion messages. Do not catch an exception and return success, swallow assertion failures, or print PASSED from `finally`.
- Use explicit waits rather than `Thread.sleep`. Avoid broad exception catches and unnecessary nested conditions. Preserve the original failure if cleanup also fails, using the framework's established failure handling.
- Protect test isolation and clean up only the operation under test. Keep resource cleanup in `finally` or the existing teardown lifecycle.
- Never commit credentials, generated reports, screenshots, logs or `target/`. Do not suppress rules broadly or exclude new tests just to obtain a green result.

These practices reduce common findings; they cannot guarantee zero SonarQube issues. The team's quality profile, analysis scope and Quality Gate determine the result. This repository currently has no SonarQube configuration in `pom.xml`. Request the approved server/project configuration through the team's normal process; keep analysis tokens in secrets, never in Git or a shared command. Verify that test Java files are actually included in analysis.

## 7. Verification and completion checklist

1. Check lowercase module package, numbered test/page class pair, global `verify...` method uniqueness, matching page method/field/constant naming, exactly two standard groups and manual-sheet priority. No annotation `retryAnalyzer`, `priority =` or `enabled = false`.
2. Compile: `mvn test-compile`.
3. Run the relevant TestNG suite, for example `mvn clean test -DsuiteXmlFile=ElementSelectionTests.xml` for TC_17/TC_18. Add new standard class and group references to the appropriate suite before running them; apply the `RetryMultipleFailedTestcases` instructions when using the standard project layout. Verify report/listener compatibility with the new groups.
4. Inspect executed counts, failures and skips in TestNG reports. Maven now fails for test failures; also verify the expected executed count and zero skips.
5. Confirm the required INFO/DEBUG steps appear in console/file logs with DEBUG enabled for the test/page packages.
6. Run the team's configured SonarQube analysis and inspect new-code issues and the Quality Gate. Report accurately if analysis was not run.
7. Update [the test case catalog](test-case-catalog.md) in the same change. Update relevant setup/test guides if behaviour or prerequisites changed.
8. Review the diff for credentials, generated files, unused code, accidental document changes and unrelated edits before committing.

## 8. Reusable request for a new test

Copy this when requesting or drafting another case:

> Write test [ID / name] using docs/test-writing-guide.md and the existing framework. Objective: [objective]. Preconditions: [project, document, permissions, original state]. Actions: [exact interaction sequence]. Expected result: [observable values in VE/DS/right panel/CV]. Confirmed HTML/locators: [details]. Use the standard lowercase module package, `<Module><Number>TestCase` and `<Module>PageObjects<Number>` pair, a globally unique `verify...` method and exactly two groups (module and `P<n>-<Module>` from the manual priority). Reuse BaseClass, BasePage and login setup, with explicit waits and meaningful assertions, and Log4j2 INFO start/result/end plus DEBUG before/after each action, frame change and assertion. Keep locators out of tests, avoid duplicate login and WebDriver calls in log arguments, and follow the SonarQube checklist. Cleanup: [undo only this test's change]. Do not add annotation retryAnalyzer/priority or commit enabled=false. Put text repeated three or more times in constants. Update the suite class/group references and docs/test-case-catalog.md, run the relevant suite and report whether SonarQube analysis was actually run.
