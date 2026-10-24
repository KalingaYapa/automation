package portal;

import a_testbase.BaseClass;
import d_loginpage.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Portal1TestCase extends BaseClass {
    private static final String MODULE_GROUP = "Portal";
    private static final String HIGH_PRIORITY_GROUP = "P2-Portal";
    private static final String PROJECT_CONFIG = "portalProject";

    @Test(groups = { MODULE_GROUP, "P1-Portal" }, description = "TC_20 | Verify login with the sandbox test account")
    public void verifySandboxLoginOpensPortal() {
        Assert.assertTrue(new HomePage(getDriver()).login().isPortalDisplayed(),
                "Successful login should display the authenticated portal project selector");
    }

    private HomePage portal() {
        return new HomePage(getDriver()).login();
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_21 | Verify selecting IATA from the project dropdown")
    public void verifySelectingIataProject() {
        HomePage page = portal().selectProject(getConfig(PROJECT_CONFIG));
        Assert.assertEquals(page.getSelectedProject(), getConfig(PROJECT_CONFIG),
                "The project dropdown should identify the selected IATA project");
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_22 | Verify Authoring File List opens for IATA")
    public void verifyAuthoringFileListOpens() {
        Assert.assertTrue(portal().selectProject(getConfig(PROJECT_CONFIG))
                .openAuthoringFileList().isFileListDisplayed(),
                "The IATA Authoring File List should display its file table");
    }

    private HomePage fileList() {
        return new HomePage(getDriver()).login().selectProject(getConfig(PROJECT_CONFIG))
                .openAuthoringFileList().findTargetFile();
    }

    @Test(groups = { MODULE_GROUP, HIGH_PRIORITY_GROUP }, description = "TC_23 | Verify the allocated document is listed")
    public void verifyTargetDocumentIsListed() {
        Assert.assertEquals(fileList().getTargetFileTitle(), getConfig("expectedDocumentTitle"),
                "The allocated file row should display the expected document title");
    }

    @Test(groups = { MODULE_GROUP, "P1-Portal" }, description = "TC_24 | Verify Edit opens the allocated document editor")
    public void verifyEditOpensDocumentEditor() {
        Assert.assertEquals(fileList().openTargetFile().getDocumentTitle(), getConfig("expectedDocumentTitle"),
                "The Edit action should open the editor for the allocated document");
    }

}
