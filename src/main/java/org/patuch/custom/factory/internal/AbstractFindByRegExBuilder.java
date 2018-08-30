package org.patuch.custom.factory.internal;

import org.openqa.selenium.By;

import java.lang.reflect.Field;

public abstract class AbstractFindByRegExBuilder {
  public abstract By buildIt(Object annotation, Field field);

  protected By buildByFromFindByRegEx(FindByRegEx findByRegEx) {
    By ans = buildByFromShortFindByRegEx(findByRegEx);

    return ans;
  }

  protected By buildByFromShortFindByRegEx(FindByRegEx findByRegEx) {

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
