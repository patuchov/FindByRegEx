package org.patuch.custom.factory.api.inputclasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.patuch.custom.factory.internal.FindByTemplate;

public class SingleFindByTemplate {
    WebDriver driver;

    @FindByTemplate(xpath = "//div/row-id%s")
    WebElement testElement;

    public SingleFindByTemplate(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getTestElement() {
        return this.testElement;
    }
}
