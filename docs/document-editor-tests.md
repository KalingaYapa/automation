# Document editor tests

These 14 checks cover the supplied title-bar HTML and existing bold text. They do not click Save,
Reset File, Validate File, or Check In. Enabled state means the HTML control accepts
interaction; it does not prove saving, resetting, validation, or check-in works.

| Manual ID | Automation method | Expected result |
| --- | --- | --- |
| TC_01 | verifyDocumentTitleIsDisplayed | Document title is visible |
| TC_02 | verifyDocumentTitleText | Title matches expectedDocumentTitle |
| TC_03 | verifySavedTimeIsDisplayed | Saved-time indicator is visible |
| TC_04 | verifySavedTimeFormat | Text matches Last Saved followed by a 12-hour time and AM/PM |
| TC_05 | verifySaveButtonLabel | Visible button reads Save |
| TC_06 | verifySaveButtonIsEnabled | Save is enabled |
| TC_07 | verifyResetButtonLabel | Visible button reads Reset File |
| TC_08 | verifyResetButtonIsEnabled | Reset File is enabled |
| TC_09 | verifyValidateButtonLabel | Visible button reads Validate File |
| TC_10 | verifyValidateButtonIsEnabled | Validate File is enabled |
| TC_11 | verifyCheckInButtonLabel | Visible button reads Check In |
| TC_12 | verifyCheckInButtonIsEnabled | Check In is enabled |
| TC_13 | verifyBoldIsActiveAfterSingleClick | Caret is inside Depending and Bold is active |
| TC_14 | verifyBoldIsActiveAfterDoubleClick | Depending is selected and Bold is active |

## Files and framework flow

`DocumentEditor.xml` selects only `Ab_DocumentEditorTC`, running sequentially.
Inherited `BaseClass.setup()` opens a fresh browser and appURL before each method.
Each test creates `Ab_DocumentEditorPage`, which uses the supplied element IDs and
explicit visibility waits. Tests contain assertions; locators remain in the page object.
The framework's existing listeners generate HTML and Excel reports.
`Master.xml` and the sample tests are unchanged.

## Before running against the sandbox

1. Use an editable sandbox document and a test account permitted to use all four buttons.
   Enabled-state expectations come from the supplied HTML, where the buttons have no
   disabled attribute. Confirm these expectations with your lead for your role and
   document status. The saved-time test expects an already-saved document and the shown
   English, 12-hour time format; it does not verify persistence or timestamp freshness.
2. Locally set `appURL` in `src/test/resources/config.properties` to:
   `https://ia-portal-sandbox.innodata.com/projects/IATA/authoring/order/4/file-allocation/33/edit`
   Use this exact endpoint, without appending an extra slash or `sample`.
   The committed template appURL is intentionally unchanged.
3. Set `expectedDocumentTitle` for the selected document. Adjust `savedTimePattern` if
   a different locale or documented format is required. Do not put credentials in Git.
4. Arrange the supported login flow in BaseClass for every new browser session.
   Merely entering username/password in config does not log in: this framework has no
   implemented application login, and this test suite does not invent one. An already
   logged-in personal browser does not authenticate Selenium's fresh browser.
5. For this cloud environment, the application domain must be allowed in environment
   settings. Activate tools with `source /workspace/.onboarding/env.sh`. The current
   BaseClass also needs container ChromeOptions before application tests can run:
   binary `/workspace/.onboarding/tools/debian/usr/lib/chromium/chromium` and arguments
   `--headless=new`, `--no-sandbox`, `--disable-dev-shm-usage`. These environment/login
   changes are outside this title-bar test addition; the verified example is in
   `/workspace/.onboarding/CloudSmoke.java`. Local desktop browser setup may differ.

Compile without launching browsers:

```bash
mvn test-compile
```

Run only the new suite after the prerequisites are complete:

```bash
mvn clean test -DsuiteXmlFile=DocumentEditor.xml
```

The pom.xml explicitly ignores test failures, so BUILD SUCCESS alone is insufficient. Inspect `target/surefire-reports/testng-results.xml`, `reports/ExtentReport.html`,
and `reports/ExcelReport.xlsx`. Expect 14 tests, no failures, and no skips; distinguish
setup failures from assertion failures. Do not run the sample suite against this editor.

## Validation scope

The locators and expectations are based on user-supplied HTML. Compilation can verify
Java integration, but cannot establish that the live editor, permissions, login, or
persistence work. The sandbox endpoint is reachable but redirects to /login without authentication.
The application tests have not been validated against the sandbox.
The remaining editor features will be added after their HTML and behavior are available.

## Bold formatting selection checks

Both cases start from the existing bold word `Depending` (`boldSampleText` in config).
TC_13 single-clicks the word and waits for a collapsed caret inside its span.
TC_14 double-clicks it and waits for a non-collapsed selection containing exactly
`Depending` after trimming whitespace. This prevents a click in the wrong place
from being reported as a successful selection. Neither case applies Bold or saves.

The page object finds visible `span[data-element='b'][data-style='bold']` elements by
the configured word, avoiding user IDs, change timestamps, session IDs and generated
reference numbers. A duplicate matching word in the same editing context fails with
a message requesting a paragraph-scoped locator.

Inline editing and the standard CKEditor 4 `iframe.cke_wysiwyg_frame` are supported.
Selection is inspected in the editing context; the driver then returns to the main
page for the toolbar. Nested frames or a toolbar inside another frame require the
actual frame markup before adapting these locators.

The supplied full Bold button HTML confirms `a.cke_button.cke_button__bold` as
its locator. The generated `cke_23` ID is deliberately avoided. The button is active
when its class tokens contain `cke_button_on` and do not contain `cke_button_off`.
It must also be enabled, without `aria-disabled=true` or `cke_button_disabled`.
The icon span and `aria-disabled=false` alone do not establish active formatting.

The locator and state rule now match the supplied full button HTML. Editor frame
structure and native selection behavior still require validation against the live
sandbox. The live cases require authentication and have not been executed there.
