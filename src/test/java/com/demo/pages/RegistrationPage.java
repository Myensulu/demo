package com.demo.pages;

import com.demo.common.utils.AppProperties;
import com.demo.common.utils.Wrappers;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Set;

import static com.demo.common.constants.PropertyNames.PHONE_VERIFICATION_URL;
import static com.demo.common.constants.PropertyNames.REGISTER_URL;

public class RegistrationPage extends BasePage {

    //Locators

    private final By LAST_NAME_TEXTBOX = By.id("//input[@id='last_name']");
    private final By DOB_TEXTBOX = By.id("//input[@id='dob']");
    private final By SSN_TEXTBOX = By.xpath("//input[@name='ssn6']");
    private final By EMAIL_TEXTBOX = By.id("//input[@id='email']");
    private final By PASSWORD_REG_TEXTBOX = By.id("//input[@id='password']");
    private final By CONFIRM_PASSWORD_TEXTBOX = By.id("//input[@id='confirm_password']");
    private final By PHONE_NUMBER_TEXTBOX = By.xpath("//input[@name='phone_number']");
    private final By REGISTER_BTN = By.xpath("//*[text()='REGISTER']");
    private final By REGISTRATION_CONFIRM_BTN = By.xpath("//button//*[text()='CONFIRM']");

    private final By ENTER_SMS_CODE_ELE = By.xpath("//input[@name='code']");
    private final By SMS_CODE_SUBMIT_ELE = By.xpath("//*[text()='Submit']");
    private final By SIGN_IN_PAGE_LINK = By.xpath("//a[text()='Go to Sign In page']");


    private Logger logger = LoggerFactory.getLogger(RegistrationPage.class);
    private WebDriver driver;



    public RegistrationPage(WebDriver webDriver, int... time) {
        super(webDriver, time);
        this.driver = webDriver;
    }

    @Step("Open Registration page")
    public void openRegistrationPage(){
        logger.info("Open the registration page");
        driver.get(AppProperties.getValueFor(REGISTER_URL));
        waitForElementToAppear(LAST_NAME_TEXTBOX, 30);
    }

    @Step("Lisa Portal Registration")
    public String fillAndConfirmPortalRegistrationForm(String lastName, String birthday, String socialSecurity,
                                           String email, String password, String confPassword, String phoneNumber ){
    try{
        logger.info("Enter Last Name");
        clearAndEnterText(LAST_NAME_TEXTBOX, lastName);

        logger.info("Enter Birthday Date");
        clearAndEnterText(DOB_TEXTBOX, birthday);

        logger.info("Enter SSN");
        clearAndEnterText(SSN_TEXTBOX, socialSecurity);

        logger.info("Enter Email");
        clearAndEnterText(EMAIL_TEXTBOX, email);

        logger.info("Enter Password");
        clearAndEnterText(PASSWORD_REG_TEXTBOX, password);

        logger.info("Enter Confirmation Password");
        clearAndEnterText(CONFIRM_PASSWORD_TEXTBOX, confPassword);

        logger.info("Enter Phone Number");
        clearAndEnterText(PHONE_NUMBER_TEXTBOX, phoneNumber);

        logger.info("Click on Registration");
        clickOnElement(REGISTER_BTN);

        logger.info("Wait For Confirmation Popup");
        waitForElementVisible(REGISTRATION_CONFIRM_BTN, 100);

        logger.info("Click on Confirmation Popup");
        clickOnElement(REGISTRATION_CONFIRM_BTN);

        logger.info("Get Verification Code");
        String verificationCode = getVerificationCode();
        if(verificationCode == null)
            throw new Exception("Got Null in Verification Code");

        logger.info("Enter Verification Code");
        clearAndEnterText(ENTER_SMS_CODE_ELE, verificationCode);

        logger.info("Click On Submit button");
        clickOnElement(SMS_CODE_SUBMIT_ELE);
        waitInSeconds(10);

        logger.info("Navigate to Sign In page");
        clickOnElement(SIGN_IN_PAGE_LINK);

    }catch (Exception e){
        return e.getMessage();
    }
    return "";
    }

}
