package com.spring.springselenium.Utilities;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.spring.springselenium.Configuraion.annotation.LazyAutowired;
import com.spring.springselenium.Configuraion.annotation.Page;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import java.security.SecureRandom;
import java.util.logging.Level;
@Page
public class ReportUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportUtil.class);
    @Autowired
    protected WebDriver driver;
    @Autowired
    private ExtentTest extentTest ;
    @LazyAutowired
    ExtentAppend extentAppend;

    public void reportSelenium(String status, String message) {
        boolean retryStatus = false;
        int retryCounter = 0;
        int maxRetryCount = 3;
        do {
            try {
                if (status.equalsIgnoreCase("info")) {
                    String screenshotPath = extentAppend.takeScreenshot(driver, "INFO_" + randomString(5) + "_");
                    extentTest.log(Status.INFO, message, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                } else if (status.equalsIgnoreCase("error")) {
                    extentTest.log(Status.FAIL, MarkupHelper.createLabel(message, ExtentColor.AMBER));
                    Assert.fail(message, new Throwable());
                } else if (status.equalsIgnoreCase("pass")) {
                    String screenshotPath = extentAppend.takeScreenshot(driver, "PASS_" + randomString(5) + "_");
                    extentTest.log(Status.PASS, message, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                } else {
                    String screenshotPath = extentAppend.takeScreenshot(driver, "FAIL_" + randomString(5) + "_");
                    extentTest.log(Status.FAIL, message, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                    Assert.fail(message, new Throwable());
                    throw new Exception();
                }
                retryStatus = false;
            } catch (Exception e) {
                LOGGER.error(Level.FINEST + "Failed in reportSelenium " + e);
                retryStatus = true;
                LOGGER.error(Level.FINEST + "  Report method exception : " + e);
                if (++retryCounter > maxRetryCount) {
                    Assert.assertTrue(false);
                    break;
                }
            }
        } while (retryStatus);
    }

    private String randomString(int len) {
        String ab = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(ab.charAt(rnd.nextInt(ab.length())));
        }
        return sb.toString();
    }
}
