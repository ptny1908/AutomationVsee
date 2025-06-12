// org/example/steps/Hooks.java
package org.example.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.example.screenAction.CustomerPage;
import org.example.screenAction.ProviderPage;
import org.openqa.selenium.WebDriver; // Cần import WebDriver

public class Hooks {
    public static BaseDriver customerBrowser;
    public static BaseDriver providerBrowser;

    public static CustomerPage customerPage;
    public static ProviderPage providerPage;
    private static final String NO_BROWSER_TAG = "@no_browser_needed";
    private static final String GRID_TAG = "@grid"; // @grid for Scenario run with Selenium Grid

    @Before(order = 0)
    public void setupScenario(Scenario scenario){ //
        // 1. Check tag @no_browser_needed first
        if (scenario.getSourceTagNames().contains(NO_BROWSER_TAG)) {
            System.out.println("HOOKS: Scenario tagged with '" + NO_BROWSER_TAG + "'. Skipping browser initialization.");
            return;
        }

        // 2. Determine Run mode as local or grid
        String runMode = "local"; // Default is 'local'
        if (scenario.getSourceTagNames().contains(GRID_TAG)) {
            runMode = "grid";
            System.out.println("HOOKS: Scenario tagged with '" + GRID_TAG + "'. Setting run mode to 'grid'.");
        } else {
            System.out.println("HOOKS: No '" + GRID_TAG + "' tag found. Defaulting run mode to 'local'.");
        }

        // 3. Set up Browser
        String browserType = "chrome";

        // Set up driver for Customer
        customerBrowser = new BaseDriver();
        WebDriver customerDriver = customerBrowser.getDriver(browserType, runMode);
        customerPage = new CustomerPage(customerDriver); // Khởi tạo Page Object

        // Set up driver for Provider
        providerBrowser = new BaseDriver();
        WebDriver providerDriver = providerBrowser.getDriver(browserType, runMode);
        providerPage = new ProviderPage(providerDriver); // Khởi tạo Page Object
    }

    @After(order=0)
    public void closeBrowser(Scenario scenario) {
        if (!scenario.getSourceTagNames().contains(NO_BROWSER_TAG)) {
            if (customerBrowser != null) {
                System.out.println("HOOKS: Quitting customer browser.");
                customerBrowser.quitDriver();
            }
            if (providerBrowser != null) {
                System.out.println("HOOKS: Quitting provider browser.");
                providerBrowser.quitDriver();
            }
        } else {
            System.out.println("HOOKS: Skipping browser teardown for '" + NO_BROWSER_TAG + "' scenario.");
        }
    }
}