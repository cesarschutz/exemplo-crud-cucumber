package br.com.unicred.cards.proposal.behavior;

import com.google.common.base.Charsets;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpResponse;

public class ResponseResults {

  private final ClientHttpResponse httpResponse;
  private final String body;

  public ResponseResults(final ClientHttpResponse httpResponse) throws IOException {
    this.httpResponse = httpResponse;
    final InputStream bodyInputStream = httpResponse.getBody();
    final StringWriter stringWriter = new StringWriter();
    IOUtils.copy(bodyInputStream, stringWriter, Charsets.UTF_8.displayName());
    this.body = stringWriter.toString();
  }

  public ClientHttpResponse getHttpResponse() {
    return httpResponse;
  }

  public String getBody() {
    return body;
  }

  public HttpHeaders getHeaders() {
    return httpResponse.getHeaders();
  }

  public String getLocationHeader() {
    if (httpResponse.getHeaders().getLocation() != null) {
      return httpResponse.getHeaders().getLocation().getPath();
    }
    throw new IllegalStateException("The header location was not found");
  }
}
