package org.patuch.custom.factory.api;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.patuch.custom.factory.api.inputclasses.MixOfFindBys;
import org.patuch.custom.factory.api.inputclasses.NoRequiredArgsProvided;
import org.patuch.custom.factory.api.inputclasses.NotEnoughArgs;
import org.patuch.custom.factory.api.inputclasses.SingleFindByTemplate;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class CustomPageFactoryTest {

    WebDriver driver;
    WebElement element;

    @Before
    public void setUp() {
        this.driver = mock(WebDriver.class);
    }

    @Test
    public void testSingleFindByTemplate() {
        SingleFindByTemplate singleFindByTemplate = CustomPageFactory.initElements(this.driver, SingleFindByTemplate.class, "1");
        element = singleFindByTemplate.getTestElement();
        String elementLocator = getElementLocator(element);

        assertEquals(elementLocator, "//div/row-id1");
    }

    @Test
    public void testTestMixOfFindBys() {
        MixOfFindBys mixOfFindBys = CustomPageFactory.initElements(this.driver, MixOfFindBys.class, "2");

        WebElement passwordInput = mixOfFindBys.getPasswordInput();
        WebElement usernameInput = mixOfFindBys.getUsernameInput();
        WebElement tableElement = mixOfFindBys.getTableElement();

        String passwordLocator = getElementLocator(passwordInput);
        String usernameLocator = getElementLocator(usernameInput);
        String tableRowLocator = getElementLocator(tableElement);

        assertEquals(passwordLocator, "password");
        assertEquals(usernameLocator, "usernameEdit");
        assertEquals(tableRowLocator, "//div/row_id2");
    }

    @Test(expected = RuntimeException.class)
    public void testNoRequiredArgsProvided() {
        NoRequiredArgsProvided noRequiredArgsProvided = CustomPageFactory.initElements(this.driver, NoRequiredArgsProvided.class);
    }

    @Test(expected = RuntimeException.class)
    public void testNotEnoughArgsProvided() {
        NotEnoughArgs notEnoughArgs = CustomPageFactory.initElements(this.driver, NotEnoughArgs.class, "1", "2");
    }

    private String getElementLocator(WebElement element) {
        try {
            Object proxyOrigin = FieldUtils.readField(element, "h", true);
            Object locator = FieldUtils.readField(proxyOrigin, "locator", true);
            Object findBy = FieldUtils.readField(locator, "by", true);
            return getLocatorString(findBy);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getLocatorString(Object findBy) {
        for (Field field : findBy.getClass().getDeclaredFields()) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }

            String fieldName = field.getName();
            try {
                switch (fieldName) {
                    case "id":
                        return FieldUtils.readField(findBy, "id", true).toString();
                    case "name":
                        return FieldUtils.readField(findBy, "name", true).toString();
                    case "xpathExpression":
                        return FieldUtils.readField(findBy, "xpathExpression", true).toString();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}