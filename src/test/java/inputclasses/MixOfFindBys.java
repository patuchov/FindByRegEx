package inputclasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.patuch.custom.factory.internal.FindByTemplate;

public class MixOfFindBys {
    private WebDriver driver;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(name = "usernameEdit")
    private WebElement usernameInput;

    @FindByTemplate(xpath = "//div/row_id%s")
    private WebElement tableElement;

    public MixOfFindBys(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getPasswordInput() {
        return passwordInput;
    }

    public WebElement getUsernameInput() {
        return usernameInput;
    }

    public WebElement getTableElement() {
        return tableElement;
    }

}
