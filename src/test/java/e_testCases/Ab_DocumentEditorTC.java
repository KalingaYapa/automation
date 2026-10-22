package e_testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import a_testBase.BaseClass;
import d_pageObjects.Ab_DocumentEditorPage;
import d_pageObjects.Ac_LoginPage;

/** Each test independently logs in and opens the allocated document. */
public class Ab_DocumentEditorTC extends BaseClass {
    private static final String CLASS_GROUP = "Ab_DocumentEditorTC";

    /** TestNG runs the superclass browser setup before this subclass setup. */
    @BeforeMethod(alwaysRun = true)
    public void openAllocatedEditor() {
        new Ac_LoginPage(getDriver()).login()
                .selectProject(getConfig("portalProject"))
                .openAuthoringFileList()
                .findTargetFile()
                .openTargetFile();
    }

    private Ab_DocumentEditorPage editor() {
        return new Ab_DocumentEditorPage(getDriver());
    }
/* 
    @Test(groups = { CLASS_GROUP, "High" }, description = "TC_01 | Verify the document title is visible")
    public void verifyDocumentTitleIsDisplayed() {
        Assert.assertTrue(editor().isDocumentTitleDisplayed(), "The document title should be visible");
    }

    @Test(groups = { CLASS_GROUP, "High" }, description = "TC_02 | Verify the document title text")
    public void verifyDocumentTitleText() {
        Assert.assertEquals(editor().getDocumentTitle(), getConfig("expectedDocumentTitle"),
                "The title bar should identify the expected document");
    }

    @Test(groups = { CLASS_GROUP, "Medium" }, description = "TC_03 | Verify the saved time is visible")
    public void verifySavedTimeIsDisplayed() {
        Assert.assertTrue(editor().isSavedTimeDisplayed(), "The saved-time indicator should be visible");
    }

    @Test(groups = { CLASS_GROUP, "Medium" }, description = "TC_04 | Verify the saved time format")
    public void verifySavedTimeFormat() {
        Assert.assertTrue(editor().getSavedTimeText().matches(getConfig("savedTimePattern")),
                "The saved-time indicator should contain Last Saved followed by a 12-hour time and AM/PM");
    }

    @Test(groups = { CLASS_GROUP, "High" }, description = "TC_05 | Verify the Save button label")
    public void verifySaveButtonLabel() {
        Assert.assertEquals(editor().getSaveButtonText(), "Save",
                "The visible Save button should display its expected label");
    }

    @Test(groups = { CLASS_GROUP, "High" }, description = "TC_06 | Verify the Save button is enabled")
    public void verifySaveButtonIsEnabled() {
        Assert.assertTrue(editor().isSaveButtonEnabled(),
                "The Save button should be enabled for the editable test document");
    }

    @Test(groups = { CLASS_GROUP, "High" }, description = "TC_07 | Verify the Reset File button label")
    public void verifyResetButtonLabel() {
        Assert.assertEquals(editor().getResetButtonText(), "Reset File",
                "The visible Reset File button should display its expected label");
    }

    @Test(groups = { CLASS_GROUP, "High" }, description = "TC_08 | Verify the Reset File button is enabled")
    public void verifyResetButtonIsEnabled() {
        Assert.assertTrue(editor().isResetButtonEnabled(),
                "The Reset File button should be enabled for the editable test document");
    }

    @Test(groups = { CLASS_GROUP, "High" }, description = "TC_09 | Verify the Validate File button label")
    public void verifyValidateButtonLabel() {
        Assert.assertEquals(editor().getValidateButtonText(), "Validate File",
                "The visible Validate File button should display its expected label");
    }

    @Test(groups = { CLASS_GROUP, "High" }, description = "TC_10 | Verify the Validate File button is enabled")
    public void verifyValidateButtonIsEnabled() {
        Assert.assertTrue(editor().isValidateButtonEnabled(),
                "The Validate File button should be enabled for the editable test document");
    }

    @Test(groups = { CLASS_GROUP, "High" }, description = "TC_11 | Verify the Check In button label")
    public void verifyCheckInButtonLabel() {
        Assert.assertEquals(editor().getCheckInButtonText(), "Check In",
                "The visible Check In button should display its expected label");
    }

    @Test(groups = { CLASS_GROUP, "High" }, description = "TC_12 | Verify the Check In button is enabled")
    public void verifyCheckInButtonIsEnabled() {
        Assert.assertTrue(editor().isCheckInButtonEnabled(),
                "The Check In button should be enabled for the editable test document");
    }

    @Test(groups = { CLASS_GROUP, "High" }, description = "TC_13 | Verify Bold is active after a single click in bold text")
    public void verifyBoldIsActiveAfterSingleClick() {
        Ab_DocumentEditorPage page = editor();
        page.clickBoldWord();
        Assert.assertTrue(page.isBoldFormattingActive(),
                "Bold should be active with the caret inside the bold word");
    }

    @Test(groups = { CLASS_GROUP, "High" }, description = "TC_14 | Verify Bold is active after double-clicking bold text")
    public void verifyBoldIsActiveAfterDoubleClick() {
        Ab_DocumentEditorPage page = editor();
        page.doubleClickBoldWord();
        Assert.assertTrue(page.isBoldFormattingActive(),
                "Bold should be active when the bold word is selected");
    }

    @Test(groups = { CLASS_GROUP, "High" }, description = "TC_15 | Verify applying Bold to a selected heading word")
    public void verifyApplyingBoldToHeadingWord() {
        Ab_DocumentEditorPage page = editor();
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

    @Test(groups = { CLASS_GROUP, "High" }, description = "TC_16 | Verify Undo restores heading formatting after applying Bold")
    public void verifyUndoRestoresHeadingFormatting() {
        Ab_DocumentEditorPage page = editor();
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

    @Test(groups = { CLASS_GROUP, "High" }, description = "TC_17 | Verify selecting a paragraph shows p")
    public void verifyParagraphSelectionShowsParagraphType() {
        Ab_DocumentEditorPage page = editor();
        page.clickTargetHeading();
        page.waitForSelectedElementType("heading");
        page.clickTargetParagraph();
        page.waitForSelectedElementType("p");
        Assert.assertEquals(page.getSelectedElementType(), "p",
                "Selected Element should change from heading to p after clicking the paragraph");
    }

    @Test(groups = { CLASS_GROUP, "High" }, description = "TC_18 | Verify selecting a heading shows heading")
    public void verifyHeadingSelectionShowsHeadingType() {
        Ab_DocumentEditorPage page = editor();
        page.clickTargetParagraph();
        page.waitForSelectedElementType("p");
        page.clickTargetHeading();
        page.waitForSelectedElementType("heading");
        Assert.assertEquals(page.getSelectedElementType(), "heading",
                "Selected Element should change from p to heading after clicking the heading");
    }
*/
    @Test(groups = { CLASS_GROUP, "High" }, description = "TC_19 | Verify Enter increases the section paragraph count by one")
    public void verifyEnterIncreasesParagraphCount() {
        Ab_DocumentEditorPage page = editor();
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
}
