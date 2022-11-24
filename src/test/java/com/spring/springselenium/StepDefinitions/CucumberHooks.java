package com.spring.springselenium.StepDefinitions;

import com.spring.springselenium.Configuraion.annotation.LazyAutowired;
import com.spring.springselenium.Configuraion.service.ScreenshotService;
import io.cucumber.java.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CucumberHooks {
    @LazyAutowired
    private ScreenshotService screenshotService;
    @LazyAutowired
    private ApplicationContext applicationContext;

    @LazyAutowired
    ScenarioContext scenarioContext;
    @Before
    public void settingScenario(Scenario scenario) {
        System.out.println("calling BeforeStep");
        scenarioContext.setScenario(scenario);
        System.out.println("scenario value assigned");
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
