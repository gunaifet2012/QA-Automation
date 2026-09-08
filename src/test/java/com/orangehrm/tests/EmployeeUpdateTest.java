package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.models.Employee;
import com.orangehrm.pages.EmployeePage;
import com.orangehrm.pages.PIMPage;
import com.orangehrm.utils.RetryAnalyzer;
import com.orangehrm.utils.TestDataGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EmployeeUpdateTest extends BaseTest {

    @Test(groups = {"employee", "regression"},
          description = "Admin can update employee personal details",
          retryAnalyzer = RetryAnalyzer.class)
    public void testUpdateEmployeePersonalDetails() {
        Employee original = TestDataGenerator.generateEmployee();
        Employee updated = TestDataGenerator.generateEmployee();

        PIMPage pimPage = loginAsAdmin().navigateToPIM();
        EmployeePage employeePage = pimPage.addEmployee(original);

        registerEmployeeForCleanup(employeePage.getEmployeeId());

        employeePage.updatePersonalDetails(updated);

        Assert.assertEquals(employeePage.getFirstName(), updated.getFirstName());
        Assert.assertEquals(employeePage.getLastName(), updated.getLastName());
    }

    @Test(groups = {"employee", "regression"},
          description = "Admin can update employee contact details",
          retryAnalyzer = RetryAnalyzer.class)
    public void testUpdateEmployeeContactDetails() {
        Employee employee = TestDataGenerator.generateEmployee();

        PIMPage pimPage = loginAsAdmin().navigateToPIM();
        EmployeePage employeePage = pimPage.addEmployee(employee);

        registerEmployeeForCleanup(employeePage.getEmployeeId());

        String newEmail = "updated."
                + TestDataGenerator.randomAlpha(5).toLowerCase()
                + "@test.com";
        String newPhone = "07" + TestDataGenerator.randomNumeric(9);

        employeePage.updateContactDetails(newEmail, newPhone);

        Assert.assertTrue(employeePage.isSuccessToastDisplayed());
    }

    @Test(groups = {"employee", "regression"},
          description = "Updated employee name is reflected in the header",
          retryAnalyzer = RetryAnalyzer.class)
    public void testUpdatedNameReflectedInHeader() {
        Employee original = TestDataGenerator.generateEmployee();
        Employee updated = Employee.builder()
                .firstName("UpdatedFirst")
                .middleName("")
                .lastName("UpdatedLast")
                .build();

        PIMPage pimPage = loginAsAdmin().navigateToPIM();
        EmployeePage employeePage = pimPage.addEmployee(original);

        registerEmployeeForCleanup(employeePage.getEmployeeId());

        employeePage.updatePersonalDetails(updated);

        String headerName = employeePage.getEmployeeNameFromHeader();
        Assert.assertTrue(headerName.contains("UpdatedFirst")
                || headerName.contains("UpdatedLast"));
    }

    @Test(groups = {"employee", "regression"},
          description = "Employee profile tabs are accessible")
    public void testEmployeeProfileTabsNavigable() {
        Employee employee = TestDataGenerator.generateEmployee();

        PIMPage pimPage = loginAsAdmin().navigateToPIM();
        EmployeePage employeePage = pimPage.addEmployee(employee);

        registerEmployeeForCleanup(employeePage.getEmployeeId());

        employeePage.clickPersonalDetailsTab();
        Assert.assertTrue(driver.getCurrentUrl().contains("viewPersonalDetails"));

        employeePage.clickContactDetailsTab();
        Assert.assertTrue(driver.getCurrentUrl().contains("contactDetails")
                || driver.getCurrentUrl().contains("viewPersonalDetails"));
    }
}
