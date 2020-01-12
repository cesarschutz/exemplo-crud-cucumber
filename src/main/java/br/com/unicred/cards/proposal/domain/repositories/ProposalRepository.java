package br.com.unicred.cards.proposal.domain.repositories;

import br.com.unicred.cards.proposal.domain.model.proposal.Proposal;
import java.util.List;
import java.util.Optional;

public interface ProposalRepository {

  String save(String institution, String organization, Proposal proposal);

  void update(String institution, String organization, Proposal proposal);

  void delete(String institution, String organization, String uuid);

  Optional<Proposal> get(String institution, String organization, String uuid);

  List<Proposal> list(String institution, String organization);
}
