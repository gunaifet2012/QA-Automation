package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.models.Employee;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.EmployeePage;
import com.orangehrm.pages.PIMPage;
import com.orangehrm.utils.RetryAnalyzer;
import com.orangehrm.utils.TestDataGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RoleBasedValidationTest extends BaseTest {

    @Test(
        groups = {"rbac", "regression"},
        description = "Admin user can access PIM module",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testAdminCanAccessPIMModule() {
        DashboardPage dashboard = loginAsAdmin();

        Assert.assertTrue(dashboard.isDashboardDisplayed(),
                "Admin should see dashboard");

        PIMPage pimPage = dashboard.navigateToPIM();
        Assert.assertTrue(driver.getCurrentUrl().contains("/pim"),
                "Admin should be able to navigate to PIM module");
        log.info("Admin role: PIM module access confirmed");
    }

    @Test(
        groups = {"rbac", "regression"},
        description = "Admin user can access Admin module",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testAdminCanAccessAdminModule() {
        DashboardPage dashboard = loginAsAdmin();
        dashboard.navigateToAdmin();

        Assert.assertTrue(driver.getCurrentUrl().contains("/admin"),
                "Admin should be able to navigate to Admin module");
        log.info("Admin role: Admin module access confirmed");
    }

    @Test(
        groups = {"rbac", "regression"},
        description = "Admin can add employee — verifies write permission for admin role"
    )
    public void testAdminHasWritePermissionForEmployees() {
        Employee employee = TestDataGenerator.generateEmployee();

        PIMPage pimPage = loginAsAdmin().navigateToPIM();
        EmployeePage employeePage = pimPage.addEmployee(employee);

        Assert.assertTrue(driver.getCurrentUrl().contains("/pim/viewPersonalDetails"),
                "Admin should be able to create an employee (write access)");
        log.info("Admin role: write permission confirmed — created {}", employee.getFullName());
    }

    @Test(
        groups = {"rbac", "regression"},
        description = "Admin role — dashboard quick launch panel is visible"
    )
    public void testAdminDashboardQuickLaunchVisible() {
        DashboardPage dashboard = loginAsAdmin();

        Assert.assertTrue(dashboard.isQuickLaunchDisplayed(),
                "Admin should see the Quick Launch panel on dashboard");
    }

    @Test(
        groups = {"rbac", "regression"},
        description = "Unauthenticated access to protected URL redirects to login"
    )
    public void testUnauthenticatedRedirectsToLogin() {
        driver.get(config.getBaseUrl() + "/web/index.php/pim/viewEmployeeList");

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("/auth/login") || currentUrl.contains("/auth"),
                "Unauthenticated access should redirect to login page. URL was: " + currentUrl
        );
        log.info("Unauthenticated redirect confirmed — URL: {}", currentUrl);
    }
}
