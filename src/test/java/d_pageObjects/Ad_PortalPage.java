package d_pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import c_basePage.BasePage;

/** Project selection and the left-hand portal navigation. */
public class Ad_PortalPage extends BasePage {
    @FindBy(css = ".navbar-right .btn-group > button[data-toggle='dropdown']")
    private WebElement projectDropdown;
    @FindBy(linkText = "Authoring File List")
    private WebElement authoringFileList;

    public Ad_PortalPage(WebDriver driver) {
        super(driver);
    }

    public void waitForPortal() {
        waitForElementToBeVisible(projectDropdown);
    }

    public boolean isPortalDisplayed() {
        return isElementDisplayed(projectDropdown)
                && !java.net.URI.create(getCurrentUrl()).getPath().matches("/(login|verify_ad)/?");
    }

    public String getSelectedProject() {
        waitForElementToBeVisible(projectDropdown);
        return getText(projectDropdown).replaceFirst("^Selected Project\\s*:\\s*", "").trim();
    }

    public Ad_PortalPage selectProject(String project) {
        clickElement(projectDropdown);
        WebElement group = projectDropdown.findElement(By.xpath(
                "ancestor::div[contains(concat(' ',normalize-space(@class),' '),' btn-group ')][1]"));
        WebElement option = mywait.until(driver -> {
            for (WebElement link : group.findElements(By.cssSelector("ul.dropdown-menu a"))) {
                if (link.isDisplayed() && project.equals(link.getText().trim())) {
                    return link;
                }
            }
            return null;
        });
        option.click();
        mywait.until(driver -> project.equals(getSelectedProject()));
        return this;
    }

    public Ae_AuthoringFileListPage openAuthoringFileList() {
        clickElement(authoringFileList);
        Ae_AuthoringFileListPage page = new Ae_AuthoringFileListPage(getDriver);
        page.waitForFileList();
        return page;
    }
}
