@payroll @adjustment
Feature: Payroll Benefit Adjustment

  Background:
    Given I am logged in as "admin"

  @smoke
  Scenario Outline: Modify benefits for employee <employeeNumber>
    When I navigate to Employee Search and search for "<employeeNumber>"
    And I click View Form and open Form One
    And I select adjustment type "<adjustmentType>"
    And I enter Main Benefit "<mainBenefit>" and Child Benefit "<childBenefit>"
    And I save changes and navigate to Payroll Reports
    And I execute and download the payroll report
    Then the report file should be downloaded
    And the Excel report should contain the updated benefit values
    And the Excel report should contain exactly <expectedRows> adjustment rows

    Examples:
      | employeeNumber | adjustmentType   | mainBenefit | childBenefit | expectedRows |
      | EMP001         | Raise Adjustment | 5000        | 1500         | 2            |
      | EMP002         | Raise Adjustment | 6000        | 2000         | 2            |
      | EMP003         | Raise Adjustment | 4500        | 1200         | 2            |
