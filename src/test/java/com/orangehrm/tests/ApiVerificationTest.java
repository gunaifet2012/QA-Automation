package com.orangehrm.tests;

import com.orangehrm.api.ApiClient;
import com.orangehrm.api.EmployeeApiService;
import com.orangehrm.base.BaseTest;
import com.orangehrm.models.Employee;
import com.orangehrm.pages.EmployeePage;
import com.orangehrm.pages.PIMPage;
import com.orangehrm.utils.RetryAnalyzer;
import com.orangehrm.utils.TestDataGenerator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;

public class ApiVerificationTest extends BaseTest {

    @BeforeClass(alwaysRun = true)
    public void apiSetup() {
        ApiClient.authenticateAsAdmin();
    }

    @Test(
        groups = {"api", "regression"},
        description = "API authentication returns a valid token",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testApiAuthenticationReturnsToken() {
        String token = ApiClient.authenticate(
                config.getAdminUsername(),
                config.getAdminPassword()
        );
        Assert.assertNotNull(token, "API should return an auth token");
        Assert.assertFalse(token.isEmpty(), "Auth token should not be empty");
        log.info("API auth token acquired: {}...", token.substring(0, Math.min(token.length(), 20)));
    }

    @Test(
        groups = {"api", "regression"},
        description = "GET employees API returns 200 with data array",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testGetEmployeesReturns200() {
        Response response = EmployeeApiService.getAllEmployees();

        Assert.assertEquals(response.statusCode(), 200,
                "GET /pim/employees should return 200");
        Assert.assertNotNull(response.jsonPath().get("data"),
                "Response should contain data array");
        log.info("GET employees — total: {}", EmployeeApiService.getEmployeeCount());
    }

    @Test(
        groups = {"api", "regression"},
        description = "UI-created employee is verifiable via API",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testUiCreatedEmployeeVerifiableViaApi() {
        Employee employee = TestDataGenerator.generateEmployee();

        PIMPage pimPage = loginAsAdmin().navigateToPIM();
        EmployeePage employeePage = pimPage.addEmployee(employee);

        String empNumberStr = employeePage.extractEmployeeIdFromUrl();
        int empNumber = Integer.parseInt(empNumberStr);
        log.info("Employee created via UI with empNumber: {}", empNumber);

        Assert.assertTrue(EmployeeApiService.employeeExistsById(empNumber),
                "Employee created via UI should be fetchable via API");

        String apiFirstName = EmployeeApiService.getEmployeeFirstName(empNumber);
        Assert.assertEquals(apiFirstName, employee.getFirstName(),
                "First name via API should match what was entered in UI");
    }

    @Test(
        groups = {"api", "regression"},
        description = "API can create an employee and verify it",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testApiCreateEmployee() {
        Employee employee = TestDataGenerator.generateEmployee();

        Response createResponse = EmployeeApiService.createEmployee(employee);
        Assert.assertEquals(createResponse.statusCode(), 200,
                "POST /pim/employees should return 200");

        int empNumber = createResponse.jsonPath().getInt("data.empNumber");
        Assert.assertTrue(empNumber > 0, "Created employee should have a valid empNumber");

        Response getResponse = EmployeeApiService.getEmployeeById(empNumber);
        Assert.assertEquals(getResponse.statusCode(), 200,
                "GET for newly created employee should return 200");

        String retrievedFirstName = getResponse.jsonPath().getString("data.firstName");
        Assert.assertEquals(retrievedFirstName, employee.getFirstName(),
                "Retrieved first name should match what was created");
        log.info("API create+verify passed for empNumber: {}", empNumber);
    }

    @Test(
        groups = {"api", "regression"},
        description = "API can update an employee and verify the change"
    )
    public void testApiUpdateEmployee() {
        Employee employee = TestDataGenerator.generateEmployee();
        Response createResponse = EmployeeApiService.createEmployee(employee);
        int empNumber = createResponse.jsonPath().getInt("data.empNumber");

        String updatedFirst = "ApiUpdated" + TestDataGenerator.randomAlpha(4);
        String updatedLast = "LastName" + TestDataGenerator.randomAlpha(4);

        Response updateResponse = EmployeeApiService.updateEmployee(empNumber, updatedFirst, "", updatedLast);
        Assert.assertEquals(updateResponse.statusCode(), 200,
                "PUT /pim/employees/{id} should return 200");

        String retrievedFirst = EmployeeApiService.getEmployeeFirstName(empNumber);
        Assert.assertEquals(retrievedFirst, updatedFirst,
                "First name should be updated via API");
        log.info("API update verified for empNumber: {}", empNumber);
    }

    @Test(
        groups = {"api", "regression"},
        description = "API can delete an employee and verify deletion"
    )
    public void testApiDeleteEmployee() {
        Employee employee = TestDataGenerator.generateEmployee();
        Response createResponse = EmployeeApiService.createEmployee(employee);
        int empNumber = createResponse.jsonPath().getInt("data.empNumber");

        Response deleteResponse = EmployeeApiService.deleteEmployees(Collections.singletonList(empNumber));
        Assert.assertEquals(deleteResponse.statusCode(), 200,
                "DELETE /pim/employees should return 200");

        Assert.assertFalse(EmployeeApiService.employeeExistsById(empNumber),
                "Employee should not be found after deletion via API");
        log.info("API delete verified — empNumber {} no longer exists", empNumber);
    }
}
