package dmit2015.resource;

import dmit2015.restclient.TodoItem;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <a href="https://github.com/rest-assured/rest-assured">REST Assured GitHub repo</a>
 * <a href="https://github.com/rest-assured/rest-assured/wiki/Usage">REST Assured Usage</a>
 * <a href="http://www.mastertheboss.com/jboss-frameworks/resteasy/restassured-tutorial">REST Assured Tutorial</a>
 * <a href="https://github.com/FasterXML/jackson-databind">Jackson Data-Binding</a>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoItemResourceRestAssuredIT {

    String todoitemResourceUrl = "http://localhost:8080/restapi/TodoItems";
    static String testDataResourceLocation;

    @Order(1)
    @ParameterizedTest
    @CsvSource(value = {
            "Create RESTAssured IT for TodoItem REST API endpoint, false"
    })
    void shouldCreate(String name, boolean completed) {
        TodoItem newTodoItem = new TodoItem();
        newTodoItem.setName(name);
        newTodoItem.setCompleted(completed);

        Jsonb jsonb = JsonbBuilder.create();
        String jsonBody = jsonb.toJson(newTodoItem);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post(todoitemResourceUrl)
                .then()
                .statusCode(201)
                .extract()
                .response();
        testDataResourceLocation = response.getHeader("location");
    }

    @Order(2)
    @ParameterizedTest
    @CsvSource(value = {
            "Create RESTAssured IT for TodoItem REST API endpoint, false"
    })
    void shouldFineOne(String name, boolean completed) {
        Response response = given()
                .accept(ContentType.JSON)
                .when()
                .get(testDataResourceLocation)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();
        String jsonBody = response.getBody().asString();

        Jsonb jsonb = JsonbBuilder.create();
        TodoItem existingTodoItem = jsonb.fromJson(jsonBody, TodoItem.class);

        assertThat(existingTodoItem.getName())
                .isEqualTo(name);
        assertThat(existingTodoItem.isCompleted())
                .isEqualTo(completed);

    }

    @Order(3)
    @ParameterizedTest
    @CsvSource(value = {
            "Create RESTAssured IT for TodoItem REST API endpoint, false"
    })
    void shouldFindAll(String name, boolean completed) {
        Response response = given()
                .accept(ContentType.JSON)
                .when()
                .get(todoitemResourceUrl)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();
        String jsonBody = response.getBody().asString();

        Jsonb jsonb = JsonbBuilder.create();
        List<TodoItem> queryResultList = jsonb.fromJson(jsonBody, new ArrayList<TodoItem>() {
        }.getClass().getGenericSuperclass());

        TodoItem lastTodoItem = queryResultList.get(queryResultList.size() - 1);
        assertThat(lastTodoItem.getName())
                .isEqualTo(name);
        assertThat(lastTodoItem.isCompleted())
                .isEqualTo(completed);

    }

    @Order(4)
    @ParameterizedTest
    @CsvSource(value = {
            "Run RESTAssured IT for TodoItem REST API endpoint, 1"
    })
    void shouldUpdate(String name, boolean completed) {
        Response response = given()
                .accept(ContentType.JSON)
                .when()
                .get(testDataResourceLocation)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();
        String jsonBody = response.getBody().asString();

        Jsonb jsonb = JsonbBuilder.create();
        TodoItem existingTodoItem = jsonb.fromJson(jsonBody, TodoItem.class);
        existingTodoItem.setName(name);
        existingTodoItem.setCompleted(completed);

        String jsonTodoItem = jsonb.toJson(existingTodoItem);

        Response putResponse = given()
                .contentType(ContentType.JSON)
                .body(jsonTodoItem)
                .when()
                .put(testDataResourceLocation)
                .then()
                .statusCode(200)
                .extract()
                .response();

        String putResponseJsonBody = putResponse.getBody().asString();
        TodoItem updatedTodoItem = jsonb.fromJson(putResponseJsonBody, TodoItem.class);

        assertThat(existingTodoItem)
                .usingRecursiveComparison()
                .ignoringFields("updateTime")
                .isEqualTo(updatedTodoItem);
    }

    @Order(5)
    @Test
    void shouldDelete() {
        given()
                .when()
                .delete(testDataResourceLocation)
                .then()
                .statusCode(204);
    }

}