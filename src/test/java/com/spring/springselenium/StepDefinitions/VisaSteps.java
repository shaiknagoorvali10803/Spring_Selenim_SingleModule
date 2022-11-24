package com.spring.springselenium.StepDefinitions;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.spring.springselenium.Configuraion.annotation.TakeScreenshot;
import com.spring.springselenium.Configuraion.service.ScreenshotService;
import com.spring.springselenium.Configuraion.annotation.LazyAutowired;
import com.spring.springselenium.PageClass.Google.GooglePage;
import com.spring.springselenium.PageClass.Visa.VisaRegistrationPage;
import com.spring.springselenium.Utilities.SeleniumUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.testng.Assert;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class VisaSteps {
    private static Map<Integer,ScenarioContext> contextMap = new HashMap<>();
    @Autowired
    protected WebDriver driver;

    @Autowired
    protected WebDriverWait wait;



    @LazyAutowired
    private VisaRegistrationPage registrationPage;
    @Autowired
    private TestUserDetails testUserDetails;

    @LazyAutowired
    private ScreenshotService screenshot;

    @LazyAutowired
    private SeleniumUtils utils;

    @LazyAutowired
    private GooglePage googlePage;

    @Autowired
    ScenarioContext scenarioContext;

    @LazyAutowired
    protected ScreenshotService screenshotService;

    @Autowired
    public VisaSteps (TestUserDetails testUserDetails)
    {
        this.testUserDetails=testUserDetails;
    }

    @PostConstruct
    private void init(){
        PageFactory.initElements(this.driver, this);
        contextMap.put(driver.hashCode(),scenarioContext);
    }
    @Given("I am on VISA registration form")
    public void launchSite() {
        this.driver.navigate().to("https://vins-udemy.s3.amazonaws.com/sb/visa/udemy-visa.html");
   }

    @When("I select my from country {string} and to country {string}")
    public void selectCountry(String from, String to) {
        this.registrationPage.setCountryFromAndTo(from, to);
    }

    @And("I enter my dob as {string}")
    public void enterDob(String dob) {
        this.registrationPage.setBirthDate(LocalDate.parse(dob));
    }

    @And("I enter my name as {string} and {string}")
    public void enterNames(String fn, String ln) {
        this.registrationPage.setNames(fn, ln);
    }

    @And("I enter my contact details as {string} and {string}")
    public void enterContactDetails(String email, String phone) {
        this.registrationPage.setContactDetails(email, phone);
    }

    @And("I enter the comment {string}")
    public void enterComment(String comment) {
        this.registrationPage.setComments(comment);
    }

    @And("I submit the form")
    public void submit() {
        this.registrationPage.submit();
        System.out.println("hashcode scenario Context "+scenarioContext.getScenario().hashCode());
        System.out.println("hashcode driver "+driver.hashCode());
        contextMap.get(driver.hashCode()).scenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png", "screenshot");
    }

    @Then("I should see get the confirmation number")
    public void verifyConfirmationNumber() {
        boolean isEmpty = StringUtils.isEmpty(this.registrationPage.getConfirmationNumber().trim());
        Assert.assertFalse(isEmpty);
    }

   }
