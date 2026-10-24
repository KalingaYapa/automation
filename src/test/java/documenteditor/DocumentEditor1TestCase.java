package documenteditor;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import a_testbase.BaseClass;

/** Each test independently logs in and opens the allocated document. */
public class DocumentEditor1TestCase extends BaseClass {
    private static final String MODULE_GROUP = "DocumentEditor";
    private static final String HIGH_PRIORITY_GROUP = "P2-DocumentEditor";
    private static final String HEADING_TYPE = "heading";
    private static final String PARAGRAPH_TYPE = "p";

    /** TestNG runs the superclass browser setup before this subclass setup. */
    @BeforeMethod(alwaysRun = true)
    public void openAllocatedEditor() {
        b_utilities.ReusableMethods.loginAndSearchFileClickEdit();
    }

    private DocumentEditorPageObjects1 editor() {
        return new DocumentEditorPageObjects1(getDriver());
    }
    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_01 | Verify the document title is visible")
    public void verifyDocumentTitleIsDisplayed() {
        Assert.assertTrue(editor().isDocumentTitleDisplayed(), "The document title should be visible");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_02 | Verify the document title text")
    public void verifyDocumentTitleText() {
        Assert.assertEquals(editor().getDocumentTitle(), getConfig("expectedDocumentTitle"),
                "The title bar should identify the expected document");
    }

    @Test(groups = { MODULE_GROUP, "P3-DocumentEditor" }, description = "TC_03 | Verify the saved time is visible")
    public void verifySavedTimeIsDisplayed() {
        Assert.assertTrue(editor().isSavedTimeDisplayed(), "The saved-time indicator should be visible");
    }

    @Test(groups = { MODULE_GROUP, "P3-DocumentEditor" }, description = "TC_04 | Verify saved time is blank initially or has the expected format")
    public void verifySavedTimeFormat() {
        String actual = editor().getSavedTimeText();
        logger.info("Saved-time indicator text: '{}'", actual);
        Assert.assertTrue(actual.isBlank() || actual.matches(getConfig("savedTimePattern")),
                "Saved time may be blank initially; otherwise expect Last Saved followed by a 12-hour time and AM/PM. Actual: '"
                        + actual + "'");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_05 | Verify the Save button label")
    public void verifySaveButtonLabel() {
        Assert.assertEquals(editor().getSaveButtonText(), "Save",
                "The visible Save button should display its expected label");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_06 | Verify the Save button is enabled")
    public void verifySaveButtonIsEnabled() {
        Assert.assertTrue(editor().isSaveButtonEnabled(),
                "The Save button should be enabled for the editable test document");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_07 | Verify the Reset File button label")
    public void verifyResetButtonLabel() {
        Assert.assertEquals(editor().getResetButtonText(), "Reset File",
                "The visible Reset File button should display its expected label");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_08 | Verify the Reset File button is enabled")
    public void verifyResetButtonIsEnabled() {
        Assert.assertTrue(editor().isResetButtonEnabled(),
                "The Reset File button should be enabled for the editable test document");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_09 | Verify the Validate File button label")
    public void verifyValidateButtonLabel() {
        Assert.assertEquals(editor().getValidateButtonText(), "Validate File",
                "The visible Validate File button should display its expected label");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_10 | Verify the Validate File button is enabled")
    public void verifyValidateButtonIsEnabled() {
        Assert.assertTrue(editor().isValidateButtonEnabled(),
                "The Validate File button should be enabled for the editable test document");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_11 | Verify the Check In button label")
    public void verifyCheckInButtonLabel() {
        Assert.assertEquals(editor().getCheckInButtonText(), "Check In",
                "The visible Check In button should display its expected label");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_12 | Verify the Check In button is enabled")
    public void verifyCheckInButtonIsEnabled() {
        Assert.assertTrue(editor().isCheckInButtonEnabled(),
                "The Check In button should be enabled for the editable test document");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_13 | Verify Bold is active after a single click in bold text")
    public void verifyBoldIsActiveAfterSingleClick() {
        DocumentEditorPageObjects1 page = editor();
        page.clickBoldWord();
        Assert.assertTrue(page.isBoldFormattingActive(),
                "Bold should be active with the caret inside the bold word");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_14 | Verify Bold is active after double-clicking bold text")
    public void verifyBoldIsActiveAfterDoubleClick() {
        DocumentEditorPageObjects1 page = editor();
        page.doubleClickBoldWord();
        Assert.assertTrue(page.isBoldFormattingActive(),
                "Bold should be active when the bold word is selected");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_15 | Verify applying Bold to a selected heading word")
    public void verifyApplyingBoldToHeadingWord() {
        DocumentEditorPageObjects1 page = editor();
        page.selectHeadingWord();
        page.requireHeadingBoldIsOff();
        boolean applied = false;
        try {
            page.applyBoldToSelection();
            applied = true;
            Assert.assertTrue(page.waitForHeadingWordToBecomeBold(),
                    "Bold should be active and the selected heading word should have rendered bold markup");
        } finally {
            if (applied) {
                page.undoHeadingBold();
            }
        }
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_16 | Verify Undo restores heading formatting after applying Bold")
    public void verifyUndoRestoresHeadingFormatting() {
        DocumentEditorPageObjects1 page = editor();
        page.selectHeadingWord();
        page.requireHeadingBoldIsOff();
        java.util.List<String> original = page.getHeadingWordFormattingSnapshot();
        boolean applied = false;
        boolean undoAttempted = false;
        try {
            page.applyBoldToSelection();
            applied = true;
            page.waitForHeadingWordToBecomeBold();
            // Never issue a second Undo if the operation being tested fails.
            undoAttempted = true;
            page.undoHeadingBoldAndWaitForInactiveState();
            Assert.assertEquals(page.getHeadingWordFormattingSnapshot(), original,
                    "Undo should restore heading text, font weight and explicit bold formatting");
        } finally {
            if (applied && !undoAttempted) {
                page.undoHeadingBold();
            }
        }
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_17 | Verify selecting a paragraph shows p")
    public void verifyParagraphSelectionShowsParagraphType() {
        DocumentEditorPageObjects1 page = editor();
        page.clickTargetHeading();
        page.waitForSelectedElementType(HEADING_TYPE);
        page.clickTargetParagraph();
        page.waitForSelectedElementType(PARAGRAPH_TYPE);
        Assert.assertEquals(page.getSelectedElementType(), PARAGRAPH_TYPE,
                "Selected Element should change from heading to p after clicking the paragraph");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_18 | Verify selecting a heading shows heading")
    public void verifyHeadingSelectionShowsHeadingType() {
        DocumentEditorPageObjects1 page = editor();
        page.clickTargetParagraph();
        page.waitForSelectedElementType(PARAGRAPH_TYPE);
        page.clickTargetHeading();
        page.waitForSelectedElementType(HEADING_TYPE);
        Assert.assertEquals(page.getSelectedElementType(), HEADING_TYPE,
                "Selected Element should change from p to heading after clicking the heading");
    }
    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_19 | Verify Enter increases the section paragraph count by one")
    public void verifyEnterIncreasesParagraphCount() {
        DocumentEditorPageObjects1 page = editor();
        int originalCount = page.getParagraphCountInTargetSection();
        if (originalCount < 1) {
            throw new IllegalStateException("Precondition: expand the target section containing the original paragraph");
        }
        boolean splitSent = false;
        try {
            page.splitTargetParagraph();
            splitSent = true;
            page.waitForParagraphCountInTargetSection(originalCount + 1);
            Assert.assertEquals(page.getParagraphCountInTargetSection(), originalCount + 1,
                    "Enter should add one paragraph row in the target section's Document Structure");
        } finally {
            if (splitSent && page.getParagraphCountInTargetSection() != originalCount) {
                page.undoParagraphSplit(originalCount);
            }
        }
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_25 | Verify applying Italic to a selected heading word")
    public void verifyApplyingItalicToHeadingWord() {
        DocumentEditorPageObjects1 page = editor();
        page.selectHeadingWord();
        page.requireHeadingItalicIsOff();
        boolean applied = false;
        try {
            page.applyItalicToSelection();
            applied = true;
            Assert.assertTrue(page.waitForHeadingWordToBecomeItalic(),
                    "Italic should be active and the selected heading word should be italic");
        } finally {
            if (applied) {
                page.undoHeadingItalic();
            }
        }
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_26 | Verify applying Underline to a selected heading word")
    public void verifyApplyingUnderlineToHeadingWord() {
        DocumentEditorPageObjects1 page = editor();
        page.selectHeadingWord();
        page.requireHeadingUnderlineIsOff();
        boolean applied = false;
        try {
            page.applyUnderlineToSelection();
            applied = true;
            Assert.assertTrue(page.waitForHeadingWordToBecomeUnderlined(),
                    "Underline should be active and the selected heading word should be underlined");
        } finally {
            if (applied) {
                page.undoHeadingUnderline();
            }
        }
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_27 | Verify Redo restores undone Bold formatting")
    public void verifyRedoRestoresBoldFormatting() {
        DocumentEditorPageObjects1 page = editor();
        page.selectHeadingWord();
        page.requireHeadingBoldIsOff();
        boolean boldApplied = false;
        try {
            page.applyBoldToSelection();
            boldApplied = true;
            page.waitForHeadingWordToBecomeBold();
            page.undoHeadingBold();
            boldApplied = false;
            page.redoHeadingBold();
            boldApplied = true;
            Assert.assertTrue(page.waitForHeadingWordToBecomeBold(),
                    "Redo should restore the heading word's bold markup and active Bold button");
        } finally {
            if (boldApplied) {
                page.undoHeadingBold();
            }
        }
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_28 | Verify applying an unordered list to a paragraph")
    public void verifyApplyingUnorderedListToParagraph() {
        DocumentEditorPageObjects1 page = editor();
        page.requireTargetParagraphIsNotAList();
        page.clickTargetParagraph();
        boolean applied = false;
        try {
            page.applyUnorderedList();
            applied = true;
            Assert.assertTrue(page.waitForTargetParagraphInUnorderedList(),
                    "The target paragraph should be an unordered list item and the list button should be active");
        } finally {
            if (applied) {
                page.undoParagraphList();
            }
        }
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_29 | Verify applying an ordered list to a paragraph")
    public void verifyApplyingOrderedListToParagraph() {
        DocumentEditorPageObjects1 page = editor();
        page.requireTargetParagraphIsNotAList();
        page.clickTargetParagraph();
        boolean applied = false;
        try {
            page.applyOrderedList();
            applied = true;
            Assert.assertTrue(page.waitForTargetParagraphInOrderedList(),
                    "The target paragraph should be an ordered list item and the list button should be active");
        } finally {
            if (applied) {
                page.undoParagraphList();
            }
        }
    }

}
