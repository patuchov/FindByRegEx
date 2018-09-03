package inputclasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.patuch.custom.factory.internal.FindByTemplate;

public class NoRequiredArgsProvided {
    private WebDriver driver;

    @FindByTemplate(xpath = "//div/test_%s")
    private WebElement element;

    public NoRequiredArgsProvided(WebDriver driver) {
        this.driver = driver;
    }
}
