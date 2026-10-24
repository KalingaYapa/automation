# Hybrid automation framework

Selenium, Java, TestNG and Maven with Page Object Model and shared portal navigation.

## Project structure

```text
src/test/java/
├── a_testbase/BaseClass.java                 browser lifecycle and configuration
├── b_utilities/                             reports, retries and reusable navigation
├── d_loginpage/BasePage.java                 shared WebDriver actions and waits
├── d_loginpage/HomePage.java                 login, project and file-list navigation
├── documenteditor/DocumentEditor1TestCase.java
├── documenteditor/DocumentEditorPageObjects1.java
└── portal/Portal1TestCase.java
src/test/resources/config.properties          local configuration
```

All 29 application tests are enabled: TC_01–TC_19 and TC_25–TC_29 cover the editor; TC_20–TC_24 cover portal navigation. The five sample-screen examples are preserved under `docs/templates` as noncompiled references.

## Run

Install JDK 17 or later, Maven and Chrome. Set the sandbox login URL and test account in your local `src/test/resources/config.properties`. Never commit credentials.

```bash
mvn clean test                                      # all 29 application tests
mvn clean test -DsuiteXmlFile=DocumentEditor.xml      # 24 editor tests
mvn clean test -DsuiteXmlFile=PortalNavigation.xml    # 5 portal tests
mvn clean test -DsuiteXmlFile=FormattingTests.xml     # TC_25–TC_29
mvn clean test -Dgroups=P2-DocumentEditor             # High-priority editor tests
```

The default `testng.xml` runs sequentially to avoid concurrent changes to the same document. `Master.xml` is an equivalent compatibility suite. Every case has a fresh browser and performs its own prerequisites. Editing tests undo their changes and do not save the document. Maven fails when tests fail; always check executed counts and skips too.

Reports: `reports/ExtentReport.html`, `reports/ExcelReport.xlsx`, `target/surefire-reports/`. Logs: `logs/Automation.log`. Failure screenshots: `screenshots/`. Generated files should not be committed.

## Writing tests

Follow [the test-writing guide](docs/test-writing-guide.md), including lowercase module packages, numbered class pairs, globally unique `verify...` methods and exactly two groups: module and `P<n>-<Module>` from the manual-sheet priority. Update [the test case catalog](docs/test-case-catalog.md) with every test change. Locators and WebDriver actions belong in page objects; tests contain assertions. Retry is attached by the framework listener.

See [terminal instructions](docs/run-formatting-tests-terminal.md) and [the restructuring guide](docs/framework-restructuring.md) for commands and migration details.

## Step logging

All application tests log INFO start, observed results and successful finish. DEBUG
logs before/after actions, assertions, frame changes and Undo cleanup are enabled
for `documenteditor`, `portal` and `d_loginpage`. Console and
`logs/Automation.log` receive the same messages; no extra Maven flag is needed.
Failure listeners emit ERROR logs for test and setup/cleanup exceptions. Credentials
are never logged. See [the logging guidance](docs/test-writing-guide.md#3-logging-requirements).
