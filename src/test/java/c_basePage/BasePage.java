package c_basePage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import a_testBase.BaseClass;

/**
 * Shared behaviour for every page object. Extend it, declare the locators of
 * one screen, and expose one method per action or check that screen offers.
 *
 * <p>
 * Locators that never change are declared on the page objects as
 * {@code @FindBy} fields and initialised here by PageFactory. Locators that
 * depend on a row number or a label are built as {@link By} at call time, so
 * both flavours of helper are offered below.
 * </p>
 *
 * <p>
 * Presence checks never throw: an element that is not on the page simply
 * reports "not displayed", which is what the negative test cases assert.
 * </p>
 */
public class BasePage {

	public WebDriver getDriver;
	public WebDriverWait mywait;
	public JavascriptExecutor js;
	public Actions action;

	/** Set debug=yes in config.properties to print a line per navigation and click. */
	private static final boolean DEBUG = "yes".equalsIgnoreCase(safeConfig("debug"));

	private static String safeConfig(String key) {
		try {
			return BaseClass.getConfig(key);
		} catch (Exception e) {
			return "no";
		}
	}

	/** Prints a debug line when debug=yes. */
	public static void debug(String message) {
		if (DEBUG) {
			System.out.println("[DEBUG][" + Thread.currentThread().getName() + "] " + message);
		}
	}

	public BasePage(WebDriver driver) {
		this.getDriver = driver;
		this.mywait = new WebDriverWait(driver, Duration.ofSeconds(BaseClass.getIntConfig("explicitWaitSeconds")));
		this.js = (JavascriptExecutor) driver;
		this.action = new Actions(driver);
		PageFactory.initElements(driver, this);
	}

	// ============================================================ navigation

	/** Opens a page of the application by its path, e.g. "orders/list". */
	public void navigateTo(String page) {
		debug("navigate to " + page);
		getDriver.get(BaseClass.getAppUrl() + page);
		waitForPageToLoad();
	}

	public String getCurrentUrl() {
		return getDriver.getCurrentUrl();
	}

	// ================================================================ clicks

	/** Native click once the element is clickable. */
	public void clickElement(WebElement element) {
		mywait.until(ExpectedConditions.elementToBeClickable(element));
		element.click();
	}

	public void clickElement(By locator) {
		mywait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}

	/** For controls under an overlay, where a native click is refused. */
	public void jsClick(WebElement element) {
		js.executeScript("arguments[0].click();", element);
	}

	public void jsClick(By locator) {
		jsClick(getDriver.findElement(locator));
	}

	// ================================================================= input

	public void enterText(WebElement element, String text) {
		mywait.until(ExpectedConditions.visibilityOf(element));
		element.clear();
		element.sendKeys(text);
	}

	/**
	 * Empties a live-search box. clear() alone does not always fire the keyup a
	 * grid listens to, so a space is typed and deleted afterwards.
	 */
	public void clearLiveSearch(WebElement searchBox) {
		searchBox.clear();
		searchBox.sendKeys(" ");
		searchBox.sendKeys(Keys.BACK_SPACE);
	}

	/**
	 * Sets a native HTML5 date input. The value is assigned in ISO form
	 * (yyyy-MM-dd); the browser renders it as DD-MM-YYYY.
	 */
	public void setDateValue(WebElement dateInput, String isoDate) {
		js.executeScript("arguments[0].value = arguments[1];", dateInput, isoDate);
		debug("set date = " + isoDate);
	}

	// ============================================================= dropdowns

	public void selectByVisibleText(WebElement dropdown, String text) {
		new Select(dropdown).selectByVisibleText(text);
	}

	public List<String> getOptionTexts(WebElement dropdown) {
		List<String> texts = new ArrayList<String>();
		for (WebElement option : new Select(dropdown).getOptions()) {
			texts.add(option.getText().trim());
		}
		return texts;
	}

	/** Option texts, leaving out the empty "please select" placeholder. */
	public List<String> getSelectableOptionTexts(WebElement dropdown) {
		List<String> texts = new ArrayList<String>();
		for (WebElement option : new Select(dropdown).getOptions()) {
			String value = option.getAttribute("value");
			if (value != null && !value.isEmpty()) {
				texts.add(option.getText().trim());
			}
		}
		return texts;
	}

	public String getSelectedText(WebElement dropdown) {
		return new Select(dropdown).getFirstSelectedOption().getText().trim();
	}

	public int getOptionCount(WebElement dropdown) {
		return new Select(dropdown).getOptions().size();
	}

	// ================================================================= state

	/** True when the element is on the page and visible. Never throws. */
	public boolean isElementDisplayed(WebElement element) {
		try {
			return element.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isElementDisplayed(By locator) {
		List<WebElement> found = getDriver.findElements(locator);
		return !found.isEmpty() && isElementDisplayed(found.get(0));
	}

	/** True when at least one element matches, visible or not. */
	public boolean isElementPresent(By locator) {
		return !getDriver.findElements(locator).isEmpty();
	}

	/**
	 * findElements without the implicit wait. For checks where "not there" is
	 * the normal answer - an empty-results row, a control that should not be
	 * there - so each call does not sit out the full implicit wait.
	 */
	public List<WebElement> findElementsNow(By locator) {
		getDriver.manage().timeouts().implicitlyWait(Duration.ZERO);
		try {
			return getDriver.findElements(locator);
		} finally {
			getDriver.manage().timeouts()
					.implicitlyWait(Duration.ofSeconds(BaseClass.getIntConfig("implicitWaitSeconds")));
		}
	}

	/** True when at least one element matches right now. */
	public boolean isElementPresentNow(By locator) {
		return !findElementsNow(locator).isEmpty();
	}

	/** True when the first match is visible right now; false at once when nothing matches. */
	public boolean isElementDisplayedNow(By locator) {
		List<WebElement> found = findElementsNow(locator);
		return !found.isEmpty() && isElementDisplayed(found.get(0));
	}

	public boolean isElementEnabled(WebElement element) {
		try {
			return element.isEnabled();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isElementSelected(WebElement element) {
		try {
			return element.isSelected();
		} catch (Exception e) {
			return false;
		}
	}

	/** True when every element is displayed and the list is not empty. */
	public boolean allDisplayed(List<WebElement> elements) {
		if (elements.isEmpty()) {
			return false;
		}
		for (WebElement element : elements) {
			if (!isElementDisplayed(element)) {
				return false;
			}
		}
		return true;
	}

	/** True when every element is enabled and the list is not empty. */
	public boolean allEnabled(List<WebElement> elements) {
		if (elements.isEmpty()) {
			return false;
		}
		for (WebElement element : elements) {
			if (!isElementEnabled(element)) {
				return false;
			}
		}
		return true;
	}

	public String getText(WebElement element) {
		return element.getText().trim();
	}

	public String getText(By locator) {
		return getDriver.findElement(locator).getText().trim();
	}

	/** Trimmed text of every element in a list. */
	public List<String> getTexts(List<WebElement> elements) {
		List<String> texts = new ArrayList<String>();
		for (WebElement element : elements) {
			texts.add(element.getText().trim());
		}
		return texts;
	}

	public String getAttributeValue(WebElement element, String attribute) {
		String value = element.getAttribute(attribute);
		return value == null ? "" : value;
	}

	public Object executeScript(String script, Object... args) {
		return js.executeScript(script, args);
	}

	// ================================================================= waits

	public void waitForElementToBeVisible(WebElement element) {
		mywait.until(ExpectedConditions.visibilityOf(element));
	}

	public void waitForElementToBeVisible(By locator) {
		mywait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public void waitForUrlToContain(String fragment) {
		mywait.until(ExpectedConditions.urlContains(fragment));
	}

	public void waitForPageToLoad() {
		mywait.until(driver -> "complete"
				.equals(((JavascriptExecutor) driver).executeScript("return document.readyState")));
	}

	/**
	 * Short fixed pause. Used only where the application gives no signal to wait
	 * on - a grid redraw after typing, or a modal fade.
	 */
	public static void sleepFor(int milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	// ================================================================ window

	/** Switches into the newest browser tab. */
	public void switchToNewestWindow() {
		List<String> handles = new ArrayList<String>(getDriver.getWindowHandles());
		getDriver.switchTo().window(handles.get(handles.size() - 1));
	}

	public int getWindowCount() {
		return getDriver.getWindowHandles().size();
	}
}
