package com.spring.springselenium.StepDefinitions;

import com.spring.springselenium.Configuraion.annotation.LazyAutowired;
import com.spring.springselenium.Configuraion.annotation.LazyConfiguration;
import com.spring.springselenium.Configuraion.annotation.ThreadScopeBean;
import com.spring.springselenium.Configuraion.service.ScreenshotService;
import io.cucumber.java.Scenario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.lang.annotation.*;

@LazyConfiguration
public class ScenarioContext {
	protected Scenario scenario;
	public Scenario getScenario() {
		return scenario;
	}
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

}
