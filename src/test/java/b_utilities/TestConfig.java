package b_utilities;

import java.io.File;

/**
 * Static settings for the optional report email.
 *
 * <p>
 * Only used when {@code email=yes} in config.properties. Application
 * credentials are NOT kept here - they live in config.properties.
 * </p>
 */
public class TestConfig {

	// ------------------------------------------------------------- mail server
	public static String server = "smtp.gmail.com";
	public static String from = "automation.reports@example.com";
	/** App password of the sending account. Replace before enabling email. */
	public static String password = "CHANGE_ME";

	/** Recipients of the automation report. */
	public static String[] to = { "first.recipient@example.com" };

	public static String subject = "Automation Report";
	public static String messageBody = "Please find the the application automation reports attached.";

	// ------------------------------------------------------------ attachments
	/** Reports folder of this project - resolved at runtime, not hard coded. */
	public static String attachmentPath = System.getProperty("user.dir") + File.separator + "reports";
	public static String attachmentName = "ExcelReport";
	public static String attachmentName1 = "ExtentReport";
}
