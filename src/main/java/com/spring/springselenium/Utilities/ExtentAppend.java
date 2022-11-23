package com.spring.springselenium.Utilities;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.spring.springselenium.Configuraion.annotation.Page;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
@Page
public class ExtentAppend {
    private static ExtentReports extent;
    private static final Logger LOGGER = LoggerFactory.getLogger(ExtentAppend.class);

    static ExtentReports getExtentInstance() {
        if (extent == null) {
            try {
                extent = createInstance();
            } catch (Exception e) {
                LOGGER.error("Failed in the ExtentReports " + e.getMessage());
            }
        }
        return extent;
    }

    private static ExtentReports createInstance() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            String extentReportPropertyFile = "src\\test\\resources\\";
            input = new FileInputStream(extentReportPropertyFile + "extentReport" + ".properties");
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            String reportPath = prop.getProperty("reportPath");
            String reportTitle = prop.getProperty("reportTitle");
            String reportName = prop.getProperty("reportName");
            String htmlReportName = reportName;
            ExtentHtmlReporter htmlReports = new ExtentHtmlReporter(reportPath);
            extent = new ExtentReports();
            extent.attachReporter(htmlReports);
            extent.setAnalysisStrategy(AnalysisStrategy.CLASS);
            htmlReports.config().setDocumentTitle(reportTitle);
            htmlReports.config().setReportName(htmlReportName);
            htmlReports.config().setTheme(Theme.STANDARD);
            htmlReports.config().setEncoding("UTF-8");
            htmlReports.config().setProtocol(Protocol.HTTPS);
            extent.setSystemInfo("Environment", "Nagoor");
        } catch (IOException ex) {
            LOGGER.error("Failed in ExtentReports " + ex.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.error("Failed in ExtentReports function " + e.getMessage());
                }
            }
        }
        return extent;
    }

    String takeScreenshot(WebDriver driver, String screenshotName) {
        String destination = null;
        String imgPath = null;
        int maxRetryCount = 5;
        int retryCounter = 0;
        while (driver instanceof TakesScreenshot) {
            String dateName = new SimpleDateFormat(CommonConstants.YYYY_MM_DD_HH_MM_SS).format(new Date());
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                imgPath = "TestsScreenshots\\" + screenshotName + dateName + ".png";
                destination = System.getProperty("user.dir") + "\\build\\extent-reports\\" + imgPath;
                File finalDestination = new File(destination);
                FileUtils.copyFile(source, finalDestination);
                LOGGER.info("Screenshot destination : " + destination);
                return imgPath;
            } catch (IOException e) {
                LOGGER.error("takeScreenshot Exception : " + e.getMessage());
                if (++retryCounter > maxRetryCount) {
                    Assert.assertTrue(false, "Exception while taking screenshot : " + e.getMessage());
                    break;
                }
            }
        }
        LOGGER.info("Destination after exception: " + null);
        return null;
    }
}
