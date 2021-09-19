package com.lisa.automation.pages;

import com.lisa.automation.common.utils.Wrappers;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardPage extends BasePage{
    private WebDriver driver;
    private final By TITLE_LOCATOR_ELE = By.cssSelector("#root > div > div:nth-child(1) > header > div > div > div.MuiGrid-root.MuiGrid-item.MuiGrid-grid-xs-true > div > img");
    private final By SETTINGS_ELE = By.xpath("//*[text()='Settings']");
    private final By CLAIM_A_SHIFT_ELE = By.xpath("//*[text()='Claim A Shift']");

    private Logger logger = LoggerFactory.getLogger(DashboardPage.class);

    public DashboardPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    @Step("Verify Title")
    public String getTitle(){
        return getText(TITLE_LOCATOR_ELE);
    }

    @Step("Click On Settings")
    public void clickOnSettings(){
        logger.info("Clicking on Settings icon");
        clickOnElement(SETTINGS_ELE);
    }

    @Step("Click On Claim Shift ")
    public void clickClaimShiftButton(){
        logger.info("Click on Claim shift button");
        clickOnElement(CLAIM_A_SHIFT_ELE);
    }
}
