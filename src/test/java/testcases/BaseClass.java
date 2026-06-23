package testcases;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import io.restassured.RestAssured;
import routes.Routes;
import utils.ConfigReader;
import utils.LogManager; // Assuming your manual logger class name is LogManager

public class BaseClass {

    // Keep public static so any payload or utility class can read variables effortlessly
    public static ConfigReader configReader;

    @BeforeSuite
    public void setupSuite() {
        // 1. Initialize and clear out the local manual log file BEFORE any test class runs
        LogManager.initLogs();
    }

    @BeforeClass
    public void setup() {
        // 2. CRITICAL FIX: Initialize config reader FIRST to avoid NullPointerExceptions later
        configReader = new ConfigReader();

        // 3. Set up the target global URI context for RestAssured configuration
        RestAssured.baseURI = Routes.BASE_URL;
    }

    @BeforeMethod
    public void beforeEveryTest(ITestResult result) {
        // 4. Automatically log the name of the test case right before it runs
        LogManager.log("==================================================");
        LogManager.log("STARTING TEST CASE: " + result.getMethod().getMethodName());
        LogManager.log("==================================================");
    }

    @AfterMethod
    public void afterEveryTest(ITestResult result) {
        // 5. Automatically intercept execution state to log pass/fail status
        if (result.getStatus() == ITestResult.FAILURE) {
            LogManager.log("STATUS: FAILED ❌");
            // Captures the exact assertion error message or failure reason
            LogManager.log("REASON FOR FAILURE: " + result.getThrowable().getMessage());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            LogManager.log("STATUS: PASSED  ✅");
        }
        LogManager.log("FINISHED TEST CASE: " + result.getMethod().getMethodName() + "\n");
    }

    @AfterSuite
    public void tearDownSuite() {
        // 6. Sign off the execution cycle at the absolute end of all running suites
        LogManager.log("==================================================");
        LogManager.log("=== ALL SUITE EXECUTIONS COMPLETED SUCCESSFULLY ===");
        LogManager.log("==================================================");
    }
}