package br.com.unicred.cards.proposal.test.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class JsonMapper {

  private final ObjectMapper objectMapper;

  public JsonMapper(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public String fileAsString(final String resourcePath, final Class<?> clazz) throws IOException {
    return objectMapper.writeValueAsString(fileAsObject(resourcePath, clazz));
  }

  public String objectAsString(final Object object) throws JsonProcessingException {
    return objectMapper.writeValueAsString(object);
  }

  public <T> T fileAsObject(final String resourcePath, final Class<T> clazz) throws IOException {
    return objectMapper.readValue(new ClassPathResource(resourcePath).getFile(), clazz);
  }

  public <T> T fileAsObject(
      final String resourcePath,
      final TypeReference<T> typeReference) throws IOException {
    return objectMapper.readValue(new ClassPathResource(resourcePath).getFile(), typeReference);
  }

  public <T> T stringAsObject(final String string, final Class<T> clazz) throws IOException {
    return objectMapper.readValue(string, clazz);
  }

  public <T> T stringAsObject(
      final String string,
      final TypeReference<T> typeReference) throws IOException {
    return objectMapper.readValue(string, typeReference);
  }

  public Collection<?> stringAsCollectionOfObjects(
      final String json,
      final Class<? extends Collection> collectionType,
      final Class<?> objectType) throws IOException {
    final JavaType type = objectMapper.getTypeFactory().
        constructCollectionType(collectionType, objectType);
    return objectMapper.readValue(json, type);
  }

  public Collection<?> fileAsCollectionOfObjects(
      final String resourcePath,
      final Class<? extends Collection> collectionType,
      final Class<?> objectType) throws IOException {
    final JavaType type = objectMapper.getTypeFactory().
        constructCollectionType(collectionType, objectType);
    return objectMapper.readValue(new ClassPathResource(resourcePath).getFile(), type);
  }

}
