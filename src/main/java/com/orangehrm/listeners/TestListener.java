package com.orangehrm.listeners;

import com.aventstack.extentreports.Status;
import com.orangehrm.config.DriverManager;
import com.orangehrm.utils.ReportManager;
import com.orangehrm.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Base64;

public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        log.info("====== Test Suite Started: {} ======", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("====== Test Suite Finished: {} ======", context.getName());
        ReportManager.flushReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        log.info("--> Starting test: {}", testName);
        ReportManager.createTest(testName, description != null ? description : testName);

        String[] groups = result.getMethod().getGroups();
        if (groups != null) {
            for (String group : groups) {
                ReportManager.getTest().assignCategory(group);
            }
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("<-- PASSED: {}", result.getMethod().getMethodName());
        ReportManager.logPass("Test passed");
        ReportManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        log.error("<-- FAILED: {} | Reason: {}", testName, result.getThrowable().getMessage());

        ReportManager.logFail("Test failed: " + result.getThrowable().getMessage());

        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            byte[] screenshotBytes = ScreenshotUtils.captureScreenshotAsBytes(driver);
            if (screenshotBytes.length > 0) {
                String base64 = Base64.getEncoder().encodeToString(screenshotBytes);
                ReportManager.logScreenshot(base64);
            }
            ScreenshotUtils.captureScreenshot(driver, testName);
        }

        ReportManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("--> SKIPPED: {}", result.getMethod().getMethodName());
        if (ReportManager.getTest() != null) {
            ReportManager.getTest().log(Status.SKIP, "Test skipped: " +
                    (result.getThrowable() != null ? result.getThrowable().getMessage() : "No reason"));
        }
        ReportManager.removeTest();
    }
}
