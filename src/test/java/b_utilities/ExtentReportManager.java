package b_utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import a_testBase.BaseClass;

/**
 * Produces both reports the suite is run for: an Extent HTML report with a
 * screenshot on every failure, and a formatted Excel workbook with a summary
 * sheet and a per-test-case detail sheet.
 *
 * <p>
 * Built for {@code parallel="methods"}. The Extent instance is created once in
 * {@link #onStart(ISuite)} and flushed once in {@link #onFinish(ISuite)}, so a
 * Master.xml holding several &lt;test&gt; blocks - the cross-browser suite, for
 * instance - produces one report rather than overwriting itself per block. Each
 * thread keeps its own ExtentTest, and results are collected in a synchronized
 * list.
 * </p>
 *
 * <p>
 * The test case id shown in both reports comes from the @Test description,
 * which every test writes as {@code "TC_80 | <title from the sheet>"}. The
 * Automation ID column carries {@code <class>.<method>}, and the method name is
 * the value held in the manual sheet's Automation ID column.
 * </p>
 */
public class ExtentReportManager implements ITestListener, ISuiteListener {

	private static final String EXTENT_REPORT_NAME = "ExtentReport.html";
	private static final String EXCEL_REPORT_NAME = "ExcelReport.xlsx";
	private static final SimpleDateFormat TIMESTAMP = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	private static ExtentReports extent;
	private static ExtentSparkReporter sparkReporter;

	/** One ExtentTest per thread so parallel methods do not overwrite each other. */
	public static final ThreadLocal<ExtentTest> EXTENT_TEST = new ThreadLocal<ExtentTest>();

	private static final List<ResultRecord> RESULTS = Collections
			.synchronizedList(new ArrayList<ResultRecord>());

	private static long suiteStartMillis;

	/** One row of the Excel report. */
	private static class ResultRecord {
		String testCaseId;
		String testCaseTitle;
		String className;
		String methodName;
		String module;
		String browser;
		String status;
		String startTime;
		String endTime;
		String duration;
		String failureReason;

		int idNumber() {
			try {
				return Integer.parseInt(testCaseId.replaceAll("[^0-9]", ""));
			} catch (Exception e) {
				return Integer.MAX_VALUE;
			}
		}
	}

	// ================================================================== suite

	@Override
	public synchronized void onStart(ISuite suite) {
		if (extent != null) {
			return; // already initialised by an earlier <test> block
		}
		suiteStartMillis = System.currentTimeMillis();

		new File(BaseClass.REPORTS_DIR).mkdirs();
		sparkReporter = new ExtentSparkReporter(BaseClass.REPORTS_DIR + File.separator + EXTENT_REPORT_NAME);
		sparkReporter.config().setDocumentTitle("Automation Report");
		sparkReporter.config().setReportName(suite.getName());
		sparkReporter.config().setTheme(Theme.STANDARD);

		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Project", BaseClass.getConfig("projectName"));
		extent.setSystemInfo("Organization", "Innodata");
		extent.setSystemInfo("Tester", "QA Automation Team");
		extent.setSystemInfo("Application URL", BaseClass.getAppUrl());
		extent.setSystemInfo("Environment", resolveEnvironment(BaseClass.getAppUrl()));
		extent.setSystemInfo("Suite", suite.getName());
		extent.setSystemInfo("Headless", BaseClass.getConfig("headless"));
		extent.setSystemInfo("Executed On", TIMESTAMP.format(new Date()));
	}

	@Override
	public synchronized void onFinish(ISuite suite) {
		if (extent != null) {
			extent.flush();
		}
		writeExcelReport(suite.getName());

		if ("yes".equalsIgnoreCase(BaseClass.getConfig("email"))) {
			try {
				new MonitoringMail().sendMail(TestConfig.server, TestConfig.from, TestConfig.to,
						TestConfig.subject + " - " + suite.getName(), TestConfig.messageBody, TestConfig.attachmentPath,
						TestConfig.attachmentName, TestConfig.attachmentName1, suite.getName());
			} catch (MessagingException e) {
				System.out.println("Could not send the report email: " + e.getMessage());
			}
		} else {
			System.out.println("Report email is disabled (email=no in config.properties)");
		}
	}

	private String resolveEnvironment(String appUrl) {
		if (appUrl.toLowerCase().contains("staging")) {
			return "Staging Environment";
		}
		if (appUrl.contains("_test.")) {
			return "Test Environment";
		}
		if (appUrl.contains("uat")) {
			return "UAT Environment";
		}
		if (appUrl.contains("sandbox")) {
			return "Sandbox Environment";
		}
		return "Unknown Environment";
	}

	// =================================================================== test

	@Override
	public synchronized void onTestStart(ITestResult result) {
		ExtentTest test = extent.createTest(testCaseId(result) + " - " + testCaseTitle(result));
		test.assignCategory(groupsOf(result));
		test.assignCategory(result.getTestClass().getRealClass().getSimpleName());
		test.info("Browser: " + BaseClass.getBrowserName());
		test.info("Method: " + result.getMethod().getMethodName());
		EXTENT_TEST.set(test);
	}

	@Override
	public synchronized void onTestSuccess(ITestResult result) {
		ExtentTest test = EXTENT_TEST.get();
		if (test != null) {
			test.log(Status.PASS, MarkupHelper
					.createLabel(testCaseId(result) + " PASSED", ExtentColor.GREEN));
		}
		record(result, "Pass", "");
	}

	@Override
	public synchronized void onTestFailure(ITestResult result) {
		ExtentTest test = EXTENT_TEST.get();
		String reason = result.getThrowable() == null ? "" : String.valueOf(result.getThrowable().getMessage());

		if (test != null) {
			test.log(Status.FAIL, MarkupHelper.createLabel(testCaseId(result) + " FAILED", ExtentColor.RED));
			if (result.getThrowable() != null) {
				test.fail(result.getThrowable());
			}
			attachScreenshot(test, result.getMethod().getMethodName());
		}
		record(result, "Fail", reason);
	}

	@Override
	public synchronized void onTestSkipped(ITestResult result) {
		ExtentTest test = EXTENT_TEST.get();
		String reason = result.getThrowable() == null ? "" : String.valueOf(result.getThrowable().getMessage());
		if (test != null) {
			test.log(Status.SKIP, MarkupHelper.createLabel(testCaseId(result) + " SKIPPED", ExtentColor.ORANGE));
			if (!reason.isEmpty()) {
				test.skip(reason);
			}
		}
		record(result, "Skip", reason);
	}

	private void attachScreenshot(ExtentTest test, String methodName) {
		try {
			WebDriver driver = BaseClass.getDriver();
			if (driver == null) {
				return;
			}
			String path = captureScreen(driver, methodName);
			String base64 = convertImageToBase64(path);
			test.fail("<b><font color='red'>Screenshot at point of failure</font></b>",
					MediaEntityBuilder.createScreenCaptureFromBase64String("data:image/png;base64," + base64).build());
		} catch (Exception e) {
			System.out.println("Could not attach a screenshot: " + e.getMessage());
		}
	}

	// ================================================================ records

	private void record(ITestResult result, String status, String failureReason) {
		ResultRecord record = new ResultRecord();
		record.testCaseId = testCaseId(result);
		record.testCaseTitle = testCaseTitle(result);
		record.className = result.getTestClass().getRealClass().getSimpleName();
		record.methodName = result.getMethod().getMethodName();
		record.module = groupsOf(result);
		record.browser = BaseClass.getBrowserName();
		record.status = status;
		record.startTime = TIMESTAMP.format(new Date(result.getStartMillis()));
		record.endTime = TIMESTAMP.format(new Date(result.getEndMillis()));

		long elapsed = result.getEndMillis() - result.getStartMillis();
		record.duration = (elapsed / 1000) + "s " + (elapsed % 1000) + "ms";
		record.failureReason = failureReason == null ? "" : failureReason;

		RESULTS.add(record);
	}

	/** Everything before the first pipe of the description, e.g. "TC_80". */
	private String testCaseId(ITestResult result) {
		String description = result.getMethod().getDescription();
		if (description != null && description.contains("|")) {
			return description.substring(0, description.indexOf('|')).trim();
		}
		return result.getMethod().getMethodName();
	}

	/** Everything after the first pipe of the description. */
	private String testCaseTitle(ITestResult result) {
		String description = result.getMethod().getDescription();
		if (description != null && description.contains("|")) {
			return description.substring(description.indexOf('|') + 1).trim();
		}
		return description == null ? result.getMethod().getMethodName() : description.trim();
	}

	private String groupsOf(ITestResult result) {
		String[] groups = result.getMethod().getGroups();
		return groups.length == 0 ? "" : String.join(", ", groups);
	}

	// ================================================================== excel

	private synchronized void writeExcelReport(String suiteName) {
		if (RESULTS.isEmpty()) {
			System.out.println("No results recorded - Excel report not written.");
			return;
		}

		// One row per test case id, keeping the last attempt, so a retried test
		// cannot appear twice in the workbook.
		java.util.LinkedHashMap<String, ResultRecord> unique =
				new java.util.LinkedHashMap<String, ResultRecord>();
		synchronized (RESULTS) {
			for (ResultRecord record : RESULTS) {
				unique.put(record.testCaseId + "|" + record.browser, record);
			}
		}
		List<ResultRecord> ordered = new ArrayList<ResultRecord>(unique.values());
		Collections.sort(ordered, new Comparator<ResultRecord>() {
			public int compare(ResultRecord a, ResultRecord b) {
				return Integer.compare(a.idNumber(), b.idNumber());
			}
		});

		Workbook workbook = new XSSFWorkbook();
		try {
			CellStyle headerStyle = headerStyle(workbook);
			CellStyle bodyStyle = bodyStyle(workbook);
			CellStyle passStyle = statusStyle(workbook, IndexedColors.BRIGHT_GREEN);
			CellStyle failStyle = statusStyle(workbook, IndexedColors.RED);
			CellStyle skipStyle = statusStyle(workbook, IndexedColors.LIGHT_ORANGE);
			CellStyle titleStyle = titleStyle(workbook);

			writeSummarySheet(workbook, suiteName, ordered, titleStyle, headerStyle, bodyStyle);
			writeDetailSheet(workbook, ordered, headerStyle, bodyStyle, passStyle, failStyle, skipStyle);

			new File(BaseClass.REPORTS_DIR).mkdirs();
			FileOutputStream out = new FileOutputStream(
					new File(BaseClass.REPORTS_DIR + File.separator + EXCEL_REPORT_NAME));
			workbook.write(out);
			out.close();
			System.out.println("Excel report written to " + BaseClass.REPORTS_DIR + File.separator + EXCEL_REPORT_NAME);
		} catch (IOException e) {
			System.out.println("Could not write the Excel report: " + e.getMessage());
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				// nothing further to do
			}
		}
	}

	private void writeSummarySheet(Workbook workbook, String suiteName, List<ResultRecord> ordered,
			CellStyle titleStyle, CellStyle headerStyle, CellStyle bodyStyle) {
		Sheet sheet = workbook.createSheet("Summary");

		int pass = 0;
		int fail = 0;
		int skip = 0;
		for (ResultRecord record : ordered) {
			if ("Pass".equals(record.status)) {
				pass++;
			} else if ("Fail".equals(record.status)) {
				fail++;
			} else {
				skip++;
			}
		}
		int total = ordered.size();
		long elapsed = System.currentTimeMillis() - suiteStartMillis;

		int rowNum = 0;
		Row titleRow = sheet.createRow(rowNum++);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Execution Summary");
		titleCell.setCellStyle(titleStyle);
		rowNum++;

		rowNum = addSummaryRow(sheet, rowNum, "Suite", suiteName, headerStyle, bodyStyle);
		rowNum = addSummaryRow(sheet, rowNum, "Application URL", BaseClass.getAppUrl(), headerStyle, bodyStyle);
		rowNum = addSummaryRow(sheet, rowNum, "Executed On", TIMESTAMP.format(new Date()), headerStyle, bodyStyle);
		rowNum = addSummaryRow(sheet, rowNum, "Total Duration",
				(elapsed / 60000) + " min " + ((elapsed / 1000) % 60) + " sec", headerStyle, bodyStyle);
		rowNum++;

		rowNum = addSummaryRow(sheet, rowNum, "Total Test Cases", String.valueOf(total), headerStyle, bodyStyle);
		rowNum = addSummaryRow(sheet, rowNum, "Passed", String.valueOf(pass), headerStyle, bodyStyle);
		rowNum = addSummaryRow(sheet, rowNum, "Failed", String.valueOf(fail), headerStyle, bodyStyle);
		rowNum = addSummaryRow(sheet, rowNum, "Skipped", String.valueOf(skip), headerStyle, bodyStyle);
		addSummaryRow(sheet, rowNum, "Pass Percentage",
				total == 0 ? "0%" : String.format("%.1f%%", (pass * 100.0) / total), headerStyle, bodyStyle);

		sheet.setColumnWidth(0, 26 * 256);
		sheet.setColumnWidth(1, 70 * 256);
	}

	private int addSummaryRow(Sheet sheet, int rowNum, String label, String value, CellStyle headerStyle,
			CellStyle bodyStyle) {
		Row row = sheet.createRow(rowNum);
		Cell labelCell = row.createCell(0);
		labelCell.setCellValue(label);
		labelCell.setCellStyle(headerStyle);
		Cell valueCell = row.createCell(1);
		valueCell.setCellValue(value);
		valueCell.setCellStyle(bodyStyle);
		return rowNum + 1;
	}

	private void writeDetailSheet(Workbook workbook, List<ResultRecord> ordered, CellStyle headerStyle,
			CellStyle bodyStyle, CellStyle passStyle, CellStyle failStyle, CellStyle skipStyle) {
		Sheet sheet = workbook.createSheet("Test Results");

		String[] headers = { "#", "Test Case ID", "Test Case Title", "Automation ID", "Module / Group", "Browser",
				"Status", "Start Time", "End Time", "Duration", "Failure Reason" };
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}

		int rowNum = 1;
		for (ResultRecord record : ordered) {
			Row row = sheet.createRow(rowNum);
			CellStyle statusStyle = "Pass".equals(record.status) ? passStyle
					: "Fail".equals(record.status) ? failStyle : skipStyle;

			writeCell(row, 0, String.valueOf(rowNum), bodyStyle);
			writeCell(row, 1, record.testCaseId, bodyStyle);
			writeCell(row, 2, record.testCaseTitle, bodyStyle);
			writeCell(row, 3, record.className + "." + record.methodName, bodyStyle);
			writeCell(row, 4, record.module, bodyStyle);
			writeCell(row, 5, record.browser, bodyStyle);
			writeCell(row, 6, record.status, statusStyle);
			writeCell(row, 7, record.startTime, bodyStyle);
			writeCell(row, 8, record.endTime, bodyStyle);
			writeCell(row, 9, record.duration, bodyStyle);
			writeCell(row, 10, record.failureReason, bodyStyle);
			rowNum++;
		}

		int[] widths = { 6, 14, 60, 46, 26, 10, 10, 20, 20, 14, 60 };
		for (int i = 0; i < widths.length; i++) {
			sheet.setColumnWidth(i, widths[i] * 256);
		}
		sheet.createFreezePane(0, 1);
	}

	private void writeCell(Row row, int column, String value, CellStyle style) {
		Cell cell = row.createCell(column);
		cell.setCellValue(value == null ? "" : value);
		cell.setCellStyle(style);
	}

	// --------------------------------------------------------------- styling

	private CellStyle titleStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Arial");
		font.setBold(true);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		return style;
	}

	private CellStyle headerStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Arial");
		font.setBold(true);
		font.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.LEFT);
		applyBorders(style);
		return style;
	}

	private CellStyle bodyStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Arial");
		style.setFont(font);
		style.setWrapText(true);
		style.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.TOP);
		applyBorders(style);
		return style;
	}

	private CellStyle statusStyle(Workbook workbook, IndexedColors colour) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Arial");
		font.setBold(true);
		style.setFont(font);
		style.setFillForegroundColor(colour.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.CENTER);
		applyBorders(style);
		return style;
	}

	private void applyBorders(CellStyle style) {
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
	}

	// ============================================================ screenshots

	/** Saves a PNG of the current browser window and returns its path. */
	public String captureScreen(WebDriver driver, String testName) throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		File folder = new File(BaseClass.SCREENSHOTS_DIR);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String destination = BaseClass.SCREENSHOTS_DIR + File.separator + testName + "_" + timeStamp + ".png";
		Files.copy(screenshot.toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
		return destination;
	}

	/** Base64 encoding of a saved screenshot, for embedding in the HTML report. */
	public String convertImageToBase64(String imagePath) throws IOException {
		return Base64.getEncoder().encodeToString(Files.readAllBytes(new File(imagePath).toPath()));
	}
}
