package e_testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import a_testBase.BaseClass;
import d_pageObjects.Ac_LoginPage;
import d_pageObjects.Ad_PortalPage;

public class Ad_PortalTC extends BaseClass {
    private Ad_PortalPage portal() {
        return new Ac_LoginPage(getDriver()).login();
    }

    @Test(groups = { "Ad_PortalTC", "High" }, description = "TC_21 | Verify selecting IATA from the project dropdown")
    public void verifySelectingIataProject() {
        Ad_PortalPage page = portal().selectProject(getConfig("portalProject"));
        Assert.assertEquals(page.getSelectedProject(), getConfig("portalProject"),
                "The project dropdown should identify the selected IATA project");
    }

    @Test(groups = { "Ad_PortalTC", "High" }, description = "TC_22 | Verify Authoring File List opens for IATA")
    public void verifyAuthoringFileListOpens() {
        Assert.assertTrue(portal().selectProject(getConfig("portalProject"))
                .openAuthoringFileList().isFileListDisplayed(),
                "The IATA Authoring File List should display its file table");
    }
}
