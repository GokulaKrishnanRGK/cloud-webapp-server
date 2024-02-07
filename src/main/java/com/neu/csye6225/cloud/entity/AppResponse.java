package com.neu.csye6225.cloud.entity;

import com.google.gson.Gson;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class AppResponse {

  private HttpStatusCode status;
  private String body;

  public AppResponse(HttpStatusCode status) {
    this.status = status;
  }

  public AppResponse(HttpStatusCode status, Object body) {
    this.status = status;
    if (body instanceof String) {
      this.body = (String) body;
    } else {
      Gson gson = new Gson();
      this.body = gson.toJson(body);
    }
  }

  public ResponseEntity<String> getResponseEntity() {
    return ResponseEntity.status(this.status).body(body);
  }

  public HttpStatusCode getStatus() {
    return status;
  }

  public void setStatus(HttpStatusCode status) {
    this.status = status;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }
}
