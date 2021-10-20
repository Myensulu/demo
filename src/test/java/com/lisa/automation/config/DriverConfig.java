package com.lisa.automation.config;

import com.lisa.automation.common.utils.AppProperties;
import com.lisa.automation.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import static com.lisa.automation.common.constants.PropertyNames.*;

public class DriverConfig {
    protected LoginPage loginPage;
    protected WebDriver driver;

    protected String getUser(){return AppProperties.getValueFor(COMM_USERNAME);}
    protected String getPwd(){return AppProperties.getValueFor(COMM_PASSWORD);}
    protected String getUrl(){ return AppProperties.getValueFor(COMM_URL);}
    private String getBrowser(){ return AppProperties.getValueFor(BROWSER_NAME);}
    public String getPhoneVerificationURL(){ return AppProperties.getValueFor(PHONE_VERIFICATION_URL);}
    private String isHeadLess(){return AppProperties.getValueFor(IS_HEADLESS);}
    protected String getSMSServiceType(){return AppProperties.getValueFor(SMS_SERVICE_TYPE);}
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
