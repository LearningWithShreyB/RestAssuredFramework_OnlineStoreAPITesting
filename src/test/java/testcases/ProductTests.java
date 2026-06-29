package testcases;

import pojo.ProductPOJO;
import routes.Routes;
import utils.ConfigReader;
import utils.ExtentReporter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import payloads.Payload;

import org.testng.ITestContext;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.List;

@Listeners(ExtentReporter.class)
public class ProductTests extends BaseClass {

	// 1) Test to retrieve all products
	@Test
	public void testGetAllProducts() {
		given()

				.when().get(Routes.GET_ALL_PRODUCTS).then().statusCode(200).body("size()", greaterThan(0));

	}

	// 2) Test to retrieve a single product by ID
	@Test
	public void testGetSingleProductById() {
		int productId = configReader.getIntProperty("productId");

		given().pathParam("id", productId)

				.when().get(Routes.GET_PRODUCT_BY_ID).then().statusCode(200);
	}

	// 3) Test to retrieve a limited number of products
	@Test
	public void testGetLimitedProduct() {
		given().pathParam("limit", 4)

				.when().get(Routes.GET_PRODUCTS_WITH_LIMIT).then().statusCode(200).body("size()", equalTo(4));
	}

	// 8) Test to add a new product
	@Test
	public void testAddNewProduct() {
		ProductPOJO newProduct = Payload.productPayload();

		int productId = given().contentType(ContentType.JSON).body(newProduct)

				.when().post(Routes.CREATE_PRODUCT).then().statusCode(201).body("id", notNullValue())
				.body("title", equalTo(newProduct.getTitle())).extract().jsonPath().getInt("id"); // Extracting Id //
																									// body

		System.out.println(productId);

	}

}
