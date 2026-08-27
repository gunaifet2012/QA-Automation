package com.orangehrm.pages;

import com.orangehrm.pageobjects.DashboardPageObjects;
import com.orangehrm.utils.WaitUtils;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public PIMPage navigateToPIM() {
        log.info("Navigating to PIM module");
        click(DashboardPageObjects.SIDEBAR_PIM);
        WaitUtils.waitForUrlContains(driver, "/pim");
        return new PIMPage(driver);
    }

    public void navigateToAdmin() {
        log.info("Navigating to Admin module");
        click(DashboardPageObjects.SIDEBAR_ADMIN);
        WaitUtils.waitForUrlContains(driver, "/admin");
    }

    public void logout() {
        log.info("Logging out");
        click(DashboardPageObjects.USER_DROPDOWN);
        click(DashboardPageObjects.LOGOUT_LINK);
        WaitUtils.waitForUrlContains(driver, "/auth/login");
    }

    public boolean isDashboardDisplayed() {
        return isDisplayed(DashboardPageObjects.MAIN_MENU);
    }

    public boolean isQuickLaunchDisplayed() {
        return isDisplayed(DashboardPageObjects.QUICK_LAUNCH_PANEL);
    }

    public String getPageTitle() {
        return getText(DashboardPageObjects.DASHBOARD_HEADER);
    }
}
