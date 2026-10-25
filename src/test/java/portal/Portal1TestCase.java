package portal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import a_testbase.BaseClass;
import d_loginpage.HomePage;

public class Portal1TestCase extends BaseClass {
    private static final Logger logger = LogManager.getLogger(Portal1TestCase.class);
    private static final String STARTING_TEST_MSG = "Starting test: {}";
    private static final String FINISHED_TEST_MSG = "Finished test: {} - PASSED";
    private static final String INITIALIZING_HOME_PAGE_MSG = "Initialising shared HomePage";
    private static final String CALLING_LOGIN_MSG = "Calling sandbox login";
    private static final String LOG_LOGIN_COMPLETE = "Sandbox login completed";
    private static final String SELECTING_PROJECT_MSG = "Selecting the configured project";
    private static final String OPENING_FILE_LIST_MSG = "Opening Authoring File List";
    private static final String FINDING_ALLOCATION_MSG = "Finding the configured document allocation";

    @Test(enabled = true, groups = { "Portal", "P1-Portal" }, description = "TC_20 | Verify login with the sandbox test account")
    public void verifySandboxLoginOpensPortal() {
        logger.info(STARTING_TEST_MSG, "verifySandboxLoginOpensPortal");
        logger.debug(INITIALIZING_HOME_PAGE_MSG);
        HomePage p = new HomePage(getDriver());
        logger.debug(CALLING_LOGIN_MSG);
        p.login();
        logger.debug(LOG_LOGIN_COMPLETE);
        logger.debug("Reading the observed portal result");
        boolean actual = p.isPortalDisplayed();
        logger.info("Observed portal result: {}", actual);
        logger.debug("Asserting the expected portal result");
        Assert.assertTrue(actual, "Expected portal screen should be displayed");
        logger.info(FINISHED_TEST_MSG, "verifySandboxLoginOpensPortal");
    }

    @Test(enabled = true, groups = { "Portal", "P2-Portal" }, description = "TC_21 | Verify selecting IATA from the project dropdown")
    public void verifySelectingIataProject() {
        logger.info(STARTING_TEST_MSG, "verifySelectingIataProject");
        logger.debug(INITIALIZING_HOME_PAGE_MSG);
        HomePage p = new HomePage(getDriver());
        logger.debug(CALLING_LOGIN_MSG);
        p.login();
        logger.debug(LOG_LOGIN_COMPLETE);
        logger.debug(SELECTING_PROJECT_MSG);
        p.selectProject(getConfig("portalProject"));
        logger.debug("Project selection completed");
        logger.debug("Reading the observed portal result");
        String actual = p.getSelectedProject();
        logger.info("Observed portal result: {}", actual);
        logger.debug("Asserting the expected portal result");
        Assert.assertEquals(actual, getConfig("portalProject"));
        logger.info(FINISHED_TEST_MSG, "verifySelectingIataProject");
    }

    @Test(enabled = true, groups = { "Portal", "P2-Portal" }, description = "TC_22 | Verify Authoring File List opens for IATA")
    public void verifyAuthoringFileListOpens() {
        logger.info(STARTING_TEST_MSG, "verifyAuthoringFileListOpens");
        logger.debug(INITIALIZING_HOME_PAGE_MSG);
        HomePage p = new HomePage(getDriver());
        logger.debug(CALLING_LOGIN_MSG);
        p.login();
        logger.debug(LOG_LOGIN_COMPLETE);
        logger.debug(SELECTING_PROJECT_MSG);
        p.selectProject(getConfig("portalProject"));
        logger.debug("Project selection completed");
        logger.debug(OPENING_FILE_LIST_MSG);
        p.openAuthoringFileList();
        logger.debug("Authoring File List opened");
        logger.debug("Reading the observed portal result");
        boolean actual = p.isFileListDisplayed();
        logger.info("Observed portal result: {}", actual);
        logger.debug("Asserting the expected portal result");
        Assert.assertTrue(actual, "Expected portal screen should be displayed");
        logger.info(FINISHED_TEST_MSG, "verifyAuthoringFileListOpens");
    }

    @Test(enabled = true, groups = { "Portal", "P2-Portal" }, description = "TC_23 | Verify the allocated document is listed")
    public void verifyTargetDocumentIsListed() {
        logger.info(STARTING_TEST_MSG, "verifyTargetDocumentIsListed");
        logger.debug(INITIALIZING_HOME_PAGE_MSG);
        HomePage p = new HomePage(getDriver());
        logger.debug(CALLING_LOGIN_MSG);
        p.login();
        logger.debug(LOG_LOGIN_COMPLETE);
        logger.debug(SELECTING_PROJECT_MSG);
        p.selectProject(getConfig("portalProject"));
        logger.debug("Project selection completed");
        logger.debug(OPENING_FILE_LIST_MSG);
        p.openAuthoringFileList();
        logger.debug("Authoring File List opened");
        logger.debug(FINDING_ALLOCATION_MSG);
        p.findTargetFile();
        logger.debug("Configured allocation found");
        logger.debug("Reading the observed portal result");
        String actual = p.getTargetFileTitle();
        logger.info("Observed portal result: {}", actual);
        logger.debug("Asserting the expected portal result");
        Assert.assertEquals(actual, getConfig("expectedDocumentTitle"));
        logger.info(FINISHED_TEST_MSG, "verifyTargetDocumentIsListed");
    }

    @Test(enabled = true, groups = { "Portal", "P1-Portal" }, description = "TC_24 | Verify Edit opens the allocated document editor")
    public void verifyEditOpensDocumentEditor() {
        logger.info(STARTING_TEST_MSG, "verifyEditOpensDocumentEditor");
        logger.debug(INITIALIZING_HOME_PAGE_MSG);
        HomePage p = new HomePage(getDriver());
        logger.debug(CALLING_LOGIN_MSG);
        p.login();
        logger.debug(LOG_LOGIN_COMPLETE);
        logger.debug(SELECTING_PROJECT_MSG);
        p.selectProject(getConfig("portalProject"));
        logger.debug("Project selection completed");
        logger.debug(OPENING_FILE_LIST_MSG);
        p.openAuthoringFileList();
        logger.debug("Authoring File List opened");
        logger.debug(FINDING_ALLOCATION_MSG);
        p.findTargetFile();
        logger.debug("Configured allocation found");
        logger.debug("Clicking allocation Edit and waiting for the editor");
        documenteditor.DocumentEditorPageObjects1 editor = p.openTargetFile();
        logger.debug("Allocation editor opened");
        logger.debug("Reading the observed portal result");
        String actual = editor.getDocumentTitle();
        logger.info("Observed portal result: {}", actual);
        logger.debug("Asserting the expected portal result");
        Assert.assertEquals(actual, getConfig("expectedDocumentTitle"));
        logger.info(FINISHED_TEST_MSG, "verifyEditOpensDocumentEditor");
    }

}
