package br.com.unicred.cards.proposal.behavior.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class Fixture {

  private List<String> propertiesNamesForKey;
  private JsonNode data;

  public List<String> getPropertiesNamesForKey() {
    return propertiesNamesForKey;
  }

  public void setPropertiesNamesForKey(List<String> propertiesNamesForKey) {
    this.propertiesNamesForKey = propertiesNamesForKey;
  }

  public JsonNode getData() {
    return data;
  }

  public void setData(JsonNode data) {
    this.data = data;
  }
}
