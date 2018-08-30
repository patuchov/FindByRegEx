package org.patuch.custom.factory.internal;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomLocatingElementHandler implements InvocationHandler {
  private final ElementLocator locator;
  private final Class<?> customInterface;

  public CustomLocatingElementHandler(ElementLocator locator, Class<?> customInterface) {
    this.locator = locator;
    this.customInterface = customInterface;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    WebElement element;

    try {
      element = locator.findElement();
    } catch (NoSuchElementException e) {
      if ("toString".equals(method.getName())) {
        return "Proxy element for: " + locator.toString();
      }

      throw e;
    }

    if ("getWrappedElement".equals(method.getName())) {
      return element;
    }

    try {
      return method.invoke(element, args);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}
