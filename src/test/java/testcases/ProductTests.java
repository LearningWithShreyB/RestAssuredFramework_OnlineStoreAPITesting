package testcases;

import pojo.ProductPOJO;
import routes.Routes;
import utils.ConfigReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import payloads.Payload;

import org.testng.ITestContext;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.List;

public class ProductTests extends BaseClass {
	
	//1) Test to retrieve all products
		@Test
		public void testGetAllProducts()
		{
			given()
			
			.when()
				.get(Routes.GET_ALL_PRODUCTS)
			.then()
				.statusCode(200)
				.body("size()",greaterThan(0));
				
		}

}
