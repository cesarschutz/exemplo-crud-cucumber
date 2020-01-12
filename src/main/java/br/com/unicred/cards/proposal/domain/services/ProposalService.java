package br.com.unicred.cards.proposal.domain.services;

import br.com.unicred.cards.proposal.domain.model.proposal.Proposal;
import br.com.unicred.cards.proposal.domain.repositories.ProposalRepository;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProposalService {

  private ProposalRepository proposalRepository;

  private Validator validator;

  public ProposalService(final Validator validator, final ProposalRepository proposalRepository) {
    this.validator = validator;
    this.proposalRepository = proposalRepository;
  }

  public String create(
      final String institution,
      final String organization,
      final Proposal proposal) {
    validate(institution, organization, proposal);
    return proposalRepository.save(institution, organization, proposal);
  }

  private void validate(
      final String institution,
      final String organization,
      final Proposal proposal) {
    validateMandatoryFields(proposal);
  }

  private void validateMandatoryFields(final Proposal proposal) {
    final Set<ConstraintViolation<Proposal>> violations =
        validator.validate(proposal);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  public void update(
      final String institution,
      final String organization,
      final Proposal proposal) {
    validate(institution, organization, proposal);
    proposalRepository.update(institution, organization, proposal);
  }

  public void delete(String institution, String organization, String uuid) {
    proposalRepository.delete(institution, organization, uuid);
  }

  public Optional<Proposal> get(
      final String institution,
      final String organization,
      final String uuid) {
    return proposalRepository.get(institution, organization, uuid);
  }

  public List<Proposal> list(final String institution, final String organization) {
    return proposalRepository.list(institution, organization);
  }
}
