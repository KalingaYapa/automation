# Document editor tests

These 19 checks cover the supplied title-bar HTML, existing bold text, and applying Bold to a heading word. They do not click Save,
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
| TC_15 | verifyApplyingBoldToHeadingWord | Selected heading word gains bold markup and Bold becomes active; Undo restores formatting |
| TC_16 | verifyUndoRestoresHeadingFormatting | Undo restores original heading text, font weight and bold markup state; Bold is off |
| TC_17 | verifyParagraphSelectionShowsParagraphType | Selected Element changes from heading to p |
| TC_18 | verifyHeadingSelectionShowsHeadingType | Selected Element changes from p to heading |
| TC_19 | verifyEnterIncreasesParagraphCount | Enter increases the target section paragraph-row count by one |

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
and `reports/ExcelReport.xlsx`. Expect 19 tests, no failures, and no skips; distinguish
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

## Applying Bold to the heading (TC_15)

Use a disposable sandbox document: this test changes formatting temporarily, then
uses the editor's Undo button. It never clicks Save. Confirm whether your application
autosaves before running editing tests against shared documents; Undo cannot roll
back server-side history or notifications.

`editorHeadingText` identifies the supplied heading without its generated reference
number. `headingWord=Transmit` specifies the exact word to select. A single click
only places a caret, so it does not reliably format an existing word. The test uses
a native double-click at the word's measured screen position and verifies that the
selected text is exactly the configured word.

Before clicking Bold, the test requires `cke_button_off` and no explicit bold
markup on the word. A heading can already look bold due to CSS, so appearance alone
is not the baseline. If the toolbar is already on, the test fails its precondition
without clicking Bold; choose a genuinely unformatted word/fixture instead of silently
toggling the existing formatting off.

After the click, the test waits for `cke_button_on`, explicit bold markup covering
the selected word (`span[data-element=b][data-style=bold]`, `b`, or `strong`), and a
computed font weight of at least 700. Thus the toolbar alone cannot make this test
pass. Undo then must remove the new explicit bold markup. The flow assumes the
confirmed CKEditor Undo button `a.cke_button.cke_button__undo`. This test does not check saved
formatting persistence.

Validation for this addition: Maven test compilation passed. A local Selenium
fixture exercised the actual page-object flow in both inline and standard iframe
editing contexts: exact word selection, off-state precondition, Bold click, toolbar
and document-format checks, and Undo all passed. This fixture simulates toolbar
behavior; it does not establish that the authenticated application works.

## Undo operation (TC_16)

This independent case selects `headingWord`, requires Bold off, and captures the
heading's text, the word's computed font weight, and its explicit bold-markup state.
It applies Bold and verifies that the edit actually took effect before testing Undo.
It then clicks the standard CKEditor Undo button, waits for the added bold markup
to disappear, reselects the word and waits for Bold off. The assertion compares the
post-Undo text and formatting snapshot with the original snapshot.

The case never saves or resets the file. If setup fails after applying Bold, it
attempts cleanup with Undo. Once the Undo under test has been attempted, it does
not issue another Undo: that could revert unrelated earlier document history.
Use a disposable sandbox document, especially if autosave is enabled. This covers
undoing a formatting edit; undoing typing, multiple edits, and Redo are separate cases.
The supplied full Undo button HTML confirms the locator
`a.cke_button.cke_button__undo`. The generated ID `cke_14` is deliberately avoided.
Before clicking, the helper waits for visibility, `aria-disabled="false"`, and absence
of the `cke_button_disabled` class. `cke_button_off` is allowed: Undo is a command,
so that class does not mean it is disabled. These checks apply to TC_16 and to
formatting-test cleanup.

Validation: compilation passed. The local inline and iframe fixtures also passed
restoration-snapshot comparison and the Bold-off toolbar check after Undo. This
validates the automation helpers against simulated editor behavior, not the live
application's undo implementation.

Confirmed Undo-state update: compilation and local inline/iframe checks passed,
including a fixture where Undo starts ARIA-disabled and becomes enabled after the
Bold edit. The helper waits for that transition before clicking. Live application
execution remains unverified.

## Selected Element synchronisation (TC_17 and TC_18)

TC_17 first clicks the configured heading and waits for `heading`, then clicks the
configured paragraph and asserts `p`. TC_18 establishes `p` first, then clicks the
heading and asserts `heading`. Establishing the opposite initial state prevents an
already-correct dropdown label from hiding a failed click. Neither case opens or
changes the dropdown or document content.

The confirmed display locator is `#selectedElmName` on the main page. This custom
dropdown is not a native select. Editor text clicks support inline and standard
CKEditor iframe contexts, use a native single-click on an actual text character,
and verify a collapsed caret inside the chosen element. Explicit waits then handle
the label update on the main page.

`editorParagraphText` holds the exact supplied sentence. The provided paragraph
HTML confirms `div[data-element='p']`; its generated `inno_ref` is deliberately
avoided. Visible text and configured text are compared after removing U+200B,
U+2063 and U+FEFF editor markers, collapsing whitespace (including non-breaking
spaces), and trimming. Punctuation and actual wording still must match; missing
sentence spaces are not silently invented. Duplicate matching elements fail
explicitly. These cases still require
the existing authenticated editor setup.

Validation: compilation and four local browser-fixture checks passed: heading to p
and p to heading, each in inline and iframe editing contexts, including asynchronous
label updates. The fixture uses the supplied paragraph and dropdown display markup.
This verifies the helper flow against simulated behavior; the authenticated sandbox
has not been tested.

## Paragraph split count (TC_19)

This case intentionally verifies only Document Structure counts. It records the
number of direct paragraph rows in the target section as N, places the caret before
`paragraphSplitBefore=By this action`, sends a native Enter, waits for N+1, and
asserts that count. It does not check the resulting paragraph texts or their row
identities. A section with only one paragraph changes 1 to 2; existing other
paragraphs are included in the baseline, so the general expectation is N to N+1.

The supplied structure HTML confirms `#content-scroll`, `li.contentBlockListItem`,
and `name='p'`. The configured heading's `inno_ref` is matched to the heading li ID
(not the duplicate wrapper ID) to locate its nearest `ul.contentBlockList`. Only
`:scope > div.contentBlockListItemParent > li.contentBlockListItem[name='p']`
entries are counted. Nested subsections/list paragraphs are excluded; the section
must be expanded and the original paragraph must still contain the configured
full sentence before the test. The configured heading and paragraph must belong
to the same section.

Caret placement uses the browser Selection/Range API after a native click focuses
the paragraph. Enter is sent as a WebDriver key action, rather than inserting markup
with JavaScript. The boundary is searched across all descendant text nodes after
removing editor markers and collapsing whitespace. A character-to-DOM-position map
places the caret before the actual boundary even when bold spans split the phrase
across nodes. Empty or repeated boundaries fail explicitly. The code reads text and
sets a collapsed selection; it does not rewrite the paragraph's HTML.

Cleanup clicks Undo only when Enter was sent and the count changed, then waits for
the baseline count. It does not blindly undo when no structure change was observed.
No Save or Reset is clicked; use a disposable document if the application autosaves.
The count-only assertion cannot prove the text was preserved or the split occurred
at the correct position. Those are deliberately outside this test's requested scope.

Validation: compilation passed. Local inline and iframe fixtures passed paragraph
count transitions 2 to 3 to 2, including cleanup, with a nested paragraph excluded
from the section count. The fixture simulates Enter and structure synchronization;
this does not verify the authenticated application's split behavior.

## Paragraph lookup and split-boundary fix

The supplied paragraph includes U+2063 invisible separators and formatting spans
inside the split phrase. Paragraph matching now normalizes those markers and
whitespace on both sides of the comparison. The split helper maps normalized text
back to the original text nodes, so `By this action` may cross multiple spans.
The text-click helper also skips marker-only characters when choosing a visible
character to click. Existing duplicate-paragraph protection remains in place.

TC_19 still asserts only the Document Structure count N to N+1 and restores the
baseline with Undo. No hard-coded paragraph reference, missing-space workaround,
longer timeout, or changes to the document's content are used to make lookup pass.

Validation: compilation passed. Six local Selenium fixtures passed: plain text,
U+2063-marked text with nested bold spans, and mixed/non-breaking whitespace, each
in inline and iframe editors. Fixtures checked the actual split boundary as a
regression check, the N to N+1 count, and Undo restoration. The production TC_19
assertion remains count-only. Live sandbox execution is not claimed.
