package com.spring.springselenium.StepDefinitions;

import java.util.HashMap;
import java.util.Map;

import com.spring.springselenium.Configuraion.annotation.LazyConfiguration;
import com.spring.springselenium.Configuraion.annotation.Page;
import com.spring.springselenium.Configuraion.annotation.PageFragment;
import com.spring.springselenium.Configuraion.annotation.ThreadScopeBean;
import io.cucumber.java.Scenario;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@LazyConfiguration
public class ScenarioContext {
	private Scenario scenario;
	private Map<String, Object> contextData;
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

	public Map<String, Object> getContextData() {
		return contextData;
	}

	public void setContextData(Map<String, Object> contextData) {
		this.contextData = contextData;
	}

    public void clearContextData() {
    	this.contextData = new HashMap<>();
    }
}
