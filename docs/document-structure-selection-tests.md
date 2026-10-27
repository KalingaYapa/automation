# Paragraph selection through Document Structure

Manual TC-018 maps to framework **TC_41**, `para.Para1TestCase.verifyDocumentStructureParagraphSelection`, with `Para/P2-Para` groups. Existing framework TC_18 remains the separate heading-selection case.

## Confirmed live elements

- Document Structure paragraph rows: `//*[@id='content-scroll']//li[@name='p']`.
- Click the row's scoped `contentBlockListItemName` span through `BasePage.elementClick`.
- The clicked row's runtime `id` maps to the VE paragraph's `inno_ref`; generated numeric references are never hardcoded.
- VE paragraphs: `//div[@data-element='p']` inside the visible `cke_wysiwyg_frame` iframe.
- The selected VE paragraph has `element-tag-color` and contains both browser-selection endpoints.
- Selected Element: `//*[@id='selectedElmName']` in default content.

The test checks multiple paragraphs, clicks the configured heading first and verifies `heading`, then selects a visible DS paragraph. It asserts `p` in the right panel and the exact matching reference in the highlighted VE paragraph. Selection readers do not modify selection. It returns to default content in finally; no document edits or Undo are required.

## Run locally

Use JDK 17+, Maven, Chrome and network access to the sandbox. Configure the existing local `src/test/resources/config.properties` with the login URL, test credentials, IATA project, allocated file and heading settings. Keep credential changes private. `headless=no` shows the local Chrome window.

```bash
python3 scripts/check_framework_standards.py
mvn test-compile
mvn test -DsuiteXmlFile=ElementSelectionTests.xml
```

The existing text-based cases require `editorParagraphText` to match the saved document exactly, including spaces after punctuation. This DS case identifies its paragraph from the live row reference.

The selection suite runs three tests: framework TC_17, TC_18 and TC_41. For only this new case:

```bash
mvn test -DsuiteXmlFile=DocumentStructureSelectionTests.xml
```

The existing BaseClass opens Chrome. Each test calls shared login → IATA → Authoring File List → configured allocation → Edit navigation; HomePage switches to the new editor window. The test owns iframe entry and default-content restoration explicitly.

`ParagraphTests.xml` includes all 15 paragraph cases. `DocumentEditor.xml`, `testng.xml` and `Master.xml` select the complete Para class and automatically include this case.

Check executed counts, failures and skips in `target/surefire-reports/`, HTML/Excel output in `reports/`, and INFO/DEBUG start, selection, assertion and finish messages in both the terminal and `logs/Automation.log`.

## Local verification

Compilation and the framework standards check passed. The new case passed against the live sandbox. The three-case selection suite passed with zero failures/skips after supplying the live paragraph text as a temporary override:

```bash
mvn test -DsuiteXmlFile=ElementSelectionTests.xml '-DeditorParagraphText=Close the flight in the Handling system. By this action, the (X)FFM is transmitted.'
```

Without that override, the two older cases timed out because the local configured text contained `system.By` while the saved document contained `system. By`. The configuration file was not changed. INFO/DEBUG action, observed-result, assertion, cleanup and successful-finish messages were verified in both terminal output and `logs/Automation.log`. SonarQube analysis was not run; this repository has no configured server/project analysis.
