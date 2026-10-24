package portal;

import a_testbase.BaseClass;
import d_loginpage.HomePage;
import org.testng.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class Portal1TestCase extends BaseClass {
    private static final Logger LOGGER = LogManager.getLogger(Portal1TestCase.class);
    private static final String STARTING_TEST_MSG = "Starting test: {}";
    private static final String FINISHED_TEST_MSG = "Finished test: {} - PASSED";

    private static final String MODULE_GROUP = "Portal";
    private static final String HIGH_PRIORITY_GROUP = "P2-Portal";
    private static final String PROJECT_CONFIG = "portalProject";

    @Test(groups = { MODULE_GROUP, "P1-Portal" }, description = "TC_20 | Verify login with the sandbox test account")
    public void verifySandboxLoginOpensPortal() {
        LOGGER.info(STARTING_TEST_MSG, "verifySandboxLoginOpensPortal");
        LOGGER.debug("Reading observed result: Authenticated portal displayed");
        boolean actualResult = homePage().login().isPortalDisplayed();
        LOGGER.info("Authenticated portal displayed: {}", actualResult);
        LOGGER.debug("Asserting expected result: Authenticated portal displayed");
        Assert.assertTrue(actualResult,
                "Successful login should display the authenticated portal project selector");
        LOGGER.info(FINISHED_TEST_MSG, "verifySandboxLoginOpensPortal");
    }

    private HomePage homePage() {
        LOGGER.debug("Initialising shared portal page object");
        return new HomePage(getDriver());
    }

    private HomePage portal() {
        return homePage().login();
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_21 | Verify selecting IATA from the project dropdown")
    public void verifySelectingIataProject() {
        LOGGER.info(STARTING_TEST_MSG, "verifySelectingIataProject");
        HomePage page = portal().selectProject(getConfig(PROJECT_CONFIG));
        LOGGER.debug("Reading observed result: Selected project");
        String actualResult = page.getSelectedProject();
        LOGGER.info("Selected project: {}", actualResult);
        LOGGER.debug("Asserting expected result: Selected project");
        Assert.assertEquals(actualResult, getConfig(PROJECT_CONFIG),
                "The project dropdown should identify the selected IATA project");
        LOGGER.info(FINISHED_TEST_MSG, "verifySelectingIataProject");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_22 | Verify Authoring File List opens for IATA")
    public void verifyAuthoringFileListOpens() {
        LOGGER.info(STARTING_TEST_MSG, "verifyAuthoringFileListOpens");
        LOGGER.debug("Reading observed result: Authoring File List displayed");
        boolean actualResult = portal().selectProject(getConfig(PROJECT_CONFIG))
                .openAuthoringFileList().isFileListDisplayed();
        LOGGER.info("Authoring File List displayed: {}", actualResult);
        LOGGER.debug("Asserting expected result: Authoring File List displayed");
        Assert.assertTrue(actualResult,
                "The IATA Authoring File List should display its file table");
        LOGGER.info(FINISHED_TEST_MSG, "verifyAuthoringFileListOpens");
    }

    private HomePage fileList() {
        return homePage().login().selectProject(getConfig(PROJECT_CONFIG))
                .openAuthoringFileList().findTargetFile();
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_23 | Verify the allocated document is listed")
    public void verifyTargetDocumentIsListed() {
        LOGGER.info(STARTING_TEST_MSG, "verifyTargetDocumentIsListed");
        LOGGER.debug("Reading observed result: Allocated file title");
        String actualResult = fileList().getTargetFileTitle();
        LOGGER.info("Allocated file title: {}", actualResult);
        LOGGER.debug("Asserting expected result: Allocated file title");
        Assert.assertEquals(actualResult, getConfig("expectedDocumentTitle"),
                "The allocated file row should display the expected document title");
        LOGGER.info(FINISHED_TEST_MSG, "verifyTargetDocumentIsListed");
    }

    @Test(groups = { MODULE_GROUP, "P1-Portal" }, description = "TC_24 | Verify Edit opens the allocated document editor")
    public void verifyEditOpensDocumentEditor() {
        LOGGER.info(STARTING_TEST_MSG, "verifyEditOpensDocumentEditor");
        LOGGER.debug("Reading observed result: Opened editor title");
        String actualResult = fileList().openTargetFile().getDocumentTitle();
        LOGGER.info("Opened editor title: {}", actualResult);
        LOGGER.debug("Asserting expected result: Opened editor title");
        Assert.assertEquals(actualResult, getConfig("expectedDocumentTitle"),
                "The Edit action should open the editor for the allocated document");
        LOGGER.info(FINISHED_TEST_MSG, "verifyEditOpensDocumentEditor");
    }

}
