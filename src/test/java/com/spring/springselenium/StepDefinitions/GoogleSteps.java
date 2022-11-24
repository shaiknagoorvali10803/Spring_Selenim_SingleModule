package com.spring.springselenium.StepDefinitions;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.spring.springselenium.Configuraion.annotation.LazyAutowired;
import com.spring.springselenium.Configuraion.service.ScreenshotService;
import com.spring.springselenium.PageClass.Google.GooglePage;

import com.spring.springselenium.Utilities.SeleniumUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;
public class GoogleSteps {
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
    }

   @Given("I am on the google site")
    public void launchSite() {
       //ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, "screenshot", MediaEntityBuilder.createScreenCaptureFromPath(utils.takeScreenshot(driver,"screenshot")).build());
       ExtentCucumberAdapter.getCurrentStep().addScreenCaptureFromPath(utils.takeScreenshot(driver,"screenshot"));

       scenarioContext.getScenario().attach(this.screenshotService.getScreenshot(), "image/png", scenarioContext.getScenario().getName());
        this.googlePage.goTo();
        testUserDetails.setUserDetails(new UserDetails("Shaik.Nagoorvali","password"));

        }
    @When("I enter {string} as a keyword")
    public void enterKeyword(String keyword) {
        this.googlePage.search(keyword);
        }

    @Then("I should see search results page")
    public void clickSearch() throws IOException {
        Assert.assertTrue(this.googlePage.isAt());

       }
    @Then("I should see at least {int} results")
    public void verifyResults(int count) throws InterruptedException, IOException {
        System.out.println("The Username from GoogleTest Class is:" + testUserDetails.getUserDetails().getUsername());
        System.out.println("The Username from GoogleTest Class is:" + testUserDetails.getUserDetails().getPassword());
        Assert.assertFalse(this.googlePage.getCount() >= count);
        utils.clickWithWait(By.xpath("//a[normalize-space()='Images']"));
        Thread.sleep(3000);
        driver.findElement(By.xpath("//a[normalize-space()='Videos']")).click();
    }
 }
