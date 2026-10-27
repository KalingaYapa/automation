package documenteditor;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import a_testbase.BaseClass;
import b_utilities.ReusableMethods;
import documenteditor.DocumentEditorPageObjects1.ParagraphFormat;

/** Explicit logged test steps; Page Objects own XPath elements and atomic actions/checks. */
public class DocumentEditor1TestCase extends BaseClass {
    private static final Logger logger = LogManager.getLogger(DocumentEditor1TestCase.class);
    private static final String CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG = "Calling login and search file Edit";
    private static final String LOG_LOGIN_AND_SEARCH_FILE = "Login and search file Edit completed";
    private static final String INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG = "Initialising DocumentEditorPageObjects1 page object";
    private static final String PAGE_OBJECT_INITIALISED_MSG = "Page object initialised";
    private static final String SWITCHING_TO_EDITOR_FRAME_MSG = "Switching to the editor frame";
    private static final String LOG_SWITCHED_TO_EDITOR_FRAME = "Switched to the editor frame";
    private static final String SWITCHING_TO_DEFAULT_CONTENT_MSG = "Switching to default content";
    private static final String LOG_SWITCH_DEFAULT_CONTENT = "Switched to default content";
    private static final String READING_BOLD_TOOLBAR_IS_ACTIVE_MSG = "Reading Bold toolbar is active";
    private static final String ASSERTING_BOLD_TOOLBAR_IS_ACTIVE_MSG = "Asserting Bold toolbar is active";
    private static final String SELECTING_THE_HEADING_WORD_MSG = "Selecting the heading word";
    private static final String HEADING_WORD_SELECTED_MSG = "Heading word selected";
    private static final String CHECKING_THE_INITIAL_HEADING_FORMATTING_MSG = "Checking the initial heading formatting";
    private static final String CLICKING_BOLD_ON_THE_TOOLBAR_MSG = "Clicking Bold on the toolbar";
    private static final String FORMATTING_BUTTON_CLICKED_MSG = "Formatting button clicked";
    private static final String WAITING_FOR_RENDERED_HEADING_FORMATTING_MSG = "Waiting for rendered heading formatting";
    private static final String HEADING_FORMATTING_RENDERED_MSG = "Heading formatting rendered";
    private static final String CLICKING_UNDO_FOR_TEST_CLEANUP_MSG = "Clicking Undo for test cleanup";
    private static final String CLEANUP_UNDO_CLICKED_MSG = "Cleanup Undo clicked";
    private static final String WAITING_FOR_ORIGINAL_HEADING_STATE_AFTER_CLEANUP_MSG = "Waiting for original heading state after cleanup";
    private static final String ORIGINAL_HEADING_STATE_RESTORED_MSG = "Original heading state restored";
    private static final String STARTING_TEST_MSG = "Starting test: {}";
    private static final String FINISHED_TEST_MSG = "Finished test: {} - PASSED";

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" },
            description = "TC_42 | Verify switching between Visual Editor and Code View (manual TC-017)")
    public void verifySwitchingBetweenVisualEditorAndCodeViewPreservesContent() {
        logger.info(STARTING_TEST_MSG, "verifySwitchingBetweenVisualEditorAndCodeViewPreservesContent");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        boolean restoreVisualEditor = false;
        Throwable scenarioFailure = null;
        try {
            logger.debug("Waiting for initial Visual Editor display");
            boolean initialVisualEditor = p.isVisualEditorDisplayed();
            logger.info("Initial Visual Editor displayed: {}", initialVisualEditor);
            logger.debug("Asserting Visual Editor is initially active");
            Assert.assertTrue(initialVisualEditor, "The document must open in Visual Editor");
            logger.debug("Initial view assertion passed");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug("Capturing the complete original document markup");
            String originalMarkup = p.getVisualDocumentMarkup();
            String originalText = p.getDocumentTextSnapshot(originalMarkup, false);
            logger.info("Original document markup length={}, text snapshot length={}",
                    originalMarkup.length(), originalText.length());
            logger.debug("Asserting the original document contains content");
            Assert.assertFalse(originalText.isBlank(), "The document must contain text before switching views");
            logger.debug("Original document content assertion passed");
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            restoreVisualEditor = true;
            logger.debug("Opening Code View");
            p.clickCodeViewTab();
            boolean codeViewDisplayed = p.isCodeViewDisplayed();
            String source = p.getCodeViewSource();
            logger.info("Code View displayed={}, source length={}", codeViewDisplayed, source.length());
            logger.debug("Asserting Code View is active with document source");
            Assert.assertTrue(codeViewDisplayed, "Code View must be visible and the VE iframe hidden");
            String sourceText = p.getDocumentTextSnapshot(source, true);
            boolean sourceMatches = originalText.equals(sourceText);
            logger.info("Source XML contains the original document text in order: {}", sourceMatches);
            Assert.assertTrue(sourceMatches, "Source XML must contain all original document text in order");
            logger.debug("Code View source assertions passed; returning to Visual Editor");
            p.clickVisualEditorTab();
            boolean returnedVisualEditor = p.isVisualEditorDisplayed();
            logger.info("Visual Editor displayed after return: {}", returnedVisualEditor);
            logger.debug("Asserting Visual Editor is active again");
            Assert.assertTrue(returnedVisualEditor, "Visual Editor must be visible and Code View hidden after return");
            restoreVisualEditor = false;
            logger.debug("Returned view assertion passed");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            String returnedMarkup = p.getVisualDocumentMarkup();
            boolean contentUnchanged = originalMarkup.equals(returnedMarkup);
            logger.info("Document markup unchanged={}, original length={}, returned length={}",
                    contentUnchanged, originalMarkup.length(), returnedMarkup.length());
            logger.debug("Asserting complete document markup is unchanged after switching views");
            Assert.assertTrue(contentUnchanged,
                    "Switching VE to Code View and back must preserve the complete document markup exactly");
            logger.debug("Document content preservation assertion passed");
        } catch (RuntimeException | AssertionError failure) {
            scenarioFailure = failure;
            throw failure;
        } finally {
            try {
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
                if (restoreVisualEditor) {
                    logger.debug("Restoring Visual Editor after an incomplete view transition");
                    p.clickVisualEditorTab();
                    p.isVisualEditorDisplayed();
                    logger.debug("Visual Editor restored");
                }
                logger.debug("View-only cleanup completed; no document edit requires Undo");
            } catch (RuntimeException | AssertionError cleanupFailure) {
                if (scenarioFailure == null) {
                    throw cleanupFailure;
                }
                scenarioFailure.addSuppressed(cleanupFailure);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifySwitchingBetweenVisualEditorAndCodeViewPreservesContent");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_01 | Verify the document title is visible")
    public void verifyDocumentTitleIsDisplayed() {
        logger.info(STARTING_TEST_MSG, "verifyDocumentTitleIsDisplayed");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug("Reading observed result: Document title visible");
        boolean actualResult = p.isDocumentTitleDisplayed();
        logger.info("Document title visible: {}", actualResult);
        logger.debug("Asserting expected result: Document title visible");
        Assert.assertTrue(actualResult, "The document title should be visible");
        logger.info(FINISHED_TEST_MSG, "verifyDocumentTitleIsDisplayed");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_02 | Verify the document title text")
    public void verifyDocumentTitleText() {
        logger.info(STARTING_TEST_MSG, "verifyDocumentTitleText");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug("Reading observed result: Document title");
        String actualResult = p.getDocumentTitle();
        logger.info("Document title: {}", actualResult);
        logger.debug("Asserting expected result: Document title");
        Assert.assertEquals(actualResult, getConfig("expectedDocumentTitle"),
                "The title bar should identify the expected document");
        logger.info(FINISHED_TEST_MSG, "verifyDocumentTitleText");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P3-DocumentEditor" }, description = "TC_03 | Verify the saved time is visible")
    public void verifySavedTimeIsDisplayed() {
        logger.info(STARTING_TEST_MSG, "verifySavedTimeIsDisplayed");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug("Reading observed result: Saved-time indicator visible");
        boolean actualResult = p.isSavedTimeDisplayed();
        logger.info("Saved-time indicator visible: {}", actualResult);
        logger.debug("Asserting expected result: Saved-time indicator visible");
        Assert.assertTrue(actualResult, "The saved-time indicator should be visible");
        logger.info(FINISHED_TEST_MSG, "verifySavedTimeIsDisplayed");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P3-DocumentEditor" }, description = "TC_04 | Verify saved time is blank initially or has the expected format")
    public void verifySavedTimeFormat() {
        logger.info(STARTING_TEST_MSG, "verifySavedTimeFormat");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        String actual = p.getSavedTimeText();
        logger.info("Saved-time indicator text: '{}'", actual);
        logger.debug("Reading observed result: Saved-time value satisfies the agreed initial-state/format requirement");
        boolean actualResult = actual.isBlank() || actual.matches(getConfig("savedTimePattern"));
        logger.info("Saved-time value satisfies the agreed initial-state/format requirement: {}", actualResult);
        logger.debug("Asserting expected result: Saved-time value satisfies the agreed initial-state/format requirement");
        Assert.assertTrue(actualResult,
                "Saved time may be blank initially; otherwise expect Last Saved followed by a 12-hour time and AM/PM. Actual: '"
                        + actual + "'");
        logger.info(FINISHED_TEST_MSG, "verifySavedTimeFormat");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_05 | Verify the Save button label")
    public void verifySaveButtonLabel() {
        logger.info(STARTING_TEST_MSG, "verifySaveButtonLabel");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug("Reading observed result: Save button label");
        String actualResult = p.getSaveButtonText();
        logger.info("Save button label: {}", actualResult);
        logger.debug("Asserting expected result: Save button label");
        Assert.assertEquals(actualResult, "Save",
                "The visible Save button should display its expected label");
        logger.info(FINISHED_TEST_MSG, "verifySaveButtonLabel");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_06 | Verify the Save button is enabled")
    public void verifySaveButtonIsEnabled() {
        logger.info(STARTING_TEST_MSG, "verifySaveButtonIsEnabled");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug("Reading observed result: Save button enabled");
        boolean actualResult = p.isSaveButtonEnabled();
        logger.info("Save button enabled: {}", actualResult);
        logger.debug("Asserting expected result: Save button enabled");
        Assert.assertTrue(actualResult,
                "The Save button should be enabled for the editable test document");
        logger.info(FINISHED_TEST_MSG, "verifySaveButtonIsEnabled");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_07 | Verify the Reset File button label")
    public void verifyResetButtonLabel() {
        logger.info(STARTING_TEST_MSG, "verifyResetButtonLabel");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug("Reading observed result: Reset File button label");
        String actualResult = p.getResetButtonText();
        logger.info("Reset File button label: {}", actualResult);
        logger.debug("Asserting expected result: Reset File button label");
        Assert.assertEquals(actualResult, "Reset File",
                "The visible Reset File button should display its expected label");
        logger.info(FINISHED_TEST_MSG, "verifyResetButtonLabel");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_08 | Verify the Reset File button is enabled")
    public void verifyResetButtonIsEnabled() {
        logger.info(STARTING_TEST_MSG, "verifyResetButtonIsEnabled");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug("Reading observed result: Reset File button enabled");
        boolean actualResult = p.isResetButtonEnabled();
        logger.info("Reset File button enabled: {}", actualResult);
        logger.debug("Asserting expected result: Reset File button enabled");
        Assert.assertTrue(actualResult,
                "The Reset File button should be enabled for the editable test document");
        logger.info(FINISHED_TEST_MSG, "verifyResetButtonIsEnabled");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_09 | Verify the Validate File button label")
    public void verifyValidateButtonLabel() {
        logger.info(STARTING_TEST_MSG, "verifyValidateButtonLabel");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug("Reading observed result: Validate File button label");
        String actualResult = p.getValidateButtonText();
        logger.info("Validate File button label: {}", actualResult);
        logger.debug("Asserting expected result: Validate File button label");
        Assert.assertEquals(actualResult, "Validate File",
                "The visible Validate File button should display its expected label");
        logger.info(FINISHED_TEST_MSG, "verifyValidateButtonLabel");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_10 | Verify the Validate File button is enabled")
    public void verifyValidateButtonIsEnabled() {
        logger.info(STARTING_TEST_MSG, "verifyValidateButtonIsEnabled");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug("Reading observed result: Validate File button enabled");
        boolean actualResult = p.isValidateButtonEnabled();
        logger.info("Validate File button enabled: {}", actualResult);
        logger.debug("Asserting expected result: Validate File button enabled");
        Assert.assertTrue(actualResult,
                "The Validate File button should be enabled for the editable test document");
        logger.info(FINISHED_TEST_MSG, "verifyValidateButtonIsEnabled");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_11 | Verify the Check In button label")
    public void verifyCheckInButtonLabel() {
        logger.info(STARTING_TEST_MSG, "verifyCheckInButtonLabel");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug("Reading observed result: Check In button label");
        String actualResult = p.getCheckInButtonText();
        logger.info("Check In button label: {}", actualResult);
        logger.debug("Asserting expected result: Check In button label");
        Assert.assertEquals(actualResult, "Check In",
                "The visible Check In button should display its expected label");
        logger.info(FINISHED_TEST_MSG, "verifyCheckInButtonLabel");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_12 | Verify the Check In button is enabled")
    public void verifyCheckInButtonIsEnabled() {
        logger.info(STARTING_TEST_MSG, "verifyCheckInButtonIsEnabled");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug("Reading observed result: Check In button enabled");
        boolean actualResult = p.isCheckInButtonEnabled();
        logger.info("Check In button enabled: {}", actualResult);
        logger.debug("Asserting expected result: Check In button enabled");
        Assert.assertTrue(actualResult,
                "The Check In button should be enabled for the editable test document");
        logger.info(FINISHED_TEST_MSG, "verifyCheckInButtonIsEnabled");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_13 | Verify Bold is active after a single click in bold text")
    public void verifyBoldIsActiveAfterSingleClick() {
        logger.info(STARTING_TEST_MSG, "verifyBoldIsActiveAfterSingleClick");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug("Clicking existing bold text");
        p.clickBoldWord();
        logger.debug("Existing bold text selected");
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        logger.debug(READING_BOLD_TOOLBAR_IS_ACTIVE_MSG);
        boolean observedBoldToolbarIsActive = p.isBoldFormattingActive();
        logger.info("Bold toolbar is active: {}", observedBoldToolbarIsActive);
        logger.debug(ASSERTING_BOLD_TOOLBAR_IS_ACTIVE_MSG);
        Assert.assertTrue(observedBoldToolbarIsActive, "Bold toolbar is active should be true");
        logger.info(FINISHED_TEST_MSG, "verifyBoldIsActiveAfterSingleClick");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_14 | Verify Bold is active after double-clicking bold text")
    public void verifyBoldIsActiveAfterDoubleClick() {
        logger.info(STARTING_TEST_MSG, "verifyBoldIsActiveAfterDoubleClick");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug("Double-clicking existing bold text");
        p.doubleClickBoldWord();
        logger.debug("Existing bold text selected");
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        logger.debug(READING_BOLD_TOOLBAR_IS_ACTIVE_MSG);
        boolean observedBoldToolbarIsActive = p.isBoldFormattingActive();
        logger.info("Bold toolbar is active: {}", observedBoldToolbarIsActive);
        logger.debug(ASSERTING_BOLD_TOOLBAR_IS_ACTIVE_MSG);
        Assert.assertTrue(observedBoldToolbarIsActive, "Bold toolbar is active should be true");
        logger.info(FINISHED_TEST_MSG, "verifyBoldIsActiveAfterDoubleClick");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_17 | Verify selecting a paragraph shows p")
    public void verifyParagraphSelectionShowsParagraphType() {
        logger.info(STARTING_TEST_MSG, "verifyParagraphSelectionShowsParagraphType");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug("Clicking the initial heading");
        p.clickTargetHeading();
        logger.debug("Initial element clicked");
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        logger.debug("Waiting for initial Selected Element");
        p.waitForSelectedElementType("heading");
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug("Clicking the target paragraph");
        p.clickTargetParagraph();
        logger.debug("Target element clicked");
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        logger.debug("Waiting for Selected Element update");
        p.waitForSelectedElementType("p");
        String actual = p.getSelectedElementType();
        logger.info("Selected Element after click: {}", actual);
        logger.debug("Asserting the Selected Element value");
        Assert.assertEquals(actual, "p");
        logger.info(FINISHED_TEST_MSG, "verifyParagraphSelectionShowsParagraphType");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_18 | Verify selecting a heading shows heading")
    public void verifyHeadingSelectionShowsHeadingType() {
        logger.info(STARTING_TEST_MSG, "verifyHeadingSelectionShowsHeadingType");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug("Clicking the initial paragraph");
        p.clickTargetParagraph();
        logger.debug("Initial element clicked");
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        logger.debug("Waiting for initial Selected Element");
        p.waitForSelectedElementType("p");
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug("Clicking the target heading");
        p.clickTargetHeading();
        logger.debug("Target element clicked");
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        logger.debug("Waiting for Selected Element update");
        p.waitForSelectedElementType("heading");
        String actual = p.getSelectedElementType();
        logger.info("Selected Element after click: {}", actual);
        logger.debug("Asserting the Selected Element value");
        Assert.assertEquals(actual, "heading");
        logger.info(FINISHED_TEST_MSG, "verifyHeadingSelectionShowsHeadingType");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_15 | Verify applying Bold to a selected heading word")
    public void verifyApplyingBoldToHeadingWord() {
        logger.info(STARTING_TEST_MSG, "verifyApplyingBoldToHeadingWord");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(SELECTING_THE_HEADING_WORD_MSG);
        p.selectHeadingWord();
        logger.debug(HEADING_WORD_SELECTED_MSG);
        List<String> original = p.getHeadingWordFormattingSnapshot();
        logger.debug(CHECKING_THE_INITIAL_HEADING_FORMATTING_MSG);
        boolean initiallyFormatted = p.isHeadingWordExplicitlyBold();
        logger.info("Heading already has the tested formatting: {}", initiallyFormatted);
        Assert.assertFalse(initiallyFormatted, "Heading must not already have the tested explicit formatting");
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        boolean applied = false;
        try {
            logger.debug(CLICKING_BOLD_ON_THE_TOOLBAR_MSG);
            p.applyBoldToSelection();
            applied = true;
            logger.debug(FORMATTING_BUTTON_CLICKED_MSG);
            logger.debug(READING_BOLD_TOOLBAR_IS_ACTIVE_MSG);
            boolean observedBoldToolbarIsActive = p.isBoldFormattingActive();
            logger.info("Bold toolbar is active: {}", observedBoldToolbarIsActive);
            logger.debug(ASSERTING_BOLD_TOOLBAR_IS_ACTIVE_MSG);
            Assert.assertTrue(observedBoldToolbarIsActive, "Bold toolbar is active should be true");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug(WAITING_FOR_RENDERED_HEADING_FORMATTING_MSG);
            p.waitForHeadingWordStyle(ParagraphFormat.BOLD);
            logger.debug(HEADING_FORMATTING_RENDERED_MSG);
            logger.debug("Reading Selected heading word has Bold");
            boolean observedSelectedHeadingWordHasBold = p.isHeadingWordExplicitlyBold();
            logger.info("Selected heading word has Bold: {}", observedSelectedHeadingWordHasBold);
            logger.debug("Asserting Selected heading word has Bold");
            Assert.assertTrue(observedSelectedHeadingWordHasBold, "Selected heading word has Bold should be true");
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (applied) {
                logger.debug(CLICKING_UNDO_FOR_TEST_CLEANUP_MSG);
                p.clickUndo();
                logger.debug(CLEANUP_UNDO_CLICKED_MSG);
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug(WAITING_FOR_ORIGINAL_HEADING_STATE_AFTER_CLEANUP_MSG);
                p.waitForHeadingFormattingRestored(original, ParagraphFormat.BOLD);
                logger.debug(ORIGINAL_HEADING_STATE_RESTORED_MSG);
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifyApplyingBoldToHeadingWord");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_16 | Verify Undo restores heading formatting after applying Bold")
    public void verifyUndoRestoresHeadingFormatting() {
        logger.info(STARTING_TEST_MSG, "verifyUndoRestoresHeadingFormatting");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(SELECTING_THE_HEADING_WORD_MSG);
        p.selectHeadingWord();
        logger.debug(HEADING_WORD_SELECTED_MSG);
        List<String> original = p.getHeadingWordFormattingSnapshot();
        logger.debug(CHECKING_THE_INITIAL_HEADING_FORMATTING_MSG);
        boolean initiallyFormatted = p.isHeadingWordExplicitlyBold();
        logger.info("Heading already has the tested formatting: {}", initiallyFormatted);
        Assert.assertFalse(initiallyFormatted, "Heading must not already have the tested explicit formatting");
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        boolean applied = false;
        try {
            logger.debug(CLICKING_BOLD_ON_THE_TOOLBAR_MSG);
            p.applyBoldToSelection();
            applied = true;
            logger.debug(FORMATTING_BUTTON_CLICKED_MSG);
            logger.debug(READING_BOLD_TOOLBAR_IS_ACTIVE_MSG);
            boolean observedBoldToolbarIsActive = p.isBoldFormattingActive();
            logger.info("Bold toolbar is active: {}", observedBoldToolbarIsActive);
            logger.debug(ASSERTING_BOLD_TOOLBAR_IS_ACTIVE_MSG);
            Assert.assertTrue(observedBoldToolbarIsActive, "Bold toolbar is active should be true");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug(WAITING_FOR_RENDERED_HEADING_FORMATTING_MSG);
            p.waitForHeadingWordStyle(ParagraphFormat.BOLD);
            logger.debug(HEADING_FORMATTING_RENDERED_MSG);
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            logger.debug("Clicking Undo");
            p.clickUndo();
            applied = false;
            logger.debug("Undo clicked");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug("Waiting for original heading formatting");
            p.waitForHeadingFormattingRestored(original, ParagraphFormat.BOLD);
            logger.debug("Original heading formatting restored");
            List<String> actual = p.getHeadingWordFormattingSnapshot();
            logger.info("Heading snapshot after Undo: {}", actual);
            logger.debug("Asserting that Undo restored the original heading snapshot");
            Assert.assertEquals(actual, original);
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            logger.debug("Reading Bold is inactive after Undo");
            boolean observedBoldIsInactiveAfterUndo = p.isBoldFormattingInactive();
            logger.info("Bold is inactive after Undo: {}", observedBoldIsInactiveAfterUndo);
            logger.debug("Asserting Bold is inactive after Undo");
            Assert.assertTrue(observedBoldIsInactiveAfterUndo, "Bold is inactive after Undo should be true");
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (applied) {
                logger.debug(CLICKING_UNDO_FOR_TEST_CLEANUP_MSG);
                p.clickUndo();
                logger.debug(CLEANUP_UNDO_CLICKED_MSG);
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug(WAITING_FOR_ORIGINAL_HEADING_STATE_AFTER_CLEANUP_MSG);
                p.waitForHeadingFormattingRestored(original, ParagraphFormat.BOLD);
                logger.debug(ORIGINAL_HEADING_STATE_RESTORED_MSG);
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifyUndoRestoresHeadingFormatting");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_25 | Verify applying Italic to a selected heading word")
    public void verifyApplyingItalicToHeadingWord() {
        logger.info(STARTING_TEST_MSG, "verifyApplyingItalicToHeadingWord");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(SELECTING_THE_HEADING_WORD_MSG);
        p.selectHeadingWord();
        logger.debug(HEADING_WORD_SELECTED_MSG);
        List<String> original = p.getHeadingWordFormattingSnapshot();
        logger.debug(CHECKING_THE_INITIAL_HEADING_FORMATTING_MSG);
        boolean initiallyFormatted = p.isHeadingWordItalic();
        logger.info("Heading already has the tested formatting: {}", initiallyFormatted);
        Assert.assertFalse(initiallyFormatted, "Heading must not already have the tested explicit formatting");
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        boolean applied = false;
        try {
            logger.debug("Clicking Italic on the toolbar");
            p.applyItalicToSelection();
            applied = true;
            logger.debug(FORMATTING_BUTTON_CLICKED_MSG);
            logger.debug("Reading Italic toolbar is active");
            boolean observedItalicToolbarIsActive = p.isItalicFormattingActive();
            logger.info("Italic toolbar is active: {}", observedItalicToolbarIsActive);
            logger.debug("Asserting Italic toolbar is active");
            Assert.assertTrue(observedItalicToolbarIsActive, "Italic toolbar is active should be true");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug(WAITING_FOR_RENDERED_HEADING_FORMATTING_MSG);
            p.waitForHeadingWordStyle(ParagraphFormat.ITALIC);
            logger.debug(HEADING_FORMATTING_RENDERED_MSG);
            logger.debug("Reading Selected heading word has Italic");
            boolean observedSelectedHeadingWordHasItalic = p.isHeadingWordItalic();
            logger.info("Selected heading word has Italic: {}", observedSelectedHeadingWordHasItalic);
            logger.debug("Asserting Selected heading word has Italic");
            Assert.assertTrue(observedSelectedHeadingWordHasItalic, "Selected heading word has Italic should be true");
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (applied) {
                logger.debug(CLICKING_UNDO_FOR_TEST_CLEANUP_MSG);
                p.clickUndo();
                logger.debug(CLEANUP_UNDO_CLICKED_MSG);
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug(WAITING_FOR_ORIGINAL_HEADING_STATE_AFTER_CLEANUP_MSG);
                p.waitForHeadingFormattingRestored(original, ParagraphFormat.ITALIC);
                logger.debug(ORIGINAL_HEADING_STATE_RESTORED_MSG);
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifyApplyingItalicToHeadingWord");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_26 | Verify applying Underline to a selected heading word")
    public void verifyApplyingUnderlineToHeadingWord() {
        logger.info(STARTING_TEST_MSG, "verifyApplyingUnderlineToHeadingWord");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(SELECTING_THE_HEADING_WORD_MSG);
        p.selectHeadingWord();
        logger.debug(HEADING_WORD_SELECTED_MSG);
        List<String> original = p.getHeadingWordFormattingSnapshot();
        logger.debug(CHECKING_THE_INITIAL_HEADING_FORMATTING_MSG);
        boolean initiallyFormatted = p.isHeadingWordUnderlined();
        logger.info("Heading already has the tested formatting: {}", initiallyFormatted);
        Assert.assertFalse(initiallyFormatted, "Heading must not already have the tested explicit formatting");
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        boolean applied = false;
        try {
            logger.debug("Clicking Underline on the toolbar");
            p.applyUnderlineToSelection();
            applied = true;
            logger.debug(FORMATTING_BUTTON_CLICKED_MSG);
            logger.debug("Reading Underline toolbar is active");
            boolean observedUnderlineToolbarIsActive = p.isUnderlineFormattingActive();
            logger.info("Underline toolbar is active: {}", observedUnderlineToolbarIsActive);
            logger.debug("Asserting Underline toolbar is active");
            Assert.assertTrue(observedUnderlineToolbarIsActive, "Underline toolbar is active should be true");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug(WAITING_FOR_RENDERED_HEADING_FORMATTING_MSG);
            p.waitForHeadingWordStyle(ParagraphFormat.UNDERLINE);
            logger.debug(HEADING_FORMATTING_RENDERED_MSG);
            logger.debug("Reading Selected heading word has Underline");
            boolean observedSelectedHeadingWordHasUnderline = p.isHeadingWordUnderlined();
            logger.info("Selected heading word has Underline: {}", observedSelectedHeadingWordHasUnderline);
            logger.debug("Asserting Selected heading word has Underline");
            Assert.assertTrue(observedSelectedHeadingWordHasUnderline, "Selected heading word has Underline should be true");
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (applied) {
                logger.debug(CLICKING_UNDO_FOR_TEST_CLEANUP_MSG);
                p.clickUndo();
                logger.debug(CLEANUP_UNDO_CLICKED_MSG);
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug(WAITING_FOR_ORIGINAL_HEADING_STATE_AFTER_CLEANUP_MSG);
                p.waitForHeadingFormattingRestored(original, ParagraphFormat.UNDERLINE);
                logger.debug(ORIGINAL_HEADING_STATE_RESTORED_MSG);
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifyApplyingUnderlineToHeadingWord");
    }

    @Test(enabled = true, groups = { "DocumentEditor", "P2-DocumentEditor" }, description = "TC_27 | Verify Redo restores undone Bold formatting")
    public void verifyRedoRestoresBoldFormatting() {
        logger.info(STARTING_TEST_MSG, "verifyRedoRestoresBoldFormatting");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_DOCUMENTEDITORPAGEOBJECTS1_PAGE_OBJECT_MSG);
        DocumentEditorPageObjects1 p = new DocumentEditorPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(SELECTING_THE_HEADING_WORD_MSG);
        p.selectHeadingWord();
        logger.debug(HEADING_WORD_SELECTED_MSG);
        List<String> original = p.getHeadingWordFormattingSnapshot();
        logger.debug(CHECKING_THE_INITIAL_HEADING_FORMATTING_MSG);
        boolean initiallyFormatted = p.isHeadingWordExplicitlyBold();
        logger.info("Heading already has the tested formatting: {}", initiallyFormatted);
        Assert.assertFalse(initiallyFormatted, "Heading must not already have the tested explicit formatting");
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        boolean applied = false;
        try {
            logger.debug(CLICKING_BOLD_ON_THE_TOOLBAR_MSG);
            p.applyBoldToSelection();
            applied = true;
            logger.debug(FORMATTING_BUTTON_CLICKED_MSG);
            logger.debug(READING_BOLD_TOOLBAR_IS_ACTIVE_MSG);
            boolean observedBoldToolbarIsActive = p.isBoldFormattingActive();
            logger.info("Bold toolbar is active: {}", observedBoldToolbarIsActive);
            logger.debug(ASSERTING_BOLD_TOOLBAR_IS_ACTIVE_MSG);
            Assert.assertTrue(observedBoldToolbarIsActive, "Bold toolbar is active should be true");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug(WAITING_FOR_RENDERED_HEADING_FORMATTING_MSG);
            p.waitForHeadingWordStyle(ParagraphFormat.BOLD);
            logger.debug(HEADING_FORMATTING_RENDERED_MSG);
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            logger.debug("Clicking Undo");
            p.clickUndo();
            applied = false;
            logger.debug("Undo clicked");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug("Waiting for original heading formatting");
            p.waitForHeadingFormattingRestored(original, ParagraphFormat.BOLD);
            logger.debug("Original heading formatting restored");
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            logger.debug("Clicking Redo");
            p.clickRedo();
            applied = true;
            logger.debug("Redo clicked");
            logger.debug("Reading Bold toolbar is active after Redo");
            boolean redoActive = p.isBoldFormattingActive();
            logger.info("Bold toolbar is active after Redo: {}", redoActive);
            logger.debug("Asserting Bold toolbar is active after Redo");
            Assert.assertTrue(redoActive, "Bold toolbar is active after Redo should be true");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug("Waiting for restored Bold formatting");
            p.waitForHeadingWordStyle(ParagraphFormat.BOLD);
            logger.debug("Reading Selected heading word has Bold");
            boolean observedSelectedHeadingWordHasBold = p.isHeadingWordExplicitlyBold();
            logger.info("Selected heading word has Bold: {}", observedSelectedHeadingWordHasBold);
            logger.debug("Asserting Selected heading word has Bold");
            Assert.assertTrue(observedSelectedHeadingWordHasBold, "Selected heading word has Bold should be true");
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (applied) {
                logger.debug(CLICKING_UNDO_FOR_TEST_CLEANUP_MSG);
                p.clickUndo();
                logger.debug(CLEANUP_UNDO_CLICKED_MSG);
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug(WAITING_FOR_ORIGINAL_HEADING_STATE_AFTER_CLEANUP_MSG);
                p.waitForHeadingFormattingRestored(original, ParagraphFormat.BOLD);
                logger.debug(ORIGINAL_HEADING_STATE_RESTORED_MSG);
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifyRedoRestoresBoldFormatting");
    }

}
