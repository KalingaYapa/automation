# Portal login and editor navigation

`PortalNavigation.xml` runs five independent tests. Each test starts a new browser
through `BaseClass`, completes its own prerequisites and closes all browser windows
through the existing teardown. Tests do not depend on a previous test's login or
project selection. The suite runs sequentially to avoid several account sessions
opening the same allocated file simultaneously.

| ID | Test method | Expected result |
| --- | --- | --- |
| TC_20 | Ac_LoginTC.verifySandboxLoginOpensPortal | Two-stage login opens the authenticated portal |
| TC_21 | Ad_PortalTC.verifySelectingIataProject | Native project dropdown selection displays IATA |
| TC_22 | Ad_PortalTC.verifyAuthoringFileListOpens | IATA Authoring File List and its table are visible |
| TC_23 | Ae_AuthoringFileListTC.verifyTargetDocumentIsListed | The configured allocation row has the expected file title |
| TC_24 | Ae_AuthoringFileListTC.verifyEditOpensDocumentEditor | Clicking that row's Edit link opens the correct editor title |

## Framework structure

- `Ac_LoginPage`: email step, Continue, password step, and portal readiness.
- `Ad_PortalPage`: project dropdown, project selection, and left-menu navigation.
- `Ae_AuthoringFileListPage`: DataTables file-name filter, allocation identification,
  and Edit navigation, including switching to the newly opened editor window.
- Matching test classes in `e_testCases` contain the assertions. Each test has a
  class group and a business-priority group, a manual ID in its description, and no
  locators, direct WebDriver operations, shared page-object fields or fixed sleeps.
- The editor title is read through the existing `Ab_DocumentEditorPage`.

The suite opens the allocated file but does not edit, format, save, reset, check out,
or check in a document. It does not run the existing editing tests.

## Credentials and target data

Provide these variables securely in the process that starts Maven or TestNG:

- `SANDBOX_URL`: the portal/login URL.
- `SANDBOX_USERNAME`: the sandbox account's email.
- `SANDBOX_PASSWORD`: the sandbox account's password.

In Eclipse, use Run Configurations > TestNG > Environment. Do not paste secrets in
Java, config.properties, suite XML, terminal commands or committed launch files.
Login errors deliberately omit Selenium exception causes that might contain typed
credentials. This flow assumes the verified email/Continue/password form; SSO/MFA
requires a supported separate flow.

BaseClass URL resolution is: explicit `-DappURL`, then `SANDBOX_URL`, then the existing
`appURL` property. The generic configuration reader now also supports Java system
property overrides. The default template appURL has not been changed. Set the portal
URL for this suite, not an already-open personal browser session.

Non-secret data is in config.properties:

```properties
portalProject=IATA
portalFileSearch=Transmit Flight and Shipment Information
expectedDocumentTitle=11.1 - Transmit Flight and Shipment Information to Down-line Stations & Authorities
editorFileEditPath=/projects/IATA/authoring/order/4/file-allocation/33/edit
```

Several allocations have the same document title. The file-list page object locates
the allocation by the configured edit path and verifies its title, instead of
clicking the first filename match. File-name filtering uses a short search phrase
because the application limits that filter to 75 characters. The verified table's
file-name column has index 5. The filter is applied with Enter.

## Run

Compile:

```bash
mvn test-compile
```

Run only the five navigation tests:

```bash
mvn clean test -DsuiteXmlFile=PortalNavigation.xml
```

Or right-click `PortalNavigation.xml` in Eclipse and run as a TestNG suite.
Do not use the sample Master.xml for this flow. Inspect the current
`target/surefire-reports/testng-results.xml`, `reports/ExtentReport.html` and
`reports/ExcelReport.xlsx`; the existing pom.xml ignores test failures, so a Maven
BUILD SUCCESS alone is not a passing result. Expect five executed tests and no
failures or skips.

For the prepared cloud environment, activate `/workspace/.onboarding/env.sh` first.
Optional JVM properties let the same BaseClass support the isolated Linux browser:

```bash
mvn test -DsuiteXmlFile=PortalNavigation.xml -Dheadless=yes \
  -DchromeBinary=/workspace/.onboarding/tools/debian/usr/lib/chromium/chromium \
  -DchromeNoSandbox=true -DchromeDisableDevShmUsage=true
```

`chromeNoSandbox` is an explicit opt-in for the isolated container, not a desktop
requirement. Certificate verification stays enabled. None of these properties are
needed for an ordinary supported local Chrome installation. The existing cloud
Maven wrapper provides the driver path; local Selenium Manager can resolve a local
driver normally.

## Scope of existing editor tests

These navigation tests do not automatically log the older editor test class in.
That class still requires an authenticated editor setup. This change adds the
requested navigation coverage and reusable page objects, without automatically
running document-changing cases such as Bold or Enter.

## Validation

On 2026-10-22, compilation passed and the committed `PortalNavigation.xml` suite
was executed against the live sandbox using the real BaseClass, TestNG tests,
page objects and existing report listeners: **5 tests passed, 0 failed, 0 skipped**.
The current run produced both the HTML and Excel reports. Tests used fresh browser
sessions, native project-dropdown selection, the table's file-name filter, and the
correct row's Edit link. No document content was changed and no Save, Reset,
Check Out, or Check In action was performed. Other suites were not part of this run.
