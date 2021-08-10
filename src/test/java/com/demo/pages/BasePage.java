package com.demo.pages;

import com.demo.common.utils.AppProperties;
import com.demo.common.utils.Wrappers;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Iterator;
import java.util.Set;

import static com.demo.common.constants.PropertyNames.PHONE_VERIFICATION_URL;

public class BasePage extends Wrappers {

    private final By SMS_CODE_VERIFICATION_ELE = By.xpath("//td[contains(text(), 'Your verification code is')]");
    private final By SMS_CODE_ELE = By.xpath("//td[contains(text(), 'authentication code')]");
    private final By LATEST_EMAIL_ELE = By.xpath("//*[@id='schranka']/tr[1]");
    private final By EMAIL_VERIFICATION_ELE = By.xpath("//*[contains(text(), 'Your verification code is')]");


    private WebDriver driver;
    public BasePage(WebDriver webDriver, int... time) {
        super(webDriver, time);
        this.driver = webDriver;
    }

    private String getSmsCode(String codeType){
        By elementFor;
        switch (codeType){
            case "VERIFICATION":
                elementFor = SMS_CODE_VERIFICATION_ELE;
                break;
            case "AUTHENTICATION":
                elementFor = SMS_CODE_ELE;
                break;
            default:
                return null;
        }
        waitForElementVisible(elementFor, 300);
        String mainWindow = getWindowName();
        executeScript("window.open('');");
        Set<String> allWindowHandles = driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandles.iterator();
        String code = null;
        while (iterator.hasNext()) {
            String childWindow = iterator.next();
            if (!mainWindow.equalsIgnoreCase(childWindow)) {
                switchToWindow(childWindow);
                driver.get(AppProperties.getValueFor(PHONE_VERIFICATION_URL));
                code = getWebElements(elementFor).get(0).getText();
                driver.close();
                switchToWindow(mainWindow);
            }
        }
        if(code != null) {
            String[] split = code.split("is");
            String actualCode = split[1];
            actualCode = actualCode.replaceAll("[-+.^:,]", "");
            return actualCode.trim();
        }
        return code;
    }

    @Step("Get Verification Code")
    public String getVerificationCode(){ return getSmsCode("VERIFICATION"); }

    @Step("Get Authentication Code")
    public String getAuthenticationCode(){
        return getSmsCode("AUTHENTICATION");
    }

    @Step("Email Confirmation Code")
    public String getEmailConfirmationCode() {
        String mainWindow = getWindowName();
        executeScript("window.open('');");
        Set<String> allWindowHandles = driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandles.iterator();
        String code = null;
        while (iterator.hasNext()) {
            String childWindow = iterator.next();
            if (!mainWindow.equalsIgnoreCase(childWindow)) {
                switchToWindow(childWindow);
                driver.get(AppProperties.getValueFor(PHONE_VERIFICATION_URL));
                waitInSeconds(10);
                clickOnElement(LATEST_EMAIL_ELE);
                waitInSeconds(5);
                code = getWebElements(EMAIL_VERIFICATION_ELE).get(0).getText();
                driver.close();
                switchToWindow(mainWindow);
            }
        }
        if(code != null) {
            String[] split = code.split("is");
            String actualCode = split[1];
            actualCode = actualCode.replaceAll("[-+.^:,]", "");
            return actualCode.trim();
        }
        return code;
    }
}
