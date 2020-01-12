package br.com.unicred.cards.proposal.application.controllers.validation;

import static java.util.stream.Collectors.toSet;
import static org.springframework.http.ResponseEntity.*;

import br.com.unicred.cards.proposal.application.controllers.validation.model.ResponseMessages;
import br.com.unicred.cards.proposal.domain.exception.DataNotFoundException;
import java.text.MessageFormat;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationInterceptor {

  private static final String MISSING_HEADER_MESSAGE = "{0} header must be informed";

  @ExceptionHandler(MissingRequestHeaderException.class)
  public ResponseEntity<ResponseMessages> handleMissingRequestHeaderParameters(
      final MissingRequestHeaderException exception) {
    final String message =
        MessageFormat.format(MISSING_HEADER_MESSAGE, exception.getHeaderName());
    return badRequest().body(buildMessages(message));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ResponseMessages> handleConstraintViolationException(
      final ConstraintViolationException exception) {
    final Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
    final Set<String> messages = constraintViolations.stream()
        .map(this::buildMessage)
        .collect(toSet());
    return badRequest().body(new ResponseMessages(messages));
  }

  @ExceptionHandler(DataNotFoundException.class)
  public ResponseEntity<ResponseMessages> handleNotFoundException(
      final RuntimeException exception) {
    return notFound().build();
  }

  private ResponseMessages buildMessages(final String message) {
    final ResponseMessages responseMessages = new ResponseMessages();
    responseMessages.addMessage(message);
    return responseMessages;
  }

  private String buildMessage(final ConstraintViolation<?> constraintViolation) {
    return MessageFormat.format(
        "{0} {1}",
        ((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().getName(),
        constraintViolation.getMessage());
  }
}