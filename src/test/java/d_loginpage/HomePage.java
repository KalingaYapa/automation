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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/** Shared login, project selection and allocation navigation. */
public class HomePage extends BasePage {
    private static final Logger logger = LogManager.getLogger(HomePage.class);

    @FindBy(xpath = "//*[@id='dataTables']//tbody/tr")
    private List<WebElement> fileRows;
    @FindBy(xpath = "//*[@id='email']")
    private WebElement email;
    @FindBy(xpath = "//input[@type='password']")
    private WebElement password;
    @FindBy(xpath = "//form//button[@type='submit']")
    private WebElement submit;
    @FindBy(xpath = "//*[contains(concat(' ',normalize-space(@class),' '),' navbar-right ')]//*[contains(concat(' ',normalize-space(@class),' '),' btn-group ')]/button[@data-toggle='dropdown']")
    private WebElement projectDropdown;
    @FindBy(xpath = "//a[normalize-space(.)='Authoring File List']")
    private WebElement authoringFileList;
    @FindBy(xpath = "//*[@id='dataTables']")
    private WebElement fileTable;
    @FindBy(xpath = "//*[@id='dataTables']//thead//input[@data-idx='5']")
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
            logger.debug("Entering sandbox username");
            enterText(email, username);
            logger.debug("Entered sandbox username");
            logger.debug("Submitting username to open password step");
            elementClick(submit);
            logger.debug("Username submitted; entering sandbox password");
            enterText(password, secret);
            logger.debug("Entered sandbox password; submitting login");
            elementClick(submit);
        } catch (WebDriverException exception) {
            // Selenium command diagnostics can contain typed input; do not expose the cause.
            throw new IllegalStateException("Portal login interaction failed ("
                    + exception.getClass().getSimpleName() + ")");
        }
        HomePage portal = new HomePage(getDriver);
        logger.debug("Login submitted; waiting for authenticated portal");
        portal.waitForPortal();
        logger.debug("Authenticated portal is ready");
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
        logger.debug("Opening project dropdown");
        elementClick(projectDropdown);
        logger.debug("Project dropdown opened; finding project: {}", project);
        WebElement group = projectDropdown.findElement(By.xpath(
                "ancestor::div[contains(concat(' ',normalize-space(@class),' '),' btn-group ')][1]"));
        WebElement option = mywait.until(driver -> {
            for (WebElement link : group.findElements(By.xpath(".//ul[contains(concat(' ',normalize-space(@class),' '),' dropdown-menu ')]//a"))) {
                if (link.isDisplayed() && project.equals(link.getText().trim())) {
                    return link;
                }
            }
            return null;
        });
        logger.debug("Selecting project: {}", project);
        option.click();
        logger.debug("Selected project option; waiting for selection confirmation");
        mywait.until(driver -> project.equals(getSelectedProject()));
        logger.debug("Project selection confirmed: {}", project);
        return this;
    }

    public HomePage openAuthoringFileList() {
        logger.debug("Opening Authoring File List");
        elementClick(authoringFileList);
        logger.debug("Authoring File List clicked; waiting for table");
        HomePage page = new HomePage(getDriver);
        page.waitForFileList();
        logger.debug("Authoring File List table is ready");
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
            for (WebElement row : fileRows) {
                for (WebElement link : row.findElements(By.xpath(".//a[@href]"))) {
                    if (row.isDisplayed() && isTargetEditLink(link)) {
                        return row;
                    }
                }
            }
            return null;
        });
    }

    public HomePage findTargetFile() {
        logger.debug("Filtering the allocated document by configured file name");
        enterText(fileNameFilter, BaseClass.getConfig("portalFileSearch"));
        // DataTables applies this column filter on Enter, not on each keystroke.
        logger.debug("Pressing Enter to apply file-name filter");
        fileNameFilter.sendKeys(Keys.ENTER);
        logger.debug("File-name filter applied; waiting for target allocation");
        targetRow();
        logger.debug("Target allocation row located");
        return this;
    }

    public String getTargetFileTitle() {
        return mywait.ignoring(StaleElementReferenceException.class).until(driver -> {
            List<WebElement> cells = targetRow().findElements(By.xpath("./td"));
            return cells.size() > 5 ? cells.get(5).getText().trim() : null;
        });
    }

    public DocumentEditorPageObjects1 openTargetFile() {
        if (!BaseClass.getConfig("expectedDocumentTitle").equals(getTargetFileTitle())) {
            throw new IllegalStateException("The configured edit link does not belong to the expected document");
        }
        Set<String> previousWindows = new HashSet<>(getDriver.getWindowHandles());
        WebElement edit = mywait.ignoring(StaleElementReferenceException.class).until(driver -> {
            for (WebElement link : targetRow().findElements(By.xpath(".//a[@href]"))) {
                if (isTargetEditLink(link) && link.isDisplayed() && link.isEnabled()) {
                    return link;
                }
            }
            return null;
        });
        logger.debug("Clicking Edit for the configured allocation");
        edit.click();
        logger.debug("Edit clicked; waiting for editor window");
        mywait.until(driver -> {
            Set<String> opened = new HashSet<>(driver.getWindowHandles());
            opened.removeAll(previousWindows);
            if (opened.size() > 1) {
                throw new IllegalStateException("More than one window opened from the Edit action");
            }
            if (!opened.isEmpty()) {
                logger.debug("Switching to newly opened editor window");
                driver.switchTo().window(opened.iterator().next());
                logger.debug("Switched to editor window");
            }
            return BaseClass.getConfig("editorFileEditPath").equals(URI.create(driver.getCurrentUrl()).getPath());
        });
        DocumentEditorPageObjects1 editor = new DocumentEditorPageObjects1(getDriver);
        editor.waitForElementToBeVisible(By.xpath("//*[@id='titlebarFileName']"));
        logger.debug("Waiting for CKEditor readiness");
        editor.waitForEditorReady();
        logger.debug("Allocated document editor is ready");
        return editor;
    }

}
