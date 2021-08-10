package com.demo.pages;

import com.demo.common.utils.AppProperties;
import com.demo.common.utils.Wrappers;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Set;

import static com.demo.common.constants.PropertyNames.COMM_USERNAME;
import static com.demo.common.constants.PropertyNames.PHONE_VERIFICATION_URL;

public class LoginPage extends BasePage {

    private Logger logger = LoggerFactory.getLogger(LoginPage.class);
    private String url;
    private String user;
    private String pwd;
    private WebDriver driver;

    private final By USER_NAME = By.xpath("//input[@name='username']");
    private final By PASSWORD = By.xpath("//input[@name='password']");
    private final By SUBMIT_BTN = By.xpath("//button[@type='submit']");

    private final By ENTER_SMS_CODE_ELE = By.xpath("//input[@name='code']");
    private final By SMS_CODE_SUBMIT_ELE = By.xpath("//*[text()='Submit']");
    private final By CLOSE_BTN = By.xpath("//button//*[text()='Close']");


    public LoginPage(String user, String pwd, String url, WebDriver driver) {
        super(driver);
        this.user = user;
        this.pwd = pwd;
        this.url = url;
        this.driver = driver;
    }

    public void loginStep() throws InterruptedException {
        if(!this.url.isEmpty())
            driver.get(url);
        clearAndEnterText(USER_NAME, user);
        clearAndEnterText(PASSWORD, pwd);
        clickOnElement(SUBMIT_BTN);
        waitInSeconds(10);
    }

    @Step("Get SMS Code")
    public String getSmsCode(){
        return getAuthenticationCode();
    }

    @Step("Entering SMS code")
    public void enterConfirmationCode(String confirmationCode){
        clearAndEnterText(ENTER_SMS_CODE_ELE, confirmationCode);
        clickOnElement(SMS_CODE_SUBMIT_ELE);
        waitInSeconds(10);
    }

    @Step("Click On Close Button")
    public void clickClose(){
        logger.info("Click on Close button");
        clickOnElement(CLOSE_BTN);
    }


}
