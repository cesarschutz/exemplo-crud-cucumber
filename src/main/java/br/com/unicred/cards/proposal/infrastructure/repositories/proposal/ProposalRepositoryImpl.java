package br.com.unicred.cards.proposal.infrastructure.repositories.proposal;

import br.com.unicred.cards.proposal.domain.exception.DataNotFoundException;
import br.com.unicred.cards.proposal.domain.model.proposal.Proposal;
import br.com.unicred.cards.proposal.domain.repositories.ProposalRepository;
import br.com.unicred.cards.proposal.infrastructure.document.proposal.ProposalDocument;
import br.com.unicred.cards.proposal.infrastructure.mappers.ProposalMapper;
import br.com.unicred.cards.proposal.infrastructure.utils.UuidProvider;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ProposalRepositoryImpl implements ProposalRepository {

  private ProposalMangoRepository proposalMongoRepository;
  private ProposalMapper proposalMapper;
  private UuidProvider uuidProvider;

  public ProposalRepositoryImpl(
      final ProposalMangoRepository proposalMongoRepository,
      final ProposalMapper proposalMapper,
      final UuidProvider uuidProvider) {
    this.proposalMongoRepository = proposalMongoRepository;
    this.proposalMapper = proposalMapper;
    this.uuidProvider = uuidProvider;
  }

  @Override
  public String save(
      final String institution,
      final String organization,
      final Proposal proposal) {

    final ProposalDocument proposalDocument =
        proposalMapper.proposaloProposalDocument(proposal);
    final String uuid = uuidProvider.get();
    proposalDocument.setUuid(uuid);
    proposalDocument.setInstitution(institution);
    proposalDocument.setOrganization(organization);

    proposalMongoRepository.save(proposalDocument);

    return uuid;
  }

  @Override
  public void update(final String institution, final String organization, final Proposal proposal) {
    final ProposalDocument proposalDocumentLoad = load(institution, organization, proposal.getUuid());
    ProposalDocument proposalDocument = proposalMapper.proposaloProposalDocument(proposal);

    proposalDocument.setId(proposalDocumentLoad.getId());
    proposalDocument.setUuid(proposalDocumentLoad.getUuid());
    proposalDocument.setInstitution(proposalDocumentLoad.getInstitution());
    proposalDocument.setOrganization(proposalDocumentLoad.getOrganization());

    proposalMongoRepository.save(proposalDocument);
  }

  @Override
  public void delete(String institution, String organization, String uuid) {
    final ProposalDocument proposalDocumentLoad = load(institution, organization, uuid);
    proposalMongoRepository.deleteById(proposalDocumentLoad.getId());
  }

  private ProposalDocument load(String institution, String organization, String uuid) {
    final Optional<ProposalDocument> proposalOptional =
        proposalMongoRepository
            .findByInstitutionAndOrganizationAndUuid(institution, organization, uuid);

    return proposalOptional.orElseThrow(DataNotFoundException::new);
  }

  @Override
  public Optional<Proposal> get(
      final String institution,
      final String organization,
      final String uuid) {
    final Optional<ProposalDocument> proposalOptional =
        proposalMongoRepository.findByInstitutionAndOrganizationAndUuid(
            institution,
            organization,
            uuid);
    return proposalOptional
        .map(proposalMapper::proposalDocumentToProposal);
  }

  @Override
  public List<Proposal> list(final String institution, final String organization) {
    final List<ProposalDocument> all = proposalMongoRepository
        .findAllByInstitutionAndOrganization(institution, organization);
    return all.stream()
        .map(proposalMapper::proposalDocumentToProposal)
        .collect(Collectors.toList());
  }
}
