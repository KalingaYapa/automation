# Automation test case catalog

This catalog describes the test cases in `src/test/java/e_testCases`. Names and IDs match the TestNG descriptions. Expected results describe the assertions, not a claim that every test has passed.

## Maintenance

Whenever a test is added, update this file in the same change with its ID, name, objective and expected result. Keep entries aligned when tests are renamed, changed, disabled or removed. Include the automation method so each entry can be traced to its implementation. New application test IDs should continue after TC_24; the sample template has its own overlapping IDs.

## Document editor — Ab_DocumentEditorTC

Each enabled test starts with a fresh browser, logs in, selects IATA, opens Authoring File List and opens the configured document editor. TC_01–TC_12 and TC_16 are currently commented out; TC_13–TC_15 and TC_17–TC_19 are enabled. A suite must include a test for it to execute.

The title-bar cases check labels and availability only. They do not perform Save, Reset File, Validate File or Check In. Formatting and paragraph-split tests undo their changes and do not save the document.

| ID | Test case name | Automation method | Objective | Expected result |
| --- | --- | --- | --- | --- |
| TC_01 | Verify the document title is visible | `verifyDocumentTitleIsDisplayed` | Confirm the title bar is shown. | The document title is visible. |
| TC_02 | Verify the document title text | `verifyDocumentTitleText` | Confirm the correct document is open. | The title equals expectedDocumentTitle from config.properties. |
| TC_03 | Verify the saved time is visible | `verifySavedTimeIsDisplayed` | Confirm the saved-time indicator is shown. | The Last Saved indicator is visible. |
| TC_04 | Verify the saved time format | `verifySavedTimeFormat` | Check the saved-time display format. | The indicator matches savedTimePattern: Last Saved followed by a 12-hour time and AM/PM. |
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
| TC_19 | Verify Enter increases the section paragraph count by one | `verifyEnterIncreasesParagraphCount` | Place the caret at the configured paragraph split point and press Enter. | The target section has one more paragraph row in Document Structure than its initial count. Cleanup uses Undo to restore the initial count. |

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

## Sample template — Aa_SampleTC

These five starter examples use a sample screen, not the IATA editor. Their TC_01–TC_05 IDs overlap with the editor IDs; identify them by class and automation method.

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
