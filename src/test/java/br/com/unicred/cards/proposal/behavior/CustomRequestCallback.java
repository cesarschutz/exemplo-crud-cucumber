package br.com.unicred.cards.proposal.behavior;

import static java.util.Objects.nonNull;

import java.io.IOException;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.RequestCallback;

public class CustomRequestCallback implements RequestCallback {

  private final Map<String, String> requestHeaders;
  private String body;

  CustomRequestCallback(final Map<String, String> headers) {
    this.requestHeaders = headers;
  }

  CustomRequestCallback(
      final Map<String, String> headers,
      final String body) {
    this.requestHeaders = headers;
    this.body = body;
  }

  @Override
  public void doWithRequest(final ClientHttpRequest request) throws IOException {
    final HttpHeaders clientHeaders = request.getHeaders();
    for (final Map.Entry<String, String> entry : requestHeaders.entrySet()) {
      clientHeaders.add(entry.getKey(), entry.getValue());
    }
    if (nonNull(body)) {
      request.getBody().write(body.getBytes());
    }
  }
}
