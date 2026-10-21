package a_testBase;

import java.io.File;
import java.time.Duration;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ThreadGuard;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Base class for every test class. Extend it and you get the browser, the
 * configuration and the reports for free.
 *
 * <p>
 * One WebDriver per thread (ThreadLocal + ThreadGuard), so the suite can run
 * {@code parallel="methods"} with several browsers at a time. A test class
 * must therefore keep no page object in an instance field - build it inside
 * the test method instead (see the sample test class).
 * </p>
 *
 * <p>
 * {@link #setup(String)} opens a browser and the application URL before every
 * test method. If the application needs a login first, do it here: call the
 * login page object once the URL is open - see the commented line below.
 * </p>
 */
public class BaseClass {

	private static final ThreadLocal<WebDriver> THREAD_LOCAL_DRIVER = new ThreadLocal<WebDriver>();
	private static final ThreadLocal<String> THREAD_LOCAL_BROWSER = new ThreadLocal<String>();

	/** config.properties from src/test/resources, loaded once and shared by every thread. */
	protected static final ResourceBundle CONFIG = ResourceBundle.getBundle("config");

	public static Logger logger = LogManager.getLogger(BaseClass.class);

	public static final String REPORTS_DIR = System.getProperty("user.dir") + File.separator + "reports";
	public static final String SCREENSHOTS_DIR = System.getProperty("user.dir") + File.separator + "screenshots";

	// ------------------------------------------------------------------ config

	/** Reads a key from config.properties. */
	public static String getConfig(String key) {
		return CONFIG.getString(key).trim();
	}

	/** Reads a numeric key from config.properties. */
	public static int getIntConfig(String key) {
		return Integer.parseInt(getConfig(key));
	}

	/** Base URL of the application, ending with a slash. */
	public static String getAppUrl() {
		return getConfig("appURL");
	}

	/** WebDriver bound to the calling thread. */
	public static WebDriver getDriver() {
		return THREAD_LOCAL_DRIVER.get();
	}

	/** Browser the calling thread is running against, lower case. */
	public static String getBrowserName() {
		String browser = THREAD_LOCAL_BROWSER.get();
		return browser == null ? "" : browser;
	}

	// ------------------------------------------------------------------ suite

	/** Clears the previous run's reports and screenshots before the suite starts. */
	@BeforeSuite(alwaysRun = true)
	public synchronized void initializeSuite() {
		deleteMatching(REPORTS_DIR, ".html");
		deleteMatching(REPORTS_DIR, ".xls");
		deleteMatching(REPORTS_DIR, ".xlsx");
		deleteMatching(SCREENSHOTS_DIR, ".png");
		new File(REPORTS_DIR).mkdirs();
		new File(SCREENSHOTS_DIR).mkdirs();
	}

	private void deleteMatching(String directory, String suffix) {
		File dir = new File(directory);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (file.isFile() && file.getName().toLowerCase().endsWith(suffix)) {
				if (!file.delete()) {
					logger.warn("Could not delete old artefact: " + file.getAbsolutePath());
				}
			}
		}
	}

	// ------------------------------------------------------------------ method

	/**
	 * Opens a browser and the application before every test method.
	 *
	 * @param browser passed from Master.xml as &lt;parameter name="browser"&gt;;
	 *                falls back to the browser named in config.properties.
	 */
	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	public void setup(@Optional("") String browser) {
		String targetBrowser = (browser == null || browser.trim().isEmpty()) ? getConfig("browser") : browser.trim();
		THREAD_LOCAL_BROWSER.set(targetBrowser.toLowerCase());

		boolean headless = getConfig("headless").equalsIgnoreCase("yes");
		WebDriver driver = createDriverInstance(targetBrowser, headless);
		THREAD_LOCAL_DRIVER.set(ThreadGuard.protect(driver));

		getDriver().manage().deleteAllCookies();
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(getIntConfig("implicitWaitSeconds")));
		getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(getIntConfig("pageLoadTimeoutSeconds")));
		if (!headless) {
			getDriver().manage().window().maximize();
		}
		getDriver().get(getAppUrl());

		// Applications behind a login start here, for example:
		// new d_pageObjects.Aa_SamplePage(getDriver())
		// .login(getConfig("username"), getConfig("password"));

		logger.info("[" + Thread.currentThread().getName() + "] " + targetBrowser + " opened " + getAppUrl());
	}

	/** Builds the driver for the requested browser. Selenium Manager resolves the driver binary. */
	private WebDriver createDriverInstance(String browser, boolean headless) {
		if ("edge".equalsIgnoreCase(browser)) {
			EdgeOptions options = new EdgeOptions();
			options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
			if (headless) {
				options.addArguments("--headless=new");
			}
			options.addArguments("--window-size=1920,1080");
			return new EdgeDriver(options);
		}
		if ("firefox".equalsIgnoreCase(browser)) {
			FirefoxOptions options = new FirefoxOptions();
			if (headless) {
				options.addArguments("-headless");
			}
			options.addArguments("--width=1920", "--height=1080");
			return new FirefoxDriver(options);
		}
		if (!"chrome".equalsIgnoreCase(browser)) {
			logger.warn("Unsupported browser '" + browser + "'. Defaulting to Chrome.");
		}
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
		if (headless) {
			options.addArguments("--headless=new");
		}
		options.addArguments("--window-size=1920,1080");
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--disable-notifications");
		return new ChromeDriver(options);
	}

	/** Closes the browser bound to this thread. */
	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		WebDriver driver = THREAD_LOCAL_DRIVER.get();
		if (driver != null) {
			try {
				driver.quit();
			} catch (Exception e) {
				logger.error("Exception while quitting WebDriver: " + e.getMessage());
			}
		}
		THREAD_LOCAL_DRIVER.remove();
		THREAD_LOCAL_BROWSER.remove();
	}
}
