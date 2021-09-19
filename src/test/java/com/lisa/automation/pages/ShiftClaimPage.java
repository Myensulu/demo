package com.lisa.automation.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShiftClaimPage extends BasePage {

    private final By SCHEDULED_BTN = By.xpath("//button//*[text()='Scheduled']");
    private final By AGREE_BTN = By.cssSelector(".MuiButtonBase-root:nth-child(2) > .MuiButton-label");

    private Logger logger = LoggerFactory.getLogger(ShiftClaimPage.class);

    public ShiftClaimPage(WebDriver webDriver, int... time) {
        super(webDriver, time);
    }

    @Step("Click on First Scheduled button")
    public void clickOnFirstScheduledButton(){
        logger.info("Click on First Scheduled button");
        waitForElementToAppear(SCHEDULED_BTN, 60);
        getWebElements(SCHEDULED_BTN).get(0).click();
    }

    @Step("Click on Agree button")
    public void clickOnAgreeButton(){
        logger.info("Click on Agree button");
        clickOnElement(AGREE_BTN);
    }

}
