package org.patuch.custom.factory.internal;



import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CustomPageFactoryFinder {
  Class<? extends AbstractFindByTemplateBuilder> value();
}
