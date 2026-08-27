package com.orangehrm.pages;

import com.orangehrm.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final Logger log;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.log = LogManager.getLogger(getClass());
    }

    protected void click(By locator) {
        WaitUtils.waitForClickable(driver, locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = WaitUtils.waitForClickable(driver, locator);
        element.clear();
        element.sendKeys(text);
    }

    protected void typeSlow(By locator, String text) {
        WebElement element = WaitUtils.waitForClickable(driver, locator);
        element.clear();
        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
        }
    }

    protected String getText(By locator) {
        return WaitUtils.waitForVisible(driver, locator).getText().trim();
    }

    protected String getValue(By locator) {
        WebElement el = WaitUtils.waitForVisible(driver, locator);
        String val = el.getDomProperty("value");
        return val != null ? val.trim() : "";
    }

    protected boolean isDisplayed(By locator) {
        return WaitUtils.isElementPresent(driver, locator);
    }

    protected void selectByVisibleText(By locator, String text) {
        WebElement element = WaitUtils.waitForVisible(driver, locator);
        new Select(element).selectByVisibleText(text);
    }

    protected void clickByText(String text) {
        click(By.xpath("//*[normalize-space()='" + text + "']"));
    }

    protected void scrollToElement(By locator) {
        WebElement element = WaitUtils.waitForVisible(driver, locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void jsClick(By locator) {
        WebElement element = WaitUtils.waitForVisible(driver, locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected List<WebElement> getElements(By locator) {
        WaitUtils.waitForVisible(driver, locator);
        return driver.findElements(locator);
    }

    protected void waitForSuccessToast() {
        WaitUtils.waitForVisible(driver, By.cssSelector(".oxd-toast-content--success"));
        WaitUtils.waitForInvisible(driver, By.cssSelector(".oxd-toast-content--success"));
    }

    protected void selectOxdDropdown(By dropdownLocator, String optionText) {
        click(dropdownLocator);
        By option = By.xpath("//div[@role='option']//span[text()='" + optionText + "']");
        click(option);
    }
}
