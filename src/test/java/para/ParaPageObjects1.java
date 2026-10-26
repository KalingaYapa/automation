package para;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    @FindBy(xpath = "//iframe[contains(concat(' ',normalize-space(@class),' '),' cke_wysiwyg_frame ')]")
    private List<WebElement> editorFrames;
    @FindBy(xpath = "//div[@data-element='p']")
    private List<WebElement> paragraphsInVe;
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

    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__casechange ')]")
    private WebElement caseChangeButton;
    @FindBy(xpath = "//iframe[contains(concat(' ',normalize-space(@class),' '),' cke_panel_frame ')]")
    private List<WebElement> caseChangeMenuFrames;
    @FindBy(xpath = "//a[@role='menuitem' and @aria-label='UPPERCASE']")
    private WebElement uppercaseMenuItem;

    // Reuse the existing technical selection, section and computed-style readers.
    private final DocumentEditorPageObjects1 editor;
    private String targetParagraphReference;

    public ParaPageObjects1(WebDriver driver) {
        super(driver);
        editor = new DocumentEditorPageObjects1(driver);
        logger.debug("Paragraph XPath elements initialised");
    }

    public WebElement getEditorFrame() {
        return mywait.until(driver -> editorFrames.stream().filter(WebElement::isDisplayed)
                .findFirst().orElse(null));
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

    /** Retain the paragraph identity because its text changes during Case Change. */
    public String getParagraphTextInVe() {
        if (targetParagraphReference == null) {
            targetParagraphReference = getTargetParagraph().getAttribute("inno_ref");
            if (targetParagraphReference == null || targetParagraphReference.isBlank()) {
                throw new IllegalStateException("Target paragraph needs a reference for Case Change verification");
            }
        }
        return mywait.until(driver -> paragraphsInVe.stream()
                .filter(paragraph -> targetParagraphReference.equals(paragraph.getAttribute("inno_ref")))
                .filter(WebElement::isDisplayed)
                .map(paragraph -> paragraph.getText().replaceAll("[\\u200B\\u2063\\uFEFF]", "")
                        .replaceAll("[\\s\\p{Z}]+", " ").trim())
                .findFirst().orElse(null));
    }

    public void waitForParagraphTextInVe(String expected) {
        mywait.until(driver -> expected.equals(getParagraphTextInVe()));
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

    public void clickCaseChange() {
        clickEnabledButton(caseChangeButton);
    }

    public WebElement getCaseChangeMenuFrame() {
        return mywait.until(driver -> caseChangeMenuFrames.stream().filter(WebElement::isDisplayed)
                .findFirst().orElse(null));
    }

    /** Called inside the visible Case Change menu frame. */
    public void clickUppercase() {
        clickEnabledButton(uppercaseMenuItem);
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
