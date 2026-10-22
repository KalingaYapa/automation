# Guide for writing automation test cases

Use this guide when writing new Selenium, Java and TestNG tests with Page Object Model (POM) and Log4j2. It records the structure supplied by the team in `verifyClickingEnterMiddleOfPara` and `ParaPageObjects1`, with adjustments for this repository's existing framework. It does not change existing test implementations.

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

### Mapping the supplied project to this repository

| Supplied team example | This repository |
| --- | --- |
| `Para1TestCase` and `ParaPageObjects1` | `e_testCases/Xx_<Screen>TC` and `d_pageObjects/Xx_<Screen>Page`. Keep existing names for existing screens. |
| `ReusableMethods.loginAndSearchFileClickEdit()` inside the test | Editor tests already navigate in `Ab_DocumentEditorTC.openAllocatedEditor()` using `@BeforeMethod`. Do not add a second login to those methods. The supplied helper is not present here. |
| `groups = { "Para", "P2-Para" }` | Current reporting convention uses class name and priority, such as `{ "Ab_DocumentEditorTC", "High" }`. Use the team groups only when working in their project or when its suite/report configuration is deliberately adopted. |
| `@Test(enabled = true, ...)` | Supported here; `true` is the default. Include the manual ID in `description`, for example `TC_25 | Verify ...`. |
| Direct frame switching in the test | Existing editor page methods handle inline/iframe contexts. Keep that ownership there; do not mix caller-owned frame switching with those methods. |
| Named log-message constants and a class logger | Use a `private static final Logger` and meaningful constants for repeated messages, with Log4j2 placeholders. |

The sample's broader paragraph scenario checks the Visual Editor (VE), Document Structure (DS), accepting a tracked change and Code View (CV). Existing TC_19 checks only that Enter increases the section's paragraph count by one, then undoes the split. Accept Change and Code View checks require their own confirmed locators and expected behaviour before implementation.

## 2. Design the case first

Before writing code, record:

1. Unique test ID, readable name and automation method name. The next application ID is currently TC_25; check the catalog before allocating it.
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
<Logger name="e_testCases" level="debug" />
<Logger name="d_pageObjects" level="debug" />
```

These inherit the existing console and file appenders. Logs are written to `logs/Automation.log`. `debug=yes` in `config.properties` controls the existing BasePage debug helper; it does not enable Log4j2 DEBUG messages. No logging configuration is changed by this guide.

## 4. Test method template using existing editor actions

The following is a reference version of the existing TC_17. Adapt the pattern for new scenarios; do not add another TC_17 or duplicate the method. Place the members in `Ab_DocumentEditorTC`, whose existing setup already opens the editor. Add the Log4j2 imports if missing.

```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

private static final Logger LOGGER = LogManager.getLogger(Ab_DocumentEditorTC.class);
private static final String STARTING_TEST_MSG = "Starting test: {}";
private static final String FINISHED_TEST_MSG = "Finished test: {} - PASSED";
private static final String CLICKING_HEADING_MSG = "Clicking plain text in the target heading";
private static final String CLICKING_PARAGRAPH_MSG = "Clicking plain text in the target paragraph";

@Test(enabled = true, groups = { CLASS_GROUP, "High" },
        description = "TC_17 | Verify selecting a paragraph shows p")
public void verifyParagraphSelectionShowsParagraphType() {
    final String testName = "verifyParagraphSelectionShowsParagraphType";
    LOGGER.info(STARTING_TEST_MSG, testName);
    LOGGER.debug("Initialising document editor page object");
    Ab_DocumentEditorPage page = new Ab_DocumentEditorPage(getDriver());

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

This standalone reference demonstrates the supplied constructor, `@FindBy`, class logger and public check methods using this repo's real BasePage helpers. It is an example, not an additional committed Java class. For the existing screen, add methods to `Ab_DocumentEditorPage` rather than creating a duplicate page object.

```java
package d_pageObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import c_basePage.BasePage;

public class ExampleSelectedElementPage extends BasePage {
    private static final Logger LOGGER = LogManager.getLogger(ExampleSelectedElementPage.class);
    private static final String WAITING_FOR_TYPE_MSG = "Waiting for Selected Element: {}";

    @FindBy(id = "selectedElmName")
    private WebElement selectedElementName;

    public ExampleSelectedElementPage(WebDriver driver) {
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

- Remove unused imports, fields, constants, parameters, dead code and commented-out test bodies from newly written code. Use `enabled = false` with a documented reason if a new case must be disabled.
- Use consistent Java naming, private locator fields and `private static final` loggers/constants. Avoid unnecessary public WebElement getters; expose meaningful actions and checks.
- Keep methods focused. Extract repeated navigation, frame handling, waits and actions into the appropriate existing helpers. Use constants for repeated messages and configuration for variable test data.
- Use parameterised logs and avoid expensive WebDriver calls inside log arguments. Read actual results once.
- Add clear assertion messages. Do not catch an exception and return success, swallow assertion failures, or print PASSED from `finally`.
- Use explicit waits rather than `Thread.sleep`. Avoid broad exception catches and unnecessary nested conditions. Preserve the original failure if cleanup also fails, using the framework's established failure handling.
- Protect test isolation and clean up only the operation under test. Keep resource cleanup in `finally` or the existing teardown lifecycle.
- Never commit credentials, generated reports, screenshots, logs or `target/`. Do not suppress rules broadly or exclude new tests just to obtain a green result.

These practices reduce common findings; they cannot guarantee zero SonarQube issues. The team's quality profile, analysis scope and Quality Gate determine the result. This repository currently has no SonarQube configuration in `pom.xml`. Request the approved server/project configuration through the team's normal process; keep analysis tokens in secrets, never in Git or a shared command. Verify that test Java files are actually included in analysis.

## 7. Verification and completion checklist

1. Compile: `mvn test-compile`.
2. Run the relevant TestNG suite, for example `mvn clean test -DsuiteXmlFile=ElementSelectionTests.xml` for TC_17/TC_18. Add new cases to the appropriate suite before running them.
3. Inspect executed counts, failures and skips in TestNG reports. This framework permits test failures without failing the Maven build, so BUILD SUCCESS alone is insufficient.
4. Confirm the required INFO/DEBUG steps appear in console/file logs with DEBUG enabled for the test/page packages.
5. Run the team's configured SonarQube analysis and inspect new-code issues and the Quality Gate. Report accurately if analysis was not run.
6. Update [the test case catalog](test-case-catalog.md) in the same change. Update relevant setup/test guides if behaviour or prerequisites changed.
7. Review the diff for credentials, generated files, unused code, accidental document changes and unrelated edits before committing.

## 8. Reusable request for a new test

Copy this when requesting or drafting another case:

> Write test [ID / name] using docs/test-writing-guide.md and the existing framework. Objective: [objective]. Preconditions: [project, document, permissions, original state]. Actions: [exact interaction sequence]. Expected result: [observable values in VE/DS/right panel/CV]. Confirmed HTML/locators: [details]. Use a BaseClass test and BasePage page object, existing login setup, explicit waits, meaningful assertions, and Log4j2 INFO start/result/end plus DEBUG before/after each action, frame change and assertion. Keep locators out of tests, avoid duplicate login and WebDriver calls in log arguments, and follow the SonarQube checklist. Cleanup: [undo only this test's change]. Update docs/test-case-catalog.md, run the relevant suite and report whether SonarQube analysis was actually run.
