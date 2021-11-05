package io.ashok;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class RestAssuredDemo {

    int productId = 9;
    Random random = new Random();
    int randomProductNumber = random.nextInt(9000) + 1000;
    int randomProductQuantity = random.nextInt(9000) + 1000;
    String productName = "P" + Integer.valueOf(randomProductNumber).toString();

    @BeforeMethod
    public void setup() {
        RestAssured.baseURI = "http://localhost:9191/api/v1";
    }

    @Test(priority = 3)
    public void getAllProducts() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/products")
                .then()
                .extract()
                .response();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 2)
    public void getProductById() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/product/" + productId )
                .then()
                .extract()
                .response();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test
    public void addNewProduct() {
        String requestBody = "{\n" +
                "  \"name\": \"" + productName + "\",\n" +
                "  \"quantity\": " + randomProductQuantity + "\n}";
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/product/add")
                .then()
                .extract()
                .response();
        Assert.assertEquals(response.statusCode(), 200);
        productId = response.jsonPath().getInt("id");
    }

    @Test(priority = 1)
    public void updateProduct() {
        String requestBody = "{\n" +
                "  \"id\": " + productId + ",\n" +
                "  \"name\": \"" + productName + " Updated" + "\",\n" +
                "  \"quantity\": " + randomProductQuantity + "\n}";
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .put("/product/update")
                .then()
                .extract()
                .response();
        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().get("name"), productName + " Updated");
    }


    @Test(priority = 4, enabled = false)
    public void deleteProduct() {
        Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/product/delete/" + productId)
                .then()
                .extract()
                .response();
        Assert.assertEquals(response.statusCode(), 200);
    }
}
