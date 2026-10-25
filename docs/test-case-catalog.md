# Automation test case catalog

All 38 application cases follow the PDF naming/grouping standard and are enabled. Run them with `mvn clean test`; see [the restructuring guide](framework-restructuring.md).

This catalog describes the application tests in `src/test/java/documenteditor` and `src/test/java/portal`, followed by archived starter examples. All 38 application cases include INFO start/result/finish logs and DEBUG step/assertion/cleanup logs, enabled in the terminal and logs/Automation.log. Names and IDs match the TestNG descriptions. Expected results describe the assertions, not a claim that every test has passed.

## Maintenance

Whenever a test is added, update this file in the same change with its ID, name, objective and expected result. Keep entries aligned when tests are renamed, changed, disabled or removed. Include the automation method so each entry can be traced to its implementation. New application test IDs should continue after TC_38; the sample template has its own overlapping IDs.

## Document editor — DocumentEditor1TestCase

Each enabled test starts with a fresh browser, logs in, selects IATA, opens Authoring File List and opens the configured document editor. All 38 application cases (33 editor cases and 5 portal cases) are enabled; no application tests are commented out. A suite must include a test for it to execute.

The title-bar cases check labels and availability only. They do not perform Save, Reset File, Validate File or Check In. Formatting and paragraph-split tests undo their changes and do not save the document.

| ID | Test case name | Automation method | Objective | Expected result |
| --- | --- | --- | --- | --- |
| TC_01 | Verify the document title is visible | `verifyDocumentTitleIsDisplayed` | Confirm the title bar is shown. | The document title is visible. |
| TC_02 | Verify the document title text | `verifyDocumentTitleText` | Confirm the correct document is open. | The title equals expectedDocumentTitle from config.properties. |
| TC_03 | Verify the saved time is visible | `verifySavedTimeIsDisplayed` | Confirm the saved-time indicator is shown. | The Last Saved indicator is visible. |
| TC_04 | Verify saved time is blank initially or has the expected format | `verifySavedTimeFormat` | Check the initially blank saved-time state or the format of an available timestamp. | Blank is valid before a save. A nonblank value must match savedTimePattern: Last Saved followed by a 12-hour time and AM/PM. |
| TC_05 | Verify the Save button label | `verifySaveButtonLabel` | Check the Save control label. | The visible button reads Save. |
| TC_06 | Verify the Save button is enabled | `verifySaveButtonIsEnabled` | Check Save is available for the editable document. | Save is enabled. |
| TC_07 | Verify the Reset File button label | `verifyResetButtonLabel` | Check the Reset File control label. | The visible button reads Reset File. |
| TC_08 | Verify the Reset File button is enabled | `verifyResetButtonIsEnabled` | Check Reset File is available for the editable document. | Reset File is enabled. |
| TC_09 | Verify the Validate File button label | `verifyValidateButtonLabel` | Check the Validate File control label. | The visible button reads Validate File. |
| TC_10 | Verify the Validate File button is enabled | `verifyValidateButtonIsEnabled` | Check Validate File is available for the editable document. | Validate File is enabled. |
| TC_11 | Verify the Check In button label | `verifyCheckInButtonLabel` | Check the Check In control label. | The visible button reads Check In. |
| TC_12 | Verify the Check In button is enabled | `verifyCheckInButtonIsEnabled` | Check Check In is available for the editable document. | Check In is enabled. |
| TC_13 | Verify Bold is active after a single click in bold text | `verifyBoldIsActiveAfterSingleClick` | Check toolbar formatting after a single click inside the already-bold word Depending. | The caret is inside Depending and Bold has the active cke_button_on state. |
| TC_14 | Verify Bold is active after double-clicking bold text | `verifyBoldIsActiveAfterDoubleClick` | Check toolbar formatting after double-clicking the already-bold word Depending. | Depending is selected and Bold has the active cke_button_on state. |
| TC_15 | Verify applying Bold to a selected heading word | `verifyApplyingBoldToHeadingWord` | Apply Bold to the configured heading word with no explicit bold formatting. | Bold becomes active and the selected word has explicit bold markup and bold rendered weight. Cleanup uses Undo. |
| TC_16 | Verify Undo restores heading formatting after applying Bold | `verifyUndoRestoresHeadingFormatting` | Apply Bold, then verify the Undo operation restores the heading word. | Bold becomes inactive and the original heading text, rendered font weight and explicit formatting snapshot are restored. |
| TC_17 | Verify selecting a paragraph shows p | `verifyParagraphSelectionShowsParagraphType` | Start on the heading, then single-click plain text in the paragraph. | Selected Element changes from heading to p. |
| TC_18 | Verify selecting a heading shows heading | `verifyHeadingSelectionShowsHeadingType` | Start on the paragraph, then single-click plain text in the heading. | Selected Element changes from p to heading. |
| TC_19 | Verify Enter splits paragraph text and Undo restores it | `verifyEnterIncreasesParagraphCount` | Press Enter at the configured middle boundary, verify split text/count, then Undo. | Both text portions occur in the correct paragraph order, section paragraph count increases by one, other paragraphs remain unchanged, and Undo restores the original text sequence/count. Covers lead TC-001 and TC-004. |

## Portal login and navigation

These five cases are enabled and are selected by `PortalNavigation.xml`. Each performs its own prerequisites.

| ID | Test case name | Automation method | Objective | Expected result |
| --- | --- | --- | --- | --- |
| TC_20 | Verify login with the sandbox test account | `verifySandboxLoginOpensPortal` | Log in with the configured sandbox test account. | The authenticated portal displays its project selector. |

| ID | Test case name | Automation method | Objective | Expected result |
| --- | --- | --- | --- | --- |
| TC_21 | Verify selecting IATA from the project dropdown | `verifySelectingIataProject` | Select the configured IATA project from the project dropdown. | The project dropdown displays the configured portalProject value (IATA). |
| TC_22 | Verify Authoring File List opens for IATA | `verifyAuthoringFileListOpens` | Open Authoring File List after selecting IATA. | The Authoring File List table is displayed. |

| ID | Test case name | Automation method | Objective | Expected result |
| --- | --- | --- | --- | --- |
| TC_23 | Verify the allocated document is listed | `verifyTargetDocumentIsListed` | Find the configured document allocation in Authoring File List. | The matching allocation row displays expectedDocumentTitle. |
| TC_24 | Verify Edit opens the allocated document editor | `verifyEditOpensDocumentEditor` | Click Edit for the configured document allocation. | The editor opens with a ready editing area and a title matching expectedDocumentTitle. |

## Editor formatting and lists — DocumentEditor1TestCase

TC_25–TC_29 use the existing editor setup, page object and test groups. Each starts with a fresh browser and opens the configured document. Run these five cases with `mvn clean test -DsuiteXmlFile=FormattingTests.xml`; they are also included by `DocumentEditor.xml`. These cases undo their own changes and never save or accept tracked changes.

| ID | Test case name | Automation method | Objective | Expected result |
| --- | --- | --- | --- | --- |
| TC_25 | Verify applying Italic to a selected heading word | `verifyApplyingItalicToHeadingWord` | Select the configured heading word, initially without italic formatting, and click Italic. | Italic becomes active and the selected word has italic markup with rendered italic font style. Undo removes the applied formatting. |
| TC_26 | Verify applying Underline to a selected heading word | `verifyApplyingUnderlineToHeadingWord` | Select the configured heading word, initially without underline formatting, and click Underline. | Underline becomes active and the selected word has underline markup with rendered underline decoration. Undo removes the applied formatting. |
| TC_27 | Verify Redo restores undone Bold formatting | `verifyRedoRestoresBoldFormatting` | Apply Bold to the configured heading word, undo it, then click Redo. | Undo removes explicit Bold formatting. Redo restores bold markup, rendered bold weight and the active Bold button. Final Undo removes the change. |
| TC_28 | Verify applying an unordered list to a paragraph | `verifyApplyingUnorderedListToParagraph` | Click plain text in the configured paragraph, initially outside a list, and click the bulleted-list button. | The unordered-list button becomes active and the unchanged paragraph text belongs to a list item inside an unordered list (ul). Undo restores a non-list paragraph. |
| TC_29 | Verify applying an ordered list to a paragraph | `verifyApplyingOrderedListToParagraph` | Click plain text in the configured paragraph, initially outside a list, and click the numbered-list button. | The ordered-list button becomes active and the unchanged paragraph text belongs to a list item inside an ordered list (ol). Undo restores a non-list paragraph. |

## Team lead paragraph additions — TC_30–TC_38

See [the lead-case mapping](team-lead-paragraph-cases.md) for duplicate decisions and all thirteen supplied scenarios. `ParagraphTests.xml` selects these nine additions, enhanced TC_19 and existing list cases TC_28/TC_29. All cases use the existing editor setup, standard groups and step logs, with Undo cleanup.

| ID | Test case name | Automation method | Objective | Expected result |
| --- | --- | --- | --- | --- |
| TC_30 | Verify Enter at the beginning of a paragraph | `verifyEnterAtParagraphBeginning` | Place the caret before the first visible character and press Enter. | An empty paragraph is inserted immediately before the unchanged target text; section paragraph count increases by one. Undo restores the original sequence/count. |
| TC_31 | Verify Enter at the end of a paragraph | `verifyEnterAtParagraphEnd` | Place the caret after the last visible character and press Enter. | An empty paragraph is inserted immediately after the unchanged target text; section paragraph count increases by one. Undo restores the original sequence/count. |
| TC_32 | Verify Redo restores the undone paragraph split | `verifyRedoRestoresParagraphSplit` | Split the paragraph, verify it, Undo, then Redo. | Undo restores the original text/count; Redo restores the same two text portions and increased count. Final Undo restores the original. |
| TC_33 | Verify Bold affects only the selected paragraph word | `verifyApplyingBoldToParagraphWord` | Select the configured plain paragraph word and apply Bold. | The selected word gains bold font weight; the matching toolbar button is active. Paragraph text and every unselected character style remain unchanged. Undo restores the original character snapshot. |
| TC_34 | Verify Italic affects only the selected paragraph word | `verifyApplyingItalicToParagraphWord` | Select the configured plain paragraph word and apply Italic. | The selected word gains italic font style; the matching toolbar button is active. Paragraph text and every unselected character style remain unchanged. Undo restores the original character snapshot. |
| TC_35 | Verify Underline affects only the selected paragraph word | `verifyApplyingUnderlineToParagraphWord` | Select the configured plain paragraph word and apply Underline. | The selected word gains underline decoration; the matching toolbar button is active. Paragraph text and every unselected character style remain unchanged. Undo restores the original character snapshot. |
| TC_36 | Verify Subscript affects only the selected paragraph word | `verifyApplyingSubscriptToParagraphWord` | Select the configured plain paragraph word and apply Subscript. | The selected word gains a smaller font and subscript vertical alignment; the matching toolbar button is active. Paragraph text and every unselected character style remain unchanged. Undo restores the original character snapshot. |
| TC_37 | Verify Superscript affects only the selected paragraph word | `verifyApplyingSuperscriptToParagraphWord` | Select the configured plain paragraph word and apply Superscript. | The selected word gains a smaller font and superscript vertical alignment; the matching toolbar button is active. Paragraph text and every unselected character style remain unchanged. Undo restores the original character snapshot. |
| TC_38 | Verify Strikethrough affects only the selected paragraph word | `verifyApplyingStrikethroughToParagraphWord` | Select the configured plain paragraph word and apply Strikethrough. | The selected word gains line-through decoration; the matching toolbar button is active. Paragraph text and every unselected character style remain unchanged. Undo restores the original character snapshot. |

## Archived sample template — Aa_SampleTC

These five starter examples are archived as `.java.example` reference files under `docs/templates` and are excluded from compilation and execution. They use a sample screen, not the IATA editor. Their TC_01–TC_05 IDs overlap with the editor IDs; identify them by class and automation method.

| ID | Test case name | Automation method | Objective | Expected result |
| --- | --- | --- | --- | --- |
| TC_01 | Verify the screen is displayed on opening it | `verifyScreenIsDisplayed` | Open the sample screen. | The results table is displayed. |
| TC_02 | Verify the screen heading is displayed | `verifyScreenHeadingIsDisplayed` | Check the sample screen heading. | The heading reads Sample Screen. |
| TC_03 | Verify the table column headers are displayed in order | `verifyTableColumnHeaders` | Check the sample table column order. | The headers are ID, Name, Status, Created On, in that order. |
| TC_04 | Verify searching a value returns only the matching rows | `verifySearchReturnsMatchingRows` | Search using sampleSearchValue. | At least one row is returned and every value in the checked column contains the search term, ignoring case. |
| TC_05 | Verify the no-result message for a value that does not exist | `verifyNoResultMessageForUnknownValue` | Search using sampleInvalidSearchValue. | The empty-table message reads No matching records found. |

## Related guides

- [Document editor setup and test details](document-editor-tests.md)
- [Portal navigation setup and test details](portal-navigation-tests.md)
