package testcases;

import java.util.List;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import io.restassured.RestAssured;
import routes.Routes;
import utils.ConfigReader;
import utils.LogManager;

public class BaseClass {

	public static ConfigReader configReader;

	@BeforeSuite
	public void setupSuite() {
		// 1. Properly clears both files out before execution starts
		LogManager.initLogs();

		// ❌ REMOVED: System.setOut redirection. Removing this brings back your classic
		// Eclipse/IntelliJ test run console views!
	}

	@BeforeClass
	public void setup() {
		// Initialize config reader FIRST to avoid NullPointerExceptions later
		configReader = new ConfigReader();

		// Set up the target global URI context for RestAssured configuration
		RestAssured.baseURI = Routes.BASE_URL;

		// 🟢 NEW FIX: Custom filter that intercepts everything and writes it cleanly to
		// executionHeaders.log
		RestAssured.filters((requestSpec, responseSpec, ctx) -> {

			StringBuilder logBuilder = new StringBuilder();
			logBuilder.append("\n==================================================================\n");
			logBuilder.append("API INTERACTION FOR TEST: ").append(requestSpec.getMethod()).append(" ")
					.append(requestSpec.getURI()).append("\n");
			logBuilder.append("==================================================================\n");

			// 1. Capture Request Headers
			logBuilder.append("RequestHeaders:\n");
			if (requestSpec.getHeaders() != null) {
				requestSpec.getHeaders().forEach(header -> logBuilder.append("  ").append(header.getName()).append(": ")
						.append(header.getValue()).append("\n"));
			}

			// Capture Request Body if it exists
			if (requestSpec.getBody() != null) {
				logBuilder.append("RequestBody:\n  ").append(requestSpec.getBody().toString()).append("\n");
			}

			logBuilder.append("\n------------------------------------------------------------------\n");

			// Execute the actual request to get the response
			io.restassured.response.Response response = ctx.next(requestSpec, responseSpec);

			// 2. Capture Response Headers
			logBuilder.append("ResponseHeaders:\n");
			if (response.getHeaders() != null) {
				response.getHeaders().forEach(header -> logBuilder.append("  ").append(header.getName()).append(": ")
						.append(header.getValue()).append("\n"));
			}

			// 3. Capture Response Body
			logBuilder.append("\nResponseBody:\n");
			logBuilder.append(response.getBody().asPrettyString()).append("\n");
			logBuilder.append("==================================================================\n\n");

			// Write the built log directly to executionHeaders.log using our LogManager
			// file utility
			LogManager.writeToHeadersLog(logBuilder.toString());

			return response;
		});
	}

	@BeforeMethod
	public void beforeEveryTest(ITestResult result) {
		// 4. Automatically log the name of the test case right before it runs into
		// execution.log
		LogManager.log("==================================================");
		LogManager.log("STARTING TEST CASE: " + result.getMethod().getMethodName());
		LogManager.log("==================================================");

		// ❌ REMOVED: Duplicate filter attachments that were corrupting the file writes
	}

	@AfterMethod
	public void afterEveryTest(ITestResult result) {
		// 5. Logs pass/fail state to execution.log
		if (result.getStatus() == ITestResult.FAILURE) {
			LogManager.log("STATUS: FAILED ❌");
			LogManager.log("REASON FOR FAILURE: " + result.getThrowable().getMessage());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			LogManager.log("STATUS: PASSED  ✅");
		}
		LogManager.log("FINISHED TEST CASE: " + result.getMethod().getMethodName() + "\n");
	}

	@AfterSuite
	public void tearDownSuite() {
		LogManager.log("==================================================");
		LogManager.log("=== ALL SUITE EXECUTIONS COMPLETED SUCCESSFULLY ===");
		LogManager.log("==================================================");
	}

	boolean isSortedDescending(List<Integer> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			if (list.get(i) < list.get(i + 1)) {
				return false;
			}
		}
		return true;
	}

	boolean idSortedAscending(List<Integer> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			if (list.get(i) > list.get(i + 1)) {
				return false;
			}
		}
		return true;
	}
}