package com.demo.config;

import com.demo.common.utils.AppProperties;
import com.demo.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


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

    public String isHeadLess(){return AppProperties.getValueFor(IS_HEADLESS);}

    public WebDriver getDriver(){return driver;}



    public void setup() {
        String browser = getBrowser();
        switch(browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                if(isHeadLess().toLowerCase().equals("true"))
                    driver = new ChromeDriver(setHeadLessOptions());
                else
                    driver=new ChromeDriver();
            break;
        }
        driver.manage().window().maximize();
    }

    private ChromeOptions setHeadLessOptions(){
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--window-size=1920,1080");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--proxy-server='direct://'");
        chromeOptions.addArguments("--proxy-bypass-list=*");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--ignore-certificate-errors");
        return chromeOptions;
    }
}
