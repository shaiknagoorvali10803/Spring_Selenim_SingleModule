package com.spring.springselenium.Configuraion.config;

import com.spring.springselenium.Configuraion.annotation.LazyConfiguration;
import com.spring.springselenium.Configuraion.annotation.ThreadScopeBean;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.BooleanUtils;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

@LazyConfiguration
public class WebDriverConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverConfig.class);
    @Value("${BUILD_TOOL_RUN}")
    private String BUILD_TOOL_RUN;

    @Value("${selenium.grid.url}")
    private String SELENIUM_GRID_URL;

    @Value("${headless}")
    private String headless;

    @ThreadScopeBean
    @ConditionalOnProperty(name = "browser", havingValue = "firefox")
    public WebDriver firefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(BooleanUtils.toBoolean(headless));
        if (BooleanUtils.toBoolean(BUILD_TOOL_RUN)) {
            try {
                return new RemoteWebDriver(new URL(SELENIUM_GRID_URL), options);
            } catch (MalformedURLException e) {
                LOGGER.info("Given remote web driver url is wrong");
            }
        }
        return new FirefoxDriver(options);
    }


    @ThreadScopeBean
    @ConditionalOnProperty(name = "browser", havingValue = "chrome")
    public WebDriver chromeDriver(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(BooleanUtils.toBoolean(headless));
        chromeOptions.addArguments("--proxy-server='direct://'");
        chromeOptions.addArguments("--proxy-bypass-list=*");
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        // to launch chrome in incognito mode
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("--incognito");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        // to set default download path
        String downloadFilepath = System.getProperty("user.dir");
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadFilepath);
        chromeOptions.setExperimentalOption("prefs", chromePrefs);

        if (BooleanUtils.toBoolean(BUILD_TOOL_RUN)) {
            try {
                return new RemoteWebDriver(new URL(SELENIUM_GRID_URL), chromeOptions);
            } catch (MalformedURLException e) {
                LOGGER.info("Given remote web driver url is wrong");
            }
        }
        return new ChromeDriver(chromeOptions);
    }

    @ThreadScopeBean
    @ConditionalOnProperty(name = "browser", havingValue = "edge")
    public WebDriver edgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        options.setCapability("InPrivate", true);
        if (BooleanUtils.toBoolean(BUILD_TOOL_RUN)) {
            try {
                return new RemoteWebDriver(new URL(SELENIUM_GRID_URL), options);
            } catch (MalformedURLException e) {
                LOGGER.info("Given remote web driver url is wrong");
            }
        }
        return new EdgeDriver(options);
    }
}
