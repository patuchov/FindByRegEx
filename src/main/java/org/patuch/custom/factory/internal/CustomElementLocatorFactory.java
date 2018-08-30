package org.patuch.custom.factory.internal;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;

public class CustomElementLocatorFactory implements ElementLocatorFactory {

  private final SearchContext searchContext;
  private final String[] args;

  public CustomElementLocatorFactory(SearchContext searchContext, String... args) {
    this.searchContext = searchContext;
    this.args = args;
  }

  @Override
  public ElementLocator createLocator(Field field) {
    return new CustomElementLocator(searchContext, field, this.args);
  }
}
