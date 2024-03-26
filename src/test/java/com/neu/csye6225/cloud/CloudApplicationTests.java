package com.neu.csye6225.cloud;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.neu.csye6225.cloud.model.User;
import com.neu.csye6225.cloud.model.Verify;
import com.neu.csye6225.cloud.service.UserService;
import jakarta.annotation.PostConstruct;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(classes = CloudApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
class CloudApplicationTests {

  private static final Logger logger = LoggerFactory.getLogger(CloudApplicationTests.class);

  @LocalServerPort
  private int port;

  @Autowired
  private OkHttpClient httpClient;

  @Autowired
  private UserService userService;

  private final Gson gson = new Gson();

  private static JsonObject updateUserRequestBody;
  private static JsonObject createUserRequestBody;
  private static User createUserResp = new User();
  private static final long currentMillis = System.currentTimeMillis();

  @PostConstruct
  public void init() {
    String username = "Jane.doe" + currentMillis + "@example.com";
    createUserRequestBody = gson.fromJson("{" + "\"firstname\": \"Jane\"," + "\"lastname\": \"Doe\"," + "\"password\": \"root\"" + "}",
        JsonObject.class);
    updateUserRequestBody = gson.fromJson("{" + "\"firstname\": \"Sane\"," + "\"lastname\": \"Doe\"," + "\"password\": \"root\"" + "}",
        JsonObject.class);
    createUserRequestBody.addProperty("username", username);
    updateUserRequestBody.addProperty("username", username);
  }

  @Test
  @Order(1)
  public void testHealthz() {
    String url = "http://localhost:" + port + "/healthz";
    Request request = new Request.Builder().url(url).build();
    try (Response response = httpClient.newCall(request).execute()) {
      Assertions.assertEquals(200, response.code());
    } catch (Exception e) {
      logger.error("Exception in testHealthz: ", e);
    }
  }

  @Test
  @Order(2)
  public void testCreateUser() {
    String userUrl = "http://localhost:" + port + "/v1/user";
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(createUserRequestBody.toString(), mediaType);
    Request request = new Request.Builder().url(userUrl).post(body).build();
    try (Response response = httpClient.newCall(request).execute()) {
      createUserResp = gson.fromJson(response.body().string(), User.class);
      Assertions.assertEquals(201, response.code());
    } catch (Exception e) {
      logger.error("Exception in testCreateUser: ", e);
    }
  }

  @Test
  @Order(3)
  public void testVerifyUser() {
    Verify verify = this.userService.loadByUsername(createUserResp.getUsername()).getVerify();
    String userUrl = "http://localhost:" + port + "/v1/user/verify?token=" + verify.getToken();
    Request request = new Request.Builder().url(userUrl).get().build();
    try (Response response = httpClient.newCall(request).execute()) {
      Assertions.assertEquals(202, response.code());
    } catch (Exception e) {
      logger.error("Exception in testVerifyUser: ", e);
    }
  }

  @Test
  @Order(4)
  public void testUpdateUser() {
    String userUrl = "http://localhost:" + port + "/v1/user/self";
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(updateUserRequestBody.toString(), mediaType);
    Request request = new Request.Builder().url(userUrl)
        .addHeader("Authorization",
            Credentials.basic(createUserRequestBody.get("username").getAsString(), createUserRequestBody.get("password").getAsString()))
        .put(body)
        .build();
    try (Response response = httpClient.newCall(request).execute()) {
      User updatedUser = userService.getUserById(createUserResp.getId());
      Assertions.assertEquals(200, response.code(), "Response code: " + response.code());
      Assertions.assertEquals("Sane", updatedUser.getFirstname(), "Updated Username: " + updatedUser.getFirstname());
    } catch (Exception e) {
      logger.error("Exception in testUpdateUser: ", e);
    }
  }
}
