package e_testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import a_testBase.BaseClass;
import d_pageObjects.Ab_DocumentEditorPage;

/** Requires the editor document to be open in an authenticated session. */
public class Ab_DocumentEditorTC extends BaseClass {
    private static final String CLASS_GROUP = "Ab_DocumentEditorTC";

    private Ab_DocumentEditorPage editor() {
        return new Ab_DocumentEditorPage(getDriver());
    }

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
}
