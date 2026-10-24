# Framework standards migration

The application code now follows the supplied Naming and Grouping Standard. This migration enables every existing application test; it adds no new business scenarios.

## File mapping

| Previous location/class | Current location/class |
| --- | --- |
| `a_testBase.BaseClass` | `a_testbase.BaseClass` |
| `c_basePage.BasePage` | `d_loginpage.BasePage` |
| `d_pageObjects.Ac_LoginPage`, `Ad_PortalPage`, `Ae_AuthoringFileListPage` | Combined into `d_loginpage.HomePage`; no duplicate navigation implementation remains. |
| `e_testCases.Ab_DocumentEditorTC` | `documenteditor.DocumentEditor1TestCase` |
| `d_pageObjects.Ab_DocumentEditorPage` | `documenteditor.DocumentEditorPageObjects1` |
| `Ac_LoginTC`, `Ad_PortalTC`, `Ae_AuthoringFileListTC` | Combined into `portal.Portal1TestCase`, retaining all five methods/IDs. |
| Sample test and page classes | Archived in `docs/templates/*.java.example`; they are starter examples for a different screen and are not application tests. |

The editor setup calls `ReusableMethods.loginAndSearchFileClickEdit()` once before each test. This uses HomePage for the existing login → IATA → Authoring File List → allocation Edit flow. The editor page retains its existing CKEditor readiness, frame, selection, formatting and Undo helpers.

## Groups and execution

Every application test has exactly two groups:

- Editor: `DocumentEditor` plus `P2-DocumentEditor` (High) or `P3-DocumentEditor` (Medium).
- Portal: `Portal` plus `P1-Portal` (Highest) or `P2-Portal` (High).

Priority mappings preserve the previous priorities. The HTML report assigns each TestNG group as its own category, so module and priority can be filtered separately; Excel retains both group values. Test method names remain unchanged and globally unique. No annotations set retryAnalyzer, numeric priority or enabled=false.

The default `testng.xml` and compatibility `Master.xml` each include both classes inside `RetryMultipleFailedTestcases`. All 29 application tests are enabled, including formerly commented TC_01–TC_12 and TC_16. Focused suite commands remain valid. Tests run sequentially to protect the shared document from concurrent edits.

```bash
mvn clean test
mvn clean test -DsuiteXmlFile=Master.xml
mvn clean test -DsuiteXmlFile=DocumentEditor.xml
mvn clean test -DsuiteXmlFile=PortalNavigation.xml
mvn clean test -DsuiteXmlFile=FormattingTests.xml
mvn clean test -Dgroups=P1-Portal
```

Do not use the previous class-name or bare High/Highest group filters. Group includes are alternatives: to select only P1, omit the module-wide include. `pom.xml` now uses `testFailureIgnore=false`; failed tests fail Maven. Inspect skips and configuration failures as well.

## Local update

Preserve local config credentials before pulling and never commit them. After updating the branch, run `mvn clean test-compile` to remove stale classes from their old package paths. IDE users should refresh the project and reimport Maven, then use the updated suite files instead of saved launch configurations referring to old classes.

The default appURL remains a placeholder; set the sandbox login URL locally or pass `-DappURL=https://ia-portal-sandbox.innodata.com/login`. Username/password continue to come from local config.properties. Existing title/text/project/allocation properties are unchanged.

## Repeatable standards check

```bash
python3 scripts/check_framework_standards.py
```

This read-only check verifies package paths, feature class naming, unique enabled
test IDs/methods against the application catalog, the two groups, prohibited
annotation options, and suite class/method references. It complements compilation
and browser execution; it is not a SonarQube analysis.

## Saved-time expectation clarified during validation

The first full run executed all 29 cases: 28 passed and TC_04 failed because the
initial saved-time value was blank. The user confirmed that blank is valid before
any save. TC_04 now accepts that initial state and requires any nonblank value to
match the existing strict 12-hour saved-time pattern. It does not save the file to
create a timestamp. Its ID and automation method are retained; the catalog name,
objective and expected result reflect the clarified requirement.

## Final validation — 24 October 2026

| Check | Result |
| --- | --- |
| Clean compilation and final incremental compilation | Passed with Java 17 target. |
| `python3 scripts/check_framework_standards.py` | Passed: all 29 enabled cases match catalog IDs, use standard groups and unique methods; packages/classes/suite references verified. |
| Initial complete sandbox run | All 29 executed; 28 passed, TC_04 identified the initial blank saved-time requirement. |
| Final default suite through Maven Surefire (`mvn surefire:test` after compilation) | **29 passed, 0 failures, 0 errors, 0 skips**: 24 editor and 5 portal cases. |
| Final setup/cleanup configuration methods | No failures or skips. |
| HTML/Excel reports and group categories | Generated and checked. |
| Documentation links and whitespace checks | Passed. |
| SonarQube analysis | Not run; this validation does not claim a SonarQube Quality Gate result. |

The final run used the default `testng.xml` and sandbox credentials temporarily
provided in generated resources; source credentials remain blank and temporary
credentials were removed afterward. TC_04 passed with the permitted initial blank
state. A nonblank timestamp remains subject to the configured format assertion.
Local users can run the complete lifecycle with `mvn clean test` after configuring
their sandbox URL and account.
