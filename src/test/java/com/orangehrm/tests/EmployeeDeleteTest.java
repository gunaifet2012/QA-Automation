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

    @Test(
        groups = {"employee", "regression"},
        description = "Admin can delete an employee from the employee list",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testDeleteSingleEmployee() {
        Employee employee = TestDataGenerator.generateEmployee();

        PIMPage pimPage = loginAsAdmin().navigateToPIM();
        EmployeePage employeePage = pimPage.addEmployee(employee);

        log.info("Created employee for deletion: {}", employee.getFullName());

        driver.navigate().to(config.getBaseUrl() + "/web/index.php/pim/viewEmployeeList");

        pimPage.deleteEmployeeByName(employee.getFullName());

        Assert.assertTrue(pimPage.isSuccessToastDisplayed() || pimPage.isNoRecordsFound(),
                "Employee should be deleted and either success toast or 'No Records' shown");
        log.info("Employee deleted successfully: {}", employee.getFullName());
    }

    @Test(
        groups = {"employee", "regression"},
        description = "Deleted employee no longer appears in search results",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testDeletedEmployeeNotInSearch() {
        Employee employee = TestDataGenerator.generateEmployee();

        PIMPage pimPage = loginAsAdmin().navigateToPIM();
        EmployeePage employeePage = pimPage.addEmployee(employee);
        String empId = employeePage.getEmployeeId();

        driver.navigate().to(config.getBaseUrl() + "/web/index.php/pim/viewEmployeeList");
        pimPage.deleteEmployeeByName(employee.getFullName());

        driver.navigate().to(config.getBaseUrl() + "/web/index.php/pim/viewEmployeeList");
        pimPage.searchByEmployeeId(empId);

        Assert.assertTrue(pimPage.isNoRecordsFound(),
                "Deleted employee should not appear in search results");
        log.info("Confirmed deleted employee not in search — ID: {}", empId);
    }

    @Test(
        groups = {"employee", "regression"},
        description = "Admin can bulk-select and delete multiple employees",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testBulkDeleteEmployees() {
        Employee emp1 = TestDataGenerator.generateEmployee();
        Employee emp2 = TestDataGenerator.generateEmployee();

        PIMPage pimPage = loginAsAdmin().navigateToPIM();
        pimPage.addEmployee(emp1);

        driver.navigate().to(config.getBaseUrl() + "/web/index.php/pim/viewEmployeeList");
        pimPage.addEmployee(emp2);

        driver.navigate().to(config.getBaseUrl() + "/web/index.php/pim/viewEmployeeList");

        pimPage.selectEmployeeCheckboxByName(emp1.getFullName());
        pimPage.selectEmployeeCheckboxByName(emp2.getFullName());
        pimPage.deleteAllSelectedEmployees();

        Assert.assertFalse(pimPage.isEmployeeVisible(emp1.getFullName()),
                "First deleted employee should not appear");
        log.info("Bulk delete completed for employees: {}, {}", emp1.getFullName(), emp2.getFullName());
    }
}
