package br.com.unicred.cards.proposal.application.controllers;

import br.com.unicred.cards.proposal.application.controllers.validation.model.ResponseMessages;
import br.com.unicred.cards.proposal.domain.model.proposal.Proposal;
import br.com.unicred.cards.proposal.domain.services.ProposalService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Validated
@RestController
@RequestMapping("/cards/v1/proposals")
public class ProposalController {

  private static final String INVALID_HEADER_VALUE_MESSAGE = "header must not be blank";

  private final ProposalService proposalService;

  public ProposalController(final ProposalService proposalService) {
    this.proposalService = proposalService;
  }

  @ApiOperation(
      value = "Add new proposal",
      response = Proposal.class,
      responseContainer = "Proposal"
  )
  @ApiResponses(value = {
      @ApiResponse(
          code = 201,
          message = ""
      ),
      @ApiResponse(
          code = 403,
          message = "",
          response = ResponseMessages.class
      ),
      @ApiResponse(
          code = 400,
          message = "",
          response = ResponseMessages.class
      )
  })
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> create(
      @RequestHeader("institution") @NotBlank(message = INVALID_HEADER_VALUE_MESSAGE) final String institution,
      @RequestHeader("organization") @NotBlank(message = INVALID_HEADER_VALUE_MESSAGE) final String organization,
      @RequestBody final Proposal proposal) {

    final String uuid = proposalService.create(institution, organization, proposal);

    final UriComponents uriComponents =
        ServletUriComponentsBuilder.fromCurrentRequest().path("/{uuid}").buildAndExpand(uuid);

    return ResponseEntity.created(uriComponents.toUri()).build();
  }

  @ApiOperation(
      value = "Update proposal"
  )
  @ApiResponses(value = {
      @ApiResponse(
          code = 204,
          message = ""
      ),
      @ApiResponse(
          code = 404,
          message = ""
      ),
      @ApiResponse(
          code = 403,
          message = "",
          response = ResponseMessages.class
      ),
      @ApiResponse(
          code = 400,
          message = "",
          response = ResponseMessages.class
      )
  })
  @PutMapping(path = "/{uuid}")
  public ResponseEntity<?> update(
      @RequestHeader("institution") @NotBlank(message = INVALID_HEADER_VALUE_MESSAGE) final String institution,
      @RequestHeader("organization") @NotBlank(message = INVALID_HEADER_VALUE_MESSAGE) final String organization,
      @PathVariable("uuid") final String uuid,
      @RequestBody final Proposal proposal) {

    proposal.setUuid(uuid);
    proposalService.update(institution, organization, proposal);

    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Delete proposal"
  )
  @ApiResponses(value = {
      @ApiResponse(
          code = 204,
          message = ""
      ),
      @ApiResponse(
          code = 404,
          message = ""
      ),
      @ApiResponse(
          code = 400,
          message = "",
          response = ResponseMessages.class
      )
  })
  @DeleteMapping(path = "/{uuid}")
  public ResponseEntity<?> delete(
      @RequestHeader("institution") @NotBlank(message = INVALID_HEADER_VALUE_MESSAGE) final String institution,
      @RequestHeader("organization") @NotBlank(message = INVALID_HEADER_VALUE_MESSAGE) final String organization,
      @PathVariable("uuid") final String uuid) {

    proposalService.delete(institution, organization, uuid);

    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Get proposal",
      response = Proposal.class
  )
  @ApiResponses(value = {
      @ApiResponse(
          code = 200,
          message = ""
      ),
      @ApiResponse(
          code = 400,
          message = "",
          response = ResponseMessages.class
      ),
      @ApiResponse(code = 404, message = "")
  })
  @GetMapping(path = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Proposal> get(
      @RequestHeader("institution") @NotBlank(message = INVALID_HEADER_VALUE_MESSAGE) final String institution,
      @RequestHeader("organization") @NotBlank(message = INVALID_HEADER_VALUE_MESSAGE) final String organization,
      @PathVariable("uuid") final String uuid) {

    return proposalService.get(institution, organization, uuid)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @ApiOperation(
      value = "Get list proposals",
      response = Proposal.class,
      responseContainer = "List"
  )
  @ApiResponses(value = {
      @ApiResponse(
          code = 200,
          message = "",
          response = Proposal.class,
          responseContainer = "List"
      ),
      @ApiResponse(
          code = 400,
          message = "",
          response = ResponseMessages.class
      ),
      @ApiResponse(code = 404, message = "")
  })
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Proposal>> list(
      @RequestHeader("institution") @NotBlank(message = INVALID_HEADER_VALUE_MESSAGE) final String institution,
      @RequestHeader("organization") @NotBlank(message = INVALID_HEADER_VALUE_MESSAGE) final String organization) {
    return ResponseEntity.ok(proposalService.list(institution, organization));
  }
}
