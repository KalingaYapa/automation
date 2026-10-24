> Current structure: all application tests use the PDF naming/grouping standard. Run all 29 with `mvn clean test` (`testng.xml`); `Master.xml` is an equivalent compatibility suite. See [the restructuring guide](framework-restructuring.md). Older validation notes below describe their original runs.

# Run editor formatting tests from the terminal

This guide runs TC_25–TC_29 on your local machine using the existing framework. Commands work in macOS Terminal and Linux shells. Use Chrome; `FormattingTests.xml` explicitly selects it.

## 1. Check the required software

Install a JDK 17 or later, Maven, Git and Google Chrome. Check the command-line tools:

```bash
java -version
javac -version
mvn -version
git --version
```

`mvn -version` shows which Java installation Maven uses. It must be JDK 17 or later. Open Chrome once to confirm it is installed. Selenium Manager normally downloads the matching browser driver automatically; the first run needs network access to Maven dependencies and driver downloads.

On macOS, if Maven reports the wrong Java installation, list the installed JDKs:

```bash
/usr/libexec/java_home -V
```

For example, if JDK 17 is installed, select it for the current terminal session:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH="$JAVA_HOME/bin:$PATH"
mvn -version
```

Choose the version you actually installed. Java 21 also supports this project's Java 17 compilation target.

## 2. Open the repository

If you already have the repository, move into its directory (replace the example path):

```bash
cd "$HOME/projects/automation"
git status
```

With a clean working tree, get the test branch and latest changes:

```bash
git fetch origin
git switch tests/document-editor-titlebar
git pull --ff-only origin tests/document-editor-titlebar
```

If you have local changes, preserve them before switching or pulling. In particular, keep local credentials private; do not commit them.

For a new checkout instead:

```bash
git clone --branch tests/document-editor-titlebar https://github.com/KalingaYapa/automation.git
cd automation
```

Confirm that you are in the project root:

```bash
ls pom.xml FormattingTests.xml
```

Both files must exist. All subsequent commands run from this directory.

## 3. Set your local test configuration

Open `src/test/resources/config.properties` in your editor. On macOS you can use:

```bash
open -e src/test/resources/config.properties
```

Set these properties locally:

```properties
appURL=https://ia-portal-sandbox.innodata.com/login
username=YOUR_TEST_USERNAME
password=YOUR_TEST_PASSWORD
browser=chrome
headless=no
email=no
```

Replace the username and password placeholders with your sandbox account and save the file. Login reads these two properties, not `SANDBOX_USERNAME` or `SANDBOX_PASSWORD`. Do not place credentials in the terminal command or commit the file containing them. Java properties treat backslashes as escapes; a literal backslash in a value must be written as `\\`.

Keep the configured project, document allocation and target text aligned with the sandbox document:

```properties
portalProject=IATA
portalFileSearch=Transmit Flight and Shipment Information
editorFileEditPath=/projects/IATA/authoring/order/4/file-allocation/33/edit
editorHeadingText=Transmit Flight and Shipment Information to Down-line Stations & Authorities
headingWord=Transmit
editorParagraphText=Close the flight in the Handling system.By this action, the (X)FFM is transmitted.
paragraphSelectionWord=flight
```

The configured paragraph currently contains `system.By` without a space. Match the actual saved text; do not edit the application to force it to match this example. Keep the other existing properties, including `expectedDocumentTitle`, in the file.

An existing `SANDBOX_URL` environment variable overrides `appURL`. The commands below set `-DappURL` explicitly so the login URL is unambiguous.

Use an editable sandbox document. The heading word must initially have no explicit Bold, Italic or Underline formatting, and the target paragraph must initially be outside a list. Each test reverses its own change through Undo and does not save the file. Avoid simultaneous manual edits to the same allocation while tests run.

## 4. Run the five new tests

Run:

```bash
mvn clean test -DsuiteXmlFile=FormattingTests.xml -DappURL=https://ia-portal-sandbox.innodata.com/login
```

The browser is visible when `headless=no` is configured. To run without a visible browser:

```bash
mvn clean test -DsuiteXmlFile=FormattingTests.xml -DappURL=https://ia-portal-sandbox.innodata.com/login -Dheadless=yes
```

Use one command at a time. The first run can take longer because Maven and Selenium download dependencies.

| Command part | Meaning |
| --- | --- |
| `mvn` | Runs Maven using `pom.xml`. |
| `clean` | Removes the previous `target/` build output. |
| `test` | Compiles the project and runs the selected TestNG suite. |
| `-DsuiteXmlFile=FormattingTests.xml` | Selects the suite containing only TC_25–TC_29 instead of the default `testng.xml`. |
| `-DappURL=...` | Sets the login URL for this run. |
| `-Dheadless=yes` | Optionally runs Chrome without a visible window. |

## 5. Understand what executes

| ID | Scenario | Main verification |
| --- | --- | --- |
| TC_25 | Apply Italic to the heading word | Italic toolbar state and actual italic formatting. |
| TC_26 | Apply Underline to the heading word | Underline toolbar state and actual underline formatting. |
| TC_27 | Apply Bold, Undo, then Redo | Redo restores bold formatting and active Bold state. |
| TC_28 | Convert the paragraph to an unordered list | Active bulleted-list button and paragraph inside an unordered list item. |
| TC_29 | Convert the paragraph to an ordered list | Active numbered-list button and paragraph inside an ordered list item. |

For each test, the framework independently:

1. Opens a fresh Chrome browser.
2. Logs in using your local `config.properties`.
3. Selects IATA and opens Authoring File List.
4. Finds the configured allocation and opens its editor.
5. Performs the test and checks the result.
6. Uses Undo to reverse the test's change, then closes the browser.

`FormattingTests.xml` runs sequentially. TestNG may choose a different method order; tests do not depend on each other.

## 6. Check the results

Confirm the terminal/TestNG report shows five tests executed, five passed, zero failures and zero skips. A skipped test is not a pass; inspect any setup failure too.

Maven now uses `testFailureIgnore=false`, so failed tests fail the build. Still check that the expected tests executed with zero skips; skipped setup is not a pass.

Reports and logs are relative to the project root:

| Location | Contents |
| --- | --- |
| `reports/ExtentReport.html` | Test results and failure details. |
| `reports/ExcelReport.xlsx` | Test case IDs, names and results. |
| `target/surefire-reports/` | Maven/TestNG execution reports and failure traces. |
| `logs/Automation.log` | Framework logs. |
| `screenshots/` | Screenshots captured on failure. |

On macOS, open the HTML report:

```bash
open reports/ExtentReport.html
```

Or open the Maven report folder:

```bash
open target/surefire-reports
```

Check report timestamps so you are reading the current run. `clean` removes `target/`; it does not clear the separate `reports/`, `logs/` or `screenshots/` folders. These are generated files and should not be committed.

## 7. Common problems

| Symptom | What to check |
| --- | --- |
| `mvn: command not found` | Install Maven or add its `bin` directory to PATH; reopen Terminal. |
| Java release/compiler error | Check both `java -version` and `mvn -version`; Maven needs a compatible JDK, not just a JRE. |
| `pom.xml` or suite file not found | Run from the repository root and check the branch contains `FormattingTests.xml`. |
| Sample tests run instead | Supply `-DsuiteXmlFile=FormattingTests.xml`; plain `mvn test` defaults to `testng.xml`. |
| Login fails or navigates to the wrong URL | Check local username/password and use the explicit `-DappURL` command above. |
| Paragraph/heading cannot be located | Compare configured text with the saved document and confirm the allocation path. Formatting spans and editor markers are already handled by the page object. |
| Formatting/list precondition fails | Use the intended starting document state; inspect the failure before rerunning. Do not bypass the precondition or accept unrelated tracked changes. |
| Chrome/driver cannot start | Confirm Chrome is installed and network/proxy settings allow Selenium Manager driver downloads. |
| CDP version warning | Standard WebDriver actions may still run; inspect the actual failure trace rather than assuming this warning caused it. |

For full objectives and expected results, see [the test case catalog](test-case-catalog.md). For other editor suites, see [the editor test guide](document-editor-tests.md).
