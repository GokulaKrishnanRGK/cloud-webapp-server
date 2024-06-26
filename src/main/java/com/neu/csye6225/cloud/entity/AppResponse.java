package com.neu.csye6225.cloud.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
    Gson gson = new Gson();
    if (body instanceof String) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("message", (String) body);
      this.body = gson.toJson(jsonObject);
    } else {
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
