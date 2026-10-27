package para;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.FindBy;
import a_testbase.BaseClass;
import d_loginpage.BasePage;
import documenteditor.DocumentEditorPageObjects1;
import documenteditor.DocumentEditorPageObjects1.ParagraphFormat;

/** XPath elements and context-local paragraph actions/checks. The test owns frame changes. */
public class ParaPageObjects1 extends BasePage {
    private static final Logger logger = LogManager.getLogger(ParaPageObjects1.class);
    private static final String PARAGRAPH_REFERENCE_ATTRIBUTE = "inno_ref";
    @FindBy(xpath = "//iframe[contains(concat(' ',normalize-space(@class),' '),' cke_wysiwyg_frame ')]")
    private List<WebElement> editorFrames;
    @FindBy(xpath = "//div[@data-element='p']")
    private List<WebElement> paragraphsInVe;
    @FindBy(xpath = "//*[@id='content-scroll']//li[@name='p']")
    private List<WebElement> structureParagraphs;

    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__bold ')]")
    private WebElement boldButton;
    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__italic ')]")
    private WebElement italicButton;
    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__underline ')]")
    private WebElement underlineButton;
    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__subscript ')]")
    private WebElement subscriptButton;
    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__superscript ')]")
    private WebElement superscriptButton;
    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__strike ')]")
    private WebElement strikethroughButton;
    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__numberedlist ')]")
    private WebElement orderedListButton;
    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__bulletedlist ')]")
    private WebElement unorderedListButton;
    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__undo ')]")
    private WebElement undoButton;
    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__redo ')]")
    private WebElement redoButton;

    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__copy ')]")
    private WebElement copyButton;
    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__paste ') and starts-with(@title,'Paste (')]")
    private WebElement pasteButton;

    @FindBy(xpath = "//*[@id='save-file']")
    private WebElement saveButton;
    @FindBy(xpath = "//*[contains(concat(' ',normalize-space(@class),' '),' bootstrap-growl ') and contains(concat(' ',normalize-space(@class),' '),' alert-success ')]/strong[normalize-space()='Saved']")
    private List<WebElement> saveSuccessNotifications;
    private String saveParagraphReference;

    private String copySourceReference;
    private String pasteDestinationReference;

    // Reuse the existing technical selection, section and computed-style readers.
    private final DocumentEditorPageObjects1 editor;

    public ParaPageObjects1(WebDriver driver) {
        super(driver);
        editor = new DocumentEditorPageObjects1(driver);
        logger.debug("Paragraph XPath elements initialised");
    }

    public WebElement getEditorFrame() {
        return mywait.until(driver -> editorFrames.stream().filter(WebElement::isDisplayed)
                .findFirst().orElse(null));
    }

    public int getParagraphCountInVe() {
        return paragraphsInVe.size();
    }

    public void clickInitialHeadingInVe() {
        logger.debug("Clicking the heading to establish a different initial selection");
        editor.clickTargetHeading();
        logger.debug("Clicked the initial heading");
    }

    public void waitForSelectedElementType(String expected) {
        editor.waitForSelectedElementType(expected);
    }

    public String getSelectedElementType() {
        return editor.getSelectedElementType();
    }

    public String getVisibleStructureParagraphReference() {
        return mywait.withMessage("Document Structure must contain a visible paragraph row")
                .until(driver -> structureParagraphs.stream().filter(WebElement::isDisplayed)
                        .map(row -> row.getAttribute("id"))
                        .filter(reference -> reference != null && !reference.isBlank())
                        .findFirst().orElse(null));
    }

    public void clickParagraphInDs(String reference) {
        WebElement row = mywait.until(driver -> structureParagraphs.stream()
                .filter(WebElement::isDisplayed)
                .filter(element -> reference.equals(element.getAttribute("id")))
                .findFirst().orElse(null));
        logger.debug("Clicking Document Structure paragraph with reference {}", reference);
        elementClick(row.findElement(By.xpath(
                ".//span[contains(concat(' ',normalize-space(@class),' '),' contentBlockListItemName ')]")));
        logger.debug("Clicked Document Structure paragraph with reference {}", reference);
    }

    /** Read selection/highlight state only; never move the caret to make the check pass. */
    public String getSelectedParagraphReferenceInVe() {
        return mywait.withMessage("The DS-selected paragraph must be highlighted and contain the editor selection")
                .until(driver -> (String) js.executeScript("""
                    const selection = window.getSelection();
                    if (!selection || !selection.rangeCount) return null;
                    const paragraph = Array.from(document.querySelectorAll('[data-element="p"]'))
                        .find(element => element.classList.contains('element-tag-color')
                            && element.contains(selection.anchorNode)
                            && element.contains(selection.focusNode));
                    return paragraph ? paragraph.getAttribute('inno_ref') : null;
                    """));
    }

    private WebElement getTargetParagraph() {
        String expected = BaseClass.getConfig("editorParagraphText");
        return mywait.until(driver -> {
            WebElement target = null;
            for (WebElement paragraph : paragraphsInVe) {
                String text = paragraph.getText().replaceAll("[\\u200B\\u2063\\uFEFF]", "")
                        .replaceAll("[\\s\\p{Z}]+", " ").trim();
                if (paragraph.isDisplayed() && expected.equals(text)) {
                    if (target != null) {
                        throw new IllegalStateException("More than one paragraph matches the configured text");
                    }
                    target = paragraph;
                }
            }
            return target;
        });
    }

    private String getParagraphText(WebElement paragraph) {
        return paragraph.getText().replaceAll("[\\u200B\\u2063\\uFEFF]", "")
                .replaceAll("[\\s\\p{Z}]+", " ").trim();
    }

    private WebElement getParagraphByAnchor(String anchor) {
        if (anchor.isBlank()) {
            throw new IllegalStateException("Clipboard paragraph anchor must not be blank");
        }
        return mywait.until(driver -> {
            List<WebElement> matches = paragraphsInVe.stream().filter(WebElement::isDisplayed)
                    .filter(paragraph -> getParagraphText(paragraph).contains(anchor)).toList();
            if (matches.size() > 1) {
                throw new IllegalStateException("Clipboard paragraph anchor must identify exactly one paragraph");
            }
            return matches.isEmpty() ? null : matches.get(0);
        });
    }

    public void rememberClipboardParagraphs() {
        copySourceReference = getParagraphByAnchor(BaseClass.getConfig("clipboardSourceParagraphAnchor"))
                .getAttribute(PARAGRAPH_REFERENCE_ATTRIBUTE);
        pasteDestinationReference = getParagraphByAnchor(BaseClass.getConfig("clipboardDestinationParagraphAnchor"))
                .getAttribute(PARAGRAPH_REFERENCE_ATTRIBUTE);
        if (copySourceReference == null || pasteDestinationReference == null
                || copySourceReference.equals(pasteDestinationReference)) {
            throw new IllegalStateException("Copy and Paste require two distinct referenced paragraphs");
        }
    }

    private WebElement getParagraphByReference(String reference) {
        return mywait.until(driver -> paragraphsInVe.stream().filter(WebElement::isDisplayed)
                .filter(paragraph -> reference.equals(paragraph.getAttribute(PARAGRAPH_REFERENCE_ATTRIBUTE)))
                .findFirst().orElse(null));
    }

    public String getCopySourceTextInVe() {
        return getParagraphText(getParagraphByReference(copySourceReference));
    }

    public String getPasteDestinationTextInVe() {
        return getParagraphText(getParagraphByReference(pasteDestinationReference));
    }

    public void selectCopySourceWord() {
        editor.selectParagraphWord(getParagraphByReference(copySourceReference));
    }

    public void placeCursorAtPasteDestinationStart() {
        placeCursorAtParagraphBoundary(getParagraphByReference(pasteDestinationReference), true);
    }

    private void placeCursorAtParagraphBoundary(WebElement paragraph, boolean atStart) {
        elementClick(paragraph);
        js.executeScript("""
                const root = arguments[0], library = parent.CKEDITOR;
                const editor = Object.values(library.instances)
                    .find(instance => instance.status === 'ready' && instance.document?.$ === document);
                if (!editor) throw new Error('The destination editor is not ready');
                editor.focus();
                const range = editor.createRange();
                range.selectNodeContents(new library.dom.element(root));
                range.collapse(arguments[1]);
                editor.getSelection().selectRanges([range]);
                editor.forceNextSelectionCheck();
                editor.selectionChange(true);
                const selection = window.getSelection();
                if (!selection.isCollapsed || !(selection.anchorNode === root || root.contains(selection.anchorNode)))
                    throw new Error('Cursor is not inside the target paragraph');
                """, paragraph, atStart);
    }

    public void clickCopy() {
        clickEnabledButton(copyButton);
    }

    public void clickPaste() {
        clickEnabledButton(pasteButton);
    }

    public void waitForPasteDestinationTextInVe(String expected) {
        mywait.until(driver -> expected.equals(getPasteDestinationTextInVe()));
    }

    public void rememberSaveParagraph() {
        saveParagraphReference = getParagraphByAnchor(BaseClass.getConfig("saveParagraphAnchor"))
                .getAttribute(PARAGRAPH_REFERENCE_ATTRIBUTE);
        if (saveParagraphReference == null || saveParagraphReference.isBlank()) {
            throw new IllegalStateException("Save paragraph must have a stable reference");
        }
    }

    public String getSaveParagraphTextInVe() {
        return getParagraphText(getParagraphByReference(saveParagraphReference));
    }

    public String getSaveParagraphHtmlInVe() {
        return getParagraphByReference(saveParagraphReference).getAttribute("innerHTML");
    }

    public void placeCursorAtSaveParagraphEnd() {
        placeCursorAtParagraphBoundary(getParagraphByReference(saveParagraphReference), false);
    }

    public void typeSaveMarker(String marker) {
        new Actions(getDriver).sendKeys(marker).perform();
    }

    public void waitForSaveParagraphTextInVe(String expected) {
        mywait.until(driver -> expected.equals(getSaveParagraphTextInVe()));
    }

    /** Restore this paragraph only; the test must first verify it still contains its own edit. */
    public void restoreSaveParagraphHtmlInVe(String originalHtml) {
        js.executeScript("""
                const root = arguments[0], library = parent.CKEDITOR;
                const editor = Object.values(library.instances)
                    .find(instance => instance.status === 'ready' && instance.document?.$ === document);
                if (!editor) throw new Error('The editor is not ready for paragraph restoration');
                editor.fire('saveSnapshot');
                new library.dom.element(root).setHtml(arguments[1]);
                editor.fire('change');
                editor.fire('saveSnapshot');
                """, getParagraphByReference(saveParagraphReference), originalHtml);
    }

    public void clickSave() {
        elementClick(saveButton);
    }

    public String waitForSaveSuccess() {
        return mywait.withMessage("Save must finish with a fresh Saved success notification")
                .until(driver -> {
                    boolean complete = Boolean.TRUE.equals(js.executeScript(
                            "return typeof savingInProgress !== 'undefined' && savingInProgress === false"
                            + " && HTMLedited_NotSaved === false;"));
                    return complete ? saveSuccessNotifications.stream().filter(WebElement::isDisplayed)
                            .map(WebElement::getText).findFirst().orElse(null) : null;
                });
    }

    public void waitForEditorReady() {
        editor.waitForEditorReady();
    }

    public void rememberTargetSection() {
        editor.rememberTargetSection();
    }

    public void clickOnFirstParaVe() {
        editor.clickPlainParagraphWord(getTargetParagraph());
    }

    public void placeCursorInMiddleOfPara() {
        editor.placeParagraphCaret(getTargetParagraph(), "middle");
    }

    public void placeCursorAtBeginningOfPara() {
        editor.placeParagraphCaret(getTargetParagraph(), "beginning");
    }

    public void placeCursorAtEndOfPara() {
        editor.placeParagraphCaret(getTargetParagraph(), "end");
    }

    public void enterKey() {
        new Actions(getDriver).sendKeys(Keys.ENTER).perform();
    }

    public int getParagraphCountInDs() {
        return editor.getParagraphCountInTargetSection();
    }

    public void waitForParagraphCountInDs(int expected) {
        editor.waitForParagraphCountInTargetSection(expected);
    }

    public List<String> getParagraphTextsInVe() {
        return editor.getEditedSectionParagraphTexts();
    }

    public List<String> getExpectedParagraphTexts() {
        return editor.getExpectedParagraphTextsAfterEnter();
    }

    public List<String> getOriginalParagraphTexts() {
        return editor.getOriginalParagraphTexts();
    }

    public void waitForEnterResultInVe() {
        editor.waitForParagraphEnterResult();
    }

    public void waitForOriginalParagraphTextsInVe() {
        mywait.until(driver -> getOriginalParagraphTexts().equals(getParagraphTextsInVe()));
    }

    public void selectParagraphWord() {
        editor.selectParagraphWord(getTargetParagraph());
    }

    public List<List<String>> getParagraphCharacterStylesInVe() {
        return editor.getParagraphCharacterStyles(getTargetParagraph());
    }

    public void requireWordFormatIsOff(ParagraphFormat format, List<List<String>> original) {
        editor.requireParagraphWordFormatIsOff(format, original);
    }

    public boolean isOnlySelectedWordFormattedInVe(ParagraphFormat format, List<List<String>> original) {
        return editor.isOnlyParagraphWordFormatted(format, original, getParagraphCharacterStylesInVe());
    }

    public void waitForWordFormattingInVe(ParagraphFormat format, List<List<String>> original) {
        mywait.until(driver -> isOnlySelectedWordFormattedInVe(format, original));
    }

    public void waitForOriginalStylesInVe(List<List<String>> original) {
        mywait.until(driver -> original.equals(getParagraphCharacterStylesInVe()));
    }

    private WebElement getFormatButton(ParagraphFormat format) {
        return switch (format) {
            case BOLD -> boldButton;
            case ITALIC -> italicButton;
            case UNDERLINE -> underlineButton;
            case SUBSCRIPT -> subscriptButton;
            case SUPERSCRIPT -> superscriptButton;
            case STRIKETHROUGH -> strikethroughButton;
        };
    }

    private void clickEnabledButton(WebElement button) {
        mywait.until(driver -> button.isDisplayed() && !"true".equals(button.getAttribute("aria-disabled"))
                && !button.getAttribute("class").contains("cke_button_disabled"));
        logger.debug("Clicking the enabled paragraph toolbar element");
        elementClick(button);
        logger.debug("Clicked paragraph toolbar element");
    }

    private boolean isButtonActive(WebElement button) {
        return mywait.until(driver -> button.isDisplayed()
                && List.of(button.getAttribute("class").split("\\s+")).contains("cke_button_on"));
    }

    public void clickBold() {
        clickEnabledButton(boldButton);
    }
    public void clickItalic() {
        clickEnabledButton(italicButton);
    }
    public void clickUnderline() {
        clickEnabledButton(underlineButton);
    }
    public void clickSubscript() {
        clickEnabledButton(subscriptButton);
    }
    public void clickSuperscript() {
        clickEnabledButton(superscriptButton);
    }
    public void clickStrikethrough() {
        clickEnabledButton(strikethroughButton);
    }
    public void clickOrderedList() {
        clickEnabledButton(orderedListButton);
    }
    public void clickUnorderedList() {
        clickEnabledButton(unorderedListButton);
    }
    public void clickUndo() {
        clickEnabledButton(undoButton);
    }
    public void clickRedo() {
        clickEnabledButton(redoButton);
    }

    public boolean isFormatButtonActive(ParagraphFormat format) {
        return isButtonActive(getFormatButton(format));
    }
    public boolean isOrderedListButtonActive() {
        return isButtonActive(orderedListButton);
    }
    public boolean isUnorderedListButtonActive() {
        return isButtonActive(unorderedListButton);
    }

    public boolean isParagraphAnOrderedListInVe() {
        return mywait.until(driver -> "ol".equals(editor.getTargetParagraphListType()));
    }

    public boolean isParagraphAnUnorderedListInVe() {
        return mywait.until(driver -> "ul".equals(editor.getTargetParagraphListType()));
    }

    public void requireParagraphNotAListInVe() {
        if (!editor.getTargetParagraphListType().isEmpty()) {
            throw new IllegalStateException("Precondition: paragraph must initially be outside a list");
        }
    }

    public void waitForParagraphNotAListInVe() {
        mywait.until(driver -> editor.getTargetParagraphListType().isEmpty());
    }
}
