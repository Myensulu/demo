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

public class LoginPage extends Wrappers {

    private Logger logger = LoggerFactory.getLogger(LoginPage.class);
    private String url;
    private String user;
    private String pwd;
    private WebDriver driver;

    private final By USER_NAME = By.xpath("//input[@name='username']");
    private final By PASSWORD = By.xpath("//input[@name='password']");
    private final By SUBMIT_BTN = By.xpath("//button[@type='submit']");
    private final By SMS_CODE_ELE = By.xpath("//td[contains(text(), 'authentication code')]");
    private final By ENTER_SMS_CODE_ELE = By.xpath("//input[@name='code']");
    private final By SMS_CODE_SUBMIT_ELE = By.xpath("//*[text()='Submit']");


    public LoginPage(String user, String pwd, String url, WebDriver driver) {
        super(driver);
        this.user = user;
        this.pwd = pwd;
        this.url = url;
        this.driver = driver;
    }

    public void loginStep() throws InterruptedException {
        driver.get(url);
        clearAndEnterText(USER_NAME, user);
        clearAndEnterText(PASSWORD, pwd);
        clickOnElement(SUBMIT_BTN);
    }

    public String getSmsCode(){
        String mainWindow = getWindowName();
        String selectLinkOpenInNewTab = Keys.chord(Keys.CONTROL,"N");
        getWebElement(By.xpath("//html/body")).sendKeys(selectLinkOpenInNewTab);
        Set<String> allWindowHandles = driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandles.iterator();
        String smsCode = null;
        while (iterator.hasNext()) {
            String childWindow = iterator.next();
            if (!mainWindow.equalsIgnoreCase(childWindow)) {
                switchToWindow(childWindow);
                driver.get(AppProperties.getValueFor(PHONE_VERIFICATION_URL));
                smsCode = getText(SMS_CODE_ELE);
                driver.close();
                switchToWindow(mainWindow);
            }
        }
        String[] split = smsCode.split("is");
        String code = split[1];
        code = code.replaceAll("[-+.^:,]","");
        return code.trim();
    }

    @Step("Entering SMS code")
    public void enterSmsCode(String smsCode){
        clearAndEnterText(ENTER_SMS_CODE_ELE, smsCode);
        clickOnElement(SMS_CODE_SUBMIT_ELE);
    }
}
