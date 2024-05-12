package br.com.fiap.produtos;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class produtosAppApplicationTests {

    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup(){
        RestAssured.baseURI="http://localhost";
        RestAssured.port = port;
    }

    static{
        mongoDBContainer.start();
    }

    @Test
    void shouldCreateProduct() {
        String requestBody = """
                {
                "name": "Android 14",
                "description": "Android 14 is the best",
                "price": 2000
                }
                """;

        RestAssured.given( )
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post( "/api/produtos")
                .then()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo("Android 14"))
                .body("description", Matchers.equalTo("Android 14 is the best"))
                .body("price", Matchers.equalTo(2000));

    }


}
