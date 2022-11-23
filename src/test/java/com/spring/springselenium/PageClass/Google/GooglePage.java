package com.spring.springselenium.PageClass.Google;

import com.spring.springselenium.Configuraion.annotation.LazyAutowired;
import com.spring.springselenium.PageClass.Base;
import com.spring.springselenium.Configuraion.annotation.Page;
import com.spring.springselenium.Utilities.XLUtility_xlsx;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Page
public class GooglePage extends Base {
    @Value("${application.url}")
    private String url;
    @FindBy(name = "q")
    private WebElement searchBox;
    @FindBy(css = "div.g")
    private List<WebElement> results;

    @FindBy(name = "btnK")
    private List<WebElement> searchBtns;

    public void goTo(){
        this.driver.navigate().to(url);
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

    public int getCount(){
        return this.results.size();
    }
    public boolean isAt() {
        return this.wait.until((d) -> this.searchBox.isDisplayed());
    }

    public void ReadExcelData() throws IOException {
        String path1 = "C:\\ECLIPSE_BACKUP\\AUTOMATION_TESTING_LOGICS\\sampledata.xlsx";
        XLUtility_xlsx xlutil1 = new XLUtility_xlsx(path1);
        // Read Data
        String sheetName=null;
        sheetName ="login";
        int totalrows = xlutil1.getRowCount(sheetName);
        int totalcols = xlutil1.getCellCount(sheetName, 1);
        List<String> loginData = new ArrayList();
        for (int i = 1; i <= totalrows; i++) // 1
        {
            for (int j = 0; j < totalcols; j++) // 0
            {
                loginData.add(xlutil1.getCellData(sheetName, i, j));
            }
        }
        System.out.println(loginData.toString());
      //Read Data From Excel Using the ColumnName
        Map<String, Integer> columns = xlutil1.getColumns(path1, sheetName);
        System.out.println(columns.toString());
        System.out.println("data based on column name");
        for (int i = 1; i <= totalrows; i++) // 1
        {
            String cellData = xlutil1.getCellData(sheetName, i, columns.get("username"));
            System.out.println(cellData);
        }
        System.out.println("total data");
        for (int i = 1; i <= totalrows; i++) // 1
        {
            for (int j = 0; j <totalcols; j++) {
                String cellData = xlutil1.getCellData(sheetName, i, j);
                if(!cellData.isBlank())
                    System.out.println(cellData);
            }
        }
    }
}
