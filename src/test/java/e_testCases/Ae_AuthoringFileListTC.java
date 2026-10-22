package e_testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import a_testBase.BaseClass;
import d_pageObjects.Ac_LoginPage;
import d_pageObjects.Ae_AuthoringFileListPage;

public class Ae_AuthoringFileListTC extends BaseClass {
    private Ae_AuthoringFileListPage fileList() {
        return new Ac_LoginPage(getDriver()).login().selectProject(getConfig("portalProject"))
                .openAuthoringFileList().findTargetFile();
    }

    @Test(groups = { "Ae_AuthoringFileListTC", "High" }, description = "TC_23 | Verify the allocated document is listed")
    public void verifyTargetDocumentIsListed() {
        Assert.assertEquals(fileList().getTargetFileTitle(), getConfig("expectedDocumentTitle"),
                "The allocated file row should display the expected document title");
    }

    @Test(groups = { "Ae_AuthoringFileListTC", "Highest" }, description = "TC_24 | Verify Edit opens the allocated document editor")
    public void verifyEditOpensDocumentEditor() {
        Assert.assertEquals(fileList().openTargetFile().getDocumentTitle(), getConfig("expectedDocumentTitle"),
                "The Edit action should open the editor for the allocated document");
    }
}
