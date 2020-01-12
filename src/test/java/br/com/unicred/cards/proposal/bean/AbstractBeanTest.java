package br.com.unicred.cards.proposal.bean;

import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.lang.Math.random;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.unicred.cards.proposal.domain.model.Model;
import br.com.unicred.cards.proposal.infrastructure.document.AbstractDocument;
import com.google.code.beanmatchers.ValueGenerator;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;

public class AbstractBeanTest {

  AbstractBeanTest() {
    registerValueGenerator(getLocalDateGenerator(), LocalDate.class);
    registerValueGenerator(getLocalDateTimeGenerator(), LocalDateTime.class);
    registerValueGenerator(getObjectIdGenerator(), ObjectId.class);
  }

  protected List<Class<?>> getAllApplicationBeans() {
    try (final ScanResult scanResult = new ClassGraph().enableAllInfo().scan()) {
      final List<Class<?>> classes = new ArrayList<>();
      classes.addAll(scanResult.getSubclasses(AbstractDocument.class.getName()).loadClasses());
      classes.addAll(scanResult.getSubclasses(Model.class.getName()).loadClasses());
      return classes;
    }
  }

  protected void testToString(final Object object)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    final Class<?> clazz = object.getClass();
    final List<String> fieldList = Arrays.stream(clazz.getDeclaredFields())
        .filter(field -> !field.isSynthetic())
        .map(Field::getName)
        .collect(Collectors.toList());
    final Method toStringMethod = clazz.getMethod("toString");
    final String toStringResult = (String) toStringMethod.invoke(object);
    assertThat(toStringResult).contains(fieldList);
  }


  private ValueGenerator<LocalDate> getLocalDateGenerator() {
    return () -> {
      final int minusDays = (int) (random() * 182500);
      return LocalDate.now().minusDays(minusDays);
    };
  }

  private ValueGenerator<LocalDateTime> getLocalDateTimeGenerator() {
    return () -> {
      final int minusDays = (int) (random() * 182500);
      return LocalDateTime.now().minusDays(minusDays);
    };
  }

  private ValueGenerator<ObjectId> getObjectIdGenerator() {
    return ObjectId::new;
  }
}