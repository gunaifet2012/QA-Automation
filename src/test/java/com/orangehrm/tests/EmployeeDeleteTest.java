package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.models.Employee;
import com.orangehrm.pages.EmployeePage;
import com.orangehrm.pages.PIMPage;
import com.orangehrm.utils.RetryAnalyzer;
import com.orangehrm.utils.TestDataGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EmployeeDeleteTest extends BaseTest {

    @Test(groups = {"employee", "regression"},
          description = "Admin can delete an employee from the employee list",
          retryAnalyzer = RetryAnalyzer.class)
    public void testDeleteSingleEmployee() {
        Employee employee = TestDataGenerator.generateEmployee();

        PIMPage pimPage = loginAsAdmin().navigateToPIM();
        EmployeePage employeePage = pimPage.addEmployee(employee);

        String employeeId = employeePage.getEmployeeId();
        registerEmployeeForCleanup(employeeId);

        driver.navigate().to(config.getBaseUrl() + "/web/index.php/pim/viewEmployeeList");
        pimPage.deleteEmployeeByName(employee.getFullName());

        Assert.assertTrue(pimPage.isSuccessToastDisplayed() || pimPage.isNoRecordsFound());

        unregisterEmployeeFromCleanup(employeeId);
    }

    @Test(groups = {"employee", "regression"},
          description = "Deleted employee no longer appears in search results",
          retryAnalyzer = RetryAnalyzer.class)
    public void testDeletedEmployeeNotInSearch() {
        Employee employee = TestDataGenerator.generateEmployee();

        PIMPage pimPage = loginAsAdmin().navigateToPIM();
        EmployeePage employeePage = pimPage.addEmployee(employee);
        String empId = employeePage.getEmployeeId();

        registerEmployeeForCleanup(empId);

        driver.navigate().to(config.getBaseUrl() + "/web/index.php/pim/viewEmployeeList");
        pimPage.deleteEmployeeByName(employee.getFullName());

        driver.navigate().to(config.getBaseUrl() + "/web/index.php/pim/viewEmployeeList");
        pimPage.searchByEmployeeId(empId);

        Assert.assertTrue(pimPage.isNoRecordsFound());

        unregisterEmployeeFromCleanup(empId);
    }

    @Test(groups = {"employee", "regression"},
          description = "Admin can bulk-select and delete multiple employees",
          retryAnalyzer = RetryAnalyzer.class)
    public void testBulkDeleteEmployees() {
        Employee emp1 = TestDataGenerator.generateEmployee();
        Employee emp2 = TestDataGenerator.generateEmployee();

        PIMPage pimPage = loginAsAdmin().navigateToPIM();

        EmployeePage page1 = pimPage.addEmployee(emp1);
        String emp1Id = page1.getEmployeeId();
        registerEmployeeForCleanup(emp1Id);

        driver.navigate().to(config.getBaseUrl() + "/web/index.php/pim/viewEmployeeList");

        EmployeePage page2 = pimPage.addEmployee(emp2);
        String emp2Id = page2.getEmployeeId();
        registerEmployeeForCleanup(emp2Id);

        driver.navigate().to(config.getBaseUrl() + "/web/index.php/pim/viewEmployeeList");

        pimPage.selectEmployeeCheckboxByName(emp1.getFullName());
        pimPage.selectEmployeeCheckboxByName(emp2.getFullName());
        pimPage.deleteAllSelectedEmployees();

        Assert.assertFalse(pimPage.isEmployeeVisible(emp1.getFullName()));

        unregisterEmployeeFromCleanup(emp1Id);
        unregisterEmployeeFromCleanup(emp2Id);
    }
}
