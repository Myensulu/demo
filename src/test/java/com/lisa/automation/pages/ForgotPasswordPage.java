package com.lisa.automation.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForgotPasswordPage extends BasePage{

    private final By LAST_NAME_TEXTBOX = By.xpath("//input[@id='last_name']");
    private final By DOB_TEXTBOX = By.xpath("//input[@id='dob']");
    private final By SSN_TEXTBOX = By.xpath("//input[@name='ssn6']");
    private final By EMAIL_TEXTBOX = By.xpath("//input[@id='email']");
    private final By SUBMIT_BTN = By.xpath("//button[@type='submit']");
    private final By EMAIL_CODE_TEXTBOX = By.xpath("//input[@name='code']");
    private final By NEW_PASSWORD_TEXTBOX = By.xpath("//input[@id='new_password']");
    private final By CONFIRM_PASSWORD_TEXTBOX = By.xpath("//input[@id='confirm_password']");  //it's also re-type password loc
    private final By FAKE_EMAIL_BACK_BUTTON = By.cssSelector("body > div > div:nth-child(3) > div > div.row.infoMail.borderBottom.noPadding > div.col-xs-2.maxWidth.text-center.vertical-middle.flex-text-left > span");


// *********** register user -> login & logout  ->  forgot pwd workflow ->  login again **********

    private Logger logger = LoggerFactory.getLogger(ForgotPasswordPage.class);
    private WebDriver driver;

    public ForgotPasswordPage(WebDriver webDriver, int... time) {
        super(webDriver, time);
        this.driver = webDriver;
    }

    @Step("Filling up the fields for Forgot password workflow")
    public void fillAndSubmitForgotPasswordDetails(String lastName, String birthday, String socialSecurity,
                                            String email, String newPassword, String confirmPassword, LoginPage loginPage){
        logger.info("Enter Last Name");
        waitForElementToAppear(LAST_NAME_TEXTBOX, 60);
        clearAndEnterText(LAST_NAME_TEXTBOX, lastName);

        logger.info("Enter Birthday Date");
        getWebElement(DOB_TEXTBOX).sendKeys(birthday);

        logger.info("Enter SSN");
        clearAndEnterText(SSN_TEXTBOX, socialSecurity);

        logger.info("Enter Email");
        clearAndEnterText(EMAIL_TEXTBOX, email);
        clickOnElement(SUBMIT_BTN);
        //Enter Email Code for extra validation
        String emailConfirmationCode = loginPage.getEmailConfirmationCode();
        clearAndEnterText(EMAIL_CODE_TEXTBOX, emailConfirmationCode);
        clearAndEnterText(NEW_PASSWORD_TEXTBOX, newPassword);
        clearAndEnterText(CONFIRM_PASSWORD_TEXTBOX, confirmPassword);
        clickOnElement(SUBMIT_BTN);
    }

}
