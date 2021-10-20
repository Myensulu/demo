package com.lisa.automation.pages;

import com.lisa.automation.common.utils.Utilities;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Set;

import static com.lisa.automation.common.constants.DataConstants.ACTUAL_PHONE_NUMBER;

public class LoginPage extends BasePage {
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
    private final By PROFILE_ICON = By.xpath("//button[@type='button']");
    private final By LOGOUT_BTN = By.xpath("//*[text()='Log Out']");
    private final By CLOSE_BTN = By.xpath("//button//*[text()='Close']");

    private final By FORGOT_PASSWORD_LINK = By.xpath("//*[text()='Forgot Password']");

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

    public String getSmsCode() {
        String mainWindow = getWindowName();
        executeScript("window.open('');");
        Set<String> allWindowHandles = driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandles.iterator();
        String smsCode = null;
        while (iterator.hasNext()) {
            String childWindow = iterator.next();
            if (!mainWindow.equalsIgnoreCase(childWindow)) {
                switchToWindow(childWindow);
                driver.get(Utilities.getPhoneNumberURL(ACTUAL_PHONE_NUMBER));
                smsCode = getText(SMS_CODE_ELE);
                driver.close();
                switchToWindow(mainWindow);
            }
        }
        if (smsCode != null) {
            String[] split = smsCode.split("is");
            String code = split[1];
            code = code.replaceAll("[-+.^:,]", "");
            return code.trim();
        }
        return smsCode;
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

    @Step("Logging out")
    public void logOut() throws InterruptedException {
        clickOnElement(PROFILE_ICON);
        waitInSeconds(2);
        clickOnElement(LOGOUT_BTN);
    }

    @Step("Forgot password link")
    public void clickForgotPasswordLink(){
        logger.info("Click on Forgot Password link");
        clickOnElement(FORGOT_PASSWORD_LINK);
    }


}
