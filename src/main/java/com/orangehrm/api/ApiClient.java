package com.orangehrm.api;

import com.orangehrm.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class ApiClient {

    private static final Logger log = LogManager.getLogger(ApiClient.class);
    private static String authToken;

    static {
        RestAssured.baseURI = ConfigManager.getInstance().getApiBaseUrl();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private ApiClient() {}

    public static String authenticate(String username, String password) {
        log.info("Authenticating via API as: {}", username);

        Response response = given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
                .post("/auth/login");

        if (response.statusCode() != 200) {
            throw new RuntimeException("API authentication failed. Status: " + response.statusCode());
        }

        authToken = response.jsonPath().getString("data.token");
        log.info("API authentication successful, token acquired");
        return authToken;
    }

    public static void authenticateAsAdmin() {
        ConfigManager config = ConfigManager.getInstance();
        authenticate(config.getAdminUsername(), config.getAdminPassword());
    }

    public static RequestSpecification withAuth() {
        if (authToken == null) {
            authenticateAsAdmin();
        }
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken);
    }

    public static Response get(String endpoint) {
        log.debug("GET {}", endpoint);
        return withAuth().get(endpoint);
    }

    public static Response post(String endpoint, String body) {
        log.debug("POST {} body={}", endpoint, body);
        return withAuth().body(body).post(endpoint);
    }

    public static Response put(String endpoint, String body) {
        log.debug("PUT {} body={}", endpoint, body);
        return withAuth().body(body).put(endpoint);
    }

    public static Response delete(String endpoint, String body) {
        log.debug("DELETE {} body={}", endpoint, body);
        return withAuth().body(body).delete(endpoint);
    }

    public static void clearToken() {
        authToken = null;
    }
}
