package br.com.unicred.cards.proposal.behavior.feature;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.unicred.cards.proposal.application.controllers.validation.model.ResponseMessages;
import br.com.unicred.cards.proposal.behavior.CucumberIntegrationTest;
import br.com.unicred.cards.proposal.behavior.ResponseResults;
import br.com.unicred.cards.proposal.infrastructure.utils.UuidProvider;
import br.com.unicred.cards.proposal.test.utils.JsonMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

public class CommonFeatureStep extends CucumberIntegrationTest {

  private static final String INSTITUTION = "institution";
  private static final String ORGANIZATION = "organization";
  private static final String URL_PATTERN = "http://localhost:%d%s";

  @Autowired
  private JsonMapper jsonMapper;

  @Autowired
  private UuidProvider uuidProvider;

  private Map<String, String> headers;

  public ResponseResults getResults() {
    return responseResults;
  }

  @Given("the headers institution {string} and organization {string}")
  public void thatTheGETAPICardsVParamsProposalsHasBeenAccessed(
      final String institution,
      final String organization) {

    headers = new HashMap<>();
    headers.put("Content-Type", "application/json");

    if (!shouldBeIgnored(institution)) {
      headers.put(INSTITUTION, institution);
    }

    if (!shouldBeIgnored(organization)) {
      headers.put(ORGANIZATION, organization);
    }
  }

  @And("mock uuid {string}")
  public void thatTheGETAPICardsVParamsProposalsHasBeenAccessed(final String uuid) {
    Mockito.when(uuidProvider.get()).thenReturn(uuid);
  }

  @When("running {string} for path {string} with payload {string}")
  public void theAPIIsCalledWithThePayload(
      final String httpMethod,
      final String resourcePath,
      final String payload) {
    execute(HttpMethod.valueOf(httpMethod), headers, resourcePath, payload);
  }

  @Then("it should return {int} HTTP status code")
  public void itShouldReturnHTTPStatusCode(Integer httpStatusCode) throws IOException {
    assertThat(responseResults.getHttpResponse().getStatusCode().value()).isEqualTo(httpStatusCode);
  }

  @Then("the body of the response should be {string}")
  public void theBodyContentShouldBe(final String bodyContent) throws IOException {
    if (StringUtils.isNotBlank(bodyContent)) {
      final ResponseMessages expectedCardTypeProperties =
          jsonMapper.stringAsObject(bodyContent, ResponseMessages.class);

      final ResponseMessages actualCardTypeProperties =
          jsonMapper.stringAsObject(responseResults.getBody(), ResponseMessages.class);

      assertThat(actualCardTypeProperties).isEqualTo(expectedCardTypeProperties);
    } else {
      assertThat(bodyContent).isEqualTo(responseResults.getBody());
    }
  }

  @And("the returned location header should be {string}")
  public void theReturnedLocationHeaderShouldBe(String path) {
    if (!path.endsWith("null")) {
      Assertions.assertThat(responseResults.getLocationHeader()).isEqualTo(path);
    }
  }

  private boolean shouldBeIgnored(final String value) {
    return "null".equals(value);
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void assertObjects(final String bodyContent, final Class<?> type) {
    final String body = responseResults.getBody();
    try {
      final Object expectedObject =
          jsonMapper.stringAsObject(
              bodyContent,
              type
          );
      final Object actualObject =
          jsonMapper.stringAsObject(
              body,
              type
          );
      assertThat(actualObject)
          .isEqualTo(expectedObject);
    } catch (Exception e) {
      assertThat(body).isEqualTo(bodyContent);
    }
  }
}

