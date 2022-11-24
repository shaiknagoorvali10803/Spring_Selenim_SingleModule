package com.spring.springselenium.StepDefinitions;
import com.spring.springselenium.Configuraion.annotation.LazyAutowired;
import com.spring.springselenium.Configuraion.service.ScreenshotService;
import io.cucumber.java.*;
import org.openqa.selenium.WebDriver;
import org.springframework.context.ApplicationContext;
import java.io.IOException;

public class CucumberHooks {
    @LazyAutowired
    private ScreenshotService screenshotService;
    @LazyAutowired
    private ApplicationContext applicationContext;
    @LazyAutowired
    ScenarioContext scenarioContext;
    @BeforeStep
    public void settingScenario(Scenario scenario) {
        scenarioContext.setScenario(scenario);
        //scenario.attach(this.screenshotService.getScreenshot(), "image/png", scenario.getName());
    }
    @AfterStep
    public void afterStep(Scenario scenario) throws IOException, InterruptedException {
        if(scenario.isFailed()){
            scenario.attach(this.screenshotService.getScreenshot(), "image/png", scenario.getName());
            }
    }
    @After
    public void afterScenario(){
        this.applicationContext.getBean(WebDriver.class).quit();
        }
    }
