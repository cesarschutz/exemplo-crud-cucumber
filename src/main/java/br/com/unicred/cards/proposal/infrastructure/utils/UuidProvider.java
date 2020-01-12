package br.com.unicred.cards.proposal.infrastructure.utils;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UuidProvider {

  public String get() {
    return UUID.randomUUID().toString();
  }

}
