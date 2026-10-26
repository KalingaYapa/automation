# Run the Uppercase test (TC-040)

TC-040 is recorded as TC_40 in the framework description/catalog. Automation ID: `verifyUppercaseChangesOnlySelectedParagraphWord`, in `para.Para1TestCase`, groups `Para` and `P2-Para`. TC_39 remains unallocated.

1. Configure local `src/test/resources/config.properties` with the sandbox URL/login and existing project/file settings. Keep credentials out of Git.
2. Set `editorParagraphText` to the current complete paragraph text after removing invisible editor markers. Set `paragraphFormatWord` to a unique word containing lowercase letters (for example `flight`). The target paragraph and word must each match exactly; do not overwrite other users' content to establish this precondition.
3. From the repository root, run:

   ```bash
   mvn clean test -DsuiteXmlFile=CaseChangeTests.xml
   ```

The test logs in through the existing navigation, selects the word, opens Case Change, selects Uppercase and compares the complete paragraph against the original with just that word converted. Its finally block clicks Undo and verifies restoration. It never saves the file. The normal default and DocumentEditor suites include it automatically; ParagraphTests.xml also includes it explicitly.

Check `target/surefire-reports`, the generated framework reports and `logs/Automation.log`. Expect one executed test, no failures and no skips in the focused suite. A success log appears only after the Uppercase assertion and Undo restoration succeed.

## Validation

On 27 October 2026 (Singapore), the focused sandbox run executed one test: one passed, zero failures/errors/skips. It verified word-only conversion and Undo restoration; INFO/DEBUG logs were present in console and `logs/Automation.log`. Compilation and `scripts/check_framework_standards.py` passed. The complete regression suite and SonarQube analysis were not run for this addition.

The live sandbox fixture differed from the committed example paragraph (it contained `CLOSE` and `HANDLING`). Validation supplied that current text through a temporary generated configuration; source configuration and saved document content were unchanged. Set your local `editorParagraphText` to the current paragraph before running.
