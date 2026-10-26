package para;

import java.util.List;
import java.util.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import a_testbase.BaseClass;
import b_utilities.ReusableMethods;
import documenteditor.DocumentEditorPageObjects1.ParagraphFormat;

/** Explicit logged test steps; Page Objects own XPath elements and atomic actions/checks. */
public class Para1TestCase extends BaseClass {
    private static final Logger logger = LogManager.getLogger(Para1TestCase.class);
    private static final String CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG = "Calling login and search file Edit";
    private static final String LOG_LOGIN_AND_SEARCH_FILE = "Login and search file Edit completed";
    private static final String INITIALISING_PARAPAGEOBJECTS1_PAGE_OBJECT_MSG = "Initialising ParaPageObjects1 page object";
    private static final String PAGE_OBJECT_INITIALISED_MSG = "Page object initialised";
    private static final String SWITCHING_TO_EDITOR_FRAME_MSG = "Switching to the editor frame";
    private static final String LOG_SWITCHED_TO_EDITOR_FRAME = "Switched to the editor frame";
    private static final String REMEMBERING_THE_TARGET_SECTION_FOR_DOCUMENT_STRUCTURE_CHECKS_MSG = "Remembering the target section for Document Structure checks";
    private static final String TARGET_SECTION_REMEMBERED_MSG = "Target section remembered";
    private static final String SWITCHING_TO_DEFAULT_CONTENT_MSG = "Switching to default content";
    private static final String LOG_SWITCH_DEFAULT_CONTENT = "Switched to default content";
    private static final String CLICKING_THE_TARGET_PARAGRAPH_IN_VE_MSG = "Clicking the target paragraph in VE";
    private static final String TARGET_PARAGRAPH_CLICKED_MSG = "Target paragraph clicked";
    private static final String PLACING_THE_CURSOR_AT_THE_REQUIRED_PARAGRAPH_POSITION_MSG = "Placing the cursor at the required paragraph position";
    private static final String PARAGRAPH_CURSOR_POSITIONED_MSG = "Paragraph cursor positioned";
    private static final String PRESSING_ENTER_IN_THE_PARAGRAPH_MSG = "Pressing Enter in the paragraph";
    private static final String ENTER_KEY_PRESSED_MSG = "Enter key pressed";
    private static final String WAITING_FOR_THE_NEW_PARAGRAPH_TEXT_IN_VE_MSG = "Waiting for the new paragraph text in VE";
    private static final String NEW_PARAGRAPH_TEXT_IS_PRESENT_IN_VE_MSG = "New paragraph text is present in VE";
    private static final String ASSERTING_THE_EXACT_PARAGRAPH_TEXT_SEQUENCE_IN_VE_MSG = "Asserting the exact paragraph text sequence in VE";
    private static final String WAITING_FOR_THE_INCREASED_PARAGRAPH_COUNT_IN_DS_MSG = "Waiting for the increased paragraph count in DS";
    private static final String ASSERTING_THE_NEW_PARAGRAPH_IS_PRESENT_IN_DS_MSG = "Asserting the new paragraph is present in DS";
    private static final String CLICKING_UNDO_TO_RESTORE_THE_PARAGRAPH_MSG = "Clicking Undo to restore the paragraph";
    private static final String CLEANUP_UNDO_CLICKED_MSG = "Cleanup Undo clicked";
    private static final String WAITING_FOR_ORIGINAL_PARAGRAPH_TEXT_IN_VE_MSG = "Waiting for original paragraph text in VE";
    private static final String ORIGINAL_PARAGRAPH_TEXT_RESTORED_MSG = "Original paragraph text restored";
    private static final String WAITING_FOR_ORIGINAL_PARAGRAPH_COUNT_IN_DS_MSG = "Waiting for original paragraph count in DS";
    private static final String ORIGINAL_PARAGRAPH_COUNT_RESTORED_MSG = "Original paragraph count restored";
    private static final String READING_ORIGINAL_PARAGRAPH_STYLES_IN_VE_MSG = "Reading original paragraph styles in VE";
    private static final String ORIGINAL_PARAGRAPH_STYLES_CAPTURED_MSG = "Original paragraph styles captured";
    private static final String CHECKING_THAT_THE_TESTED_PARAGRAPH_FORMAT_IS_INITIALLY_OFF_MSG = "Checking that the tested paragraph format is initially off";
    private static final String INITIAL_PARAGRAPH_FORMAT_CHECKED_MSG = "Initial paragraph format checked";
    private static final String SELECTING_THE_PARAGRAPH_WORD_IN_VE_MSG = "Selecting the paragraph word in VE";
    private static final String PARAGRAPH_WORD_SELECTED_MSG = "Paragraph word selected";
    private static final String FORMATTING_BUTTON_CLICKED_MSG = "Formatting button clicked";
    private static final String WAITING_FOR_FORMATTING_OF_ONLY_THE_SELECTED_PARAGRAPH_WORD_MSG = "Waiting for formatting of only the selected paragraph word";
    private static final String SELECTED_PARAGRAPH_WORD_FORMATTING_IS_PRESENT_MSG = "Selected paragraph word formatting is present";
    private static final String CLICKING_UNDO_FOR_PARAGRAPH_FORMATTING_CLEANUP_MSG = "Clicking Undo for paragraph formatting cleanup";
    private static final String PARAGRAPH_FORMATTING_UNDO_CLICKED_MSG = "Paragraph formatting Undo clicked";
    private static final String WAITING_FOR_THE_ORIGINAL_PARAGRAPH_CHARACTER_STYLES_MSG = "Waiting for the original paragraph character styles";
    private static final String ORIGINAL_PARAGRAPH_TEXT_AND_STYLES_RESTORED_MSG = "Original paragraph text and styles restored";
    private static final String STARTING_TEST_MSG = "Starting test: {}";
    private static final String FINISHED_TEST_MSG = "Finished test: {} - PASSED";

    @Test(enabled = true, groups = { "Para", "P2-Para" }, description = "TC_19 | Verify Enter splits paragraph text and Undo restores it")
    public void verifyEnterIncreasesParagraphCount() {
        logger.info(STARTING_TEST_MSG, "verifyEnterIncreasesParagraphCount");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_PARAPAGEOBJECTS1_PAGE_OBJECT_MSG);
        ParaPageObjects1 p = new ParaPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(REMEMBERING_THE_TARGET_SECTION_FOR_DOCUMENT_STRUCTURE_CHECKS_MSG);
        p.rememberTargetSection();
        logger.debug(TARGET_SECTION_REMEMBERED_MSG);
        logger.debug(CLICKING_THE_TARGET_PARAGRAPH_IN_VE_MSG);
        p.clickOnFirstParaVe();
        logger.debug(TARGET_PARAGRAPH_CLICKED_MSG);
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        int originalCount = p.getParagraphCountInDs();
        logger.info("Original paragraph count in DS: {}", originalCount);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(PLACING_THE_CURSOR_AT_THE_REQUIRED_PARAGRAPH_POSITION_MSG);
        p.placeCursorInMiddleOfPara();
        logger.debug(PARAGRAPH_CURSOR_POSITIONED_MSG);
        boolean enterPending = false;
        try {
            logger.debug(PRESSING_ENTER_IN_THE_PARAGRAPH_MSG);
            p.enterKey();
            enterPending = true;
            logger.debug(ENTER_KEY_PRESSED_MSG);
            logger.debug(WAITING_FOR_THE_NEW_PARAGRAPH_TEXT_IN_VE_MSG);
            p.waitForEnterResultInVe();
            logger.debug(NEW_PARAGRAPH_TEXT_IS_PRESENT_IN_VE_MSG);
            List<String> actualTexts = p.getParagraphTextsInVe();
            logger.info("Paragraph text sequence in VE: {}", actualTexts);
            logger.debug(ASSERTING_THE_EXACT_PARAGRAPH_TEXT_SEQUENCE_IN_VE_MSG);
            Assert.assertEquals(actualTexts, p.getExpectedParagraphTexts());
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            logger.debug(WAITING_FOR_THE_INCREASED_PARAGRAPH_COUNT_IN_DS_MSG);
            p.waitForParagraphCountInDs(originalCount + 1);
            int actualCount = p.getParagraphCountInDs();
            logger.info("Paragraph count in DS after Enter: {}", actualCount);
            logger.debug(ASSERTING_THE_NEW_PARAGRAPH_IS_PRESENT_IN_DS_MSG);
            Assert.assertEquals(actualCount, originalCount + 1);
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (enterPending) {
                logger.debug(CLICKING_UNDO_TO_RESTORE_THE_PARAGRAPH_MSG);
                p.clickUndo();
                logger.debug(CLEANUP_UNDO_CLICKED_MSG);
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug(WAITING_FOR_ORIGINAL_PARAGRAPH_TEXT_IN_VE_MSG);
                p.waitForOriginalParagraphTextsInVe();
                logger.debug(ORIGINAL_PARAGRAPH_TEXT_RESTORED_MSG);
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
                logger.debug(WAITING_FOR_ORIGINAL_PARAGRAPH_COUNT_IN_DS_MSG);
                p.waitForParagraphCountInDs(originalCount);
                logger.debug(ORIGINAL_PARAGRAPH_COUNT_RESTORED_MSG);
            }
        }
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        List<String> restoredTexts = p.getParagraphTextsInVe();
        logger.info("Paragraph text sequence after Undo: {}", restoredTexts);
        logger.debug("Asserting Undo joined the text back into the original paragraph");
        Assert.assertEquals(restoredTexts, p.getOriginalParagraphTexts());
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        logger.info(FINISHED_TEST_MSG, "verifyEnterIncreasesParagraphCount");
    }

    @Test(enabled = true, groups = { "Para", "P2-Para" }, description = "TC_30 | Verify Enter at the beginning of a paragraph")
    public void verifyEnterAtParagraphBeginning() {
        logger.info(STARTING_TEST_MSG, "verifyEnterAtParagraphBeginning");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_PARAPAGEOBJECTS1_PAGE_OBJECT_MSG);
        ParaPageObjects1 p = new ParaPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(REMEMBERING_THE_TARGET_SECTION_FOR_DOCUMENT_STRUCTURE_CHECKS_MSG);
        p.rememberTargetSection();
        logger.debug(TARGET_SECTION_REMEMBERED_MSG);
        logger.debug(CLICKING_THE_TARGET_PARAGRAPH_IN_VE_MSG);
        p.clickOnFirstParaVe();
        logger.debug(TARGET_PARAGRAPH_CLICKED_MSG);
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        int originalCount = p.getParagraphCountInDs();
        logger.info("Original paragraph count in DS: {}", originalCount);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(PLACING_THE_CURSOR_AT_THE_REQUIRED_PARAGRAPH_POSITION_MSG);
        p.placeCursorAtBeginningOfPara();
        logger.debug(PARAGRAPH_CURSOR_POSITIONED_MSG);
        boolean enterPending = false;
        try {
            logger.debug(PRESSING_ENTER_IN_THE_PARAGRAPH_MSG);
            p.enterKey();
            enterPending = true;
            logger.debug(ENTER_KEY_PRESSED_MSG);
            logger.debug(WAITING_FOR_THE_NEW_PARAGRAPH_TEXT_IN_VE_MSG);
            p.waitForEnterResultInVe();
            logger.debug(NEW_PARAGRAPH_TEXT_IS_PRESENT_IN_VE_MSG);
            List<String> actualTexts = p.getParagraphTextsInVe();
            logger.info("Paragraph text sequence in VE: {}", actualTexts);
            logger.debug(ASSERTING_THE_EXACT_PARAGRAPH_TEXT_SEQUENCE_IN_VE_MSG);
            Assert.assertEquals(actualTexts, p.getExpectedParagraphTexts());
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            logger.debug(WAITING_FOR_THE_INCREASED_PARAGRAPH_COUNT_IN_DS_MSG);
            p.waitForParagraphCountInDs(originalCount + 1);
            int actualCount = p.getParagraphCountInDs();
            logger.info("Paragraph count in DS after Enter: {}", actualCount);
            logger.debug(ASSERTING_THE_NEW_PARAGRAPH_IS_PRESENT_IN_DS_MSG);
            Assert.assertEquals(actualCount, originalCount + 1);
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (enterPending) {
                logger.debug(CLICKING_UNDO_TO_RESTORE_THE_PARAGRAPH_MSG);
                p.clickUndo();
                logger.debug(CLEANUP_UNDO_CLICKED_MSG);
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug(WAITING_FOR_ORIGINAL_PARAGRAPH_TEXT_IN_VE_MSG);
                p.waitForOriginalParagraphTextsInVe();
                logger.debug(ORIGINAL_PARAGRAPH_TEXT_RESTORED_MSG);
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
                logger.debug(WAITING_FOR_ORIGINAL_PARAGRAPH_COUNT_IN_DS_MSG);
                p.waitForParagraphCountInDs(originalCount);
                logger.debug(ORIGINAL_PARAGRAPH_COUNT_RESTORED_MSG);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifyEnterAtParagraphBeginning");
    }

    @Test(enabled = true, groups = { "Para", "P2-Para" }, description = "TC_31 | Verify Enter at the end of a paragraph")
    public void verifyEnterAtParagraphEnd() {
        logger.info(STARTING_TEST_MSG, "verifyEnterAtParagraphEnd");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_PARAPAGEOBJECTS1_PAGE_OBJECT_MSG);
        ParaPageObjects1 p = new ParaPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(REMEMBERING_THE_TARGET_SECTION_FOR_DOCUMENT_STRUCTURE_CHECKS_MSG);
        p.rememberTargetSection();
        logger.debug(TARGET_SECTION_REMEMBERED_MSG);
        logger.debug(CLICKING_THE_TARGET_PARAGRAPH_IN_VE_MSG);
        p.clickOnFirstParaVe();
        logger.debug(TARGET_PARAGRAPH_CLICKED_MSG);
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        int originalCount = p.getParagraphCountInDs();
        logger.info("Original paragraph count in DS: {}", originalCount);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(PLACING_THE_CURSOR_AT_THE_REQUIRED_PARAGRAPH_POSITION_MSG);
        p.placeCursorAtEndOfPara();
        logger.debug(PARAGRAPH_CURSOR_POSITIONED_MSG);
        boolean enterPending = false;
        try {
            logger.debug(PRESSING_ENTER_IN_THE_PARAGRAPH_MSG);
            p.enterKey();
            enterPending = true;
            logger.debug(ENTER_KEY_PRESSED_MSG);
            logger.debug(WAITING_FOR_THE_NEW_PARAGRAPH_TEXT_IN_VE_MSG);
            p.waitForEnterResultInVe();
            logger.debug(NEW_PARAGRAPH_TEXT_IS_PRESENT_IN_VE_MSG);
            List<String> actualTexts = p.getParagraphTextsInVe();
            logger.info("Paragraph text sequence in VE: {}", actualTexts);
            logger.debug(ASSERTING_THE_EXACT_PARAGRAPH_TEXT_SEQUENCE_IN_VE_MSG);
            Assert.assertEquals(actualTexts, p.getExpectedParagraphTexts());
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            logger.debug(WAITING_FOR_THE_INCREASED_PARAGRAPH_COUNT_IN_DS_MSG);
            p.waitForParagraphCountInDs(originalCount + 1);
            int actualCount = p.getParagraphCountInDs();
            logger.info("Paragraph count in DS after Enter: {}", actualCount);
            logger.debug(ASSERTING_THE_NEW_PARAGRAPH_IS_PRESENT_IN_DS_MSG);
            Assert.assertEquals(actualCount, originalCount + 1);
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (enterPending) {
                logger.debug(CLICKING_UNDO_TO_RESTORE_THE_PARAGRAPH_MSG);
                p.clickUndo();
                logger.debug(CLEANUP_UNDO_CLICKED_MSG);
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug(WAITING_FOR_ORIGINAL_PARAGRAPH_TEXT_IN_VE_MSG);
                p.waitForOriginalParagraphTextsInVe();
                logger.debug(ORIGINAL_PARAGRAPH_TEXT_RESTORED_MSG);
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
                logger.debug(WAITING_FOR_ORIGINAL_PARAGRAPH_COUNT_IN_DS_MSG);
                p.waitForParagraphCountInDs(originalCount);
                logger.debug(ORIGINAL_PARAGRAPH_COUNT_RESTORED_MSG);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifyEnterAtParagraphEnd");
    }

    @Test(enabled = true, groups = { "Para", "P2-Para" }, description = "TC_32 | Verify Redo restores the undone paragraph split")
    public void verifyRedoRestoresParagraphSplit() {
        logger.info(STARTING_TEST_MSG, "verifyRedoRestoresParagraphSplit");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_PARAPAGEOBJECTS1_PAGE_OBJECT_MSG);
        ParaPageObjects1 p = new ParaPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(REMEMBERING_THE_TARGET_SECTION_FOR_DOCUMENT_STRUCTURE_CHECKS_MSG);
        p.rememberTargetSection();
        logger.debug(TARGET_SECTION_REMEMBERED_MSG);
        logger.debug(CLICKING_THE_TARGET_PARAGRAPH_IN_VE_MSG);
        p.clickOnFirstParaVe();
        logger.debug(TARGET_PARAGRAPH_CLICKED_MSG);
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        int originalCount = p.getParagraphCountInDs();
        logger.info("Original paragraph count in DS: {}", originalCount);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(PLACING_THE_CURSOR_AT_THE_REQUIRED_PARAGRAPH_POSITION_MSG);
        p.placeCursorInMiddleOfPara();
        logger.debug(PARAGRAPH_CURSOR_POSITIONED_MSG);
        boolean enterPending = false;
        try {
            logger.debug(PRESSING_ENTER_IN_THE_PARAGRAPH_MSG);
            p.enterKey();
            enterPending = true;
            logger.debug(ENTER_KEY_PRESSED_MSG);
            logger.debug(WAITING_FOR_THE_NEW_PARAGRAPH_TEXT_IN_VE_MSG);
            p.waitForEnterResultInVe();
            logger.debug(NEW_PARAGRAPH_TEXT_IS_PRESENT_IN_VE_MSG);
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            logger.debug("Clicking Undo for the Enter action");
            p.clickUndo();
            enterPending = false;
            logger.debug("Undo clicked");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug(WAITING_FOR_ORIGINAL_PARAGRAPH_TEXT_IN_VE_MSG);
            p.waitForOriginalParagraphTextsInVe();
            logger.debug(ORIGINAL_PARAGRAPH_TEXT_RESTORED_MSG);
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            logger.debug("Verifying the original paragraph count in DS");
            p.waitForParagraphCountInDs(originalCount);
            logger.debug("Clicking Redo for the Enter action");
            p.clickRedo();
            enterPending = true;
            logger.debug("Redo clicked");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug("Waiting for the repeated split in VE");
            p.waitForEnterResultInVe();
            logger.debug("Redo split is present in VE");
            List<String> actualTexts = p.getParagraphTextsInVe();
            logger.info("Paragraph text sequence in VE: {}", actualTexts);
            logger.debug(ASSERTING_THE_EXACT_PARAGRAPH_TEXT_SEQUENCE_IN_VE_MSG);
            Assert.assertEquals(actualTexts, p.getExpectedParagraphTexts());
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            logger.debug(WAITING_FOR_THE_INCREASED_PARAGRAPH_COUNT_IN_DS_MSG);
            p.waitForParagraphCountInDs(originalCount + 1);
            int actualCount = p.getParagraphCountInDs();
            logger.info("Paragraph count in DS after Enter: {}", actualCount);
            logger.debug(ASSERTING_THE_NEW_PARAGRAPH_IS_PRESENT_IN_DS_MSG);
            Assert.assertEquals(actualCount, originalCount + 1);
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (enterPending) {
                logger.debug(CLICKING_UNDO_TO_RESTORE_THE_PARAGRAPH_MSG);
                p.clickUndo();
                logger.debug(CLEANUP_UNDO_CLICKED_MSG);
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug(WAITING_FOR_ORIGINAL_PARAGRAPH_TEXT_IN_VE_MSG);
                p.waitForOriginalParagraphTextsInVe();
                logger.debug(ORIGINAL_PARAGRAPH_TEXT_RESTORED_MSG);
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
                logger.debug(WAITING_FOR_ORIGINAL_PARAGRAPH_COUNT_IN_DS_MSG);
                p.waitForParagraphCountInDs(originalCount);
                logger.debug(ORIGINAL_PARAGRAPH_COUNT_RESTORED_MSG);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifyRedoRestoresParagraphSplit");
    }

    @Test(enabled = true, groups = { "Para", "P2-Para" }, description = "TC_28 | Verify applying an unordered list to a paragraph")
    public void verifyApplyingUnorderedListToParagraph() {
        logger.info(STARTING_TEST_MSG, "verifyApplyingUnorderedListToParagraph");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_PARAPAGEOBJECTS1_PAGE_OBJECT_MSG);
        ParaPageObjects1 p = new ParaPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug("Checking that the paragraph is initially outside a list");
        p.requireParagraphNotAListInVe();
        logger.debug(CLICKING_THE_TARGET_PARAGRAPH_IN_VE_MSG);
        p.clickOnFirstParaVe();
        logger.debug(TARGET_PARAGRAPH_CLICKED_MSG);
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        boolean applied = false;
        try {
            logger.debug("Clicking Unordered List in the toolbar");
            p.clickUnorderedList();
            applied = true;
            logger.debug("List toolbar button clicked");
            logger.debug("Reading Unordered list toolbar is active");
            boolean observedUnorderedListToolbarIsActive = p.isUnorderedListButtonActive();
            logger.info("Unordered list toolbar is active: {}", observedUnorderedListToolbarIsActive);
            logger.debug("Asserting Unordered list toolbar is active");
            Assert.assertTrue(observedUnorderedListToolbarIsActive, "Unordered list toolbar is active should be true");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug("Reading Paragraph is an unordered list item in VE");
            boolean listPresent = p.isParagraphAnUnorderedListInVe();
            logger.info("Paragraph is an unordered list item in VE: {}", listPresent);
            logger.debug("Asserting Paragraph is an unordered list item in VE");
            Assert.assertTrue(listPresent, "Paragraph is an unordered list item in VE should be true");
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (applied) {
                logger.debug("Clicking Undo for list cleanup");
                p.clickUndo();
                logger.debug("List cleanup Undo clicked");
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug("Waiting for the original non-list paragraph");
                p.waitForParagraphNotAListInVe();
                logger.debug("Original paragraph restored");
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifyApplyingUnorderedListToParagraph");
    }

    @Test(enabled = true, groups = { "Para", "P2-Para" }, description = "TC_29 | Verify applying an ordered list to a paragraph")
    public void verifyApplyingOrderedListToParagraph() {
        logger.info(STARTING_TEST_MSG, "verifyApplyingOrderedListToParagraph");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_PARAPAGEOBJECTS1_PAGE_OBJECT_MSG);
        ParaPageObjects1 p = new ParaPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug("Checking that the paragraph is initially outside a list");
        p.requireParagraphNotAListInVe();
        logger.debug(CLICKING_THE_TARGET_PARAGRAPH_IN_VE_MSG);
        p.clickOnFirstParaVe();
        logger.debug(TARGET_PARAGRAPH_CLICKED_MSG);
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        boolean applied = false;
        try {
            logger.debug("Clicking Ordered List in the toolbar");
            p.clickOrderedList();
            applied = true;
            logger.debug("List toolbar button clicked");
            logger.debug("Reading Ordered list toolbar is active");
            boolean observedOrderedListToolbarIsActive = p.isOrderedListButtonActive();
            logger.info("Ordered list toolbar is active: {}", observedOrderedListToolbarIsActive);
            logger.debug("Asserting Ordered list toolbar is active");
            Assert.assertTrue(observedOrderedListToolbarIsActive, "Ordered list toolbar is active should be true");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug("Reading Paragraph is an ordered list item in VE");
            boolean listPresent = p.isParagraphAnOrderedListInVe();
            logger.info("Paragraph is an ordered list item in VE: {}", listPresent);
            logger.debug("Asserting Paragraph is an ordered list item in VE");
            Assert.assertTrue(listPresent, "Paragraph is an ordered list item in VE should be true");
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (applied) {
                logger.debug("Clicking Undo for list cleanup");
                p.clickUndo();
                logger.debug("List cleanup Undo clicked");
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug("Waiting for the original non-list paragraph");
                p.waitForParagraphNotAListInVe();
                logger.debug("Original paragraph restored");
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifyApplyingOrderedListToParagraph");
    }

    @Test(enabled = true, groups = { "Para", "P2-Para" }, description = "TC_33 | Verify Bold affects only the selected paragraph word")
    public void verifyApplyingBoldToParagraphWord() {
        logger.info(STARTING_TEST_MSG, "verifyApplyingBoldToParagraphWord");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_PARAPAGEOBJECTS1_PAGE_OBJECT_MSG);
        ParaPageObjects1 p = new ParaPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(READING_ORIGINAL_PARAGRAPH_STYLES_IN_VE_MSG);
        List<List<String>> original = p.getParagraphCharacterStylesInVe();
        logger.debug(ORIGINAL_PARAGRAPH_STYLES_CAPTURED_MSG);
        logger.debug(CHECKING_THAT_THE_TESTED_PARAGRAPH_FORMAT_IS_INITIALLY_OFF_MSG);
        p.requireWordFormatIsOff(ParagraphFormat.BOLD, original);
        logger.debug(INITIAL_PARAGRAPH_FORMAT_CHECKED_MSG);
        logger.debug(SELECTING_THE_PARAGRAPH_WORD_IN_VE_MSG);
        p.selectParagraphWord();
        logger.debug(PARAGRAPH_WORD_SELECTED_MSG);
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        boolean applied = false;
        try {
            logger.debug("Clicking Bold in the toolbar");
            p.clickBold();
            applied = true;
            logger.debug(FORMATTING_BUTTON_CLICKED_MSG);
            logger.debug("Reading Bold toolbar is active");
            boolean observedBoldToolbarIsActive = p.isFormatButtonActive(ParagraphFormat.BOLD);
            logger.info("Bold toolbar is active: {}", observedBoldToolbarIsActive);
            logger.debug("Asserting Bold toolbar is active");
            Assert.assertTrue(observedBoldToolbarIsActive, "Bold toolbar is active should be true");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug(WAITING_FOR_FORMATTING_OF_ONLY_THE_SELECTED_PARAGRAPH_WORD_MSG);
            p.waitForWordFormattingInVe(ParagraphFormat.BOLD, original);
            logger.debug(SELECTED_PARAGRAPH_WORD_FORMATTING_IS_PRESENT_MSG);
            logger.debug("Reading Only the selected word has Bold and other text is unchanged");
            boolean isolated = p.isOnlySelectedWordFormattedInVe(ParagraphFormat.BOLD, original);
            logger.info("Only the selected word has Bold and other text is unchanged: {}", isolated);
            logger.debug("Asserting Only the selected word has Bold and other text is unchanged");
            Assert.assertTrue(isolated, "Only the selected word has Bold and other text is unchanged should be true");
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (applied) {
                logger.debug(CLICKING_UNDO_FOR_PARAGRAPH_FORMATTING_CLEANUP_MSG);
                p.clickUndo();
                logger.debug(PARAGRAPH_FORMATTING_UNDO_CLICKED_MSG);
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug(WAITING_FOR_THE_ORIGINAL_PARAGRAPH_CHARACTER_STYLES_MSG);
                p.waitForOriginalStylesInVe(original);
                logger.debug(ORIGINAL_PARAGRAPH_TEXT_AND_STYLES_RESTORED_MSG);
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifyApplyingBoldToParagraphWord");
    }

    @Test(enabled = true, groups = { "Para", "P2-Para" }, description = "TC_34 | Verify Italic affects only the selected paragraph word")
    public void verifyApplyingItalicToParagraphWord() {
        logger.info(STARTING_TEST_MSG, "verifyApplyingItalicToParagraphWord");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_PARAPAGEOBJECTS1_PAGE_OBJECT_MSG);
        ParaPageObjects1 p = new ParaPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(READING_ORIGINAL_PARAGRAPH_STYLES_IN_VE_MSG);
        List<List<String>> original = p.getParagraphCharacterStylesInVe();
        logger.debug(ORIGINAL_PARAGRAPH_STYLES_CAPTURED_MSG);
        logger.debug(CHECKING_THAT_THE_TESTED_PARAGRAPH_FORMAT_IS_INITIALLY_OFF_MSG);
        p.requireWordFormatIsOff(ParagraphFormat.ITALIC, original);
        logger.debug(INITIAL_PARAGRAPH_FORMAT_CHECKED_MSG);
        logger.debug(SELECTING_THE_PARAGRAPH_WORD_IN_VE_MSG);
        p.selectParagraphWord();
        logger.debug(PARAGRAPH_WORD_SELECTED_MSG);
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        boolean applied = false;
        try {
            logger.debug("Clicking Italic in the toolbar");
            p.clickItalic();
            applied = true;
            logger.debug(FORMATTING_BUTTON_CLICKED_MSG);
            logger.debug("Reading Italic toolbar is active");
            boolean observedItalicToolbarIsActive = p.isFormatButtonActive(ParagraphFormat.ITALIC);
            logger.info("Italic toolbar is active: {}", observedItalicToolbarIsActive);
            logger.debug("Asserting Italic toolbar is active");
            Assert.assertTrue(observedItalicToolbarIsActive, "Italic toolbar is active should be true");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug(WAITING_FOR_FORMATTING_OF_ONLY_THE_SELECTED_PARAGRAPH_WORD_MSG);
            p.waitForWordFormattingInVe(ParagraphFormat.ITALIC, original);
            logger.debug(SELECTED_PARAGRAPH_WORD_FORMATTING_IS_PRESENT_MSG);
            logger.debug("Reading Only the selected word has Italic and other text is unchanged");
            boolean isolated = p.isOnlySelectedWordFormattedInVe(ParagraphFormat.ITALIC, original);
            logger.info("Only the selected word has Italic and other text is unchanged: {}", isolated);
            logger.debug("Asserting Only the selected word has Italic and other text is unchanged");
            Assert.assertTrue(isolated, "Only the selected word has Italic and other text is unchanged should be true");
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (applied) {
                logger.debug(CLICKING_UNDO_FOR_PARAGRAPH_FORMATTING_CLEANUP_MSG);
                p.clickUndo();
                logger.debug(PARAGRAPH_FORMATTING_UNDO_CLICKED_MSG);
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug(WAITING_FOR_THE_ORIGINAL_PARAGRAPH_CHARACTER_STYLES_MSG);
                p.waitForOriginalStylesInVe(original);
                logger.debug(ORIGINAL_PARAGRAPH_TEXT_AND_STYLES_RESTORED_MSG);
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifyApplyingItalicToParagraphWord");
    }

    @Test(enabled = true, groups = { "Para", "P2-Para" }, description = "TC_35 | Verify Underline affects only the selected paragraph word")
    public void verifyApplyingUnderlineToParagraphWord() {
        logger.info(STARTING_TEST_MSG, "verifyApplyingUnderlineToParagraphWord");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_PARAPAGEOBJECTS1_PAGE_OBJECT_MSG);
        ParaPageObjects1 p = new ParaPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(READING_ORIGINAL_PARAGRAPH_STYLES_IN_VE_MSG);
        List<List<String>> original = p.getParagraphCharacterStylesInVe();
        logger.debug(ORIGINAL_PARAGRAPH_STYLES_CAPTURED_MSG);
        logger.debug(CHECKING_THAT_THE_TESTED_PARAGRAPH_FORMAT_IS_INITIALLY_OFF_MSG);
        p.requireWordFormatIsOff(ParagraphFormat.UNDERLINE, original);
        logger.debug(INITIAL_PARAGRAPH_FORMAT_CHECKED_MSG);
        logger.debug(SELECTING_THE_PARAGRAPH_WORD_IN_VE_MSG);
        p.selectParagraphWord();
        logger.debug(PARAGRAPH_WORD_SELECTED_MSG);
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        boolean applied = false;
        try {
            logger.debug("Clicking Underline in the toolbar");
            p.clickUnderline();
            applied = true;
            logger.debug(FORMATTING_BUTTON_CLICKED_MSG);
            logger.debug("Reading Underline toolbar is active");
            boolean observedUnderlineToolbarIsActive = p.isFormatButtonActive(ParagraphFormat.UNDERLINE);
            logger.info("Underline toolbar is active: {}", observedUnderlineToolbarIsActive);
            logger.debug("Asserting Underline toolbar is active");
            Assert.assertTrue(observedUnderlineToolbarIsActive, "Underline toolbar is active should be true");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug(WAITING_FOR_FORMATTING_OF_ONLY_THE_SELECTED_PARAGRAPH_WORD_MSG);
            p.waitForWordFormattingInVe(ParagraphFormat.UNDERLINE, original);
            logger.debug(SELECTED_PARAGRAPH_WORD_FORMATTING_IS_PRESENT_MSG);
            logger.debug("Reading Only the selected word has Underline and other text is unchanged");
            boolean isolated = p.isOnlySelectedWordFormattedInVe(ParagraphFormat.UNDERLINE, original);
            logger.info("Only the selected word has Underline and other text is unchanged: {}", isolated);
            logger.debug("Asserting Only the selected word has Underline and other text is unchanged");
            Assert.assertTrue(isolated, "Only the selected word has Underline and other text is unchanged should be true");
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (applied) {
                logger.debug(CLICKING_UNDO_FOR_PARAGRAPH_FORMATTING_CLEANUP_MSG);
                p.clickUndo();
                logger.debug(PARAGRAPH_FORMATTING_UNDO_CLICKED_MSG);
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug(WAITING_FOR_THE_ORIGINAL_PARAGRAPH_CHARACTER_STYLES_MSG);
                p.waitForOriginalStylesInVe(original);
                logger.debug(ORIGINAL_PARAGRAPH_TEXT_AND_STYLES_RESTORED_MSG);
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifyApplyingUnderlineToParagraphWord");
    }

    @Test(enabled = true, groups = { "Para", "P2-Para" }, description = "TC_36 | Verify Subscript affects only the selected paragraph word")
    public void verifyApplyingSubscriptToParagraphWord() {
        logger.info(STARTING_TEST_MSG, "verifyApplyingSubscriptToParagraphWord");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_PARAPAGEOBJECTS1_PAGE_OBJECT_MSG);
        ParaPageObjects1 p = new ParaPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(READING_ORIGINAL_PARAGRAPH_STYLES_IN_VE_MSG);
        List<List<String>> original = p.getParagraphCharacterStylesInVe();
        logger.debug(ORIGINAL_PARAGRAPH_STYLES_CAPTURED_MSG);
        logger.debug(CHECKING_THAT_THE_TESTED_PARAGRAPH_FORMAT_IS_INITIALLY_OFF_MSG);
        p.requireWordFormatIsOff(ParagraphFormat.SUBSCRIPT, original);
        logger.debug(INITIAL_PARAGRAPH_FORMAT_CHECKED_MSG);
        logger.debug(SELECTING_THE_PARAGRAPH_WORD_IN_VE_MSG);
        p.selectParagraphWord();
        logger.debug(PARAGRAPH_WORD_SELECTED_MSG);
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        boolean applied = false;
        try {
            logger.debug("Clicking Subscript in the toolbar");
            p.clickSubscript();
            applied = true;
            logger.debug(FORMATTING_BUTTON_CLICKED_MSG);
            logger.debug("Reading Subscript toolbar is active");
            boolean observedSubscriptToolbarIsActive = p.isFormatButtonActive(ParagraphFormat.SUBSCRIPT);
            logger.info("Subscript toolbar is active: {}", observedSubscriptToolbarIsActive);
            logger.debug("Asserting Subscript toolbar is active");
            Assert.assertTrue(observedSubscriptToolbarIsActive, "Subscript toolbar is active should be true");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug(WAITING_FOR_FORMATTING_OF_ONLY_THE_SELECTED_PARAGRAPH_WORD_MSG);
            p.waitForWordFormattingInVe(ParagraphFormat.SUBSCRIPT, original);
            logger.debug(SELECTED_PARAGRAPH_WORD_FORMATTING_IS_PRESENT_MSG);
            logger.debug("Reading Only the selected word has Subscript and other text is unchanged");
            boolean isolated = p.isOnlySelectedWordFormattedInVe(ParagraphFormat.SUBSCRIPT, original);
            logger.info("Only the selected word has Subscript and other text is unchanged: {}", isolated);
            logger.debug("Asserting Only the selected word has Subscript and other text is unchanged");
            Assert.assertTrue(isolated, "Only the selected word has Subscript and other text is unchanged should be true");
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (applied) {
                logger.debug(CLICKING_UNDO_FOR_PARAGRAPH_FORMATTING_CLEANUP_MSG);
                p.clickUndo();
                logger.debug(PARAGRAPH_FORMATTING_UNDO_CLICKED_MSG);
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug(WAITING_FOR_THE_ORIGINAL_PARAGRAPH_CHARACTER_STYLES_MSG);
                p.waitForOriginalStylesInVe(original);
                logger.debug(ORIGINAL_PARAGRAPH_TEXT_AND_STYLES_RESTORED_MSG);
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifyApplyingSubscriptToParagraphWord");
    }

    @Test(enabled = true, groups = { "Para", "P2-Para" }, description = "TC_37 | Verify Superscript affects only the selected paragraph word")
    public void verifyApplyingSuperscriptToParagraphWord() {
        logger.info(STARTING_TEST_MSG, "verifyApplyingSuperscriptToParagraphWord");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_PARAPAGEOBJECTS1_PAGE_OBJECT_MSG);
        ParaPageObjects1 p = new ParaPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(READING_ORIGINAL_PARAGRAPH_STYLES_IN_VE_MSG);
        List<List<String>> original = p.getParagraphCharacterStylesInVe();
        logger.debug(ORIGINAL_PARAGRAPH_STYLES_CAPTURED_MSG);
        logger.debug(CHECKING_THAT_THE_TESTED_PARAGRAPH_FORMAT_IS_INITIALLY_OFF_MSG);
        p.requireWordFormatIsOff(ParagraphFormat.SUPERSCRIPT, original);
        logger.debug(INITIAL_PARAGRAPH_FORMAT_CHECKED_MSG);
        logger.debug(SELECTING_THE_PARAGRAPH_WORD_IN_VE_MSG);
        p.selectParagraphWord();
        logger.debug(PARAGRAPH_WORD_SELECTED_MSG);
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        boolean applied = false;
        try {
            logger.debug("Clicking Superscript in the toolbar");
            p.clickSuperscript();
            applied = true;
            logger.debug(FORMATTING_BUTTON_CLICKED_MSG);
            logger.debug("Reading Superscript toolbar is active");
            boolean observedSuperscriptToolbarIsActive = p.isFormatButtonActive(ParagraphFormat.SUPERSCRIPT);
            logger.info("Superscript toolbar is active: {}", observedSuperscriptToolbarIsActive);
            logger.debug("Asserting Superscript toolbar is active");
            Assert.assertTrue(observedSuperscriptToolbarIsActive, "Superscript toolbar is active should be true");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug(WAITING_FOR_FORMATTING_OF_ONLY_THE_SELECTED_PARAGRAPH_WORD_MSG);
            p.waitForWordFormattingInVe(ParagraphFormat.SUPERSCRIPT, original);
            logger.debug(SELECTED_PARAGRAPH_WORD_FORMATTING_IS_PRESENT_MSG);
            logger.debug("Reading Only the selected word has Superscript and other text is unchanged");
            boolean isolated = p.isOnlySelectedWordFormattedInVe(ParagraphFormat.SUPERSCRIPT, original);
            logger.info("Only the selected word has Superscript and other text is unchanged: {}", isolated);
            logger.debug("Asserting Only the selected word has Superscript and other text is unchanged");
            Assert.assertTrue(isolated, "Only the selected word has Superscript and other text is unchanged should be true");
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (applied) {
                logger.debug(CLICKING_UNDO_FOR_PARAGRAPH_FORMATTING_CLEANUP_MSG);
                p.clickUndo();
                logger.debug(PARAGRAPH_FORMATTING_UNDO_CLICKED_MSG);
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug(WAITING_FOR_THE_ORIGINAL_PARAGRAPH_CHARACTER_STYLES_MSG);
                p.waitForOriginalStylesInVe(original);
                logger.debug(ORIGINAL_PARAGRAPH_TEXT_AND_STYLES_RESTORED_MSG);
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifyApplyingSuperscriptToParagraphWord");
    }

    @Test(enabled = true, groups = { "Para", "P2-Para" }, description = "TC_38 | Verify Strikethrough affects only the selected paragraph word")
    public void verifyApplyingStrikethroughToParagraphWord() {
        logger.info(STARTING_TEST_MSG, "verifyApplyingStrikethroughToParagraphWord");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_PARAPAGEOBJECTS1_PAGE_OBJECT_MSG);
        ParaPageObjects1 p = new ParaPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug(READING_ORIGINAL_PARAGRAPH_STYLES_IN_VE_MSG);
        List<List<String>> original = p.getParagraphCharacterStylesInVe();
        logger.debug(ORIGINAL_PARAGRAPH_STYLES_CAPTURED_MSG);
        logger.debug(CHECKING_THAT_THE_TESTED_PARAGRAPH_FORMAT_IS_INITIALLY_OFF_MSG);
        p.requireWordFormatIsOff(ParagraphFormat.STRIKETHROUGH, original);
        logger.debug(INITIAL_PARAGRAPH_FORMAT_CHECKED_MSG);
        logger.debug(SELECTING_THE_PARAGRAPH_WORD_IN_VE_MSG);
        p.selectParagraphWord();
        logger.debug(PARAGRAPH_WORD_SELECTED_MSG);
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        boolean applied = false;
        try {
            logger.debug("Clicking Strikethrough in the toolbar");
            p.clickStrikethrough();
            applied = true;
            logger.debug(FORMATTING_BUTTON_CLICKED_MSG);
            logger.debug("Reading Strikethrough toolbar is active");
            boolean observedStrikethroughToolbarIsActive = p.isFormatButtonActive(ParagraphFormat.STRIKETHROUGH);
            logger.info("Strikethrough toolbar is active: {}", observedStrikethroughToolbarIsActive);
            logger.debug("Asserting Strikethrough toolbar is active");
            Assert.assertTrue(observedStrikethroughToolbarIsActive, "Strikethrough toolbar is active should be true");
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug(WAITING_FOR_FORMATTING_OF_ONLY_THE_SELECTED_PARAGRAPH_WORD_MSG);
            p.waitForWordFormattingInVe(ParagraphFormat.STRIKETHROUGH, original);
            logger.debug(SELECTED_PARAGRAPH_WORD_FORMATTING_IS_PRESENT_MSG);
            logger.debug("Reading Only the selected word has Strikethrough and other text is unchanged");
            boolean isolated = p.isOnlySelectedWordFormattedInVe(ParagraphFormat.STRIKETHROUGH, original);
            logger.info("Only the selected word has Strikethrough and other text is unchanged: {}", isolated);
            logger.debug("Asserting Only the selected word has Strikethrough and other text is unchanged");
            Assert.assertTrue(isolated, "Only the selected word has Strikethrough and other text is unchanged should be true");
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (applied) {
                logger.debug(CLICKING_UNDO_FOR_PARAGRAPH_FORMATTING_CLEANUP_MSG);
                p.clickUndo();
                logger.debug(PARAGRAPH_FORMATTING_UNDO_CLICKED_MSG);
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug(WAITING_FOR_THE_ORIGINAL_PARAGRAPH_CHARACTER_STYLES_MSG);
                p.waitForOriginalStylesInVe(original);
                logger.debug(ORIGINAL_PARAGRAPH_TEXT_AND_STYLES_RESTORED_MSG);
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifyApplyingStrikethroughToParagraphWord");
    }

    @Test(enabled = true, groups = { "Para", "P2-Para" },
            description = "TC_40 | Verify Uppercase changes only the selected paragraph word")
    public void verifyUppercaseChangesOnlySelectedParagraphWord() {
        logger.info(STARTING_TEST_MSG, "verifyUppercaseChangesOnlySelectedParagraphWord");
        logger.debug(CALLING_LOGIN_AND_SEARCH_FILE_EDIT_MSG);
        ReusableMethods.loginAndSearchFileClickEdit();
        logger.debug(LOG_LOGIN_AND_SEARCH_FILE);
        logger.debug(INITIALISING_PARAPAGEOBJECTS1_PAGE_OBJECT_MSG);
        ParaPageObjects1 p = new ParaPageObjects1(getDriver());
        logger.debug(PAGE_OBJECT_INITIALISED_MSG);
        logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
        getDriver().switchTo().frame(p.getEditorFrame());
        logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
        logger.debug("Reading the original paragraph and checking the Case Change word");
        String originalText = p.getParagraphTextInVe();
        String word = getConfig("paragraphFormatWord");
        int start = originalText.indexOf(word);
        Assert.assertTrue(!word.isBlank() && start >= 0
                && originalText.indexOf(word, start + word.length()) < 0,
                "The configured Case Change word must occur exactly once in the paragraph");
        String uppercaseWord = word.toUpperCase(Locale.ROOT);
        Assert.assertNotEquals(word, uppercaseWord, "The selected word must contain lowercase letters");
        String expectedText = originalText.substring(0, start) + uppercaseWord
                + originalText.substring(start + word.length());
        logger.debug("Original paragraph captured and Case Change preconditions verified");
        logger.debug(SELECTING_THE_PARAGRAPH_WORD_IN_VE_MSG);
        p.selectParagraphWord();
        logger.debug(PARAGRAPH_WORD_SELECTED_MSG);
        logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
        getDriver().switchTo().defaultContent();
        logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
        boolean applied = false;
        try {
            logger.debug("Opening the Case Change toolbar menu");
            p.clickCaseChange();
            logger.debug("Case Change menu opened");
            logger.debug("Switching to the Case Change menu frame");
            getDriver().switchTo().frame(p.getCaseChangeMenuFrame());
            logger.debug("Switched to the Case Change menu frame");
            logger.debug("Selecting Uppercase from the Case Change menu");
            p.clickUppercase();
            applied = true;
            logger.debug("Uppercase selected");
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
            getDriver().switchTo().frame(p.getEditorFrame());
            logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
            logger.debug("Waiting for the selected word to become uppercase");
            p.waitForParagraphTextInVe(expectedText);
            logger.debug("Uppercase paragraph result is available");
            String actualText = p.getParagraphTextInVe();
            logger.info("Uppercase changes only the selected word: {}", expectedText.equals(actualText));
            logger.debug("Asserting the full paragraph matches with only the selected word converted");
            Assert.assertEquals(actualText, expectedText,
                    "Only the selected word should become uppercase; remaining text must stay unchanged");
            logger.debug("Uppercase paragraph assertion completed");
        } finally {
            logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
            getDriver().switchTo().defaultContent();
            logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            if (applied) {
                logger.debug("Clicking Undo to restore the original letter case");
                p.clickUndo();
                logger.debug(CLEANUP_UNDO_CLICKED_MSG);
                logger.debug(SWITCHING_TO_EDITOR_FRAME_MSG);
                getDriver().switchTo().frame(p.getEditorFrame());
                logger.debug(LOG_SWITCHED_TO_EDITOR_FRAME);
                logger.debug(WAITING_FOR_ORIGINAL_PARAGRAPH_TEXT_IN_VE_MSG);
                p.waitForParagraphTextInVe(originalText);
                String restoredText = p.getParagraphTextInVe();
                logger.info("Original paragraph restored after Uppercase Undo: {}", originalText.equals(restoredText));
                logger.debug("Asserting Undo restores the original paragraph text");
                Assert.assertEquals(restoredText, originalText, "Undo must restore the original letter case");
                logger.debug(ORIGINAL_PARAGRAPH_TEXT_RESTORED_MSG);
                logger.debug(SWITCHING_TO_DEFAULT_CONTENT_MSG);
                getDriver().switchTo().defaultContent();
                logger.debug(LOG_SWITCH_DEFAULT_CONTENT);
            }
        }
        logger.info(FINISHED_TEST_MSG, "verifyUppercaseChangesOnlySelectedParagraphWord");
    }

}
