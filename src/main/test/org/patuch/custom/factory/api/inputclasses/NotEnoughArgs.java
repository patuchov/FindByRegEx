package org.patuch.custom.factory.api.inputclasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.patuch.custom.factory.internal.FindByTemplate;

public class NotEnoughArgs {
    private WebDriver driver;

    @FindByTemplate(xpath = "//div/row_id%s")
    private WebElement element;

    @FindByTemplate(xpath = "//div/row_id%s")
    private WebElement element1;

    @FindByTemplate(xpath = "//div/row_id%s")
    private WebElement element2;

    public NotEnoughArgs(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getElement() {
        return element;
    }

    public WebElement getElement1() {
        return element1;
    }
}
