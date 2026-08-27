package com.orangehrm.pages;

import com.orangehrm.pageobjects.LoginPageObjects;
import com.orangehrm.utils.WaitUtils;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void enterUsername(String username) {
        type(LoginPageObjects.USERNAME_INPUT, username);
    }

    public void enterPassword(String password) {
        type(LoginPageObjects.PASSWORD_INPUT, password);
    }

    public void clickLogin() {
        click(LoginPageObjects.LOGIN_BUTTON);
    }

    public DashboardPage login(String username, String password) {
        log.info("Logging in as user: {}", username);
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        WaitUtils.waitForUrlContains(driver, "/dashboard");
        return new DashboardPage(driver);
    }

    public String getErrorMessage() {
        return getText(LoginPageObjects.ERROR_MESSAGE);
    }

    public boolean isLoginPageDisplayed() {
        return isDisplayed(LoginPageObjects.LOGIN_BUTTON);
    }

    public boolean isLogoDisplayed() {
        return isDisplayed(LoginPageObjects.LOGO);
    }

    public void clickForgotPassword() {
        click(LoginPageObjects.FORGOT_PASSWORD_LINK);
    }
}
