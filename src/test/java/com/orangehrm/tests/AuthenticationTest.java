package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.utils.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthenticationTest extends BaseTest {

    @Test(
        groups = {"auth", "smoke", "regression"},
        description = "Valid admin credentials should navigate to dashboard",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testValidAdminLogin() {
        dashboardPage = loginAsAdmin();

        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be visible after successful login");
        Assert.assertTrue(driver.getCurrentUrl().contains("/dashboard"),
                "URL should contain /dashboard after login");
        log.info("Valid login test passed");
    }

    @Test(
        groups = {"auth", "smoke", "regression"},
        description = "Invalid credentials should show error message",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testInvalidCredentialsShowsError() {
        loginPage.enterUsername("invalid_user");
        loginPage.enterPassword("wrong_password");
        loginPage.clickLogin();

        String errorMessage = loginPage.getErrorMessage();
        Assert.assertNotNull(errorMessage, "Error message should be present");
        Assert.assertTrue(
                errorMessage.contains("Invalid credentials") || errorMessage.contains("invalid"),
                "Error message should indicate invalid credentials, was: " + errorMessage
        );
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Should remain on login page after failed login");
    }

    @Test(
        groups = {"auth", "regression"},
        description = "Empty credentials should show validation error"
    )
    public void testEmptyCredentialsShowsValidation() {
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Should remain on login page with empty credentials");
    }

    @Test(
        groups = {"auth", "regression"},
        description = "Login page should display logo and required elements"
    )
    public void testLoginPageElements() {
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Login button should be visible");
        Assert.assertTrue(loginPage.isLogoDisplayed(),
                "OrangeHRM logo should be visible");
    }

    @Test(
        groups = {"auth", "regression"},
        description = "Successful logout should return to login page"
    )
    public void testLogout() {
        dashboardPage = loginAsAdmin();
        dashboardPage.logout();

        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Login page should be displayed after logout");
        Assert.assertTrue(driver.getCurrentUrl().contains("/auth/login"),
                "URL should point to login page after logout");
    }

    @Test(
        groups = {"auth", "regression"},
        description = "Correct username with wrong password should fail"
    )
    public void testCorrectUsernameWrongPassword() {
        loginPage.enterUsername(config.getAdminUsername());
        loginPage.enterPassword("wrongpassword123");
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Should remain on login page with wrong password");
        String error = loginPage.getErrorMessage();
        Assert.assertFalse(error.isEmpty(), "Error message should be shown");
    }
}
