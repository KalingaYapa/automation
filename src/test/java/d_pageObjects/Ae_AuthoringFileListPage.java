package d_pageObjects;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import a_testBase.BaseClass;
import c_basePage.BasePage;

/** Find the allocated document and use its existing Edit link, without checking files out. */
public class Ae_AuthoringFileListPage extends BasePage {
    private static final By ROWS = By.cssSelector("#dataTables tbody tr");
    @FindBy(id = "dataTables")
    private WebElement fileTable;
    // Verified application table: file-name column is index 5.
    @FindBy(css = "#dataTables thead input.dt-filter-input[data-idx='5']")
    private WebElement fileNameFilter;

    public Ae_AuthoringFileListPage(WebDriver driver) {
        super(driver);
    }

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

    public Ae_AuthoringFileListPage findTargetFile() {
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

    public Ab_DocumentEditorPage openTargetFile() {
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
        Ab_DocumentEditorPage editor = new Ab_DocumentEditorPage(getDriver);
        editor.waitForElementToBeVisible(By.id("titlebarFileName"));
        return editor;
    }
}
