package com.orangehrm.pageobjects;

import org.openqa.selenium.By;

public class LoginPageObjects {

    private LoginPageObjects() {}

    public static final By USERNAME_INPUT = By.name("username");
    public static final By PASSWORD_INPUT = By.name("password");
    public static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    public static final By ERROR_MESSAGE = By.cssSelector(".oxd-alert-content-text");
    public static final By FORGOT_PASSWORD_LINK = By.cssSelector(".orangehrm-login-forgot");
    public static final By LOGO = By.cssSelector(".orangehrm-login-branding img");
}
