# Hybrid Automation Framework — starter project

Selenium + Java + TestNG + Maven, Page Object Model. Clone it, point it at an application, and
start writing tests. Everything in it is a template: there is no application-specific code.

**Hybrid** means three things are combined: the **Page Object Model** (one class per screen), a
**data layer** (config.properties and Excel, never values hard-coded in a test), and **modular
utilities** (reports, retries, mail) shared by every test.

## Layers

```
Master.xml                        the suite: which classes run, and 4 browsers in parallel
pom.xml                           dependencies and the Surefire run
src/test/java/
├── a_testBase/BaseClass.java     browser per thread, config, opens the app, closes the browser
├── b_utilities/                  shared services, no test logic
│   ├── AnnotationTransfer.java   attaches the retry analyzer to every @Test
│   ├── RetryAnalyzer.java        re-runs a failed test (0 by default)
│   ├── ExtentReportManager.java  the TestNG listener: HTML report, Excel report, screenshots
│   ├── ExcelUtility.java         read and write .xlsx test data
│   ├── ReusableMethods.java      small shared checks used by the tests
│   ├── MonitoringMail.java       emails the reports when email=yes
│   └── TestConfig.java           mail server and recipients
├── c_basePage/BasePage.java      waits, clicks, typing, dropdowns, presence checks
├── d_pageObjects/                one class per screen  (Aa_SamplePage — template)
└── e_testCases/                  one class per screen  (Aa_SampleTC — template)
src/test/resources/
├── config.properties             URL, credentials, browser, waits, test data
└── log4j2.xml                    logging
```

Generated at run time, none of it committed: `reports/`, `screenshots/`, `logs/`, `target/`.

## The rules

1. **A test never touches WebDriver and never names a locator.** Locators live in the page object.
   If a test reads `driver.findElement(...)`, it belongs in the page object instead.
2. **One page object per screen, one test class per screen**, named in pairs:
   `Ab_OrdersPage` ↔ `Ab_OrdersTC`. The letter prefix keeps the pair together.
3. **A test class extends `BaseClass`** and builds its page object inside each test method, never in
   a field — the suite runs methods in parallel.
4. **Two groups per test**: the class name and the priority from the manual sheet (`Highest`,
   `High`, `Medium`, `Low`). No `priority=` attribute, no smoke / sanity / regression groups.
5. **The description carries the manual test case ID**: `"TC_01 | <title from the sheet>"`. Both
   reports key on it.
6. **The method name is the Automation ID** written back into the manual sheet, so the sheet and
   the results reconcile with a straight lookup. Use plain names: `verifyOrderListIsDisplayed()`.
7. **One action and one assertion per test**, with a message saying what was expected.
8. **Nothing that varies is written into a test.** URLs, credentials, search values and timeouts
   live in `config.properties`; bulk data goes in an .xlsx read through `ExcelUtility`.
9. **No `Thread.sleep` in a test.** Use the waits in `BasePage`; `sleepFor` exists only for places
   where the application gives no signal to wait on.

## Adding a screen

1. **Page object** in `d_pageObjects`: `Xx_<Screen>Page extends BasePage`.
   Declare fixed locators as `@FindBy` fields, build row- or label-dependent ones as `By` inside the
   method. Expose one method per action and one per check. Put the screen's fixed text in constants.
2. **Test class** in `e_testCases`: `Xx_<Screen>TC extends BaseClass`.
   Add a small private helper that opens the screen and returns the page object, then one `@Test`
   per manual test case with the groups and description above.
3. **Suite**: add one `<include name="Xx_<Screen>TC" />` line to `Master.xml`.
4. **Config**: add a key for any value the tests need.
5. **Manual sheet**: write the method name into the Automation ID column.

`Aa_SamplePage` and `Aa_SampleTC` are the templates for steps 1 and 2. Copy them for the first real
screen, then delete them along with their `<include>` line.

## Running

```bash
mvn clean test                            # everything, 4 browsers in parallel
mvn clean test -Dgroups=Aa_SampleTC       # one class
mvn clean test -Dgroups=Highest           # one priority
mvn clean test -DsuiteXmlFile=Smoke.xml   # another suite file
```

In Eclipse: right-click `Master.xml` → Run As → TestNG Suite. Import the project with
File → Import → Existing Maven Projects.

Before the first run, set `appURL` in `src/test/resources/config.properties`, and `username` /
`password` if the application needs a login. They are blank on purpose — fill them in locally and
never commit them.

Browser and headless mode come from the same file. Selenium Manager fetches the browser driver, so
there is nothing to download by hand.

### Parallel runs

`Master.xml` uses `parallel="methods" thread-count="4"`: four test methods at a time, each with its
own browser and its own login. Raise or lower `thread-count` to suit the machine and the
application. Because the methods share nothing, a test must not depend on another test having run.

## Reports

| Where | What |
|---|---|
| `reports/ExtentReport.html` | one entry per test, grouped by class and priority, screenshot on failure |
| `reports/ExcelReport.xlsx` | Summary sheet plus one row per test case: ID, title, `Class.method`, status, duration, failure reason |
| `screenshots/` | one PNG per failure |
| `logs/Automation.log` | run log |

Set `email=yes` in `config.properties` and fill in `b_utilities/TestConfig.java` to have both
reports emailed when the suite finishes.

## Retries

`RetryAnalyzer.MAX_RETRY_LIMIT` is `0`, so every test runs once and the reports hold one row per
test case. Set it to `1` to give flaky tests a second chance, and read the reports knowing a
retried test appears twice.
