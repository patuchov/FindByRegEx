package org.patuch.custom.factory.internal;

import org.openqa.selenium.By;
import org.openqa.selenium.support.AbstractFindByBuilder;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.PageFactoryFinder;
import org.openqa.selenium.support.pagefactory.Annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

public class CustomAnnotation extends Annotations {

  private Field field;
  private final String[] args;
  private static int currentPos;

  public CustomAnnotation(Field field, String[] args) {
    super(field);
    this.field = field;
    this.args = args;
  }

  @Override
  public By buildBy() {
    if (isFindByRegEx()) {
      return getByRegEx();
    }

    return getDefaultBy();
  }

  @Override
  public boolean isLookupCached() {
    return (field.getAnnotation(CacheLookup.class) != null);
  }

  public static void resetCurrentPos() {
    currentPos = 0;
  }

  protected Field getField() {
    return field;
  }

  protected By buildByFromDefault() {
    return new ByIdOrName(field.getName());
  }

  protected void assertValidAnnotations() {

  }

  private boolean isFindByRegEx() {
    FindByRegEx findByRegEx = field.getAnnotation(FindByRegEx.class);

    if (findByRegEx == null) {
      return false;
    }

    return true;
  }

  private By getDefaultBy() {
    By ans = null;

    for (Annotation annotation : field.getDeclaredAnnotations()) {
      AbstractFindByBuilder builder = null;

      if (annotation.annotationType().isAnnotationPresent(PageFactoryFinder.class)) {
        try {
          builder = annotation.annotationType()
            .getAnnotation(PageFactoryFinder.class).value()
            .newInstance();
        } catch (ReflectiveOperationException e) {
          // Fall through.
        }
      }

      if (builder != null) {
        ans = builder.buildIt(annotation, field);
        break;
      }
    }

    if (ans == null) {
      ans = buildByFromDefault();
    }

    if (ans == null) {
      throw new IllegalArgumentException("Cannot determine how to locate element " + field);
    }

    return ans;
  }

  private By getByRegEx() {
    By locateBy = null;

    FindByRegEx findByRegEx = field.getAnnotation(FindByRegEx.class);

    if (findByRegEx == null) {
      locateBy = super.buildByFromDefault();

      return locateBy;
    }

    int numberOfRequiredArgs = HelperFunctions.getNumberOfRequiredParams(findByRegEx);

    if ((numberOfRequiredArgs > args.length) && findByRegEx.reuseArgs()) {
      throw new RuntimeException("Not enough arguments provided. Number of arguments should match number of arguments required by template!!!");
    }

    if (((args.length - currentPos) < numberOfRequiredArgs) && !findByRegEx.reuseArgs()) {
      throw new RuntimeException("Total number of arguments provided, should match number of format arguments in all templates!!!");
    }

    String[] subArgs = Arrays.copyOfRange(this.args, currentPos, currentPos + numberOfRequiredArgs);

    if (!findByRegEx.reuseArgs()) {
      currentPos += numberOfRequiredArgs;
    } else {
      currentPos = 0;
    }

    locateBy = HelperFunctions.getLocator(findByRegEx, field, subArgs);

    if (locateBy == null) {
      throw new IllegalArgumentException("Cannot determine how to locate element " + field);
    }

    return locateBy;
  }

}
