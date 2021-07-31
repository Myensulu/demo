package com.demo.pages;

import com.demo.common.utils.Wrappers;
import com.demo.config.DriverConfig;
import com.demo.tests.Base;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends Wrappers {

    private WebDriver driver;
    private final By TITLE_LOCATOR_ELE = By.cssSelector("");

    public HomePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    @Step("Verify Title")
    public String getTitle(){
        return getText(TITLE_LOCATOR_ELE);
    }
}
