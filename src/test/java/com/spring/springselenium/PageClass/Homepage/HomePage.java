package com.spring.springselenium.PageClass.Homepage;

import com.spring.springselenium.Configuraion.annotation.Page;
import com.spring.springselenium.PageClass.Base;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Page
public class HomePage extends Base {
    @Value("${application.url}")
    private String url;
    @FindBy(name = "q")
    private WebElement searchBox;
    @FindBy(name = "btnK")
    private List<WebElement> searchBtns;

    public void goTo(){
        this.driver.get(url);
    }

    public void search(final String keyword){
        this.searchBox.sendKeys(keyword);
        this.searchBox.sendKeys(Keys.TAB);
        this.searchBtns
                .stream()
                .filter(e -> e.isDisplayed() && e.isEnabled())
                .findFirst()
                .ifPresent(WebElement::click);
    }

   public boolean isAt() {
        return this.wait.until((d) -> this.searchBox.isDisplayed());
    }

}
