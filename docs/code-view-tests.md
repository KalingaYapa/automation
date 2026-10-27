# Visual Editor and Code View switching

Manual **TC-017** maps to framework **TC_42** and automation method `documenteditor.DocumentEditor1TestCase.verifySwitchingBetweenVisualEditorAndCodeViewPreservesContent`. Existing framework TC_17 remains the paragraph selection case. The new case uses `DocumentEditor/P2-DocumentEditor` groups.

## Live-confirmed controls and checks

- Code View tab: `//*[@id='code-view-tab']`; its parent tab item is `tab-li-xml`.
- Visual Editor tab: `//*[@id='visual-editor-tab']`; its parent tab item is `tab-li-html`.
- CodeMirror: `//*[@id='tab-xml-editor']//div[contains(concat(' ',normalize-space(@class),' '),' CodeMirror ')]`.
- Visual document: `//body` inside the visible `cke_wysiwyg_frame` iframe.

The test captures the complete VE body markup and verifies its text is nonempty. It then clicks Code View using `BasePage.elementClick`, waits for the active tab and visible CodeMirror while the VE iframe is hidden, and reads the complete buffer through the live CodeMirror instance's read-only `getValue()` API. Reading only rendered CodeMirror lines would miss source outside the viewport.

The source must parse as XML and contain all original document text in order. This HTML/XML text comparison ignores serialization whitespace and invisible editor markers. After returning, the Visual Editor tab must be active with its iframe visible and CodeMirror hidden. The complete VE body markup must equal the original exactly, covering text, whitespace, structure, attributes and formatting in the editor body. This is stricter than comparing text alone.

The test owns all frame/default-content transitions and logs observed lengths/booleans rather than document content. Cleanup restores default content and, if a transition failed, attempts to restore VE while retaining the original failure. It does not edit, Save, Reset, Accept Change or Undo.

## Run locally

Use the existing sandbox configuration, JDK 17+, Maven, Chrome and network access. Keep `headless=no` to see the browser. Shared navigation opens the configured IATA allocation in its editor window.

```bash
python3 scripts/check_framework_standards.py
mvn test-compile
mvn test -DsuiteXmlFile=CodeViewTests.xml
```

The focused suite runs one case and retains both framework listeners. The complete editor/default suites automatically include the new method through the existing DocumentEditor class reference; their totals are now 37 editor cases and 42 application cases respectively.

Review `reports/ExtentReport.html`, `reports/ExcelReport.xlsx`, `target/surefire-reports/` and console/file logs (`logs/Automation.log`). Check executed counts, failures and skips. SonarQube analysis requires the team's server/project configuration; it is not configured in this repository.

## Verification

The framework standards check and Maven test compilation passed. `CodeViewTests.xml` passed locally in Chrome: 1 test, 0 failures, 0 errors, 0 skips. Source XML text matched the original document, and the complete VE markup was exactly unchanged after switching back. Required INFO/DEBUG messages were verified in terminal and file output. SonarQube analysis was not run.
