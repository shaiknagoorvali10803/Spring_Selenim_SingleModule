package com.spring.springselenium.Utilities;

import com.spring.springselenium.Configuraion.annotation.Page;
import org.apache.commons.io.FileUtils;
import org.joda.time.Days;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import static com.spring.springselenium.Utilities.CommonConstants.BEFORE_WAIT_FOR_ELEMENT_IN_BUTTON_CLICK;
import static com.spring.springselenium.Utilities.CommonConstants.MM_DD_YYYY_WITH_SLASH;

@Page
public class SeleniumUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumUtils.class);
    @Autowired
    protected WebDriver driver;
    @Autowired
    protected WebDriverWait wait;
    private int defaultMaxTime = 60;
    private int maxSyncTime = defaultMaxTime;
    private boolean isCustomWait = false;

    public String takeScreenshot(String screenshotName) {
        String destination = null;
        String imgPath = null;
        int maxRetryCount = 5;
        int retryCounter = 0;
        while (driver instanceof TakesScreenshot) {
            String dateName = new SimpleDateFormat(CommonConstants.YYYY_MM_DD_HH_MM_SS).format(new Date());
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                imgPath = "\\TestsScreenshots\\" + screenshotName + dateName + ".png";
                destination = System.getProperty("user.dir") + imgPath;
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
        return destination;
    }
   public void clickElementWithWait(String locator, int time){
        WebElement element=driver.findElement(By.xpath(locator));
        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();

    }
    public void clickElementWithWait(WebElement element, int time){
        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();

    }

    private FluentWait webDriverFluentWait() {
        return new FluentWait(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(3))
                .ignoring(NoSuchElementException.class, NoSuchFrameException.class);
    }

    private FluentWait webDriverFluentWait(int time) {
        return new FluentWait(driver)
                .withTimeout(Duration.ofSeconds(time))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class, NoSuchFrameException.class);
    }

    protected WebElement waitForElement(final By byElement) {
        WebElement element = null;
        try {
            LOGGER.info("BeforeWaitForElement::" + byElement);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(CommonConstants.MAX_WAIT));
            wait.until(ExpectedConditions.elementToBeClickable(byElement));
            if (!isElementVisible(byElement)) {
                scrollToView(byElement);
            } else {
                element = driver.findElement(byElement);
            }
        } catch (WebDriverException e) {
            LOGGER.error("Exception in waitForElement::" + byElement + " " + e);
            throw new WebDriverException(e);
        }
        return element;
    }

    protected void waitForElement(final By byElement,int time) {
        try {
            LOGGER.info("BeforeWaitForElement::" + byElement);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
            wait.until(ExpectedConditions.visibilityOfElementLocated(byElement));

        } catch (WebDriverException e) {
            LOGGER.error("Exception in waitForElement::" + byElement + " " + e);
            throw new WebDriverException(e);
        }

    }

    private List<WebElement> waitForElements(By element, int timeout) {
        new WebDriverWait(driver, Duration.ofSeconds(timeout)).until(ExpectedConditions.visibilityOfElementLocated(element));
        return driver.findElements(element);
    }

    protected List<WebElement> waitForElements(By element) {
        return waitForElements(element, CommonConstants.MAX_WAIT);
    }

    protected void waitForElementDisplayed(By locator) {
        try {
            if (!driver.getWindowHandles().isEmpty()) {
                waitForElementLoading(locator);
            }
        } catch (Exception e) {
            LOGGER.error("Locator not found and the reason for failure is " + e);
        }
    }

    private void waitForElementLoading(final By byElement) {
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(Duration.ofSeconds(15))
                    .pollingEvery(Duration.ofMillis(5))
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(TimeoutException.class);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(byElement));
            if (!element.isDisplayed()) {
                LOGGER.info("Element " + byElement + " is not displayed");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to find the element and the reason is " + e);
        }
    }

    protected void waitUntilDomLoad() {
        LOGGER.info("Inside waitForElementLoading");
        FluentWait fluentWait = readyStateWait(driver);
        if (driver.getTitle().contains("/maintenix/")) {
            ExpectedCondition<Boolean> jQueryLoad;
            try {
                jQueryLoad = webDriver -> ((Long) ((JavascriptExecutor) driver)
                        .executeScript("return jQuery.active") == 0);
            } catch (Exception e) {
                jQueryLoad = webDriver -> (true);
                LOGGER.error("Failed to waitForElementLoading: " + e);
            }
            fluentWait.until(jQueryLoad);
        }
        try {
            ExpectedCondition<Boolean> docLoad = webDriver -> ((Boolean) ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").toString().equals("complete"));
            fluentWait.until(docLoad);
        } catch (Exception e) {
            LOGGER.error("Failed to waitForElementLoading " + e);
        }
        LOGGER.info("Dom load completed");
    }

    private FluentWait<WebDriver> readyStateWait(WebDriver driver) {
        return new FluentWait(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(WebDriverException.class);
    }

    protected boolean isElementPresent(By locator) {
        try {
            LOGGER.info("Before isElementPresent::" + locator);
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            LOGGER.info("Exception isElementPresent::" + locator + " " + e);
            return false;
        }
    }

    protected boolean isElementVisible(By locator) {
        try {
            webDriverFluentWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            return true;
        } catch (Exception e) {
            LOGGER.info("Exception isElementVisible::" + locator + " " + e);
            return false;
        }
    }

    protected boolean isElementVisible(By locator, int time) {
        try {
            webDriverFluentWait(time).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            return true;
        } catch (Exception e) {
            LOGGER.info("Exception isElementVisible::" + locator + " " + e);
            return false;
        }
    }

    public void waitByTime(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            LOGGER.info("Fail in wait due to : " + e);
            Thread.currentThread().interrupt();
        }
    }

    protected void clearAndEnterText(By locator, String text) {
        LOGGER.info("Before clearAndEnterText::" + locator + ", with text: " + text);
        WebElement webElementEnter = waitForElement(locator);
        if (!isElementVisible(locator)) {
            scrollToElement(webElementEnter);
        }
        highlightElement(locator);
        webElementEnter.click();
        webElementEnter.clear();
        String value = webElementEnter.getAttribute("value");
        if (!value.isEmpty()) {
            webElementEnter.sendKeys(Keys.CONTROL + "a");
            webElementEnter.sendKeys(Keys.DELETE);
        }
        Actions action = new Actions(driver);
        action.sendKeys(webElementEnter, text).build().perform();
    }

    protected void clearAndEnterValue(By locator, String text) {
        LOGGER.info("Before clearAndEnterText::" + locator + ", with text: " + text);
        WebElement webElementEnter = driver.findElement(locator);
        if (!isElementVisible(locator)) {
            scrollToElement(webElementEnter);
        }
        webElementEnter.click();
        webElementEnter.clear();
        Actions action = new Actions(driver);
        LOGGER.info("entering with text: " + text);
        action.sendKeys(webElementEnter, text).build().perform();
    }

    protected void enterText(By locator, String text) {
        LOGGER.info("Before enterText::" + locator + ", with text::" + text);
        if (!isElementVisible(locator)) {
            scrollToView(locator);
        }
        WebElement webElementEnter = waitForElement(locator);
        highlightElement(locator);
        webElementEnter.sendKeys(text);
    }

    protected void enteringText(By locator, String text) {
        LOGGER.info("Before enterText::" + locator + ", with text::" + text);
        if (!isElementVisible(locator)) {
            scrollToView(locator);
        }
        WebElement webElementEnter = waitForElement(locator);
        webElementEnter.sendKeys(text);
    }

    protected void clickUsingJavaScript(By locator) {
        LOGGER.info("BeforeWaitForElement in clickUsingJavaScript::" + locator);
        try {
            WebElement elm = waitForElement(locator);
            if (!isElementVisible(locator)) {
                scrollToElement(elm);
            }
            highlightElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click()", elm);
            waitTillLoadingCompletes();
        } catch (Exception e) {
            LOGGER.info("Unable to highlight : " + e);
        }
    }

    public void clickWithWait(By locator) {
        try {
            WebElement elm = waitForElement(locator);
            LOGGER.info(BEFORE_WAIT_FOR_ELEMENT_IN_BUTTON_CLICK + locator);
            if (!isElementVisible(locator)) {
                scrollToView(locator);
            }
            waitForElementDisplayed(locator);
           if (isElementVisible(locator)) {
                elm.click();
            } else {
                waitUntilElementClickable(locator);
                elm.click();
            }
            waitTillLoadingCompletes();
        } catch (Exception e) {
            LOGGER.error(Level.FINEST + "Exception in clicking::" + e);
        }
    }

    public void buttonClick(By locator) {
        try {
            LOGGER.info(BEFORE_WAIT_FOR_ELEMENT_IN_BUTTON_CLICK + locator);
            if (!isElementVisible(locator)) {
                scrollToView(locator);
            }
            WebElement elm = waitForElement(locator);
            waitUntilElementClickable(locator);
            if (isElementVisible(locator)) {
                highlightElement(locator);
                elm.click();
            } else {
                waitUntilElementClickable(locator);
                highlightElement(locator);
                elm.click();
            }
            waitTillLoadingCompletes();
        } catch (Exception e) {
            LOGGER.error("Exception in buttonClick::" + e);
        }
    }

    public void buttonClick(WebElement element) {
        try {
            LOGGER.info("BeforeWaitForElement in buttonClick::");
            webDriverFluentWait().until(ExpectedConditions.elementToBeClickable(element));
            scrollToElement(element);
            highlightElement(element);
            waitByTime(1000);
            element.click();
            waitTillLoadingCompletes();
        } catch (Exception e) {
            LOGGER.error("Exception in buttonClick ::" + e);
        }
    }

    protected void doubleClick(By locator) {
        Actions actions = new Actions(driver);
        WebElement elementLocator = driver.findElement(locator);
        waitForElement(locator);
        waitByTime(1000);
        if (!isElementVisible(locator)) {
            scrollToView(locator);
        }
        waitForElement(locator);
        if (isElementVisible(locator)) {
            actions.moveToElement(elementLocator).build();
            highlightElement(locator);
            actions.doubleClick(elementLocator).build().perform();
        } else {
            waitUntilElementClickable(locator);
            highlightElement(locator);
            actions.moveToElement(elementLocator).build();
            actions.doubleClick(elementLocator).build().perform();
        }
        waitTillLoadingCompletes();
    }

    protected void singleClick(By locator) {
        Actions actions = new Actions(driver);
        WebElement elementLocator = driver.findElement(locator);
        waitForElement(locator);
        waitByTime(1000);
        if (!isElementVisible(locator)) {
            scrollToView(locator);
        }
        waitForElement(locator);
        if (isElementVisible(locator)) {
            actions.moveToElement(elementLocator).build();
            highlightElement(locator);
            actions.click(elementLocator).build().perform();
        } else {
            waitUntilElementClickable(locator);
            highlightElement(locator);
            actions.moveToElement(elementLocator).build();
            actions.click(elementLocator).build().perform();
        }
        waitTillLoadingCompletes();
    }

    protected void waitUntilElementClickable(By locator) {
        LOGGER.info("Before waitUntilElementVisible::" + locator);
        WebDriverWait expWait = new WebDriverWait(driver, Duration.ofSeconds(60));
        expWait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void selectOption(By locator, String opt) {
        LOGGER.info("Before selectOption::" + locator + ", with Select Option::" + opt);
        WebElement element = waitForElement(locator);
        webDriverFluentWait().until(ExpectedConditions.elementToBeClickable(locator));
        Select select = new Select(element);
        select.selectByVisibleText(opt);
    }

    protected boolean waitUntilTextPresentInElement(By locator, String elementText, int seconds) {
        WebDriverWait expWait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return expWait.until(ExpectedConditions.textToBePresentInElementLocated(locator, elementText));
    }

    protected void scrollToView(By locator) {
        WebElement element = driver.findElement(locator);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void scrollToElement(WebElement webelement) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView(true);", webelement);
        jse.executeScript("window.scrollBy(0,100)", "");
        LOGGER.info("ScrollToElement::" + webelement + "Done");
    }

    protected void uncheckCheckbox(By locator) {
        waitForElement(locator);
        if (Boolean.TRUE.equals(driver.findElement(locator).isSelected())) {
            highlightElement(locator);
            clickWithWait(locator);
        }
    }

    protected void checkCheckbox(By locator) {
        waitForElement(locator);
        if (Boolean.FALSE.equals(driver.findElement(locator).isSelected())) {
            highlightElement(locator);
            clickWithWait(locator);
        }
    }

    protected void highlightElement(WebElement webElement) {
        try {
            if (driver instanceof JavascriptExecutor) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='4px solid orange'", webElement);
            }
        } catch (Exception e) {
            LOGGER.error("Fail in highlightElement " + e);
        }
    }

    protected void highlightElement(By locator) {
        highlightElement(driver.findElement(locator));
    }

    protected void clickWithMultipleTimes(By locator, int clicks) {
        try {
            WebElement elm = waitForElement(locator);
            LOGGER.info(BEFORE_WAIT_FOR_ELEMENT_IN_BUTTON_CLICK + locator);
            if (!isElementVisible(locator)) {
                scrollToView(locator);
            }
            for (int i = 0; i < clicks; i++) {
                waitForElement(locator);
                if (isElementVisible(locator)) {
                    elm.click();
                } else {
                    waitUntilElementClickable(locator);
                    elm.click();
                }
                waitTillLoadingCompletes();
            }
            waitByTime(2000);
        } catch (Exception e) {
            LOGGER.error("Exception in clickWithMultipleTimes::" + e);
        }
    }

    protected void waitTillLoadingCompletes() {
        waitTillLoadingCompletes(By.xpath("//*[@class='uicLoaderOverlay uicLo-loading']"));
        waitTillLoadingCompletes(By.xpath("//*[@class='loadingBox loading']"));
    }

    protected void setMaxSyncTime(int newSyncTime) {
        maxSyncTime = newSyncTime;
    }

    private void waitTillLoadingCompletes(By loadingCircle) {
        try {
            long start = System.currentTimeMillis();
            int elapsedTime = 0;
            waitByTime(250);
            while (driver.findElement(loadingCircle).isDisplayed()) {
                LOGGER.info("Waiting for Loading Screens to go away");
                if (elapsedTime > maxSyncTime) {
                    break;
                }
                waitByTime(500);
                elapsedTime = Math.round((System.currentTimeMillis() - start) / 1000F);
            }
        } catch (Exception e) {
            if ((e.toString().contains("NoSuchElementException") || e.getMessage().contains("NoStale")))
                LOGGER.info("Loading screenshot is not present now");
            else {
                LOGGER.error(Level.FINEST + "Fail in waitTillLoadingCompletes " + e);
            }
        }
    }

    private boolean waitUntilLocatorPresent(By expectedLocator, String context) {
        boolean locatorFound = false;
        try {
            if (isCustomWait) {
                isCustomWait = false;
            } else {
                maxSyncTime = defaultMaxTime;
            }
            long start = System.currentTimeMillis();
            int elapsedTime = 0;
            while (elapsedTime < maxSyncTime) {
                if (isElementPresent(expectedLocator)) {
                    locatorFound = true;
                    waitUntilElementClickable(expectedLocator);
                    break;
                }
                elapsedTime = Math.round((System.currentTimeMillis() - start) / 1000F);
            }
        } catch (Exception e) {
            LOGGER.error("Exception occurred in + 'waitUntilLocatorPresent' for locator:: " + expectedLocator.toString() + " and context:: " + context + "Exception" + e);
        }
        return locatorFound;
    }

    public String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(CommonConstants.DD_MMM);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public String getCurrentDateInDDMMMyy() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMMyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public String getFutureDate(int value) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(CommonConstants.DD_MMM);
        LocalDateTime now = LocalDateTime.now();
        now = now.plusDays(value);
        return dtf.format(now);
    }

    public String getNewTravelDate(String departureDate, String typeOfFormat) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(typeOfFormat);
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(departureDate));
        c.add(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(c.getTime());
    }

    public String getDate(String depArrDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(MM_DD_YYYY_WITH_SLASH);
        SimpleDateFormat sdf1 = new SimpleDateFormat("ddMMMyy");
        Date d = sdf.parse(depArrDate);
        return sdf1.format(d);
    }

    public String getCurrentDateInMMYY() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMyy");
        LocalDateTime now = LocalDateTime.now();
        now = now.plusYears(1);
        return dtf.format(now);
    }

    public String getCurrentYearInYY() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public int dateConversion(String date) throws ParseException {
        String date1 = date.replace("/", "");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(CommonConstants.MM_DD_YYYY);
        Date newDate = new SimpleDateFormat(CommonConstants.MM_DD_YYYY).parse(date1);
        LocalDate now = LocalDate.now();
        Date currentDate = new SimpleDateFormat(CommonConstants.MM_DD_YYYY).parse(dtf.format(now));
        return Days.daysBetween(new org.joda.time.LocalDate(currentDate.getTime()), new org.joda.time.LocalDate(newDate.getTime())).getDays();
    }

    public String getUMNRDOB(int years) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(CommonConstants.DD_MMM_YY);
        LocalDateTime now = LocalDateTime.now();
        now = now.minusYears(years);
        return dtf.format(now);
    }

    public String getTimeATD() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("CST"));
        return format.format(new Date());
    }

    public String getTimeInATW() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("CST"));
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 1);
        return format.format(now.getTime());
    }

    public String getPreviousDayInddMMM() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(CommonConstants.DD_MMM);
        LocalDateTime now = LocalDateTime.now();
        now = now.minusDays(1);
        return dtf.format(now);
    }

    public String getDateInDdMmm(String depArrDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(MM_DD_YYYY_WITH_SLASH);
        SimpleDateFormat sdf1 = new SimpleDateFormat(CommonConstants.DD_MMM);
        Date d = sdf.parse(depArrDate);
        return sdf1.format(d);
    }

    public String getFutureDateMmDdYyyy(int value) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(MM_DD_YYYY_WITH_SLASH);
        LocalDateTime now = LocalDateTime.now();
        now = now.plusDays(value);
        return dtf.format(now);
    }

    public int getCurrentYear()  {
        Date d = new Date();
        int year = d.getYear();
        return year + 1900;
    }

}
