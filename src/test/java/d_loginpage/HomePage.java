package d_loginpage;

import a_testbase.BaseClass;
import documenteditor.DocumentEditorPageObjects1;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/** Shared login, project selection and allocation navigation. */
public class HomePage extends BasePage {
    private static final By ROWS = By.cssSelector("#dataTables tbody tr");
    @FindBy(id = "email")
    private WebElement email;
    @FindBy(css = "input[type='password']")
    private WebElement password;
    @FindBy(css = "form button[type='submit']")
    private WebElement submit;
    @FindBy(css = ".navbar-right .btn-group > button[data-toggle='dropdown']")
    private WebElement projectDropdown;
    @FindBy(linkText = "Authoring File List")
    private WebElement authoringFileList;
    @FindBy(id = "dataTables")
    private WebElement fileTable;
    @FindBy(css = "#dataTables thead input.dt-filter-input[data-idx='5']")
    private WebElement fileNameFilter;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    private String requiredConfig(String name) {
        String value = BaseClass.getConfig(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Set " + name + " in your local src/test/resources/config.properties");
        }
        return value;
    }

    public HomePage login() {
        String username = requiredConfig("username");
        String secret = requiredConfig("password");
        try {
            enterText(email, username);
            clickElement(submit);
            enterText(password, secret);
            clickElement(submit);
        } catch (WebDriverException exception) {
            // Selenium command diagnostics can contain typed input; do not expose the cause.
            throw new IllegalStateException("Portal login interaction failed ("
                    + exception.getClass().getSimpleName() + ")");
        }
        HomePage portal = new HomePage(getDriver);
        portal.waitForPortal();
        return portal;
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

    public HomePage selectProject(String project) {
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

    public HomePage openAuthoringFileList() {
        clickElement(authoringFileList);
        HomePage page = new HomePage(getDriver);
        page.waitForFileList();
        return page;
    }

    // Verified application table: file-name column is index 5.

    public void waitForFileList() {
        waitForElementToBeVisible(fileTable);
        waitForElementToBeVisible(fileNameFilter);
    }

    public boolean isFileListDisplayed() {
        String expectedPath = "/projects/" + BaseClass.getConfig("portalProject") + "/authoring";
        return isElementDisplayed(fileTable) && expectedPath.equals(URI.create(getCurrentUrl()).getPath());
    }

    private boolean isTargetEditLink(WebElement link) {
        String href = link.getAttribute("href");
        return href != null && BaseClass.getConfig("editorFileEditPath").equals(URI.create(href).getPath());
    }

    private WebElement targetRow() {
        return mywait.ignoring(StaleElementReferenceException.class).until(driver -> {
            for (WebElement row : findElementsNow(ROWS)) {
                for (WebElement link : row.findElements(By.cssSelector("a[href]"))) {
                    if (row.isDisplayed() && isTargetEditLink(link)) {
                        return row;
                    }
                }
            }
            return null;
        });
    }

    public HomePage findTargetFile() {
        enterText(fileNameFilter, BaseClass.getConfig("portalFileSearch"));
        // DataTables applies this column filter on Enter, not on each keystroke.
        fileNameFilter.sendKeys(Keys.ENTER);
        targetRow();
        return this;
    }

    public String getTargetFileTitle() {
        return mywait.ignoring(StaleElementReferenceException.class).until(driver -> {
            List<WebElement> cells = targetRow().findElements(By.tagName("td"));
            return cells.size() > 5 ? cells.get(5).getText().trim() : null;
        });
    }

    public DocumentEditorPageObjects1 openTargetFile() {
        if (!BaseClass.getConfig("expectedDocumentTitle").equals(getTargetFileTitle())) {
            throw new IllegalStateException("The configured edit link does not belong to the expected document");
        }
        Set<String> previousWindows = new HashSet<>(getDriver.getWindowHandles());
        WebElement edit = mywait.ignoring(StaleElementReferenceException.class).until(driver -> {
            for (WebElement link : targetRow().findElements(By.cssSelector("a[href]"))) {
                if (isTargetEditLink(link) && link.isDisplayed() && link.isEnabled()) {
                    return link;
                }
            }
            return null;
        });
        edit.click();
        mywait.until(driver -> {
            Set<String> opened = new HashSet<>(driver.getWindowHandles());
            opened.removeAll(previousWindows);
            if (opened.size() > 1) {
                throw new IllegalStateException("More than one window opened from the Edit action");
            }
            if (!opened.isEmpty()) {
                driver.switchTo().window(opened.iterator().next());
            }
            return BaseClass.getConfig("editorFileEditPath").equals(URI.create(driver.getCurrentUrl()).getPath());
        });
        DocumentEditorPageObjects1 editor = new DocumentEditorPageObjects1(getDriver);
        editor.waitForElementToBeVisible(By.id("titlebarFileName"));
        editor.waitForEditorReady();
        return editor;
    }

}
