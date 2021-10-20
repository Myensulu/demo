package com.lisa.automation.pages;

import com.lisa.automation.common.constants.DataConstants;
import com.lisa.automation.common.utils.AppProperties;
import com.lisa.automation.common.utils.DbManager;
import com.lisa.automation.common.utils.Utilities;
import com.lisa.automation.common.utils.Wrappers;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.lisa.automation.common.constants.DataConstants.ACTUAL_PHONE_NUMBER;
import static com.lisa.automation.common.constants.DataConstants.CHANGE_PHONE_NUMBER;
import static com.lisa.automation.common.constants.PropertyNames.*;

public class BasePage extends Wrappers {

    private final By FREE_PHONE_SMS_CODE_VERIFICATION_ELE = By.xpath("//td[contains(text(), 'Your verification code is')]");
    private final By FREE_PHONE_SMS_CODE_AUTHORIZATION_ELE = By.xpath("//td[contains(text(), 'authentication code')]");

    private final By LATEST_EMAIL_ELE = By.xpath("//*[@id='schranka']/tr[1]");
    private final By EMAIL_VERIFICATION_ELE = By.xpath("//*[contains(text(), 'Your verification code is')]");
    private final By DASHBOARD_ELE = By.cssSelector("#root > div > div:nth-child(1) > header > div > div > div.MuiGrid-root.MuiGrid-item.MuiGrid-grid-xs-true > div > img");

    //Google Voice Elements
    private final By SIGN_IN_BTN = By.xpath("//a[text()='Sign in']");
    private final By INPUT_USERNAME = By.cssSelector("input[type='email']");
    private final By INPUT_PASSWORD = By.cssSelector("input[type='password']");
    private final By NEXT_BTN = By.xpath("//span[text()='Next']");
    private final By DATA_ICON_MESSAGE_ELE = By.cssSelector("div[layout='row'] [data-mat-icon-name='message']");
    private final By GV_CONTACT_ELE = By.cssSelector("div[layout=row] [gv-test-id='item-contact']");
    private final By SMS_CODE_VERIFICATION_ELE = By.xpath("//div[@gv-test-id='bubble']//*[contains(text(), 'Your verification code is')]");
    private final By SMS_CODE_AUTHORIZATION_ELE = By.xpath("//div[@gv-test-id='bubble']//*[contains(text(), 'Your authentication code is')]");


    private Logger logger = LoggerFactory.getLogger(BasePage.class);

    protected WebDriver driver;
    private String serviceType;

    public BasePage(WebDriver webDriver, int... time) {
        super(webDriver, time);
        this.driver = webDriver;
        serviceType = AppProperties.getValueFor(SMS_SERVICE_TYPE);
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

    public String getCustomRegHash() throws SQLException, ClassNotFoundException {
        String query = "select * from public.fn_setup_custom_reg_hash('9189108','000000','19540910');";
        return new DbManager().runQuery(query).getString(0);
    }

    @Step("Navigate to Home/Root/Dashboard Page")
    public void navigateToDashboard(){
        logger.info("Navigate to Dashboard Page");
        clickOnElement(DASHBOARD_ELE);
    }

    private String getSMSCodeFromGoogleVoiceService(By elementFor, boolean isChangeURL){
        String username;
        String password;
        if(isChangeURL){
            username = AppProperties.getValueFor(CHANGE_GV_USERNAME);
            password = AppProperties.getValueFor(CHANGE_GV_PASSWORD);
        }else {
            username = AppProperties.getValueFor(ACTUAL_GV_USERNAME);
            password = AppProperties.getValueFor(ACTUAL_GV_PASSWORD);
        }
        logger.info("Entering Google Voice URL");
        driver.get(AppProperties.getValueFor(GOOGLE_VOICE_URL));
        logger.info("Click on Sign In Button");
        waitForElementToAppear(SIGN_IN_BTN, 60);
        clickOnElement(SIGN_IN_BTN);
        logger.info("Enter Google Voice username");
        waitForElementToAppear(INPUT_USERNAME, 60);
        clearAndEnterText(INPUT_USERNAME, username);
        clickOnElement(NEXT_BTN);
        logger.info("Enter Google Voice Password");
        waitForElementToAppear(INPUT_PASSWORD, 60);
        clearAndEnterText(INPUT_PASSWORD, password);
        logger.info("Click on Next button");
        clickOnElement(NEXT_BTN);
        waitForElementToAppear(DATA_ICON_MESSAGE_ELE, 60);
        waitInSeconds(30);
        logger.info("Click on Left Nave Message Icon");
        clickOnElement(DATA_ICON_MESSAGE_ELE);
        logger.info("Click on Contact");
        clickOnElement(GV_CONTACT_ELE);
        List<WebElement> codeElementList = getWebElements(elementFor);
       return codeElementList.get(codeElementList.size()-1).getText();
    }

    private String getSMSCodeFromFreePhoneService(By elementFor, boolean isChangeURL){
        String phoneURL;
        if(isChangeURL)
            phoneURL = Utilities.getChangePhoneNumberURL(CHANGE_PHONE_NUMBER);
        else
            phoneURL = Utilities.getPhoneNumberURL(ACTUAL_PHONE_NUMBER);
        driver.get(phoneURL);
        waitInSeconds(30);
        driver.navigate().refresh();
        return getWebElements(elementFor).get(0).getText();
    }

    private String getSmsCode(String serviceType, String codeType, boolean isChangeURL){
        By elementFor;

        switch (codeType){
            case "VERIFICATION":
                if(serviceType.equalsIgnoreCase("google"))
                    elementFor = SMS_CODE_VERIFICATION_ELE;
                else
                    elementFor = FREE_PHONE_SMS_CODE_VERIFICATION_ELE;
                break;
            case "AUTHENTICATION":
                if(serviceType.equalsIgnoreCase("google"))
                    elementFor = SMS_CODE_AUTHORIZATION_ELE;
                else
                    elementFor = FREE_PHONE_SMS_CODE_AUTHORIZATION_ELE;
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
                if(serviceType.equalsIgnoreCase("google"))
                    code = getSMSCodeFromGoogleVoiceService(elementFor, isChangeURL);
                else
                    code = getSMSCodeFromFreePhoneService(elementFor, isChangeURL);

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
    public String getVerificationCode(){ return getSmsCode(serviceType, "VERIFICATION", false); }

    @Step("Get Authentication Code")
    public String getAuthenticationCode(){ return getSmsCode(serviceType, "AUTHENTICATION", false); }

    @Step("Change verification Code")
    public String getChangedVerificationCode(){ return getSmsCode(serviceType, "VERIFICATION", true); }

    @Step("Change authentication Code")
    public String getChangedAuthenticationCode(){ return getSmsCode(serviceType, "AUTHENTICATION", true); }

}

