@payroll @report
Feature: Payroll Report Generation

  Background:
    Given I am logged in as "admin"

  @smoke
  Scenario Outline: Admin downloads payroll report for <month>
    When I navigate to Admin Payroll Reports
    And I select report "<reportName>" for month "<month>" and download the report
    Then the report file should be downloaded
    And the Excel report should contain exactly <expectedRows> data row(s)

    Examples:
      | reportName             | month         | expectedRows |
      | Monthly Payroll Report | January 2024  | 1            |
      | Monthly Payroll Report | February 2024 | 1            |
      | Monthly Payroll Report | March 2024    | 1            |
