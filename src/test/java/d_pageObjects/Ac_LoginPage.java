package d_pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import a_testBase.BaseClass;
import c_basePage.BasePage;

/** Two-stage portal login. Credentials are read through the framework configuration. */
public class Ac_LoginPage extends BasePage {
    @FindBy(id = "email")
    private WebElement email;
    @FindBy(css = "input[type='password']")
    private WebElement password;
    @FindBy(css = "form button[type='submit']")
    private WebElement submit;

    public Ac_LoginPage(WebDriver driver) {
        super(driver);
    }

    private String requiredConfig(String name) {
        String value = BaseClass.getConfig(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Set " + name + " in your local src/test/resources/config.properties");
        }
        return value;
    }

    public Ad_PortalPage login() {
        String username = requiredConfig("username");
        String secret = requiredConfig("password");
        try {
            enterText(email, username);
            clickElement(submit);
            enterText(password, secret);
            clickElement(submit);
        } catch (WebDriverException exception) {
            // Selenium command diagnostics can contain typed input; do not expose the cause.
            throw new IllegalStateException("Portal login interaction failed ("
                    + exception.getClass().getSimpleName() + ")");
        }
        Ad_PortalPage portal = new Ad_PortalPage(getDriver);
        portal.waitForPortal();
        return portal;
    }
}
