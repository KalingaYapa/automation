package b_utilities;

import java.util.List;

import a_testbase.BaseClass;

/** Shared navigation and reusable value checks. */
public class ReusableMethods extends BaseClass {

    /** Login and open the configured allocation once for each editor test. */
    public static documenteditor.DocumentEditorPageObjects1 loginAndSearchFileClickEdit() {
        return new d_loginpage.HomePage(getDriver()).login()
                .selectProject(getConfig("portalProject"))
                .openAuthoringFileList().findTargetFile().openTargetFile();
    }

	/** Common timestamp shape: YYYY-MM-DD HH:MM:SS. Adjust to the application under test. */
	public static final String TIMESTAMP_PATTERN = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.*";

	private ReusableMethods() {
		// utility class
	}

	/** True when the list holds at least one value and none of them is blank. */
	public static boolean allNonEmpty(List<String> values) {
		if (values.isEmpty()) {
			return false;
		}
		for (String value : values) {
			if (value == null || value.trim().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/** True when every value matches the regular expression. */
	public static boolean allMatch(List<String> values, String regex) {
		for (String value : values) {
			if (!value.matches(regex)) {
				return false;
			}
		}
		return true;
	}

	/** True when every value equals the expected text, ignoring case. */
	public static boolean allEqualIgnoreCase(List<String> values, String expected) {
		for (String value : values) {
			if (!value.equalsIgnoreCase(expected)) {
				return false;
			}
		}
		return true;
	}

	/** True when every value contains the expected text, ignoring case. */
	public static boolean allContainIgnoreCase(List<String> values, String expected) {
		String needle = expected.toLowerCase();
		for (String value : values) {
			if (!value.toLowerCase().contains(needle)) {
				return false;
			}
		}
		return true;
	}

	/** True when every value starts with the expected prefix. */
	public static boolean allStartWith(List<String> values, String prefix) {
		for (String value : values) {
			if (!value.startsWith(prefix)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * True when the date part of every timestamp falls inside an inclusive ISO
	 * date range. ISO dates compare correctly as plain strings.
	 */
	public static boolean allDatesWithin(List<String> timestamps, String fromIso, String toIso) {
		for (String stamp : timestamps) {
			String date = stamp.trim().split(" ")[0];
			if (date.compareTo(fromIso) < 0 || date.compareTo(toIso) > 0) {
				return false;
			}
		}
		return true;
	}

	/** Joins a list for a readable assertion message. */
	public static String join(List<String> values) {
		return String.join(", ", values);
	}
}
