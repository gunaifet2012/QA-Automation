package com.orangehrm.pages;

import com.orangehrm.models.Employee;
import com.orangehrm.pageobjects.PIMPageObjects;
import com.orangehrm.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PIMPage extends BasePage {

    public PIMPage(WebDriver driver) {
        super(driver);
    }

    public EmployeePage clickAddEmployee() {
        log.info("Clicking Add Employee button");
        click(PIMPageObjects.ADD_BUTTON);
        WaitUtils.waitForUrlContains(driver, "/pim/addEmployee");
        return new EmployeePage(driver);
    }

    public EmployeePage addEmployee(Employee employee) {
        EmployeePage employeePage = clickAddEmployee();
        employeePage.fillAddEmployeeForm(employee);
        employeePage.saveEmployee();
        return employeePage;
    }

    public void searchByEmployeeName(String name) {
        log.info("Searching for employee: {}", name);
        typeSlow(PIMPageObjects.EMPLOYEE_NAME_SEARCH, name);
        WaitUtils.waitForVisible(driver, By.cssSelector(".oxd-autocomplete-option"));
        click(By.cssSelector(".oxd-autocomplete-option"));
        click(PIMPageObjects.SEARCH_BUTTON);
    }

    public void searchByEmployeeId(String employeeId) {
        log.info("Searching by Employee ID: {}", employeeId);
        type(PIMPageObjects.EMPLOYEE_ID_SEARCH, employeeId);
        click(PIMPageObjects.SEARCH_BUTTON);
    }

    public void clickSearch() {
        click(PIMPageObjects.SEARCH_BUTTON);
    }

    public void clickReset() {
        click(PIMPageObjects.RESET_BUTTON);
    }

    public int getRowCount() {
        WaitUtils.waitForVisible(driver, PIMPageObjects.TABLE_HEADER);
        List<WebElement> rows = driver.findElements(PIMPageObjects.TABLE_ROWS);
        return rows.size();
    }

    public boolean isNoRecordsFound() {
        return isDisplayed(PIMPageObjects.NO_RECORDS_FOUND);
    }

    public EmployeePage openEmployeeByName(String fullName) {
        log.info("Opening employee profile: {}", fullName);
        By editLink = By.xpath("//div[@class='oxd-table-cell oxd-padding-cell'][3]//p[text()='" + fullName + "']/../../following-sibling::div//button[@title='Edit']");
        click(editLink);
        return new EmployeePage(driver);
    }

    public EmployeePage openFirstEmployeeInList() {
        By firstEditButton = By.cssSelector(".oxd-table-body .oxd-table-row:first-child button[title='Edit']");
        click(firstEditButton);
        return new EmployeePage(driver);
    }

    public void deleteEmployeeByName(String fullName) {
        log.info("Deleting employee: {}", fullName);
        By deleteButton = By.xpath("//p[text()='" + fullName + "']/ancestor::div[@class='oxd-table-row oxd-table-row--with-border']//button[@title='Delete']");
        click(deleteButton);
        click(PIMPageObjects.CONFIRM_DELETE_BUTTON);
        waitForSuccessToast();
    }

    public void deleteAllSelectedEmployees() {
        click(PIMPageObjects.DELETE_SELECTED_BUTTON);
        click(PIMPageObjects.CONFIRM_DELETE_BUTTON);
        waitForSuccessToast();
    }

    public void selectEmployeeCheckboxByName(String fullName) {
        By checkbox = By.xpath("//p[text()='" + fullName + "']/ancestor::div[@class='oxd-table-row oxd-table-row--with-border']//input[@type='checkbox']");
        click(checkbox);
    }

    public String getSuccessToastMessage() {
        return getText(PIMPageObjects.TOAST_MESSAGE);
    }

    public boolean isSuccessToastDisplayed() {
        return isDisplayed(PIMPageObjects.TOAST_SUCCESS);
    }

    public boolean isEmployeeVisible(String fullName) {
        return isDisplayed(By.xpath("//p[text()='" + fullName + "']"));
    }
}
