package com.orangehrm.pageobjects;

import org.openqa.selenium.By;

public class EmployeePageObjects {

    private EmployeePageObjects() {}

    // Profile Tabs
    public static final By TAB_PERSONAL_DETAILS = By.xpath("//a[normalize-space()='Personal Details']");
    public static final By TAB_CONTACT_DETAILS = By.xpath("//a[normalize-space()='Contact Details']");
    public static final By TAB_EMERGENCY_CONTACTS = By.xpath("//a[normalize-space()='Emergency Contacts']");
    public static final By TAB_DEPENDENTS = By.xpath("//a[normalize-space()='Dependents']");
    public static final By TAB_JOB = By.xpath("//a[normalize-space()='Job']");

    // Personal Details
    public static final By FIRST_NAME = By.name("firstName");
    public static final By MIDDLE_NAME = By.name("middleName");
    public static final By LAST_NAME = By.name("lastName");
    public static final By EMPLOYEE_ID = By.xpath("//label[text()='Employee Id']/following::input[1]");
    public static final By DRIVERS_LICENSE = By.xpath("//label[text()=\"Driver's License Number\"]/following::input[1]");
    public static final By LICENSE_EXPIRY_DATE = By.xpath("//label[text()='License Expiry Date']/following::input[1]");
    public static final By NATIONALITY = By.xpath("//label[text()='Nationality']/following::div[@class='oxd-select-text-input'][1]");
    public static final By MARITAL_STATUS = By.xpath("//label[text()='Marital Status']/following::div[@class='oxd-select-text-input'][1]");
    public static final By DATE_OF_BIRTH = By.xpath("//label[text()='Date of Birth']/following::input[1]");
    public static final By GENDER_MALE = By.xpath("//label[text()='Male']/preceding-sibling::input");
    public static final By GENDER_FEMALE = By.xpath("//label[text()='Female']/preceding-sibling::input");

    // Contact Details
    public static final By STREET1 = By.xpath("//label[text()='Street 1']/following::input[1]");
    public static final By CITY = By.xpath("//label[text()='City']/following::input[1]");
    public static final By COUNTRY = By.xpath("//label[text()='Country']/following::div[@class='oxd-select-text-input'][1]");
    public static final By WORK_EMAIL = By.xpath("//label[text()='Work Email']/following::input[1]");
    public static final By MOBILE_PHONE = By.xpath("//label[text()='Mobile']/following::input[1]");

    // Save / Cancel
    public static final By SAVE_BUTTON = By.xpath("//button[@type='submit']");
    public static final By CANCEL_BUTTON = By.xpath("//button[@type='button'][normalize-space()='Cancel']");
    public static final By TOAST_SUCCESS = By.cssSelector(".oxd-toast-content--success");
    public static final By EMPLOYEE_NAME_HEADER = By.cssSelector(".orangehrm-edit-employee-name h6");

    // Job Details
    public static final By JOB_TITLE = By.xpath("//label[text()='Job Title']/following::div[@class='oxd-select-text-input'][1]");
    public static final By JOB_CATEGORY = By.xpath("//label[text()='Job Category']/following::div[@class='oxd-select-text-input'][1]");
    public static final By LOCATION = By.xpath("//label[text()='Location']/following::div[@class='oxd-select-text-input'][1]");
    public static final By JOINED_DATE = By.xpath("//label[text()='Joined Date']/following::input[1]");
}
