package com.spring.springselenium.StepDefinitions;

import com.spring.springselenium.Configuraion.annotation.LazyAutowired;
import com.spring.springselenium.PageClass.Homepage.HomePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;

@CucumberContextConfiguration
@SpringBootTest
public class HomeSteps {
    @LazyAutowired
    private HomePage HomePage;
    @Autowired
    protected WebDriver driver;

    @Autowired
    protected WebDriverWait wait;

    @PostConstruct
    private void init(){
        PageFactory.initElements(this.driver, this);
    }

    @Given("I am Google Page")
    public void launchSite() {
        this.HomePage.goTo();
         }

    @When("Search for the Word {string}")
    public void enterKeyword(String keyword) {
        this.HomePage.search(keyword);
    }

}
