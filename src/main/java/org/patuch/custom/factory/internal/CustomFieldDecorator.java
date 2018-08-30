package org.patuch.custom.factory.internal;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import java.lang.reflect.*;
import java.util.List;

public class CustomFieldDecorator implements FieldDecorator {

  protected CustomElementLocatorFactory factory;

  public CustomFieldDecorator(CustomElementLocatorFactory factory, Class<?> customInterface) {
    this.factory = factory;
  }

  @Override
  public Object decorate(ClassLoader loader, Field field) {
    if (!(WebElement.class.isAssignableFrom(field.getType()) ||
      isDecoratableList(field))) {
      return null;
    }

    ElementLocator locator = factory.createLocator(field);

    if (locator == null) {
      return null;
    }

    Class<?> customInterface = HelperFunctions.getBaseInterface(field.getType());

    if (WebElement.class.isAssignableFrom(field.getType())) {
      return proxyForLocator(loader, customInterface, locator);
    } else if (List.class.isAssignableFrom(field.getType())) {
      return proxyForListLocator(loader, customInterface, locator);
    } else {
      return null;
    }
  }

  protected Object proxyForLocator(ClassLoader loader, Class<?> customInterface, ElementLocator locator) {
    InvocationHandler handler = new CustomLocatingElementHandler(locator, customInterface);

    Object proxy = getNewProxy(loader, customInterface, handler);

    return proxy;
  }

  protected List<Object> proxyForListLocator(ClassLoader loader, Class<?> customInterface, ElementLocator locator) {
    InvocationHandler handler = new CustomLocatingListHandler(customInterface, locator);

    List<Object> proxy = (List<Object>) Proxy.newProxyInstance(
      loader, new Class[]{List.class}, handler);

    return proxy;
  }

  private Object getNewProxy(ClassLoader loader, Class<?> customInterface, InvocationHandler handler) {
    if ("WebElement".equals(customInterface.getSimpleName())) {
      return Proxy.newProxyInstance(loader, new Class[] {WebElement.class, WrapsElement.class, Locatable.class}, handler);
    }

    return Proxy.newProxyInstance(loader, new Class[] {customInterface, WebElement.class, WrapsElement.class, Locatable.class}, handler);
  }

  private boolean isDecoratableList(Field field) {
    if (!List.class.isAssignableFrom(field.getType())) {
      return false;
    }

    Type genericType = field.getGenericType();

    if (!(genericType instanceof ParameterizedType)) {
      return false;
    }

    Type listType = ((ParameterizedType) genericType).getActualTypeArguments()[0];

    if (!WebElement.class.equals(listType)) {
      return false;
    }

    if (field.getAnnotation(FindByRegEx.class) == null) {
      return false;
    }

    return true;
  }
}
