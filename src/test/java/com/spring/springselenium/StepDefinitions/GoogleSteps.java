package com.spring.springselenium.StepDefinitions;

import com.spring.springselenium.Configuraion.annotation.LazyAutowired;
import com.spring.springselenium.Configuraion.service.ScreenshotService;
import com.spring.springselenium.PageClass.Google.GooglePage;
import com.spring.springselenium.Utilities.SeleniumUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GoogleSteps {
    private static Map<Integer,ScenarioContext> contextMap = new HashMap<>();
    @Autowired
    protected WebDriver driver;
    @Autowired
    protected WebDriverWait wait;
    @LazyAutowired
    protected ScreenshotService screenshotService;
    @Autowired
    public static TestUserDetails testUserDetails;
    @Autowired
    ScenarioContext scenarioContext;
    @LazyAutowired
    private SeleniumUtils utils;
    @LazyAutowired
    private GooglePage googlePage;
     @Autowired
    public GoogleSteps (TestUserDetails testUserDetails)
    {
        this.testUserDetails=testUserDetails;
    }

    @PostConstruct
    private void init(){
        PageFactory.initElements(this.driver, this);
        contextMap.put(driver.hashCode(),scenarioContext);
    }

   @Given("I am on the google site")
    public void launchSite() {
         this.googlePage.goTo();
        //scenarioContext.getScenario().attach(this.screenshotService.getScreenshot(), "image/png", scenarioContext.getScenario().getName());
        testUserDetails.setUserDetails(new UserDetails("Shaik.Nagoorvali","password"));
        }
    @When("I enter {string} as a keyword")
    public void enterKeyword(String keyword) {
        this.googlePage.search(keyword);
        contextMap.get(driver.hashCode()).scenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png", "screenShot");
        }

    @Then("I should see search results page")
    public void clickSearch() throws IOException {
        Assert.assertTrue(this.googlePage.isAt());
        System.out.println("hashcode scenario Context "+scenarioContext.getScenario().hashCode());
        System.out.println("hashcode driver "+driver.hashCode());
        contextMap.get(driver.hashCode()).scenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png", "screenShot");
        //Allure.addAttachment("Screenshot", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        //ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, "screenshot", MediaEntityBuilder.createScreenCaptureFromPath(utils.takeScreenshot("screenshot")).build());
    }
    @Then("I should see at least {int} results")
    public void verifyResults(int count) throws InterruptedException, IOException {
        System.out.println("The Username from GoogleTest Class is:" + testUserDetails.getUserDetails().getUsername());
        System.out.println("The Username from GoogleTest Class is:" + testUserDetails.getUserDetails().getPassword());
        Assert.assertTrue(this.googlePage.getCount() >= count);
        utils.clickWithWait(By.xpath("//a[normalize-space()='Images']"));
        Thread.sleep(3000);
        driver.findElement(By.xpath("//a[normalize-space()='Videos']")).click();
    }
 }
