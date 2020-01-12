package br.com.unicred.cards.proposal.infrastructure.mappers;

import br.com.unicred.cards.proposal.domain.model.proposal.Proposal;
import br.com.unicred.cards.proposal.infrastructure.document.proposal.ProposalDocument;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProposalMapper {

  ProposalDocument proposaloProposalDocument(Proposal proposal);

  Proposal proposalDocumentToProposal(ProposalDocument proposalDocument);
}
