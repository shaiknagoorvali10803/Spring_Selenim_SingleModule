package com.spring.springselenium.StepDefinitions;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.spring.springselenium.Configuraion.annotation.LazyAutowired;
import com.spring.springselenium.Configuraion.service.ScreenshotService;
import com.spring.springselenium.PageClass.Google.GooglePage;
import com.spring.springselenium.Utilities.ReportUtil;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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
    @Autowired
    public static TestUserDetails testUserDetails;
    @LazyAutowired
    private GooglePage googlePage;

    @LazyAutowired
    protected ScreenshotService screenshotService;

    @LazyAutowired
    private ScenarioContext scenarioContext;

    public static final ReportUtil report = new ReportUtil();
    @PostConstruct
    private void init(){
        PageFactory.initElements(this.driver, this);
    }

    public GoogleSteps (TestUserDetails testUserDetails)
    {
        this.testUserDetails=testUserDetails;
    }
   @Given("I am on the google site")
    public void launchSite() {
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
        googlePage.ReadExcelData();
        scenarioContext.getScenario().attach(this.screenshotService.getScreenshot(), "image/png", scenarioContext.getScenario().getName());
       }
 }
