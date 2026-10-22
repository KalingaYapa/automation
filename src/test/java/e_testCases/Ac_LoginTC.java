package e_testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import a_testBase.BaseClass;
import d_pageObjects.Ac_LoginPage;

public class Ac_LoginTC extends BaseClass {
    @Test(groups = { "Ac_LoginTC", "Highest" }, description = "TC_20 | Verify login with the sandbox test account")
    public void verifySandboxLoginOpensPortal() {
        Assert.assertTrue(new Ac_LoginPage(getDriver()).login().isPortalDisplayed(),
                "Successful login should display the authenticated portal project selector");
    }
}
