package org.patuch.custom.factory.internal;

import org.openqa.selenium.By;

import java.lang.reflect.Field;

public abstract class AbstractFindByTemplateBuilder {
  public abstract By buildIt(Object annotation, Field field);

  protected By buildByFromFindByTemplate(FindByTemplate findByRegEx) {
    By ans = buildByFromShortFindByRegEx(findByRegEx);

    return ans;
  }

  protected By buildByFromShortFindByRegEx(FindByTemplate findByRegEx) {

    if (!"".equals(findByRegEx.css())) {
      return By.cssSelector(findByRegEx.css());
    }

    if (!"".equals(findByRegEx.xpath())) {
      return By.xpath(findByRegEx.xpath());
    }

    // Fall through
    return null;
  }

}
