package com.lisa.automation.tests;

import com.lisa.automation.pages.DashboardPage;
import com.lisa.automation.pages.SettingsPage;
import com.lisa.automation.pages.ShiftClaimPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.IOException;
import java.sql.SQLException;

public class LoginAndLogoutTest extends Base{

    private DashboardPage dashboardPage;
    private SettingsPage settingsPage;
    private ShiftClaimPage shiftClaimPage;

    private Logger logger = LoggerFactory.getLogger(LoginAndLogoutTest.class);

    @BeforeClass
    public void setUpAndLogin() throws IOException, InterruptedException, SQLException, ClassNotFoundException {
        String customRegHash = dashboardPage.getCustomRegHash();
        loginAndVerifySMSCode();
        dashboardPage = new DashboardPage(driver);
        settingsPage = new SettingsPage(driver);
        shiftClaimPage = new ShiftClaimPage(driver);
    }

    @Test
    public void testLoginAndVerifyWithSMSCode() throws InterruptedException {
        System.out.println("Logging in and logging out");
        dashboardPage.getTitle();
        loginPage.logOut();
    }

    @Test
    public void changePhoneNumberTest(){
        String newPhoneNumber = "";
        dashboardPage.clickOnSettings();
        String actualPhoneNumber = settingsPage.getCurrentPhoneNumber().trim();
        settingsPage.enterNewNumber(newPhoneNumber);
        settingsPage.saveNumber();
        settingsPage.sendCodeAgain();
        String changePhoneNumberCode = settingsPage.getChangePhoneNumberCode(newPhoneNumber);
        settingsPage.enterSMSCodeInPopup(changePhoneNumberCode);
        settingsPage.submitPopupSMSCode();
        settingsPage.closeButton();
        String updatedPhoneNumber = settingsPage.getCurrentPhoneNumber().trim();
        Assert.assertNotEquals(actualPhoneNumber, updatedPhoneNumber);
    }

    @Test
    public void testOptInOptOut(){
        dashboardPage.navigateToDashboard();
        dashboardPage.clickOnSettings();
        String actualPhoneNumber = settingsPage.getCurrentPhoneNumber().trim();
        logger.info("Actual Phone number is: " + actualPhoneNumber);
        if(actualPhoneNumber.isEmpty())
            Assert.assertEquals("", "Didn't got phone number from text box");
        boolean toggleButtonStatus = settingsPage.getToggleButtonStatus();
        if(!toggleButtonStatus)
            Assert.assertEquals("Toggle Not Selected", "But selected");
        dashboardPage.navigateToDashboard();
        dashboardPage.clickClaimShiftButton();
        shiftClaimPage.clickOnFirstScheduledButton();
        boolean shiftAppeared = shiftClaimPage.validateScheduledShiftAppeared();
        Assert.assertTrue(shiftAppeared, "should appear as scheduled shift text");

        //opt out scenario
        dashboardPage.navigateToDashboard();
        dashboardPage.clickOnSettings();
        settingsPage.clickOnToggleButton();
        settingsPage.waitInSeconds(5);
        settingsPage.clickOnOptOutBtn();
        dashboardPage.navigateToDashboard();
        dashboardPage.clickClaimShiftButton();
        shiftClaimPage.clickOnFirstScheduledButton();
        shiftClaimPage.clickOnOptInButtonOnPopup();
        shiftClaimPage.clickOnAgreeButton();

    }

    @AfterClass
    public void closeBrowser(){
        driver.quit();
    }
}
