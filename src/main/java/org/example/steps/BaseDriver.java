// org/example/steps/BaseDriver.java
package org.example.steps;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.Configuration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BaseDriver {

    private WebDriver driver;

    public BaseDriver() {
    }

    public WebDriver getDriver(String browserType, String runMode) {
        final String GRID_HUB_URL = Configuration.get().getTestProperty("gridHubUrl");
        try {
            System.out.println("Initializing driver for browser '" + browserType + "' in '" + runMode + "' mode.");

            if ("chrome".equalsIgnoreCase(browserType)) {
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("profile.default_content_setting_values.media_stream_mic", 1); // 1 = Allow, 2 = Block
                prefs.put("profile.default_content_setting_values.media_stream_camera", 1); // 1 = Allow, 2 = Block
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setExperimentalOption("prefs", prefs);
                if ("grid".equalsIgnoreCase(runMode)) {
                    System.out.println("Attempting to connect to Grid at: " + GRID_HUB_URL);
                    driver = new RemoteWebDriver(new URL(GRID_HUB_URL), chromeOptions);
                    System.out.println("Successfully initialized Chrome driver via Grid.");
                } else {
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(chromeOptions);
                    System.out.println("Successfully initialized Chrome driver locally.");
                }
            } else if ("firefox".equalsIgnoreCase(browserType)) {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addPreference("permissions.default.microphone", true);
                firefoxOptions.addPreference("permissions.default.camera", true);
                firefoxOptions.addPreference("media.navigator.permission.disabled", true);

                if ("grid".equalsIgnoreCase(runMode)) {
                    driver = new RemoteWebDriver(new URL(GRID_HUB_URL), firefoxOptions);
                    System.out.println("Successfully initialized Firefox driver via Grid.");
                } else {
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver(firefoxOptions);
                    System.out.println("Successfully initialized Firefox driver locally.");
                }
            } else {
                throw new IllegalArgumentException("Unsupported browser type: " + browserType);
            }
            driver.manage().window().maximize();
        } catch (Exception e) {
            System.err.println("Error initializing WebDriver: " + e.getMessage());
            throw new RuntimeException("Failed to initialize WebDriver.", e);
        }
        return driver;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}