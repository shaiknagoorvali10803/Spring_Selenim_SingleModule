package com.spring.springselenium.StepDefinitions;

import com.spring.springselenium.Configuraion.annotation.LazyAutowired;
import com.spring.springselenium.Configuraion.service.ScreenshotService;
import io.cucumber.java.*;
import org.openqa.selenium.WebDriver;
import org.springframework.context.ApplicationContext;
import org.springframework.util.DefaultPropertiesPersister;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class CucumberHooks {

    @LazyAutowired
    private ScreenshotService screenshotService;
    @LazyAutowired
    private ApplicationContext applicationContext;

    @BeforeAll
    public static void before_or_after_all() throws IOException {
        Properties properties = new Properties();
        properties.put("browser","edge");
        File file = new File("application.properties");
        OutputStream outputStream = new FileOutputStream( file );
        DefaultPropertiesPersister defaultPropertiesPersister = new DefaultPropertiesPersister();
        defaultPropertiesPersister.store(properties, outputStream, "Comment");
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
