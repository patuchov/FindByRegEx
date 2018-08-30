package org.patuch.custom.factory.api;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.patuch.custom.factory.internal.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class CustomPageFactory {

  public static <T> T initElements(WebDriver driver, Class<T> pageClassToProxy, String... args) {
    T page = instantiatePage(driver, pageClassToProxy);
    initElements(driver, page, args);
    return page;
  }

  public static void initElements(WebDriver driver, Object page, String... args) {
    final WebDriver driverRef = driver;
    initElements(new CustomElementLocatorFactory(driverRef, args), page);
  }
  private static void initElements(CustomElementLocatorFactory factory, Object page) {
    final CustomElementLocatorFactory factoryRef = factory;
    Class<?> baseInterface = HelperFunctions.getBaseInterface(page.getClass());
    initElements(new CustomFieldDecorator(factoryRef, baseInterface), page);
  }

  private static void initElements(FieldDecorator decorator, Object page) {
    Class<?> proxyIn = page.getClass();

    while (proxyIn != Object.class) {
      proxyFields(decorator, page, proxyIn);
      proxyIn = proxyIn.getSuperclass();
    }
  }

  private static void proxyFields(FieldDecorator decorator, Object page, Class<?> proxyIn) {
    Field[] fields = proxyIn.getDeclaredFields();

    CustomAnnotation.resetCurrentPos();

    for (Field field : fields) {
      Object value = decorator.decorate(page.getClass().getClassLoader(), field);

      if (value != null) {
        try {
          field.setAccessible(true);
          field.set(page, value);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  private static <T> T instantiatePage(WebDriver driver, Class<T> pageClassToProxy) {
    try {
      try {
        Constructor<T> constructor = pageClassToProxy.getConstructor(WebDriver.class);
        return constructor.newInstance(driver);
      } catch (NoSuchMethodException e) {
        return pageClassToProxy.getDeclaredConstructor().newInstance();
      }
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  private static boolean isAnnotated(Field field) {
    if (field.getAnnotation(FindByRegEx.class) == null) {
      return false;
    }

    return true;
  }
}
