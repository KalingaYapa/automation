# Run the toolbar Copy and Paste test

Manual case **TC-015** maps to framework **TC_39** and Automation ID `verifyCopyAndPasteBetweenParagraphs` in `para.Para1TestCase`. Existing framework TC_15 is the Bold test and retains its ID. This case uses the standard `Para` / `P2-Para` groups, explicit navigation/frame steps, XPath Page Objects and INFO/DEBUG logging.

## Setup and execution

1. Install the project's supported Java and Maven versions and Chrome, using the existing local setup guide. Configure your private sandbox login, project and document allocation in `src/test/resources/config.properties`.
2. Keep `allowClipboard=true` for this test. Selenium grants clipboard read/write permission for the application in the temporary Chromium test browser. It does not change your everyday browser profile. `-DallowClipboard=false` disables the grant; toolbar Paste requires permission and is expected to fail when denied. This scenario is validated on Chrome.
3. Configure `clipboardSourceParagraphAnchor` and `clipboardDestinationParagraphAnchor` as unique visible substrings identifying two different paragraphs. The defaults locate the opening flight paragraph and the paragraph beginning “These messages contain details of their shipments”. Invisible editor markers are ignored. Their complete text is captured at runtime; `editorParagraphText` is not used by this case.
4. Set `paragraphFormatWord` to a unique selectable word in the source paragraph (default: `flight`). The word must be inside a single text node, as required by the existing selection helper.
5. From the repository root run:

   ```bash
   mvn clean test -DsuiteXmlFile=ClipboardTests.xml
   ```

The focused suite contains one test. It is also included in `ParagraphTests.xml`, `DocumentEditor.xml` and the default suite.

## Assertions and cleanup

The test selects the configured word through the editor selection model and clicks the actual toolbar **Copy** button. It places the caret at the start of the other paragraph, then clicks the actual toolbar **Paste** button. It does not type replacement text or supply artificial clipboard contents.

The destination must equal the copied word followed by the complete original destination text, and the source must equal its original text. No separator is typed by the test. Finally, it undoes its paste if the destination changed and verifies both original paragraphs. It never saves or resets the document. Check `target/surefire-reports` and `logs/Automation.log` for the result and step logs.

## Validation

Validated on 27 October 2026 against the sandbox in Chrome. The focused Copy/Paste run passed, and the final fresh-browser run passed both Copy/Paste and the portal login check: two executed, zero failures/errors/skips. Source preservation, exact destination text and Undo restoration passed. INFO/DEBUG output was verified in both console and `logs/Automation.log`; compilation and the framework standards checker passed. The complete regression suite and SonarQube analysis were not run for this addition.
