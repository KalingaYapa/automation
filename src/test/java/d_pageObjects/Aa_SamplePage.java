package d_pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import c_basePage.BasePage;

/**
 * TEMPLATE page object - copy it, rename it, delete this one.
 *
 * <p>
 * One page object per screen, named {@code Xx_<Screen>Page}, paired one to one
 * with the test class {@code Xx_<Screen>TC}. The letter prefix keeps the pair
 * next to each other in the Package Explorer and in the reports.
 * </p>
 *
 * <p>
 * Three rules this file follows:
 * </p>
 * <ol>
 * <li><b>Locators are fields, actions are methods.</b> A test never names a
 * locator and never calls the WebDriver.</li>
 * <li><b>Fixed locators use {@code @FindBy}.</b> Locators that depend on a row
 * number or a label are built as {@link By} inside the method that needs
 * them.</li>
 * <li><b>Page-level text and values are constants</b>, so the test asserts
 * against a name and not a loose string.</li>
 * </ol>
 */
public class Aa_SamplePage extends BasePage {

	/** Path of this screen, appended to appURL by {@link BasePage#navigateTo(String)}. */
	public static final String PAGE = "sample";
	public static final String HEADING = "Sample Screen";
	public static final String NO_RESULT_MESSAGE = "No matching records found";

	/** A locator built at call time, because "not there" is a valid answer. */
	private static final By EMPTY_ROW = By.cssSelector("#resultsTable tbody .empty-row");

	public Aa_SamplePage(WebDriver driver) {
		super(driver);
	}

	// ------------------------------------------------------------- elements
	@FindBy(id = "username")
	WebElement usernameField;

	@FindBy(id = "password")
	WebElement passwordField;

	@FindBy(id = "loginButton")
	WebElement loginButton;

	@FindBy(css = "h1.page-title")
	WebElement heading;

	@FindBy(id = "searchInput")
	WebElement searchBox;

	@FindBy(id = "statusFilter")
	WebElement statusFilter;

	@FindBy(id = "resultsTable")
	WebElement resultsTable;

	@FindBy(css = "#resultsTable thead th")
	List<WebElement> headers;

	@FindBy(css = "#resultsTable tbody tr")
	List<WebElement> rows;

	// -------------------------------------------------------------- actions
	/** Opens the screen directly and waits for it to be on display. */
	public Aa_SamplePage open() {
		navigateTo(PAGE);
		waitForElementToBeVisible(resultsTable);
		return this;
	}

	/** Login flow, for applications that need one before anything else. */
	public void login(String username, String password) {
		enterText(usernameField, username);
		enterText(passwordField, password);
		clickElement(loginButton);
		waitForPageToLoad();
	}

	public void search(String value) {
		enterText(searchBox, value);
		sleepFor(600);
	}

	public void selectStatus(String status) {
		selectByVisibleText(statusFilter, status);
		sleepFor(600);
	}

	// ----------------------------------------------------------- assertions
	public boolean isTableDisplayed() {
		return isElementDisplayed(resultsTable);
	}

	public String getHeading() {
		return getText(heading);
	}

	public List<String> getColumnHeaders() {
		return getTexts(headers);
	}

	/** Zero when the grid is showing its empty-results row. */
	public int getRowCount() {
		return isElementPresentNow(EMPTY_ROW) ? 0 : rows.size();
	}

	public String getEmptyTableMessage() {
		return getText(EMPTY_ROW);
	}

	/** Values of one column, 1-based, as plain strings. */
	public List<String> getColumnValues(int oneBasedIndex) {
		return getTexts(
				getDriver.findElements(By.cssSelector("#resultsTable tbody td:nth-child(" + oneBasedIndex + ")")));
	}
}
