package br.com.unicred.cards.proposal.bean;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class BeanTest extends AbstractBeanTest {

  @Test
  void testConstructor() {
    for (final Class<?> clazz : getAllApplicationBeans()) {
      assertThat(clazz, hasValidBeanConstructor());
    }
  }

  @Test
  void testGetterAndSetter() {
    for (final Class<?> clazz : getAllApplicationBeans()) {
      if (Modifier.isAbstract(clazz.getModifiers())) {
        continue;
      }
      assertThat(clazz, hasValidGettersAndSetters());
    }
  }

  @Test
  void testEqualsAndHashCode() {
    for (final Class<?> clazz : getAllApplicationBeans()) {
      EqualsVerifier.forClass(clazz).suppress(Warning.NONFINAL_FIELDS).withRedefinedSuperclass()
          .verify();
    }
  }

  @Test
  void testToString()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
    for (final Class<?> clazz : getAllApplicationBeans()) {
      if (Modifier.isAbstract(clazz.getModifiers())) {
        continue;
      }
      testToString(clazz.getConstructor().newInstance());
    }
  }

}
