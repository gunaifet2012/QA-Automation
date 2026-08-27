package com.orangehrm.pages;

import com.orangehrm.models.Employee;
import com.orangehrm.pageobjects.EmployeePageObjects;
import com.orangehrm.pageobjects.PIMPageObjects;
import com.orangehrm.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EmployeePage extends BasePage {

    public EmployeePage(WebDriver driver) {
        super(driver);
    }

    public void fillAddEmployeeForm(Employee employee) {
        log.info("Filling Add Employee form for: {} {}", employee.getFirstName(), employee.getLastName());
        type(PIMPageObjects.FIRST_NAME_INPUT, employee.getFirstName());
        if (employee.getMiddleName() != null && !employee.getMiddleName().isEmpty()) {
            type(PIMPageObjects.MIDDLE_NAME_INPUT, employee.getMiddleName());
        }
        type(PIMPageObjects.LAST_NAME_INPUT, employee.getLastName());

        if (employee.getEmployeeId() != null) {
            type(PIMPageObjects.EMPLOYEE_ID_INPUT, employee.getEmployeeId());
        }
    }

    public void saveEmployee() {
        log.info("Saving employee record");
        WaitUtils.waitForInvisible(driver, By.cssSelector(".oxd-form-loader"));
        click(PIMPageObjects.SAVE_BUTTON);
        WaitUtils.waitForUrlContains(driver, "/pim/viewPersonalDetails");
    }

    public void updatePersonalDetails(Employee updatedEmployee) {
        log.info("Updating personal details");
        click(EmployeePageObjects.TAB_PERSONAL_DETAILS);

        type(EmployeePageObjects.FIRST_NAME, updatedEmployee.getFirstName());
        type(EmployeePageObjects.MIDDLE_NAME, updatedEmployee.getMiddleName());
        type(EmployeePageObjects.LAST_NAME, updatedEmployee.getLastName());

        click(EmployeePageObjects.SAVE_BUTTON);
        waitForSuccessToast();
    }

    public void updateContactDetails(String email, String mobile) {
        log.info("Updating contact details");
        click(EmployeePageObjects.TAB_CONTACT_DETAILS);
        WaitUtils.waitForVisible(driver, EmployeePageObjects.WORK_EMAIL);

        type(EmployeePageObjects.WORK_EMAIL, email);
        type(EmployeePageObjects.MOBILE_PHONE, mobile);

        click(EmployeePageObjects.SAVE_BUTTON);
        waitForSuccessToast();
    }

    public String getFirstName() {
        return WaitUtils.waitForFieldValue(driver, EmployeePageObjects.FIRST_NAME);
    }

    public String getLastName() {
        return WaitUtils.waitForFieldValue(driver, EmployeePageObjects.LAST_NAME);
    }

    public String getEmployeeId() {
        return getValue(EmployeePageObjects.EMPLOYEE_ID);
    }

    public String getEmployeeNameFromHeader() {
        return getText(EmployeePageObjects.EMPLOYEE_NAME_HEADER);
    }

    public boolean isSuccessToastDisplayed() {
        return isDisplayed(EmployeePageObjects.TOAST_SUCCESS);
    }

    public void clickPersonalDetailsTab() {
        click(EmployeePageObjects.TAB_PERSONAL_DETAILS);
    }

    public void clickJobTab() {
        click(EmployeePageObjects.TAB_JOB);
    }

    public void clickContactDetailsTab() {
        click(EmployeePageObjects.TAB_CONTACT_DETAILS);
    }

    public String extractEmployeeIdFromUrl() {
        String url = driver.getCurrentUrl();
        String[] parts = url.split("/");
        return parts[parts.length - 1];
    }
}
