package org.patuch.custom.factory.internal;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.How;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelperFunctions {

  private static final List<String> REQUIRED_INTERFACES = new ArrayList<String>(
    Arrays.asList("WebElement", "SearchContext", "TakesScreenshot", "WrapsElement", "Locatable"));

  public static By getLocator(FindByTemplate findByRegEx, Field field, String[] args) {
    if (isFieldSet(findByRegEx.css())) {
      return createLocatorFromTemplate(findByRegEx.css(), How.CSS, field, args);
    } else if (isFieldSet(findByRegEx.xpath())) {
      return createLocatorFromTemplate(findByRegEx.xpath(), How.XPATH, field, args);
    }

    return buildByFromDefault(field);
  }

  public static int getNumberOfRequiredParams(FindByTemplate findByRegEx) {
    if (isFieldSet(findByRegEx.css())) {
      return StringUtils.countMatches(findByRegEx.css(), "%");
    } else if (isFieldSet(findByRegEx.xpath())) {
      return StringUtils.countMatches(findByRegEx.xpath(), "%");
    }

    return 0;
  }

  public static Class<?> getBaseInterface(Class<?> clazz) {
    return getBaseIface(clazz);
  }

  public static Class<?> getBaseImplementation(Class<?> clazz, Class<?> iface) {
    if ("WebElement".equals(iface.getSimpleName())) {
      return iface;
    }

    if (isImplementingInterface(clazz, iface) && isTopClass(clazz.getSuperclass())) {
      return clazz;
    }

    return getBaseImplementation(clazz.getSuperclass(), iface);
  }

  private static boolean isTopClass(Class<?> clazz) {
    System.out.println("Super clazz " + clazz.getSimpleName());
    if ("Object".equals(clazz.getSimpleName())) {
      return true;
    }

    return false;
  }

  private static boolean isImplementingInterface(Class<?> clazz, Class<?> expectedInterface) {
    if (expectedInterface.isInterface() && isDirectDescendant(expectedInterface)) {
      if (ClassUtils.getAllInterfaces(clazz).get(0).equals(expectedInterface)) {
        return true;
      }

      return false;
    }

    return false;
  }

  private static boolean isDirectDescendant(Class<?> clazz) {
    List<Class<?>> interfaces = ClassUtils.getAllInterfaces(clazz);
    if (REQUIRED_INTERFACES.containsAll(getIfacesNames(interfaces))) {
      return true;
    }

    return false;
  }

  private static List<String> getIfacesNames(List<Class<?>> implementedInterfaces) {
    List<String> ifaceNames = new ArrayList<>();

    for (Class<?> iface : implementedInterfaces) {
      ifaceNames.add(iface.getSimpleName());
    }

    return ifaceNames;
  }

  private static Class<?> getBaseIface(Class<?> clazz) {
    if (ClassUtils.getAllInterfaces(clazz).size() == 0) {
      return WebElement.class;
    }

    if (clazz.isInterface() && isDirectDescendant(clazz)) {
      return clazz;
    }

    return getBaseIface(ClassUtils.getAllInterfaces(clazz).get(0));
  }

  private static By createLocatorFromTemplate(String template, How how,
                                              Field field, String[] args) {
    String locatorStr = String.format(template, args);

    switch (how) {
      case CSS:
        return By.cssSelector(locatorStr);
      case XPATH:
        return By.xpath(locatorStr);
    }

    return buildByFromDefault(field);
  }

  private static boolean isFieldSet(String field) {
    if (!"".equals(field)) {
      return true;
    }

    return false;
  }

  private static By buildByFromDefault(Field field) {
    return new ByIdOrName(field.getName());
  }

}
