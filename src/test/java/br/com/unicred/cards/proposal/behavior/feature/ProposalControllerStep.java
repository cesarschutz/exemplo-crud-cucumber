package br.com.unicred.cards.proposal.behavior.feature;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.unicred.cards.proposal.domain.model.proposal.Proposal;
import br.com.unicred.cards.proposal.infrastructure.document.proposal.ProposalDocument;
import br.com.unicred.cards.proposal.test.utils.JsonMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class ProposalControllerStep {

  private static final String UUID_V4_REGEX = "^[0-9a-zA-F]{8}-[0-9a-zA-F]{4}-[4][0-9a-zA-F]{3}-[89aAbB][0-9a-zA-F]{3}-[0-9a-zA-F]{12}$";

  @Autowired
  private JsonMapper jsonMapper;

  @Autowired
  private CommonFeatureStep commonFeatureStep;

  @Autowired
  private MongoTemplate mongoTemplate;

  /**
   * Test in @ProposalGET
   *
   * @param bodyContent
   */
  @Then("the proposal returned should be {string}")
  public void theBodyContentShouldBe(final String bodyContent) {
    commonFeatureStep.assertObjects(bodyContent, Proposal.class);
  }

  /**
   * Test in @ProposalLIST
   *
   * @param resource
   */
  @And("the list of proposals returned should be {string}")
  public void theListOfPropertiesReturnedShouldBe(final String resource) throws IOException {
    final List<Proposal> expectedProposal =
        jsonMapper.stringAsObject(
            resource,
            new TypeReference<>() {
            }
        );
    final List<Proposal> actualProposal =
        jsonMapper.stringAsObject(
            commonFeatureStep.getResults().getBody(),
            new TypeReference<>() {
            }
        );
    assertThat(actualProposal).containsExactlyInAnyOrderElementsOf(expectedProposal);
  }

  @And("the data stored in the proposal database with uuid {string} must be {string}")
  public void theDataStoredInTheDatabaseForTheNameOfProposalShouldBeStoredData(
      final String uuidProposalForSearch,
      final String expectedProposalDocumentJSON
  ) throws IOException {

    final ProposalDocument expectedProposalDocument = getProposalDocument(expectedProposalDocumentJSON);
    final int expectedSize = (expectedProposalDocument == null) ? 0 : 1;

    final List<ProposalDocument> listProposalDocumentsStored =
        getProposalDocumentsFromDatabase(uuidProposalForSearch);

    assertThat(listProposalDocumentsStored).hasSize(expectedSize);
    if (expectedSize == 1) {
      final ProposalDocument actual = listProposalDocumentsStored.get(0);
      assertThat(actual.getId()).isNotNull();
      assertThat(actual.getUuid()).matches(UUID_V4_REGEX);
      assertThat(actual).isEqualToIgnoringGivenFields(expectedProposalDocument, "id");
    }
  }

  /**
   * Converts a JSON to an {@link ProposalDocument} ​​object.
   *
   * @param proposalDocumentJSON JSON to be converted to {@link ProposalDocument}.
   * @return {@link ProposalDocument}
   * @throws IOException error converting.
   */
  private ProposalDocument getProposalDocument(
      final String proposalDocumentJSON) throws IOException {
    return proposalDocumentJSON.equals("null")
        ? null
        : jsonMapper.stringAsObject(proposalDocumentJSON, ProposalDocument.class);
  }

  /**
   * The database retrieves proposals by name, institution and organization.
   *
   * @param uuidProposal proposal name for search.
   * @return list of proposals found.
   */
  private List<ProposalDocument> getProposalDocumentsFromDatabase(final String uuidProposal) {
    final Criteria criteria = Criteria.where("uuid").is(uuidProposal)
        .and("organization").is(commonFeatureStep.getHeaders().get("organization"))
        .and("institution").is(commonFeatureStep.getHeaders().get("institution"));
    final Query query = new Query();
    query.addCriteria(criteria);
    return mongoTemplate.find(query, ProposalDocument.class);
  }
}
