package br.com.unicred.cards.proposal.behavior;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import br.com.unicred.cards.proposal.Application;
import br.com.unicred.cards.proposal.infrastructure.utils.UuidProvider;
import io.cucumber.core.api.Scenario;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.Assume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@AutoConfigureWireMock(port = 0)
@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = DEFINED_PORT)
@ContextConfiguration(classes = Application.class, loader = SpringBootContextLoader.class)
public abstract class CucumberIntegrationTest {

  private static Map<String, Class<?>> collectionsMapped = new HashMap<>();

  protected ResponseResults responseResults;

  @LocalServerPort
  protected Integer localServerPort;
  @Autowired
  protected MongoTemplate mongoTemplate;
  @Autowired
  private TestRestTemplate testRestTemplate;

  @MockBean
  private UuidProvider uuidProvider;

  public void before(final Scenario scenario) {
    Assume.assumeTrue(!scenario.getSourceTagNames().contains("@Ignore"));
  }

  protected void execute(
      final HttpMethod httpMethod,
      final Map<String, String> headers,
      final String url) {
    responseResults = testRestTemplate.execute(
        url,
        httpMethod,
        new CustomRequestCallback(headers),
        ResponseResults::new
    );
  }

  protected void execute(
      final HttpMethod httpMethod,
      final Map<String, String> headers,
      final String url,
      final String body) {
    responseResults = testRestTemplate.execute(
        url,
        httpMethod,
        new CustomRequestCallback(headers, body),
        ResponseResults::new
    );
  }

  protected Class<?> getDocumentType(final String collectionName) {
    if (collectionsMapped.containsKey(collectionName)) {
      return collectionsMapped.get(collectionName);
    } else {
      try (ScanResult scanResult = new ClassGraph().enableAllInfo().scan()) {
        ClassInfoList classInfoList = scanResult.getClassesWithAnnotation(Document.class.getName());
        return classInfoList.stream()
            .filter(classInfo -> classPredicate(classInfo, collectionName))
            .map(this::getTargetClass)
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                  if (list.size() != 1) {
                    throw new IllegalStateException();
                  }
                  final Optional<Class<?>> detectedClassOptional = list.get(0);
                  detectedClassOptional.ifPresent(
                      detectedClass -> collectionsMapped.put(collectionName, detectedClass));
                  return detectedClassOptional;
                }
            ))
            .orElseThrow();
      }
    }
  }

  private Optional<Class<?>> getTargetClass(final ClassInfo classInfo) {
    try {
      return Optional.of(Class.forName(classInfo.getName()));
    } catch (ClassNotFoundException e) {
      // TODO: add log
      return Optional.empty();
    }
  }

  private boolean classPredicate(final ClassInfo classInfo, final String collectionName) {
    final AnnotationInfo annotationInfo = classInfo.getAnnotationInfo(Document.class.getName());
    return annotationInfo != null
        && annotationInfo.getParameterValues().get("collection") != null
        && annotationInfo.getParameterValues().get("collection").getValue().equals(collectionName);
  }

}
