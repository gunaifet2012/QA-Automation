package com.orangehrm.utils;

import com.orangehrm.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private static final Logger log = LogManager.getLogger(WaitUtils.class);
    private static final int DEFAULT_TIMEOUT = ConfigManager.getInstance().getExplicitWait();

    private WaitUtils() {}

    public static WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public static WebDriverWait getWait(WebDriver driver, int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }

    public static WebElement waitForVisible(WebDriver driver, By locator) {
        log.debug("Waiting for element to be visible: {}", locator);
        return getWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisible(WebDriver driver, By locator, int seconds) {
        return getWait(driver, seconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator) {
        log.debug("Waiting for element to be clickable: {}", locator);
        return getWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void waitForInvisible(WebDriver driver, By locator) {
        log.debug("Waiting for element to disappear: {}", locator);
        getWait(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void waitForUrlContains(WebDriver driver, String urlFragment) {
        getWait(driver).until(ExpectedConditions.urlContains(urlFragment));
    }

    public static void waitForTextPresent(WebDriver driver, By locator, String text) {
        getWait(driver).until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public static boolean isElementPresent(WebDriver driver, By locator) {
        try {
            waitForVisible(driver, locator, 3);
            return true;
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    public static String waitForFieldValue(WebDriver driver, By locator) {
        WebElement el = waitForVisible(driver, locator);
        getWait(driver).until(d -> {
            String v = el.getDomProperty("value");
            return v != null && !v.trim().isEmpty();
        });
        String val = el.getDomProperty("value");
        return val != null ? val.trim() : "";
    }

    public static void waitForPageLoad(WebDriver driver) {
        getWait(driver).until(d ->
                ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
    }

    public static void waitForAjax(WebDriver driver) {
        getWait(driver).until(d -> {
            try {
                return ((JavascriptExecutor) d).executeScript("return jQuery.active").toString().equals("0");
            } catch (Exception e) {
                return true;
            }
        });
    }
}
