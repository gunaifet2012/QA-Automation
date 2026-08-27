package com.orangehrm.api;

import com.orangehrm.models.Employee;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class EmployeeApiService {

    private static final Logger log = LogManager.getLogger(EmployeeApiService.class);
    private static final String EMPLOYEES_ENDPOINT = "/pim/employees";

    private EmployeeApiService() {}

    public static Response getAllEmployees() {
        log.info("Fetching all employees via API");
        return ApiClient.get(EMPLOYEES_ENDPOINT);
    }

    public static Response getEmployeeById(int empNumber) {
        log.info("Fetching employee by empNumber: {}", empNumber);
        return ApiClient.get(EMPLOYEES_ENDPOINT + "/" + empNumber);
    }

    public static Response createEmployee(Employee employee) {
        log.info("Creating employee via API: {} {}", employee.getFirstName(), employee.getLastName());
        String body = String.format(
                "{\"firstName\":\"%s\",\"middleName\":\"%s\",\"lastName\":\"%s\"}",
                employee.getFirstName(),
                employee.getMiddleName() != null ? employee.getMiddleName() : "",
                employee.getLastName()
        );
        return ApiClient.post(EMPLOYEES_ENDPOINT, body);
    }

    public static Response updateEmployee(int empNumber, String firstName, String middleName, String lastName) {
        log.info("Updating employee {} via API", empNumber);
        String body = String.format(
                "{\"firstName\":\"%s\",\"middleName\":\"%s\",\"lastName\":\"%s\"}",
                firstName, middleName, lastName
        );
        return ApiClient.put(EMPLOYEES_ENDPOINT + "/" + empNumber, body);
    }

    public static Response deleteEmployees(List<Integer> empNumbers) {
        log.info("Deleting employees via API: {}", empNumbers);
        StringBuilder ids = new StringBuilder("[");
        for (int i = 0; i < empNumbers.size(); i++) {
            ids.append(empNumbers.get(i));
            if (i < empNumbers.size() - 1) ids.append(",");
        }
        ids.append("]");
        String body = "{\"ids\":" + ids + "}";
        return ApiClient.delete(EMPLOYEES_ENDPOINT, body);
    }

    public static boolean employeeExistsById(int empNumber) {
        Response response = getEmployeeById(empNumber);
        return response.statusCode() == 200;
    }

    public static int getEmployeeCount() {
        Response response = getAllEmployees();
        return response.jsonPath().getInt("meta.total");
    }

    public static String getEmployeeFirstName(int empNumber) {
        return getEmployeeById(empNumber).jsonPath().getString("data.firstName");
    }

    public static String getEmployeeLastName(int empNumber) {
        return getEmployeeById(empNumber).jsonPath().getString("data.lastName");
    }
}
