package com.application.tests.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Collections;
import java.util.HashMap;

public class BrowserManager {

    private static final Logger logger = LogManager.getLogger(BrowserManager.class);

    public BrowserManager() {

    }

    public WebDriver getBrowserWebDriver(String key) {
        WebDriver driver = null;
        logger.info("Opening browser [{}]", key);
        switch (key) {
            case "Chrome":
                driver = setUpChromeDriver(key);
                break;
            case "Firefox":
                driver = setUpGeckoDriver(key);
                break;
            case "IE":
                driver = setUpIEDriver(key);
                break;
            default:
                Assert.fail("Invalid browser name in config.properties: " + key);
        }
        return driver;
    }

    private ChromeDriver setUpChromeDriver(String chromedriver) {
        System.setProperty("webdriver.chrome.driver", FileMgmtUtil.getPropertyValue("local.chrome.driver.path"));
        ChromeOptions options = new ChromeOptions().merge(getDesiredCapabilities(chromedriver));
        return new ChromeDriver(options);
    }

    private FirefoxDriver setUpGeckoDriver(String geckoText) {
        System.setProperty("webdriver.gecko.driver", FileMgmtUtil.getPropertyValue("local.firefox.driver.path"));
        FirefoxOptions options = new FirefoxOptions().merge(getDesiredCapabilities(geckoText));
        return new FirefoxDriver(options);
    }

    private InternetExplorerDriver setUpIEDriver(String iedriver) {
        System.setProperty("webdriver.ie.driver", FileMgmtUtil.getPropertyValue("local.ie.driver.path"));
        InternetExplorerOptions options = new InternetExplorerOptions().merge(getDesiredCapabilities(iedriver));
        return new InternetExplorerDriver(options);
    }
    /**
     * If you want to add more Capabilities for browser modify this method
     * @param browser is browser name
     * @return WebDriver Capabilities for selected browser
     */
    private static DesiredCapabilities getDesiredCapabilities(String browser) {
        DesiredCapabilities capabilities;
        if (browser.toUpperCase().trim().equalsIgnoreCase("chrome".trim().toUpperCase())){
            capabilities = DesiredCapabilities.chrome();
            // Turn off default browser selection and remember password
            capabilities.setCapability("chrome.switches", Collections.singletonList("--no-default-browser-check"));
            HashMap<String, String> chromePreferences = new HashMap<>();
            chromePreferences.put("profile.password_manager_enabled", "false");
            capabilities.setCapability("chrome.prefs", chromePreferences);
        }
        else if (browser.toUpperCase().trim().equalsIgnoreCase("ie".toUpperCase().trim())) {
            capabilities = DesiredCapabilities.internetExplorer();
            capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
            capabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
            capabilities.setCapability("requireWindowFocus", true);
            capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
        }
        else if (browser.toUpperCase().trim().equalsIgnoreCase("safari".toUpperCase().trim())) {
            capabilities = DesiredCapabilities.safari();
            capabilities.setCapability("safari.cleanSession", true);
        }
        else if (browser.toUpperCase().trim().equalsIgnoreCase("firefox".trim().toUpperCase())){
            capabilities = DesiredCapabilities.firefox();
        }
        else {
            capabilities = DesiredCapabilities.firefox();
        }
        return capabilities;
    }

}