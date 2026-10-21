package d_pageObjects;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import c_basePage.BasePage;

/** Editor checks and focused formatting actions. */
public class Ab_DocumentEditorPage extends BasePage {
    @FindBy(css = "#titlebarFileName > span:first-child")
    private WebElement documentTitle;
    @FindBy(id = "showSavedTime")
    private WebElement savedTime;
    @FindBy(id = "save-file")
    private WebElement saveButton;
    @FindBy(id = "btn-reset-file")
    private WebElement resetButton;
    @FindBy(id = "btn-validate-against-schema")
    private WebElement validateButton;
    @FindBy(id = "btn-checkin-file")
    private WebElement checkInButton;

    public Ab_DocumentEditorPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDocumentTitleDisplayed() {
        waitForElementToBeVisible(documentTitle);
        return isElementDisplayed(documentTitle);
    }

    public String getDocumentTitle() {
        waitForElementToBeVisible(documentTitle);
        return getText(documentTitle);
    }

    public boolean isSavedTimeDisplayed() {
        waitForElementToBeVisible(savedTime);
        return isElementDisplayed(savedTime);
    }

    public String getSavedTimeText() {
        waitForElementToBeVisible(savedTime);
        return getText(savedTime);
    }

    public String getSaveButtonText() {
        waitForElementToBeVisible(saveButton);
        return getText(saveButton);
    }

    public boolean isSaveButtonEnabled() {
        waitForElementToBeVisible(saveButton);
        return isElementEnabled(saveButton);
    }

    public String getResetButtonText() {
        waitForElementToBeVisible(resetButton);
        return getText(resetButton);
    }

    public boolean isResetButtonEnabled() {
        waitForElementToBeVisible(resetButton);
        return isElementEnabled(resetButton);
    }

    public String getValidateButtonText() {
        waitForElementToBeVisible(validateButton);
        return getText(validateButton);
    }

    public boolean isValidateButtonEnabled() {
        waitForElementToBeVisible(validateButton);
        return isElementEnabled(validateButton);
    }

    public String getCheckInButtonText() {
        waitForElementToBeVisible(checkInButton);
        return getText(checkInButton);
    }

    public boolean isCheckInButtonEnabled() {
        waitForElementToBeVisible(checkInButton);
        return isElementEnabled(checkInButton);
    }
    private static final By BOLD_TEXT = By.cssSelector("span[data-element='b'][data-style='bold']");
    private static final By EDITOR_FRAMES = By.cssSelector("iframe.cke_wysiwyg_frame");
    // Use the stable command class, not the generated cke_23 ID or decorative icon.
    private static final By BOLD_BUTTON = By.cssSelector("a.cke_button.cke_button__bold");

    private WebElement findVisibleBoldWord() {
        String expected = a_testBase.BaseClass.getConfig("boldSampleText");
        List<WebElement> matches = new java.util.ArrayList<>();
        for (WebElement element : findElementsNow(BOLD_TEXT)) {
            if (element.isDisplayed() && expected.equals(element.getText().trim())) {
                matches.add(element);
            }
        }
        if (matches.size() > 1) {
            throw new IllegalStateException("More than one visible bold word matches: " + expected
                    + ". Scope the locator to the intended paragraph.");
        }
        return matches.isEmpty() ? null : matches.get(0);
    }

    /** Supports inline CKEditor and the standard CKEditor 4 editing iframe. */
    private WebElement locateBoldWordInEditor() {
        return locateInEditor(this::findVisibleBoldWord);
    }

    private WebElement locateInEditor(java.util.function.Supplier<WebElement> findElement) {
        return mywait.until(driver -> {
            driver.switchTo().defaultContent();
            WebElement word = findElement.get();
            if (word != null) {
                return word;
            }
            for (WebElement frame : findElementsNow(EDITOR_FRAMES)) {
                if (frame.isDisplayed()) {
                    driver.switchTo().frame(frame);
                    word = findElement.get();
                    if (word != null) {
                        return word;
                    }
                    driver.switchTo().defaultContent();
                }
            }
            return null;
        });
    }

    /** Places a caret inside the known bold word without modifying its text. */
    public void clickBoldWord() {
        interactWithBoldWord(false);
    }

    /** Selects the known bold word using a native double-click. */
    public void doubleClickBoldWord() {
        interactWithBoldWord(true);
    }

    private void interactWithBoldWord(boolean doubleClick) {
        try {
            WebElement word = locateBoldWordInEditor();
            if (doubleClick) {
                action.doubleClick(word).perform();
            } else {
                action.click(word).perform();
            }
            // Check native selection in the editing context before inspecting the toolbar.
            // This reads the selection; it does not manufacture one with JavaScript.
            mywait.until(driver -> Boolean.TRUE.equals(js.executeScript(
                    "const s = window.getSelection();"
                    + "if (!s || !s.rangeCount || !arguments[0].contains(s.anchorNode)"
                    + " || !arguments[0].contains(s.focusNode)) return false;"
                    + "return arguments[1] ? (!s.isCollapsed && s.toString().trim() === arguments[2])"
                    + " : s.isCollapsed;",
                    word, doubleClick, a_testBase.BaseClass.getConfig("boldSampleText"))));
        } finally {
            getDriver.switchTo().defaultContent();
        }
    }

    /** Checks active formatting, rather than merely whether Bold can be clicked. */
    public boolean isBoldFormattingActive() {
        return waitForBoldFormattingState(true);
    }

    private boolean waitForBoldFormattingState(boolean expectedActive) {
        getDriver.switchTo().defaultContent();
        return mywait.until(driver -> {
            for (WebElement button : findElementsNow(BOLD_BUTTON)) {
                if (!button.isDisplayed()) {
                    continue;
                }
                String classes = " " + getAttributeValue(button, "class").replaceAll("\\s+", " ") + " ";
                boolean enabled = button.isEnabled()
                        && !"true".equalsIgnoreCase(button.getAttribute("aria-disabled"))
                        && !classes.contains(" cke_button_disabled ");
                String expected = expectedActive ? " cke_button_on " : " cke_button_off ";
                String opposite = expectedActive ? " cke_button_off " : " cke_button_on ";
                boolean active = classes.contains(expected) && !classes.contains(opposite);
                if (enabled && active) {
                    return true;
                }
            }
            return false;
        });
    }

    private static final By HEADING = By.cssSelector("div[data-element='heading']");
    private static final By UNDO_BUTTON = By.cssSelector("a.cke_button.cke_button__undo");
    private static final String FIND_HEADING_WORD =
            "const root=arguments[0], word=arguments[1];"
            + "const walker=document.createTreeWalker(root,NodeFilter.SHOW_TEXT);"
            + "let node, match=null; while(node=walker.nextNode()){"
            + "const start=node.textContent.indexOf(word); if(start>=0){"
            + "if(match) throw new Error('Heading word is ambiguous');"
            + "match={node:node,start:start};}}"
            + "if(!match) throw new Error('Heading word was not found');"
            + "node=match.node;";

    private WebElement locateHeadingInEditor() {
        return locateInEditor(() -> {
            WebElement match = null;
            for (WebElement heading : findElementsNow(HEADING)) {
                if (heading.isDisplayed() && heading.getText().replace("\u200b", "").trim()
                        .equals(a_testBase.BaseClass.getConfig("editorHeadingText"))) {
                    if (match != null) {
                        throw new IllegalStateException("Multiple matching editor headings");
                    }
                    match = heading;
                }
            }
            return match;
        });
    }

    /** Native double-click at the configured word, rather than the heading's centre. */
    public void selectHeadingWord() {
        try {
            WebElement heading = locateHeadingInEditor();
            js.executeScript("arguments[0].scrollIntoView({block:'center',inline:'nearest'});", heading);
            List<?> point = (List<?>) js.executeScript(FIND_HEADING_WORD
                    + "const r=document.createRange();r.setStart(node,match.start);"
                    + "r.setEnd(node,match.start+word.length);const b=r.getClientRects()[0];"
                    + "const h=root.getClientRects()[0];"
                    + "const cx=Math.floor((Math.max(0,h.left)+Math.min(innerWidth,h.right))/2);"
                    + "const cy=Math.floor((Math.max(0,h.top)+Math.min(innerHeight,h.bottom))/2);"
                    + "return [Math.floor(b.x+b.width/2)-cx,Math.floor(b.y+b.height/2)-cy];",
                    heading, a_testBase.BaseClass.getConfig("headingWord"));
            action.moveToElement(heading, ((Number) point.get(0)).intValue(),
                    ((Number) point.get(1)).intValue()).doubleClick().perform();
            mywait.until(driver -> Boolean.TRUE.equals(js.executeScript(
                    "const s=window.getSelection();return !!s && !s.isCollapsed"
                    + " && arguments[0].contains(s.anchorNode) && arguments[0].contains(s.focusNode)"
                    + " && s.toString().trim()===arguments[1];",
                    heading, a_testBase.BaseClass.getConfig("headingWord"))));
        } finally {
            getDriver.switchTo().defaultContent();
        }
    }

    public void requireHeadingBoldIsOff() {
        waitForBoldFormattingState(false);
        if (isHeadingWordExplicitlyBold()) {
            throw new IllegalStateException("Precondition: heading word already has explicit bold markup");
        }
    }

    /** Verify document markup and rendered font weight independently of the toolbar. */
    public boolean isHeadingWordExplicitlyBold() {
        try {
            WebElement heading = locateHeadingInEditor();
            return Boolean.TRUE.equals(js.executeScript(FIND_HEADING_WORD
                    + "const bold=node.parentElement.closest('[data-element=\"b\"][data-style=\"bold\"],b,strong');"
                    + "return !!bold && root.contains(bold)"
                    + " && parseInt(getComputedStyle(node.parentElement).fontWeight,10)>=700;",
                    heading, a_testBase.BaseClass.getConfig("headingWord")));
        } finally {
            getDriver.switchTo().defaultContent();
        }
    }

    public void applyBoldToSelection() {
        getDriver.switchTo().defaultContent();
        clickElement(BOLD_BUTTON);
    }

    public boolean waitForHeadingWordToBecomeBold() {
        isBoldFormattingActive();
        return mywait.until(driver -> isHeadingWordExplicitlyBold());
    }

    /** Undo only this test's formatting command; never save or reset the file. */
    public void undoHeadingBold() {
        getDriver.switchTo().defaultContent();
        WebElement undo = mywait.withMessage("Undo must be visible and enabled before clicking")
                .until(driver -> {
                    for (WebElement button : findElementsNow(UNDO_BUTTON)) {
                        String classes = " " + getAttributeValue(button, "class").replaceAll("\\s+", " ") + " ";
                        // An anchor's isEnabled() alone does not check ARIA-disabled state.
                        // cke_button_off is a normal enabled state for this command.
                        if (button.isDisplayed() && button.isEnabled()
                                && "false".equalsIgnoreCase(button.getAttribute("aria-disabled"))
                                && !classes.contains(" cke_button_disabled ")) {
                            return button;
                        }
                    }
                    return null;
                });
        clickElement(undo);
        mywait.withMessage("Undo should remove the heading word's explicit bold formatting")
                .until(driver -> !isHeadingWordExplicitlyBold());
    }

    /** Capture text, rendered weight and explicit formatting for before/after comparison. */
    @SuppressWarnings("unchecked")
    public List<String> getHeadingWordFormattingSnapshot() {
        try {
            WebElement heading = locateHeadingInEditor();
            return (List<String>) js.executeScript(FIND_HEADING_WORD
                    + "const bold=node.parentElement.closest('[data-element=\"b\"][data-style=\"bold\"],b,strong');"
                    + "return [root.textContent.replace(/\\u200b/g,'').trim(),"
                    + "getComputedStyle(node.parentElement).fontWeight,"
                    + "String(!!bold && root.contains(bold))];",
                    heading, a_testBase.BaseClass.getConfig("headingWord"));
        } finally {
            getDriver.switchTo().defaultContent();
        }
    }

    /** Undo the latest Bold edit and reselect its word to check toolbar synchronisation. */
    public void undoHeadingBoldAndWaitForInactiveState() {
        undoHeadingBold();
        selectHeadingWord();
        waitForBoldFormattingState(false);
    }
}
