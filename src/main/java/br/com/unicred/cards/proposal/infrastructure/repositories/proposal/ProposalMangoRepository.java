package br.com.unicred.cards.proposal.infrastructure.repositories.proposal;

import br.com.unicred.cards.proposal.infrastructure.document.proposal.ProposalDocument;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalMangoRepository extends MongoRepository<ProposalDocument, ObjectId> {

  Optional<ProposalDocument> findByInstitutionAndOrganizationAndUuid(
      String institution,
      String organization,
      String uuid);

  List<ProposalDocument> findAllByInstitutionAndOrganization(
      final String institution,
      final String organization);
}
