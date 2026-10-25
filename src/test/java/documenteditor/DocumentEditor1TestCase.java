package documenteditor;

import org.testng.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import a_testbase.BaseClass;
import documenteditor.DocumentEditorPageObjects1.ParagraphFormat;

/** Each test independently logs in and opens the allocated document. */
public class DocumentEditor1TestCase extends BaseClass {
    private static final Logger LOGGER = LogManager.getLogger(DocumentEditor1TestCase.class);
    private static final String STARTING_TEST_MSG = "Starting test: {}";
    private static final String FINISHED_TEST_MSG = "Finished test: {} - PASSED";

    private static final String SELECTING_HEADING_WORD_MSG = "Selecting the configured heading word";
    private static final String SELECTED_HEADING_WORD_MSG = "Completed: selecting the configured heading word";
    private static final String CHECKING_BOLD_OFF_MSG = "Checking that explicit Bold is initially off";
    private static final String CHECKED_BOLD_OFF_MSG = "Completed: checking that explicit Bold is initially off";
    private static final String APPLYING_BOLD_MSG = "Applying Bold to the selected heading word";
    private static final String APPLIED_BOLD_MSG = "Completed: applying Bold to the selected heading word";
    private static final String UNDOING_BOLD_MSG = "Undoing this test's Bold change";
    private static final String UNDONE_BOLD_MSG = "Completed: undoing this test's Bold change";
    private static final String WAITING_FOR_SELECTED_ELEMENT_MSG = "Waiting for the Selected Element value";
    private static final String SELECTED_ELEMENT_UPDATED_MSG = "Completed: waiting for the Selected Element value";
    private static final String CLICKING_PARAGRAPH_MSG = "Clicking plain text in the target paragraph";
    private static final String CLICKED_PARAGRAPH_MSG = "Completed: clicking plain text in the target paragraph";
    private static final String INITIAL_PARAGRAPH_COUNT_MSG = "Initial target section paragraph count: {}";
    private static final String PARAGRAPH_TEXT_AFTER_ENTER_MSG = "Paragraph text sequence after Enter: {}";
    private static final String MODULE_GROUP = "DocumentEditor";
    private static final String HIGH_PRIORITY_GROUP = "P2-DocumentEditor";
    private static final String HEADING_TYPE = "heading";
    private static final String PARAGRAPH_TYPE = "p";

    /** TestNG runs the superclass browser setup before this subclass setup. */
    @BeforeMethod(alwaysRun = true)
    public void openAllocatedEditor() {
        LOGGER.debug("Logging in and opening the configured allocation editor");
        b_utilities.ReusableMethods.loginAndSearchFileClickEdit();
        LOGGER.debug("Login and allocation editor setup completed");
    }

    private DocumentEditorPageObjects1 editor() {
        LOGGER.debug("Initialising document editor page object");
        return new DocumentEditorPageObjects1(getDriver());
    }
    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_01 | Verify the document title is visible")
    public void verifyDocumentTitleIsDisplayed() {
        LOGGER.info(STARTING_TEST_MSG, "verifyDocumentTitleIsDisplayed");
        LOGGER.debug("Reading observed result: Document title visible");
        boolean actualResult = editor().isDocumentTitleDisplayed();
        LOGGER.info("Document title visible: {}", actualResult);
        LOGGER.debug("Asserting expected result: Document title visible");
        Assert.assertTrue(actualResult, "The document title should be visible");
        LOGGER.info(FINISHED_TEST_MSG, "verifyDocumentTitleIsDisplayed");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_02 | Verify the document title text")
    public void verifyDocumentTitleText() {
        LOGGER.info(STARTING_TEST_MSG, "verifyDocumentTitleText");
        LOGGER.debug("Reading observed result: Document title");
        String actualResult = editor().getDocumentTitle();
        LOGGER.info("Document title: {}", actualResult);
        LOGGER.debug("Asserting expected result: Document title");
        Assert.assertEquals(actualResult, getConfig("expectedDocumentTitle"),
                "The title bar should identify the expected document");
        LOGGER.info(FINISHED_TEST_MSG, "verifyDocumentTitleText");
    }

    @Test(groups = { MODULE_GROUP, "P3-DocumentEditor" }, description = "TC_03 | Verify the saved time is visible")
    public void verifySavedTimeIsDisplayed() {
        LOGGER.info(STARTING_TEST_MSG, "verifySavedTimeIsDisplayed");
        LOGGER.debug("Reading observed result: Saved-time indicator visible");
        boolean actualResult = editor().isSavedTimeDisplayed();
        LOGGER.info("Saved-time indicator visible: {}", actualResult);
        LOGGER.debug("Asserting expected result: Saved-time indicator visible");
        Assert.assertTrue(actualResult, "The saved-time indicator should be visible");
        LOGGER.info(FINISHED_TEST_MSG, "verifySavedTimeIsDisplayed");
    }

    @Test(groups = { MODULE_GROUP, "P3-DocumentEditor" }, description = "TC_04 | Verify saved time is blank initially or has the expected format")
    public void verifySavedTimeFormat() {
        LOGGER.info(STARTING_TEST_MSG, "verifySavedTimeFormat");
        String actual = editor().getSavedTimeText();
        LOGGER.info("Saved-time indicator text: '{}'", actual);
        LOGGER.debug("Reading observed result: Saved-time value satisfies the agreed initial-state/format requirement");
        boolean actualResult = actual.isBlank() || actual.matches(getConfig("savedTimePattern"));
        LOGGER.info("Saved-time value satisfies the agreed initial-state/format requirement: {}", actualResult);
        LOGGER.debug("Asserting expected result: Saved-time value satisfies the agreed initial-state/format requirement");
        Assert.assertTrue(actualResult,
                "Saved time may be blank initially; otherwise expect Last Saved followed by a 12-hour time and AM/PM. Actual: '"
                        + actual + "'");
        LOGGER.info(FINISHED_TEST_MSG, "verifySavedTimeFormat");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_05 | Verify the Save button label")
    public void verifySaveButtonLabel() {
        LOGGER.info(STARTING_TEST_MSG, "verifySaveButtonLabel");
        LOGGER.debug("Reading observed result: Save button label");
        String actualResult = editor().getSaveButtonText();
        LOGGER.info("Save button label: {}", actualResult);
        LOGGER.debug("Asserting expected result: Save button label");
        Assert.assertEquals(actualResult, "Save",
                "The visible Save button should display its expected label");
        LOGGER.info(FINISHED_TEST_MSG, "verifySaveButtonLabel");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_06 | Verify the Save button is enabled")
    public void verifySaveButtonIsEnabled() {
        LOGGER.info(STARTING_TEST_MSG, "verifySaveButtonIsEnabled");
        LOGGER.debug("Reading observed result: Save button enabled");
        boolean actualResult = editor().isSaveButtonEnabled();
        LOGGER.info("Save button enabled: {}", actualResult);
        LOGGER.debug("Asserting expected result: Save button enabled");
        Assert.assertTrue(actualResult,
                "The Save button should be enabled for the editable test document");
        LOGGER.info(FINISHED_TEST_MSG, "verifySaveButtonIsEnabled");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_07 | Verify the Reset File button label")
    public void verifyResetButtonLabel() {
        LOGGER.info(STARTING_TEST_MSG, "verifyResetButtonLabel");
        LOGGER.debug("Reading observed result: Reset File button label");
        String actualResult = editor().getResetButtonText();
        LOGGER.info("Reset File button label: {}", actualResult);
        LOGGER.debug("Asserting expected result: Reset File button label");
        Assert.assertEquals(actualResult, "Reset File",
                "The visible Reset File button should display its expected label");
        LOGGER.info(FINISHED_TEST_MSG, "verifyResetButtonLabel");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_08 | Verify the Reset File button is enabled")
    public void verifyResetButtonIsEnabled() {
        LOGGER.info(STARTING_TEST_MSG, "verifyResetButtonIsEnabled");
        LOGGER.debug("Reading observed result: Reset File button enabled");
        boolean actualResult = editor().isResetButtonEnabled();
        LOGGER.info("Reset File button enabled: {}", actualResult);
        LOGGER.debug("Asserting expected result: Reset File button enabled");
        Assert.assertTrue(actualResult,
                "The Reset File button should be enabled for the editable test document");
        LOGGER.info(FINISHED_TEST_MSG, "verifyResetButtonIsEnabled");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_09 | Verify the Validate File button label")
    public void verifyValidateButtonLabel() {
        LOGGER.info(STARTING_TEST_MSG, "verifyValidateButtonLabel");
        LOGGER.debug("Reading observed result: Validate File button label");
        String actualResult = editor().getValidateButtonText();
        LOGGER.info("Validate File button label: {}", actualResult);
        LOGGER.debug("Asserting expected result: Validate File button label");
        Assert.assertEquals(actualResult, "Validate File",
                "The visible Validate File button should display its expected label");
        LOGGER.info(FINISHED_TEST_MSG, "verifyValidateButtonLabel");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_10 | Verify the Validate File button is enabled")
    public void verifyValidateButtonIsEnabled() {
        LOGGER.info(STARTING_TEST_MSG, "verifyValidateButtonIsEnabled");
        LOGGER.debug("Reading observed result: Validate File button enabled");
        boolean actualResult = editor().isValidateButtonEnabled();
        LOGGER.info("Validate File button enabled: {}", actualResult);
        LOGGER.debug("Asserting expected result: Validate File button enabled");
        Assert.assertTrue(actualResult,
                "The Validate File button should be enabled for the editable test document");
        LOGGER.info(FINISHED_TEST_MSG, "verifyValidateButtonIsEnabled");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_11 | Verify the Check In button label")
    public void verifyCheckInButtonLabel() {
        LOGGER.info(STARTING_TEST_MSG, "verifyCheckInButtonLabel");
        LOGGER.debug("Reading observed result: Check In button label");
        String actualResult = editor().getCheckInButtonText();
        LOGGER.info("Check In button label: {}", actualResult);
        LOGGER.debug("Asserting expected result: Check In button label");
        Assert.assertEquals(actualResult, "Check In",
                "The visible Check In button should display its expected label");
        LOGGER.info(FINISHED_TEST_MSG, "verifyCheckInButtonLabel");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_12 | Verify the Check In button is enabled")
    public void verifyCheckInButtonIsEnabled() {
        LOGGER.info(STARTING_TEST_MSG, "verifyCheckInButtonIsEnabled");
        LOGGER.debug("Reading observed result: Check In button enabled");
        boolean actualResult = editor().isCheckInButtonEnabled();
        LOGGER.info("Check In button enabled: {}", actualResult);
        LOGGER.debug("Asserting expected result: Check In button enabled");
        Assert.assertTrue(actualResult,
                "The Check In button should be enabled for the editable test document");
        LOGGER.info(FINISHED_TEST_MSG, "verifyCheckInButtonIsEnabled");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_13 | Verify Bold is active after a single click in bold text")
    public void verifyBoldIsActiveAfterSingleClick() {
        LOGGER.info(STARTING_TEST_MSG, "verifyBoldIsActiveAfterSingleClick");
        DocumentEditorPageObjects1 page = editor();
        LOGGER.debug("Single-clicking the existing bold word");
        page.clickBoldWord();
        LOGGER.debug("Completed: single-clicking the existing bold word");
        LOGGER.debug("Reading observed result: Bold active after single click");
        boolean actualResult = page.isBoldFormattingActive();
        LOGGER.info("Bold active after single click: {}", actualResult);
        LOGGER.debug("Asserting expected result: Bold active after single click");
        Assert.assertTrue(actualResult,
                "Bold should be active with the caret inside the bold word");
        LOGGER.info(FINISHED_TEST_MSG, "verifyBoldIsActiveAfterSingleClick");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_14 | Verify Bold is active after double-clicking bold text")
    public void verifyBoldIsActiveAfterDoubleClick() {
        LOGGER.info(STARTING_TEST_MSG, "verifyBoldIsActiveAfterDoubleClick");
        DocumentEditorPageObjects1 page = editor();
        LOGGER.debug("Double-clicking the existing bold word");
        page.doubleClickBoldWord();
        LOGGER.debug("Completed: double-clicking the existing bold word");
        LOGGER.debug("Reading observed result: Bold active after double click");
        boolean actualResult = page.isBoldFormattingActive();
        LOGGER.info("Bold active after double click: {}", actualResult);
        LOGGER.debug("Asserting expected result: Bold active after double click");
        Assert.assertTrue(actualResult,
                "Bold should be active when the bold word is selected");
        LOGGER.info(FINISHED_TEST_MSG, "verifyBoldIsActiveAfterDoubleClick");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_15 | Verify applying Bold to a selected heading word")
    public void verifyApplyingBoldToHeadingWord() {
        LOGGER.info(STARTING_TEST_MSG, "verifyApplyingBoldToHeadingWord");
        DocumentEditorPageObjects1 page = editor();
        LOGGER.debug(SELECTING_HEADING_WORD_MSG);
        page.selectHeadingWord();
        LOGGER.debug(SELECTED_HEADING_WORD_MSG);
        LOGGER.debug(CHECKING_BOLD_OFF_MSG);
        page.requireHeadingBoldIsOff();
        LOGGER.debug(CHECKED_BOLD_OFF_MSG);
        boolean applied = false;
        try {
            LOGGER.debug(APPLYING_BOLD_MSG);
            page.applyBoldToSelection();
            LOGGER.debug(APPLIED_BOLD_MSG);
            applied = true;
            LOGGER.debug("Reading observed result: Selected heading word became bold");
            boolean actualResult = page.waitForHeadingWordToBecomeBold();
            LOGGER.info("Selected heading word became bold: {}", actualResult);
            LOGGER.debug("Asserting expected result: Selected heading word became bold");
            Assert.assertTrue(actualResult,
                    "Bold should be active and the selected heading word should have rendered bold markup");
        } finally {
            if (applied) {
                LOGGER.debug(UNDOING_BOLD_MSG);
                page.undoHeadingBold();
                LOGGER.debug(UNDONE_BOLD_MSG);
            }
        }
        LOGGER.info(FINISHED_TEST_MSG, "verifyApplyingBoldToHeadingWord");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_16 | Verify Undo restores heading formatting after applying Bold")
    public void verifyUndoRestoresHeadingFormatting() {
        LOGGER.info(STARTING_TEST_MSG, "verifyUndoRestoresHeadingFormatting");
        DocumentEditorPageObjects1 page = editor();
        LOGGER.debug(SELECTING_HEADING_WORD_MSG);
        page.selectHeadingWord();
        LOGGER.debug(SELECTED_HEADING_WORD_MSG);
        LOGGER.debug(CHECKING_BOLD_OFF_MSG);
        page.requireHeadingBoldIsOff();
        LOGGER.debug(CHECKED_BOLD_OFF_MSG);
        java.util.List<String> original = page.getHeadingWordFormattingSnapshot();
        LOGGER.info("Initial heading formatting snapshot: {}", original);
        boolean applied = false;
        boolean undoAttempted = false;
        try {
            LOGGER.debug(APPLYING_BOLD_MSG);
            page.applyBoldToSelection();
            LOGGER.debug(APPLIED_BOLD_MSG);
            applied = true;
            LOGGER.debug("Waiting for Bold toolbar and content formatting");
            page.waitForHeadingWordToBecomeBold();
            LOGGER.debug("Completed: waiting for Bold toolbar and content formatting");
            // Never issue a second Undo if the operation being tested fails.
            undoAttempted = true;
            LOGGER.debug("Undoing Bold and waiting for inactive toolbar state");
            page.undoHeadingBoldAndWaitForInactiveState();
            LOGGER.debug("Completed: undoing Bold and waiting for inactive toolbar state");
            LOGGER.debug("Reading observed result: Heading formatting snapshot after Undo");
            java.util.List<String> actualResult = page.getHeadingWordFormattingSnapshot();
            LOGGER.info("Heading formatting snapshot after Undo: {}", actualResult);
            LOGGER.debug("Asserting expected result: Heading formatting snapshot after Undo");
            Assert.assertEquals(actualResult, original,
                    "Undo should restore heading text, font weight and explicit bold formatting");
        } finally {
            if (applied && !undoAttempted) {
                LOGGER.debug(UNDOING_BOLD_MSG);
                page.undoHeadingBold();
                LOGGER.debug(UNDONE_BOLD_MSG);
            }
        }
        LOGGER.info(FINISHED_TEST_MSG, "verifyUndoRestoresHeadingFormatting");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_17 | Verify selecting a paragraph shows p")
    public void verifyParagraphSelectionShowsParagraphType() {
        LOGGER.info(STARTING_TEST_MSG, "verifyParagraphSelectionShowsParagraphType");
        DocumentEditorPageObjects1 page = editor();
        LOGGER.debug("Clicking plain text in the target heading");
        page.clickTargetHeading();
        LOGGER.debug("Completed: clicking plain text in the target heading");
        LOGGER.debug(WAITING_FOR_SELECTED_ELEMENT_MSG);
        page.waitForSelectedElementType(HEADING_TYPE);
        LOGGER.debug(SELECTED_ELEMENT_UPDATED_MSG);
        LOGGER.debug(CLICKING_PARAGRAPH_MSG);
        page.clickTargetParagraph();
        LOGGER.debug(CLICKED_PARAGRAPH_MSG);
        LOGGER.debug(WAITING_FOR_SELECTED_ELEMENT_MSG);
        page.waitForSelectedElementType(PARAGRAPH_TYPE);
        LOGGER.debug(SELECTED_ELEMENT_UPDATED_MSG);
        LOGGER.debug("Reading observed result: Selected Element after paragraph click");
        String actualResult = page.getSelectedElementType();
        LOGGER.info("Selected Element after paragraph click: {}", actualResult);
        LOGGER.debug("Asserting expected result: Selected Element after paragraph click");
        Assert.assertEquals(actualResult, PARAGRAPH_TYPE,
                "Selected Element should change from heading to p after clicking the paragraph");
        LOGGER.info(FINISHED_TEST_MSG, "verifyParagraphSelectionShowsParagraphType");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_18 | Verify selecting a heading shows heading")
    public void verifyHeadingSelectionShowsHeadingType() {
        LOGGER.info(STARTING_TEST_MSG, "verifyHeadingSelectionShowsHeadingType");
        DocumentEditorPageObjects1 page = editor();
        LOGGER.debug(CLICKING_PARAGRAPH_MSG);
        page.clickTargetParagraph();
        LOGGER.debug(CLICKED_PARAGRAPH_MSG);
        LOGGER.debug(WAITING_FOR_SELECTED_ELEMENT_MSG);
        page.waitForSelectedElementType(PARAGRAPH_TYPE);
        LOGGER.debug(SELECTED_ELEMENT_UPDATED_MSG);
        LOGGER.debug("Clicking plain text in the target heading");
        page.clickTargetHeading();
        LOGGER.debug("Completed: clicking plain text in the target heading");
        LOGGER.debug(WAITING_FOR_SELECTED_ELEMENT_MSG);
        page.waitForSelectedElementType(HEADING_TYPE);
        LOGGER.debug(SELECTED_ELEMENT_UPDATED_MSG);
        LOGGER.debug("Reading observed result: Selected Element after heading click");
        String actualResult = page.getSelectedElementType();
        LOGGER.info("Selected Element after heading click: {}", actualResult);
        LOGGER.debug("Asserting expected result: Selected Element after heading click");
        Assert.assertEquals(actualResult, HEADING_TYPE,
                "Selected Element should change from p to heading after clicking the heading");
        LOGGER.info(FINISHED_TEST_MSG, "verifyHeadingSelectionShowsHeadingType");
    }
    /** Lead case(s): TC-001 and TC-004; see docs/team-lead-paragraph-cases.md. */
    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_19 | Verify Enter splits paragraph text and Undo restores it")
    public void verifyEnterIncreasesParagraphCount() {
        LOGGER.info(STARTING_TEST_MSG, "verifyEnterIncreasesParagraphCount");
        DocumentEditorPageObjects1 page = editor();
        int originalCount = page.getParagraphCountInTargetSection();
        LOGGER.info(INITIAL_PARAGRAPH_COUNT_MSG, originalCount);
        if (originalCount < 1) {
            throw new IllegalStateException("Precondition: expand the target section containing the original paragraph");
        }
        boolean splitSent = false;
        try {
            LOGGER.debug("Splitting the target paragraph with Enter");
            page.splitTargetParagraph();
            LOGGER.debug("Completed: splitting the target paragraph with Enter");
            splitSent = true;
            LOGGER.debug("Waiting for the section paragraph count");
            page.waitForParagraphCountInTargetSection(originalCount + 1);
            LOGGER.debug("Completed: waiting for the section paragraph count");
            LOGGER.debug("Reading observed result: Target section paragraph count after Enter");
            int actualResult = page.getParagraphCountInTargetSection();
            LOGGER.info("Target section paragraph count after Enter: {}", actualResult);
            LOGGER.debug("Asserting expected result: Target section paragraph count after Enter");
            Assert.assertEquals(actualResult, originalCount + 1,
                    "Enter should add one paragraph row in the target section's Document Structure");
            LOGGER.debug("Waiting for exact text on both sides of the split");
            page.waitForParagraphEnterResult();
            java.util.List<String> splitTexts = page.getEditedSectionParagraphTexts();
            LOGGER.info(PARAGRAPH_TEXT_AFTER_ENTER_MSG, splitTexts);
            LOGGER.debug("Asserting that text split at the configured boundary without other changes");
            Assert.assertEquals(splitTexts, page.getExpectedParagraphTextsAfterEnter(),
                    "Enter should split only the target paragraph at the configured cursor position");
        } finally {
            if (splitSent && page.getParagraphCountInTargetSection() != originalCount) {
                LOGGER.debug("Undoing this test's paragraph split");
                page.undoParagraphSplit(originalCount);
                LOGGER.debug("Completed: undoing this test's paragraph split");
            }
        }
        java.util.List<String> restored = page.getEditedSectionParagraphTexts();
        LOGGER.info("Paragraph text sequence after Undo: {}", restored);
        LOGGER.debug("Asserting that Undo restored the original paragraph text sequence");
        Assert.assertEquals(restored, page.getOriginalParagraphTexts(),
                "Undo should join the split text back into the original paragraph");
        LOGGER.info(FINISHED_TEST_MSG, "verifyEnterIncreasesParagraphCount");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_25 | Verify applying Italic to a selected heading word")
    public void verifyApplyingItalicToHeadingWord() {
        LOGGER.info(STARTING_TEST_MSG, "verifyApplyingItalicToHeadingWord");
        DocumentEditorPageObjects1 page = editor();
        LOGGER.debug(SELECTING_HEADING_WORD_MSG);
        page.selectHeadingWord();
        LOGGER.debug(SELECTED_HEADING_WORD_MSG);
        LOGGER.debug("Checking that Italic is initially off");
        page.requireHeadingItalicIsOff();
        LOGGER.debug("Completed: checking that Italic is initially off");
        boolean applied = false;
        try {
            LOGGER.debug("Applying Italic to the selected heading word");
            page.applyItalicToSelection();
            LOGGER.debug("Completed: applying Italic to the selected heading word");
            applied = true;
            LOGGER.debug("Reading observed result: Selected heading word became italic");
            boolean actualResult = page.waitForHeadingWordToBecomeItalic();
            LOGGER.info("Selected heading word became italic: {}", actualResult);
            LOGGER.debug("Asserting expected result: Selected heading word became italic");
            Assert.assertTrue(actualResult,
                    "Italic should be active and the selected heading word should be italic");
        } finally {
            if (applied) {
                LOGGER.debug("Undoing this test's Italic change");
                page.undoHeadingItalic();
                LOGGER.debug("Completed: undoing this test's Italic change");
            }
        }
        LOGGER.info(FINISHED_TEST_MSG, "verifyApplyingItalicToHeadingWord");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_26 | Verify applying Underline to a selected heading word")
    public void verifyApplyingUnderlineToHeadingWord() {
        LOGGER.info(STARTING_TEST_MSG, "verifyApplyingUnderlineToHeadingWord");
        DocumentEditorPageObjects1 page = editor();
        LOGGER.debug(SELECTING_HEADING_WORD_MSG);
        page.selectHeadingWord();
        LOGGER.debug(SELECTED_HEADING_WORD_MSG);
        LOGGER.debug("Checking that Underline is initially off");
        page.requireHeadingUnderlineIsOff();
        LOGGER.debug("Completed: checking that Underline is initially off");
        boolean applied = false;
        try {
            LOGGER.debug("Applying Underline to the selected heading word");
            page.applyUnderlineToSelection();
            LOGGER.debug("Completed: applying Underline to the selected heading word");
            applied = true;
            LOGGER.debug("Reading observed result: Selected heading word became underlined");
            boolean actualResult = page.waitForHeadingWordToBecomeUnderlined();
            LOGGER.info("Selected heading word became underlined: {}", actualResult);
            LOGGER.debug("Asserting expected result: Selected heading word became underlined");
            Assert.assertTrue(actualResult,
                    "Underline should be active and the selected heading word should be underlined");
        } finally {
            if (applied) {
                LOGGER.debug("Undoing this test's Underline change");
                page.undoHeadingUnderline();
                LOGGER.debug("Completed: undoing this test's Underline change");
            }
        }
        LOGGER.info(FINISHED_TEST_MSG, "verifyApplyingUnderlineToHeadingWord");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_27 | Verify Redo restores undone Bold formatting")
    public void verifyRedoRestoresBoldFormatting() {
        LOGGER.info(STARTING_TEST_MSG, "verifyRedoRestoresBoldFormatting");
        DocumentEditorPageObjects1 page = editor();
        LOGGER.debug(SELECTING_HEADING_WORD_MSG);
        page.selectHeadingWord();
        LOGGER.debug(SELECTED_HEADING_WORD_MSG);
        LOGGER.debug(CHECKING_BOLD_OFF_MSG);
        page.requireHeadingBoldIsOff();
        LOGGER.debug(CHECKED_BOLD_OFF_MSG);
        boolean boldApplied = false;
        try {
            LOGGER.debug(APPLYING_BOLD_MSG);
            page.applyBoldToSelection();
            LOGGER.debug(APPLIED_BOLD_MSG);
            boldApplied = true;
            LOGGER.debug("Waiting for Bold toolbar and content formatting");
            page.waitForHeadingWordToBecomeBold();
            LOGGER.debug("Completed: waiting for Bold toolbar and content formatting");
            LOGGER.debug(UNDOING_BOLD_MSG);
            page.undoHeadingBold();
            LOGGER.debug(UNDONE_BOLD_MSG);
            boldApplied = false;
            LOGGER.debug("Redoing the undone Bold change");
            page.redoHeadingBold();
            LOGGER.debug("Completed: redoing the undone Bold change");
            boldApplied = true;
            LOGGER.debug("Reading observed result: Bold restored after Redo");
            boolean actualResult = page.waitForHeadingWordToBecomeBold();
            LOGGER.info("Bold restored after Redo: {}", actualResult);
            LOGGER.debug("Asserting expected result: Bold restored after Redo");
            Assert.assertTrue(actualResult,
                    "Redo should restore the heading word's bold markup and active Bold button");
        } finally {
            if (boldApplied) {
                LOGGER.debug(UNDOING_BOLD_MSG);
                page.undoHeadingBold();
                LOGGER.debug(UNDONE_BOLD_MSG);
            }
        }
        LOGGER.info(FINISHED_TEST_MSG, "verifyRedoRestoresBoldFormatting");
    }

    /** Lead case(s): TC-007; see docs/team-lead-paragraph-cases.md. */
    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_28 | Verify applying an unordered list to a paragraph")
    public void verifyApplyingUnorderedListToParagraph() {
        LOGGER.info(STARTING_TEST_MSG, "verifyApplyingUnorderedListToParagraph");
        DocumentEditorPageObjects1 page = editor();
        LOGGER.debug("Checking that the target paragraph is outside a list");
        page.requireTargetParagraphIsNotAList();
        LOGGER.debug("Completed: checking that the target paragraph is outside a list");
        LOGGER.debug(CLICKING_PARAGRAPH_MSG);
        page.clickTargetParagraph();
        LOGGER.debug(CLICKED_PARAGRAPH_MSG);
        boolean applied = false;
        try {
            LOGGER.debug("Converting the target paragraph to an unordered list");
            page.applyUnorderedList();
            LOGGER.debug("Completed: converting the target paragraph to an unordered list");
            applied = true;
            LOGGER.debug("Reading observed result: Paragraph converted to an unordered list item");
            boolean actualResult = page.waitForTargetParagraphInUnorderedList();
            LOGGER.info("Paragraph converted to an unordered list item: {}", actualResult);
            LOGGER.debug("Asserting expected result: Paragraph converted to an unordered list item");
            Assert.assertTrue(actualResult,
                    "The target paragraph should be an unordered list item and the list button should be active");
        } finally {
            if (applied) {
                LOGGER.debug("Undoing this test's list conversion");
                page.undoParagraphList();
                LOGGER.debug("Completed: undoing this test's list conversion");
            }
        }
        LOGGER.info(FINISHED_TEST_MSG, "verifyApplyingUnorderedListToParagraph");
    }

    /** Lead case(s): TC-006; see docs/team-lead-paragraph-cases.md. */
    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_29 | Verify applying an ordered list to a paragraph")
    public void verifyApplyingOrderedListToParagraph() {
        LOGGER.info(STARTING_TEST_MSG, "verifyApplyingOrderedListToParagraph");
        DocumentEditorPageObjects1 page = editor();
        LOGGER.debug("Checking that the target paragraph is outside a list");
        page.requireTargetParagraphIsNotAList();
        LOGGER.debug("Completed: checking that the target paragraph is outside a list");
        LOGGER.debug(CLICKING_PARAGRAPH_MSG);
        page.clickTargetParagraph();
        LOGGER.debug(CLICKED_PARAGRAPH_MSG);
        boolean applied = false;
        try {
            LOGGER.debug("Converting the target paragraph to an ordered list");
            page.applyOrderedList();
            LOGGER.debug("Completed: converting the target paragraph to an ordered list");
            applied = true;
            LOGGER.debug("Reading observed result: Paragraph converted to an ordered list item");
            boolean actualResult = page.waitForTargetParagraphInOrderedList();
            LOGGER.info("Paragraph converted to an ordered list item: {}", actualResult);
            LOGGER.debug("Asserting expected result: Paragraph converted to an ordered list item");
            Assert.assertTrue(actualResult,
                    "The target paragraph should be an ordered list item and the list button should be active");
        } finally {
            if (applied) {
                LOGGER.debug("Undoing this test's list conversion");
                page.undoParagraphList();
                LOGGER.debug("Completed: undoing this test's list conversion");
            }
        }
        LOGGER.info(FINISHED_TEST_MSG, "verifyApplyingOrderedListToParagraph");
    }

    /** Lead case(s): TC-002; see docs/team-lead-paragraph-cases.md. */
    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_30 | Verify Enter at the beginning of a paragraph")
    public void verifyEnterAtParagraphBeginning() {
        LOGGER.info(STARTING_TEST_MSG, "verifyEnterAtParagraphBeginning");
        DocumentEditorPageObjects1 page = editor();
        int originalCount = page.getParagraphCountInTargetSection();
        LOGGER.info(INITIAL_PARAGRAPH_COUNT_MSG, originalCount);
        boolean enterSent = false;
        try {
            LOGGER.debug("Placing the caret at the paragraph beginning and pressing Enter");
            page.pressEnterAtParagraphBeginning();
            enterSent = true;
            LOGGER.debug("Pressed Enter; waiting for the new empty paragraph");
            page.waitForParagraphEnterResult();
            page.waitForParagraphCountInTargetSection(originalCount + 1);
            java.util.List<String> actual = page.getEditedSectionParagraphTexts();
            LOGGER.info(PARAGRAPH_TEXT_AFTER_ENTER_MSG, actual);
            LOGGER.debug("Asserting empty paragraph placement and unchanged original text");
            Assert.assertEquals(actual, page.getExpectedParagraphTextsAfterEnter(),
                    "Enter at the beginning should insert an empty paragraph and preserve original text");
        } finally {
            if (enterSent) {
                LOGGER.debug("Undoing this test's paragraph Enter action");
                page.undoParagraphSplit(originalCount);
                LOGGER.debug("Original paragraph count and text restored after Undo");
            }
        }
        LOGGER.info(FINISHED_TEST_MSG, "verifyEnterAtParagraphBeginning");
    }

    /** Lead case(s): TC-003; see docs/team-lead-paragraph-cases.md. */
    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_31 | Verify Enter at the end of a paragraph")
    public void verifyEnterAtParagraphEnd() {
        LOGGER.info(STARTING_TEST_MSG, "verifyEnterAtParagraphEnd");
        DocumentEditorPageObjects1 page = editor();
        int originalCount = page.getParagraphCountInTargetSection();
        LOGGER.info(INITIAL_PARAGRAPH_COUNT_MSG, originalCount);
        boolean enterSent = false;
        try {
            LOGGER.debug("Placing the caret at the paragraph end and pressing Enter");
            page.pressEnterAtParagraphEnd();
            enterSent = true;
            LOGGER.debug("Pressed Enter; waiting for the new empty paragraph");
            page.waitForParagraphEnterResult();
            page.waitForParagraphCountInTargetSection(originalCount + 1);
            java.util.List<String> actual = page.getEditedSectionParagraphTexts();
            LOGGER.info(PARAGRAPH_TEXT_AFTER_ENTER_MSG, actual);
            LOGGER.debug("Asserting empty paragraph placement and unchanged original text");
            Assert.assertEquals(actual, page.getExpectedParagraphTextsAfterEnter(),
                    "Enter at the end should insert an empty paragraph and preserve original text");
        } finally {
            if (enterSent) {
                LOGGER.debug("Undoing this test's paragraph Enter action");
                page.undoParagraphSplit(originalCount);
                LOGGER.debug("Original paragraph count and text restored after Undo");
            }
        }
        LOGGER.info(FINISHED_TEST_MSG, "verifyEnterAtParagraphEnd");
    }

    /** Lead case(s): TC-005; see docs/team-lead-paragraph-cases.md. */
    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_32 | Verify Redo restores the undone paragraph split")
    public void verifyRedoRestoresParagraphSplit() {
        LOGGER.info(STARTING_TEST_MSG, "verifyRedoRestoresParagraphSplit");
        DocumentEditorPageObjects1 page = editor();
        int originalCount = page.getParagraphCountInTargetSection();
        boolean splitPending = false;
        try {
            LOGGER.debug("Splitting the paragraph in the middle with Enter");
            page.splitTargetParagraph();
            splitPending = true;
            page.waitForParagraphEnterResult();
            LOGGER.debug("Undoing Enter and verifying original paragraph restoration");
            page.undoParagraphSplit(originalCount);
            splitPending = false;
            LOGGER.debug("Redoing the undone Enter action");
            page.redoParagraphSplit();
            splitPending = true;
            page.waitForParagraphEnterResult();
            page.waitForParagraphCountInTargetSection(originalCount + 1);
            java.util.List<String> actual = page.getEditedSectionParagraphTexts();
            LOGGER.info("Paragraph text sequence after Redo: {}", actual);
            LOGGER.debug("Asserting that Redo restored the same text split");
            Assert.assertEquals(actual, page.getExpectedParagraphTextsAfterEnter(),
                    "Redo should restore the original split point and both text portions");
        } finally {
            if (splitPending) {
                LOGGER.debug("Undoing this test's restored paragraph split");
                page.undoParagraphSplit(originalCount);
                LOGGER.debug("Original paragraph count and text restored after final Undo");
            }
        }
        LOGGER.info(FINISHED_TEST_MSG, "verifyRedoRestoresParagraphSplit");
    }

    /** Lead case(s): TC-008; see docs/team-lead-paragraph-cases.md. */
    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_33 | Verify Bold affects only the selected paragraph word")
    public void verifyApplyingBoldToParagraphWord() {
        LOGGER.info(STARTING_TEST_MSG, "verifyApplyingBoldToParagraphWord");
        checkParagraphWordFormatting(ParagraphFormat.BOLD);
        LOGGER.info(FINISHED_TEST_MSG, "verifyApplyingBoldToParagraphWord");
    }

    /** Lead case(s): TC-009; see docs/team-lead-paragraph-cases.md. */
    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_34 | Verify Italic affects only the selected paragraph word")
    public void verifyApplyingItalicToParagraphWord() {
        LOGGER.info(STARTING_TEST_MSG, "verifyApplyingItalicToParagraphWord");
        checkParagraphWordFormatting(ParagraphFormat.ITALIC);
        LOGGER.info(FINISHED_TEST_MSG, "verifyApplyingItalicToParagraphWord");
    }

    /** Lead case(s): TC-010; see docs/team-lead-paragraph-cases.md. */
    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_35 | Verify Underline affects only the selected paragraph word")
    public void verifyApplyingUnderlineToParagraphWord() {
        LOGGER.info(STARTING_TEST_MSG, "verifyApplyingUnderlineToParagraphWord");
        checkParagraphWordFormatting(ParagraphFormat.UNDERLINE);
        LOGGER.info(FINISHED_TEST_MSG, "verifyApplyingUnderlineToParagraphWord");
    }

    /** Lead case(s): TC-011; see docs/team-lead-paragraph-cases.md. */
    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_36 | Verify Subscript affects only the selected paragraph word")
    public void verifyApplyingSubscriptToParagraphWord() {
        LOGGER.info(STARTING_TEST_MSG, "verifyApplyingSubscriptToParagraphWord");
        checkParagraphWordFormatting(ParagraphFormat.SUBSCRIPT);
        LOGGER.info(FINISHED_TEST_MSG, "verifyApplyingSubscriptToParagraphWord");
    }

    /** Lead case(s): TC-012; see docs/team-lead-paragraph-cases.md. */
    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_37 | Verify Superscript affects only the selected paragraph word")
    public void verifyApplyingSuperscriptToParagraphWord() {
        LOGGER.info(STARTING_TEST_MSG, "verifyApplyingSuperscriptToParagraphWord");
        checkParagraphWordFormatting(ParagraphFormat.SUPERSCRIPT);
        LOGGER.info(FINISHED_TEST_MSG, "verifyApplyingSuperscriptToParagraphWord");
    }

    /** Lead case(s): TC-013; see docs/team-lead-paragraph-cases.md. */
    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_38 | Verify Strikethrough affects only the selected paragraph word")
    public void verifyApplyingStrikethroughToParagraphWord() {
        LOGGER.info(STARTING_TEST_MSG, "verifyApplyingStrikethroughToParagraphWord");
        checkParagraphWordFormatting(ParagraphFormat.STRIKETHROUGH);
        LOGGER.info(FINISHED_TEST_MSG, "verifyApplyingStrikethroughToParagraphWord");
    }

    /** Shared steps keep six distinct formatting scenarios consistent without copying their implementation. */
    private void checkParagraphWordFormatting(ParagraphFormat format) {
        DocumentEditorPageObjects1 page = editor();
        LOGGER.debug("Reading original paragraph character styles for {}", format);
        java.util.List<java.util.List<String>> original = page.getParagraphCharacterStyles();
        LOGGER.info("Original paragraph character count: {}", original.size());
        LOGGER.debug("Checking that the target word is not already formatted with {}", format);
        page.requireParagraphWordFormatIsOff(format, original);
        LOGGER.debug("Selecting the configured plain paragraph word");
        page.selectParagraphWord();
        LOGGER.debug("Selected paragraph word; applying {}", format);
        boolean applied = false;
        try {
            page.applyParagraphFormatting(format);
            applied = true;
            LOGGER.debug("Applied {}; waiting for formatting and unchanged surrounding text", format);
            page.waitForParagraphWordFormatting(format, original);
            boolean actual = page.isOnlyParagraphWordFormatted(format, original);
            LOGGER.info("Only the selected paragraph word has {} and surrounding text is unchanged: {}", format, actual);
            LOGGER.debug("Asserting isolated paragraph word formatting for {}", format);
            Assert.assertTrue(actual,
                    "Only the selected word should gain " + format + "; paragraph text and unselected styles must remain unchanged");
        } finally {
            if (applied) {
                LOGGER.debug("Undoing this test's paragraph {} change", format);
                page.undoParagraphFormatting(original);
                LOGGER.debug("Paragraph text and original character styles restored after Undo");
            }
        }
    }

}
