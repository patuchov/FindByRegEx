package org.patuch.custom.factory.internal;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CustomLocatingListHandler implements InvocationHandler {
  private final ElementLocator locator;
  private final Class<?> wrappingType;

  public <T> CustomLocatingListHandler(Class<T> ifaceImplementation, ElementLocator locator) {
    this.locator = locator;
    if (!ifaceImplementation.isAssignableFrom(ifaceImplementation)) {
      throw new RuntimeException("Interface is not assignable to " + ifaceImplementation.getSimpleName() + ".");
    }

    this.wrappingType = ifaceImplementation;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    List<Object> elements = new ArrayList<>();
    Constructor<?> constructor = wrappingType.getConstructor(WebElement.class);

    for(WebElement element : locator.findElements()) {
      Object obj = constructor.newInstance(element);
      elements.add(wrappingType.cast(obj));
    }
    try {
      return method.invoke(elements, args);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}
