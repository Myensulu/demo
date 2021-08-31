package com.lisa.automation.pages;

import com.lisa.automation.common.utils.AppProperties;
import com.lisa.automation.common.utils.Wrappers;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.util.Iterator;
import java.util.Set;
import static com.lisa.automation.common.constants.PropertyNames.FAKE_EMAIL_URL;
import static com.lisa.automation.common.constants.PropertyNames.PHONE_VERIFICATION_URL2;

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
        String mainWindow = getWindowName();
        executeScript("window.open('');");
        Set<String> allWindowHandles = driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandles.iterator();
        String code = null;
        while (iterator.hasNext()) {
            String childWindow = iterator.next();
            if (!mainWindow.equalsIgnoreCase(childWindow)) {
                switchToWindow(childWindow);
                driver.get(AppProperties.getValueFor(PHONE_VERIFICATION_URL2));
                waitInSeconds(30);
                driver.navigate().refresh();
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
                driver.get(AppProperties.getValueFor(FAKE_EMAIL_URL));
                waitInSeconds(10);
                clickOnElement(LATEST_EMAIL_ELE);
                waitInSeconds(10);
                driver.switchTo().frame(getWebElement(By.id("iframeMail")));
                code = getWebElements(EMAIL_VERIFICATION_ELE).get(0).getText();
                driver.switchTo().defaultContent();
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

