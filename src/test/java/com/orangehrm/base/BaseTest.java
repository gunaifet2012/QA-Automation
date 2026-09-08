package com.orangehrm.base;

import com.orangehrm.config.ConfigManager;
import com.orangehrm.config.DriverManager;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.PIMPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class BaseTest {

    protected final Logger log = LogManager.getLogger(getClass());
    protected WebDriver driver;
    protected ConfigManager config;
    protected LoginPage loginPage;
    protected DashboardPage dashboardPage;

    private final Set<String> employeeIdsToCleanup = new LinkedHashSet<>();

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        config = ConfigManager.getInstance();
        DriverManager.initDriver();
        driver = DriverManager.getDriver();

        driver.get(config.getBaseUrl());
        loginPage = new LoginPage(driver);
        log.info("Browser launched and navigated to: {}", config.getBaseUrl());
    }

    protected void registerEmployeeForCleanup(String employeeId) {
        if (employeeId != null && !employeeId.trim().isEmpty()) {
            employeeIdsToCleanup.add(employeeId.trim());
            log.info("Registered employee for cleanup. ID: {}", employeeId);
        }
    }

    protected void unregisterEmployeeFromCleanup(String employeeId) {
        if (employeeId != null && employeeIdsToCleanup.remove(employeeId.trim())) {
            log.info("Removed employee from cleanup registry. ID: {}", employeeId);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            cleanupTestData();
        } catch (Exception e) {
            log.warn("Unexpected error during test-data cleanup: {}", e.getMessage(), e);
        } finally {
            employeeIdsToCleanup.clear();
            DriverManager.quitDriver();
            log.info("Test teardown complete. Test: {}, Status: {}",
                    result.getName(), result.getStatus());
        }
    }

    private void cleanupTestData() {
        if (driver == null || employeeIdsToCleanup.isEmpty()) {
            return;
        }

        PIMPage pimPage = new PIMPage(driver);

        for (String employeeId : employeeIdsToCleanup) {
            try {
                driver.navigate().to(
                        config.getBaseUrl() + "/web/index.php/pim/viewEmployeeList"
                );

                boolean deleted = pimPage.deleteEmployeeByIdIfPresent(employeeId);

                if (deleted) {
                    log.info("Cleanup successful. Employee ID: {}", employeeId);
                } else {
                    log.info("Cleanup skipped; employee already absent. ID: {}", employeeId);
                }
            } catch (Exception cleanupException) {
                log.warn("Cleanup failed for employee ID {}: {}",
                        employeeId, cleanupException.getMessage());
            }
        }
    }

    protected DashboardPage loginAsAdmin() {
        dashboardPage = loginPage.login(
                config.getAdminUsername(),
                config.getAdminPassword()
        );
        return dashboardPage;
    }

    protected DashboardPage loginAs(String username, String password) {
        return loginPage.login(username, password);
    }
}
