package com.orangehrm.pageobjects;

import org.openqa.selenium.By;

public class DashboardPageObjects {

    private DashboardPageObjects() {}

    public static final By USER_DROPDOWN = By.cssSelector(".oxd-userdropdown-tab");
    public static final By LOGOUT_LINK = By.xpath("//a[text()='Logout']");
    public static final By SIDEBAR_PIM = By.xpath("//span[text()='PIM']");
    public static final By SIDEBAR_ADMIN = By.xpath("//span[text()='Admin']");
    public static final By SIDEBAR_LEAVE = By.xpath("//span[text()='Leave']");
    public static final By DASHBOARD_HEADER = By.cssSelector(".oxd-topbar-header-breadcrumb h6");
    public static final By QUICK_LAUNCH_PANEL = By.cssSelector(".orangehrm-quick-launch");
    public static final By MAIN_MENU = By.cssSelector(".oxd-main-menu");
    public static final By PAGE_TITLE = By.cssSelector(".oxd-topbar-header-breadcrumb");
}
