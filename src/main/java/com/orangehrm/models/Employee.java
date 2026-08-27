package com.orangehrm.models;

public class Employee {

    private String firstName;
    private String middleName;
    private String lastName;
    private String employeeId;
    private String email;
    private String mobilePhone;
    private int empNumber;

    private Employee() {}

    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getLastName() { return lastName; }
    public String getEmployeeId() { return employeeId; }
    public String getEmail() { return email; }
    public String getMobilePhone() { return mobilePhone; }
    public int getEmpNumber() { return empNumber; }
    public String getFullName() { return firstName + " " + lastName; }

    public void setEmpNumber(int empNumber) { this.empNumber = empNumber; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Employee employee = new Employee();

        public Builder firstName(String firstName) {
            employee.firstName = firstName;
            return this;
        }

        public Builder middleName(String middleName) {
            employee.middleName = middleName;
            return this;
        }

        public Builder lastName(String lastName) {
            employee.lastName = lastName;
            return this;
        }

        public Builder employeeId(String employeeId) {
            employee.employeeId = employeeId;
            return this;
        }

        public Builder email(String email) {
            employee.email = email;
            return this;
        }

        public Builder mobilePhone(String mobilePhone) {
            employee.mobilePhone = mobilePhone;
            return this;
        }

        public Employee build() {
            return employee;
        }
    }

    @Override
    public String toString() {
        return "Employee{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", empNumber=" + empNumber +
                '}';
    }
}
