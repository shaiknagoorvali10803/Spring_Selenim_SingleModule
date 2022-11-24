package com.spring.springselenium.StepDefinitions;

import com.spring.springselenium.Configuraion.annotation.LazyConfiguration;
import io.cucumber.java.Scenario;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Component(value=BeanDefinition.SCOPE_SINGLETON)
public class ScenarioContext {

	protected Scenario scenario;

	public Scenario getScenario() {
		return scenario;
	}
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

}
