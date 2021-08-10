package com.demo.config;

import com.demo.common.utils.AppProperties;
import com.demo.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


import java.io.IOException;

import static com.demo.common.constants.PropertyNames.*;

public class DriverConfig {

    protected LoginPage loginPage;
    protected WebDriver driver;

    public String getUser(){return AppProperties.getValueFor(COMM_USERNAME);}

    public String getPwd(){return AppProperties.getValueFor(COMM_PASSWORD);}

    public String getUrl(){ return AppProperties.getValueFor(COMM_URL);}

    public String getBrowser(){ return AppProperties.getValueFor(BROWSER_NAME);}

    public String getPhoneVerification(){ return AppProperties.getValueFor(PHONE_VERIFICATION_URL);}

    public WebDriver getDriver(){return driver;}



    public void setup() {
        String browser = getBrowser();
        switch(browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver=new ChromeDriver();
            break;
        }
        driver.manage().window().maximize();
    }
}
