# Run the Save persistence test

Manual **TC-016** maps to framework **TC_40**, Automation ID `verifySavedParagraphChangesPersistAfterReload` in `para.Para1TestCase`. Existing TC_16 is the Undo test and remains unchanged. This case follows the `Para` / `P2-Para` groups, XPath Page Objects, explicit navigation/frame steps and INFO/DEBUG logging.

## Run from the terminal

1. Configure your sandbox login, project and document allocation in your local `src/test/resources/config.properties`. The allocation must be editable and allow saving.
2. Set `saveParagraphAnchor` to a unique visible phrase identifying the existing paragraph. The default is `These messages contain details of their shipments`. Full original text and markup are captured at runtime; this case does not rely on `editorParagraphText`.
3. From the repository root run:

   ```bash
   mvn clean test -DsuiteXmlFile=SaveTests.xml
   ```

Expect one executed test with no failures or skips. It also belongs to `ParagraphTests.xml`, `DocumentEditor.xml` and the default suite.

## What it verifies

The test appends a unique `[savecheck-…]` marker using native keyboard input, clicks the real top-right Save button, and waits for the **Saved** success heading and completed save state. The timestamp alone does not prove success. It reloads the editor, waits for CKEditor readiness, and asserts that the complete paragraph still equals the original text plus this run's marker.

## Cleanup

Reload clears Undo history. In a finally block, the test checks that the target paragraph still has its expected test text (or its original text), restores only that paragraph's captured markup through the editor DOM API, and clicks the real Save button again. Another reload verifies that the original paragraph text persisted. It does not reset the file or accept tracked changes, and it refuses to overwrite unexpected paragraph text. Cleanup failures are reported without hiding an earlier scenario failure.

This case performs two real saves; timestamps and version history record them even after the paragraph content is restored. Run it against the intended sandbox allocation without concurrent editing of that file.

Check `target/surefire-reports`, framework reports and `logs/Automation.log` for the Save, persistence and restoration results. Success is logged only after the saved restoration has been verified.

## Validation

Validated against the sandbox in Chrome on 27 October 2026. The focused Save test passed, followed by a fresh-browser run of Save persistence, Copy/Paste and portal login: three executed, zero failures/errors/skips. Both modified-text persistence and saved restoration passed. Console/file INFO and DEBUG output, compilation and `scripts/check_framework_standards.py` passed. The complete 40-case suite and SonarQube analysis were not run for this addition.
