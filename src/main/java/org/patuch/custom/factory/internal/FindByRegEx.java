package org.patuch.custom.factory.internal;

import org.openqa.selenium.By;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@CustomPageFactoryFinder(FindByRegEx.FindByRegExBuilder.class)
public @interface FindByRegEx {
  String css() default "";
  String xpath() default "";
  boolean reuseArgs() default false;

  public static class FindByRegExBuilder extends AbstractFindByRegExBuilder {
    public By buildIt(Object annotation, Field field) {
      FindByRegEx findByRegEx = (FindByRegEx) annotation;

      By ans = buildByFromShortFindByRegEx(findByRegEx);

      if (ans == null) {
        throw new IllegalArgumentException("'@FindByRegEx' annotation requires parameter.");
      }

      return ans;
    }
  }
}
