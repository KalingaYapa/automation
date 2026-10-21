package b_utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Re-runs a failed test up to {@link #MAX_RETRY_LIMIT} times.
 *
 * <p>
 * Set MAX_RETRY_LIMIT to 0 to disable retries, or to 1 to give a flaky case one
 * second chance. A fresh browser is opened for each attempt because the driver
 * is created in a @BeforeMethod.
 * </p>
 */
public class RetryAnalyzer implements IRetryAnalyzer {

	/**
	 * How many times a failed test is retried.
	 *
	 * <p>
	 * Kept at 0 so the reports hold exactly one row per test case, and so the six
	 * tests that catch real defects fail once rather than three times. Raise it
	 * to 1 if you would rather give a flaky case a second chance.
	 * </p>
	 */
	public static final int MAX_RETRY_LIMIT = 0;

	private int retryAttemptsCounter = 0;

	@Override
	public synchronized boolean retry(ITestResult result) {
		try {
			if (!result.isSuccess() && result.getStatus() != ITestResult.SKIP
					&& result.getStatus() != ITestResult.SUCCESS_PERCENTAGE_FAILURE) {
				if (retryAttemptsCounter < MAX_RETRY_LIMIT) {
					retryAttemptsCounter++;
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			System.out.println("Error while deciding whether to retry: " + e.getMessage());
			return false;
		}
	}
}
