package com.orangehrm.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportManager {

    private static final Logger log = LogManager.getLogger(ReportManager.class);
    private static final String REPORT_DIR = "test-output/reports/";
    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();

    private ReportManager() {}

    public static synchronized ExtentReports getReports() {
        if (extentReports == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String reportPath = REPORT_DIR + "OrangeHRM_Report_" + timestamp + ".html";

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("OrangeHRM Automation Report");
            sparkReporter.config().setReportName("Employee Lifecycle Test Report");
            sparkReporter.config().setEncoding("utf-8");

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("Application", "OrangeHRM");
            extentReports.setSystemInfo("Environment", System.getProperty("env", "dev"));
            extentReports.setSystemInfo("Browser", System.getProperty("browser", "chrome"));
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            log.info("ExtentReports initialized at: {}", reportPath);
        }
        return extentReports;
    }

    public static synchronized void createTest(String testName, String description) {
        ExtentTest test = getReports().createTest(testName, description);
        testNode.set(test);
    }

    public static ExtentTest getTest() {
        return testNode.get();
    }

    public static void logInfo(String message) {
        if (testNode.get() != null) testNode.get().info(message);
    }

    public static void logPass(String message) {
        if (testNode.get() != null) testNode.get().pass(message);
    }

    public static void logFail(String message) {
        if (testNode.get() != null) testNode.get().fail(message);
    }

    public static void logScreenshot(String base64Screenshot) {
        if (testNode.get() != null && base64Screenshot != null) {
            testNode.get().addScreenCaptureFromBase64String(base64Screenshot, "Failure Screenshot");
        }
    }

    public static synchronized void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
            log.info("ExtentReports flushed");
        }
    }

    public static void removeTest() {
        testNode.remove();
    }
}
