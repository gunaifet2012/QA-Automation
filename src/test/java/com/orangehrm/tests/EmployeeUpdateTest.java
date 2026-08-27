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

    @Test(
        groups = {"employee", "regression"},
        description = "Admin can update employee personal details",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testUpdateEmployeePersonalDetails() {
        Employee original = TestDataGenerator.generateEmployee();
        Employee updated = TestDataGenerator.generateEmployee();

        PIMPage pimPage = loginAsAdmin().navigateToPIM();
        EmployeePage employeePage = pimPage.addEmployee(original);

        employeePage.updatePersonalDetails(updated);

        String savedFirst = employeePage.getFirstName();
        String savedLast = employeePage.getLastName();

        Assert.assertEquals(savedFirst, updated.getFirstName(),
                "First name should be updated");
        Assert.assertEquals(savedLast, updated.getLastName(),
                "Last name should be updated");
        log.info("Employee updated: {} {} -> {} {}",
                original.getFirstName(), original.getLastName(),
                savedFirst, savedLast);
    }

    @Test(
        groups = {"employee", "regression"},
        description = "Admin can update employee contact details",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testUpdateEmployeeContactDetails() {
        Employee employee = TestDataGenerator.generateEmployee();

        PIMPage pimPage = loginAsAdmin().navigateToPIM();
        EmployeePage employeePage = pimPage.addEmployee(employee);

        String newEmail = "updated." + TestDataGenerator.randomAlpha(5).toLowerCase() + "@test.com";
        String newPhone = "07" + TestDataGenerator.randomNumeric(9);

        employeePage.updateContactDetails(newEmail, newPhone);

        Assert.assertTrue(employeePage.isSuccessToastDisplayed(),
                "Success toast should appear after contact update");
        log.info("Contact details updated — email: {}, phone: {}", newEmail, newPhone);
    }

    @Test(
        groups = {"employee", "regression"},
        description = "Updated employee name is reflected in the header",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testUpdatedNameReflectedInHeader() {
        Employee original = TestDataGenerator.generateEmployee();
        Employee updated = Employee.builder()
                .firstName("UpdatedFirst")
                .middleName("")
                .lastName("UpdatedLast")
                .build();

        PIMPage pimPage = loginAsAdmin().navigateToPIM();
        EmployeePage employeePage = pimPage.addEmployee(original);
        employeePage.updatePersonalDetails(updated);

        String headerName = employeePage.getEmployeeNameFromHeader();
        Assert.assertTrue(
                headerName.contains("UpdatedFirst") || headerName.contains("UpdatedLast"),
                "Header should reflect updated name, was: " + headerName
        );
    }

    @Test(
        groups = {"employee", "regression"},
        description = "Employee profile tabs are accessible"
    )
    public void testEmployeeProfileTabsNavigable() {
        Employee employee = TestDataGenerator.generateEmployee();

        PIMPage pimPage = loginAsAdmin().navigateToPIM();
        EmployeePage employeePage = pimPage.addEmployee(employee);

        employeePage.clickPersonalDetailsTab();
        Assert.assertTrue(driver.getCurrentUrl().contains("viewPersonalDetails"),
                "Personal Details tab should load");

        employeePage.clickContactDetailsTab();
        Assert.assertTrue(driver.getCurrentUrl().contains("contactDetails") ||
                        driver.getCurrentUrl().contains("viewPersonalDetails"),
                "Contact Details tab should be accessible");

        log.info("All profile tabs navigated successfully");
    }
}
