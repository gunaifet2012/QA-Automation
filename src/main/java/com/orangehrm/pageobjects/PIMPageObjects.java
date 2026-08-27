package com.orangehrm.pageobjects;

import org.openqa.selenium.By;

public class PIMPageObjects {

    private PIMPageObjects() {}

    // Employee List
    public static final By ADD_BUTTON = By.xpath("//button[normalize-space()='Add']");
    public static final By EMPLOYEE_NAME_SEARCH = By.xpath("//label[text()='Employee Name']/following::input[1]");
    public static final By EMPLOYEE_ID_SEARCH = By.xpath("//label[text()='Employee Id']/following::input[1]");
    public static final By SEARCH_BUTTON = By.xpath("//button[@type='submit'][normalize-space()='Search']");
    public static final By RESET_BUTTON = By.xpath("//button[@type='reset']");
    public static final By TABLE_ROWS = By.cssSelector(".oxd-table-body .oxd-table-row");
    public static final By TABLE_HEADER = By.cssSelector(".oxd-table-header");
    public static final By NO_RECORDS_FOUND = By.xpath("//span[text()='No Records Found']");
    public static final By RECORDS_FOUND = By.cssSelector(".orangehrm-horizontal-padding .oxd-text");

    // Add Employee Form
    public static final By FIRST_NAME_INPUT = By.name("firstName");
    public static final By MIDDLE_NAME_INPUT = By.name("middleName");
    public static final By LAST_NAME_INPUT = By.name("lastName");
    public static final By EMPLOYEE_ID_INPUT = By.xpath("//label[text()='Employee Id']/following::input[1]");
    public static final By CREATE_LOGIN_TOGGLE = By.xpath("//p[text()='Create Login Details']//following-sibling::div//span[@class='oxd-switch-input']");
    public static final By LOGIN_USERNAME_INPUT = By.xpath("//label[text()='Username']/following::input[1]");
    public static final By LOGIN_PASSWORD_INPUT = By.xpath("//label[text()='Password']/following::input[1]");
    public static final By LOGIN_CONFIRM_PASSWORD = By.xpath("//label[text()='Confirm Password']/following::input[1]");
    public static final By STATUS_DROPDOWN = By.xpath("//label[text()='Status']/following::div[@class='oxd-select-text-input'][1]");
    public static final By SAVE_BUTTON = By.xpath("//button[@type='submit']");
    public static final By PROFILE_PICTURE = By.cssSelector(".employee-image");

    // Table actions
    public static final By DELETE_CHECKBOX = By.cssSelector("input[type='checkbox']");
    public static final By DELETE_SELECTED_BUTTON = By.xpath("//button[normalize-space()='Delete Selected']");
    public static final By CONFIRM_DELETE_BUTTON = By.xpath("//button[normalize-space()='Yes, Delete']");
    public static final By TOAST_SUCCESS = By.cssSelector(".oxd-toast-content--success");
    public static final By TOAST_MESSAGE = By.cssSelector(".oxd-toast-content .oxd-text");

    // Filter dropdown
    public static final By SUPERVISOR_NAME_SEARCH = By.xpath("//label[text()='Supervisor Name']/following::input[1]");
    public static final By JOB_TITLE_DROPDOWN = By.xpath("//label[text()='Job Title']/following::div[@class='oxd-select-text-input'][1]");
    public static final By EMPLOYMENT_STATUS_DROPDOWN = By.xpath("//label[text()='Employment Status']/following::div[@class='oxd-select-text-input'][1]");
}
