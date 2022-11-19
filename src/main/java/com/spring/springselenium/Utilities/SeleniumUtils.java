package com.spring.springselenium.Utilities;

import com.spring.springselenium.Configuraion.annotation.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
@Page
public class SeleniumUtils {
    @Autowired
    protected WebDriver driver;

    @Autowired
    protected WebDriverWait wait;

    public void clickElementWithWait(String locator, int time){
        WebElement element=driver.findElement(By.xpath(locator));
        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();

    }
    public void clickElementWithWait(WebElement element, int time){
        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();

    }
}
