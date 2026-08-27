package com.orangehrm.base;

import com.orangehrm.config.ConfigManager;
import com.orangehrm.config.DriverManager;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.utils.RetryAnalyzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    protected final Logger log = LogManager.getLogger(getClass());
    protected WebDriver driver;
    protected ConfigManager config;
    protected LoginPage loginPage;
    protected DashboardPage dashboardPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        config = ConfigManager.getInstance();
        DriverManager.initDriver();
        driver = DriverManager.getDriver();

        driver.get(config.getBaseUrl());
        loginPage = new LoginPage(driver);
        log.info("Browser launched and navigated to: {}", config.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
        log.info("Test teardown complete");
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
