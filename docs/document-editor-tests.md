# Document editor tests

The 37 editor-related cases are split between `documenteditor.DocumentEditor1TestCase` (22) and `para.Para1TestCase` (15). Their actions and checks live in the matching `DocumentEditorPageObjects1` and `ParaPageObjects1` Page Objects. All editor cases are enabled; detailed IDs, objectives and expected results are in [the catalog](test-case-catalog.md).

## Scope

- TC_01–TC_04: title visibility/text and saved-time visibility/format. The saved-time value may initially be blank; any displayed timestamp must have the configured 12-hour format.
- TC_05–TC_12: Save, Reset File, Validate File and Check In labels and enabled state. These tests do not click those buttons or verify persistence, reset, schema validation or check-in.
- TC_13–TC_14: existing Bold state after a native single/double click on Depending.
- TC_15–TC_16: apply explicit Bold to the heading word and verify Undo restores it.
- TC_17–TC_18: Selected Element changes between heading and p after native plain-text clicks.
- TC_19: Enter splits the target paragraph; the target section's paragraph count increases by one, then Undo restores the original count.
- TC_25–TC_26: apply Italic and Underline; check toolbar state and actual rendered formatting/markup, then Undo.
- TC_27: apply Bold, Undo, Redo and verify Bold is restored; final Undo removes the change.
- TC_28–TC_29: convert the target paragraph to unordered/ordered list items; verify toolbar and list structure, then Undo.
- TC_39 (manual TC-015): toolbar Copy/Paste between paragraphs, source preservation and Undo cleanup.
- TC_40 (manual TC-016): Save a paragraph edit, reload and verify persistence; save the original paragraph back and reload to verify restoration.

- TC_41 (manual TC-018): click a Document Structure paragraph and verify its exact VE selection/highlight and the right-panel p label.

- TC_42 (manual TC-017): switch VE → Code View → VE, verify source XML text and exact preservation of the complete VE markup.

## Setup and lifecycle

Set your local sandbox username/password and appURL in `src/test/resources/config.properties`; never commit credentials. Confirm the configured IATA project, allocation path, document title, heading text/word and paragraph text match the saved sandbox document. The formatting word must initially lack explicit Bold, Italic and Underline. The paragraph must initially be outside a list. Use an editable allocation and avoid simultaneous edits to that document.

`a_testbase.BaseClass` creates a fresh browser before each test and closes it after the method. Each editor/paragraph test method explicitly calls `ReusableMethods.loginAndSearchFileClickEdit()`, which uses `d_loginpage.HomePage` for login → IATA → Authoring File List → configured allocation Edit. Navigation happens once per test, including each independently selected case.

Shared navigation waits for CKEditor readiness. Each test explicitly enters and leaves its editing iframe. Invisible editor markers and nested formatting spans are handled when matching text. Keep configured paragraph text aligned with the actual saved text, including spaces after punctuation.

Selected Element checks read `#selectedElmName` in the main page; they do not open or modify the dropdown. They click direct plain-text words (`paragraphSelectionWord=flight`, `headingSelectionWord=Flight`) to avoid selecting nested Bold or the adjacent heading number. Bold/Italic/Underline active state requires the command's `cke_button_on` state, not merely an enabled icon. Undo/Redo wait for enabled commands before native clicks.

Most editing cases reverse only their own changes with Undo. TC_40 explicitly tests Save, then restores and saves only its target paragraph because reload clears Undo history. Cases never Reset File or Accept Change. A failing precondition should be investigated rather than bypassed.

## Run

```bash
mvn clean test -DsuiteXmlFile=DocumentEditor.xml
mvn clean test -DsuiteXmlFile=BoldTests.xml
mvn clean test -DsuiteXmlFile=ElementSelectionTests.xml
mvn clean test -DsuiteXmlFile=FormattingTests.xml
mvn clean test -DsuiteXmlFile=ClipboardTests.xml
mvn clean test -DsuiteXmlFile=SaveTests.xml
```

`DocumentEditor.xml` runs all 37 editor cases. The other suites select TC_13–TC_15, TC_17–TC_18 plus TC_41, and TC_25–TC_29 respectively; ClipboardTests.xml selects TC_39 and SaveTests.xml selects TC_40. `mvn clean test` defaults to `testng.xml` and runs all 42 application cases, including the five portal cases. `Master.xml` is an equivalent compatibility suite.

Editor-level groups are `DocumentEditor` plus `P2-DocumentEditor` for High or `P3-DocumentEditor` for Medium; paragraph groups are `Para` and `P2-Para`. All suites use the standard `RetryMultipleFailedTestcases` test block and updated class references. Retry remains attached by the framework listener, with retry limit zero by default.

Maven now fails when tests fail. Check executed counts, failures, skipped tests and setup errors in `target/surefire-reports/` and `reports/ExtentReport.html`. Logs are in `logs/Automation.log` and screenshots in `screenshots/`. A CDP-version warning alone is not evidence that a WebDriver test failed.

See [terminal instructions](run-formatting-tests-terminal.md), [the restructuring guide](framework-restructuring.md) and [the writing standard](test-writing-guide.md).

## Team lead paragraph cases

TC_19 now verifies split text and restoration after Undo as well as count. New
TC_30–TC_38 cover Enter at either boundary, paragraph Redo and six paragraph word
formatting scenarios. Existing list cases are reused. Run
`mvn clean test -DsuiteXmlFile=ParagraphTests.xml` for the supplied scope. See
[the mapping and duplicate decisions](team-lead-paragraph-cases.md).

The latest structure follows [the supplied sample](sample-format-alignment.md): XPath `@FindBy` fields, navigation and frame changes visible in every test, and `Para/P2-Para` groups for paragraph cases.

See [Copy/Paste instructions](clipboard-tests.md) and [Save persistence instructions](save-tests.md) for their configuration and cleanup.
