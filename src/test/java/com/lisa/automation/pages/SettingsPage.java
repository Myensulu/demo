package com.lisa.automation.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingsPage extends BasePage {

    private final By INPUT_PHONE_ELE = By.xpath("//input[@name='phone_number']");
    private final By SAVE_BTN = By.xpath("//button[@type='submit']");
    private final By OPT_TOGGLE_BTN = By.cssSelector("#root > div > div:nth-child(2) > div:nth-child(2) > div > div.MuiGrid-root.MuiGrid-container.MuiGrid-item > div.MuiGrid-root.MuiGrid-container.MuiGrid-item.MuiGrid-direction-xs-column.MuiGrid-grid-xs-10.MuiGrid-grid-md-10 > div.MuiGrid-root.MuiGrid-container.MuiGrid-item > div.MuiGrid-root.MuiGrid-item.MuiGrid-grid-xs-5.MuiGrid-grid-md-2 > span");
    private final By OPT_OUT_BTN = By.cssSelector(".MuiButtonBase-root:nth-child(2) > .MuiButton-label");


    private final By POPUP_ENTER_SMS_CODE_ELE = By.xpath("//input[@name='code']");
    private final By POPUP_SMS_CODE_SUBMIT_ELE = By.xpath("//*[text()='Submit']");
    private final By CLOSE_BTN = By.xpath("//button//*[text()='Close']");
    private final By POPUP_SEND_CODE_AGAIN_BTN = By.xpath("//*[text()='Send Code Again']");

    private Logger logger = LoggerFactory.getLogger(SettingsPage.class);

    public SettingsPage(WebDriver webDriver, int... time) {
        super(webDriver, time);
    }

    @Step("Get Existing phone number")
    public String getCurrentPhoneNumber(){
        logger.info("Get Current phone number");
        waitInSeconds(5);
        waitForElementToAppear(INPUT_PHONE_ELE, 30);
        return getText(INPUT_PHONE_ELE);
    }

    @Step("Input Change number")
    public void enterNewNumber(String phoneNumber) {
        logger.info("Enter Change Number");
        waitForElementToAppear(INPUT_PHONE_ELE, 30);
        clearAndEnterText(INPUT_PHONE_ELE, phoneNumber);
    }

    @Step("Save Change Number")
    public void saveNumber(){
        logger.info("Save Change Phone Number");
        clickOnElement(SAVE_BTN);
        waitForElementToAppear(POPUP_ENTER_SMS_CODE_ELE, 60);
    }

    @Step("Enter SMS Code in Popup")
    public void enterSMSCodeInPopup(String smsCode){
        logger.info("Enter SMS Code");
        clearAndEnterText(POPUP_ENTER_SMS_CODE_ELE, smsCode);
    }

    @Step("Click On Send Code again")
    public void sendCodeAgain(){
        logger.info("Click On send code again button");
        clickOnElement(POPUP_SMS_CODE_SUBMIT_ELE);
    }

    @Step("Save Change Number")
    public void submitPopupSMSCode(){
        logger.info("Submit popup sms code");
        clickOnElement(POPUP_SEND_CODE_AGAIN_BTN);
        waitInSeconds(10);
    }

    @Step("Click on close button")
    public void closeButton(){
        logger.info("Click on Close popup after successful");
        clickOnElement(CLOSE_BTN);
        waitInSeconds(2);
    }

    @Step("Get Toggle button status")
    public boolean getToggleButtonStatus(){
        logger.info("Toggle button status");
        return isSelected(OPT_TOGGLE_BTN);
    }

    @Step("Click on Toggle button")
    public void clickOnToggleButton(){
        logger.info("Click on Toggle button");
        clickOnElement(OPT_TOGGLE_BTN);
    }

    @Step("Click on opt out butotn")
    public void clickOnOptOutBtn(){
        logger.info("Click on opt out button");
        clickOnElement(OPT_OUT_BTN);
    }



}
