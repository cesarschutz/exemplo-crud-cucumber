package br.com.unicred.cards.proposal.application.controllers.validation.model;

import br.com.unicred.cards.proposal.domain.model.Model;
import java.util.HashSet;
import java.util.Set;

public class ResponseMessages extends Model {

  private Set<String> messages;

  public ResponseMessages() {
    buildMessages();
  }

  public ResponseMessages(final Set<String> messages) {
    this.messages = messages;
  }

  public Set<String> getMessages() {
    return messages;
  }

  public void setMessages(final Set<String> messages) {
    this.messages = messages;
  }

  public void addMessage(final String message) {
    if (this.messages == null) {
      buildMessages();
    }
    this.messages.add(message);
  }

  private void buildMessages() {
    this.messages = new HashSet<>();
  }

}
