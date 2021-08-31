package com.lisa.automation.pages;

import com.lisa.automation.common.utils.Wrappers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import io.qameta.allure.Step;

public class HomePage extends Wrappers{
    private WebDriver driver;
    private final By TITLE_LOCATOR_ELE = By.cssSelector("#root > div > div:nth-child(1) > header > div > div > div.MuiGrid-root.MuiGrid-item.MuiGrid-grid-xs-true > div > img");

    public HomePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    @Step("Verify Title")
    public String getTitle(){
        return getText(TITLE_LOCATOR_ELE);
    }
}
