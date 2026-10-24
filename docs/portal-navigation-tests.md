# Portal navigation tests

Five enabled tests are in `portal.Portal1TestCase`. They reuse `d_loginpage.HomePage` for login, project selection and file-list navigation; no separate login page or duplicate navigation implementation remains.

| ID | Method | Expected result |
| --- | --- | --- |
| TC_20 | verifySandboxLoginOpensPortal | Configured test account logs in and the project selector is displayed. |
| TC_21 | verifySelectingIataProject | The configured IATA project is selected. |
| TC_22 | verifyAuthoringFileListOpens | Authoring File List displays its file table. |
| TC_23 | verifyTargetDocumentIsListed | The configured allocation row has the expected document title. |
| TC_24 | verifyEditOpensDocumentEditor | Native Edit opens the intended allocation and waits for its editor to be ready. |

Every test has a fresh browser and performs its own prerequisites. `HomePage` uses the native project dropdown, file-name column filter plus Enter, and the allocation-specific edit path to distinguish files with identical titles. It handles the editor window opened by Edit. Portal tests do not edit or save document content.

## Configuration

Set local `username`, `password` and `appURL` in `src/test/resources/config.properties`. Credentials are read from that file, not SANDBOX_USERNAME/SANDBOX_PASSWORD. Keep them private and out of Git. Set `portalProject=IATA`, `portalFileSearch`, `editorFileEditPath`, `expectedDocumentTitle` and the editor readiness text to match the sandbox allocation.

URL precedence is JVM `-DappURL`, then SANDBOX_URL environment variable, then config appURL. The committed appURL remains a placeholder, so configure it before running.

```bash
mvn clean test -DsuiteXmlFile=PortalNavigation.xml -DappURL=https://ia-portal-sandbox.innodata.com/login
```

This suite runs all five portal cases sequentially. The default `mvn clean test` runs all 29 application cases. Portal groups are `Portal` plus `P1-Portal` for Highest or `P2-Portal` for High.

Inspect the TestNG counts and setup errors, `reports/ExtentReport.html`, `reports/ExcelReport.xlsx` and `target/surefire-reports/`. Maven now fails for test failures. A skipped test does not count as a pass.

See [the catalog](test-case-catalog.md), [terminal guide](run-formatting-tests-terminal.md) and [restructuring notes](framework-restructuring.md).
