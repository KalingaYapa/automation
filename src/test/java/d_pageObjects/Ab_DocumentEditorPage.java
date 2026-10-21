package d_pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import c_basePage.BasePage;

/** Read-only checks for the editor title bar; no document-changing actions. */
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
}
