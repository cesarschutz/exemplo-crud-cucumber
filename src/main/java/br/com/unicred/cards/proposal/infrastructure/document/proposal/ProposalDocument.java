package br.com.unicred.cards.proposal.infrastructure.document.proposal;

import br.com.unicred.cards.proposal.infrastructure.document.AbstractDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "proposals")
public class ProposalDocument extends AbstractDocument {

  @MongoId
  private ObjectId id;

  @Indexed(unique = true)
  private String uuid;

  private String institution;
  private String organization;
  private String name;

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getInstitution() {
    return institution;
  }

  public void setInstitution(String institution) {
    this.institution = institution;
  }

  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}