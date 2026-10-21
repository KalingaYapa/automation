package d_pageObjects;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import c_basePage.BasePage;

/** Read-only editor checks; no document-changing actions. */
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
    // The supplied icon is decorative. CKEditor stores state on its containing button.
    private static final By BOLD_BUTTON = By.xpath(
            "//span[contains(concat(' ', normalize-space(@class), ' '), ' cke_button__bold_icon ')]"
            + "/ancestor::*[contains(concat(' ', normalize-space(@class), ' '), ' cke_button ')"
            + " or @role='button' or self::button][1]");

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
        return mywait.until(driver -> {
            driver.switchTo().defaultContent();
            WebElement word = findVisibleBoldWord();
            if (word != null) {
                return word;
            }
            for (WebElement frame : findElementsNow(EDITOR_FRAMES)) {
                if (frame.isDisplayed()) {
                    driver.switchTo().frame(frame);
                    word = findVisibleBoldWord();
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
                String pressed = button.getAttribute("aria-pressed");
                boolean active = pressed == null ? classes.contains(" cke_button_on ")
                        : "true".equalsIgnoreCase(pressed);
                if (enabled && active) {
                    return true;
                }
            }
            return false;
        });
    }
}
