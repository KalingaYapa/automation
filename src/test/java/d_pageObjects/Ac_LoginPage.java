package d_pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import c_basePage.BasePage;

/** Two-stage portal login. Credentials are read only from the process environment. */
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

    private String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Set " + name + " securely in the test process environment");
        }
        return value;
    }

    public Ad_PortalPage login() {
        String username = requiredEnvironment("SANDBOX_USERNAME");
        String secret = requiredEnvironment("SANDBOX_PASSWORD");
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
