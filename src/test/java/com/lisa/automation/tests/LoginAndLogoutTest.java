package com.lisa.automation.tests;

import com.lisa.automation.pages.DashboardPage;
import com.lisa.automation.pages.SettingsPage;
import com.lisa.automation.pages.ShiftClaimPage;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.IOException;

public class LoginAndLogoutTest extends Base{

    private DashboardPage dashboardPage;
    private SettingsPage settingsPage;
    private ShiftClaimPage shiftClaimPage;

    @BeforeClass
    public void setUpAndLogin() throws IOException, InterruptedException {
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
        Assert.assertEquals(actualPhoneNumber, updatedPhoneNumber);
    }

    @Test
    public void testOptInOptOut(){
        dashboardPage.navigateToDashboard();
        dashboardPage.clickOnSettings();
        String actualPhoneNumber = settingsPage.getCurrentPhoneNumber().trim();
        if(actualPhoneNumber.isEmpty())
            Assert.fail();
        boolean toggleButtonStatus = settingsPage.getToggleButtonStatus();
        if(!toggleButtonStatus)
            Assert.assertEquals("Toggle Not Selected", "But selected");
        dashboardPage.navigateToDashboard();
        dashboardPage.clickClaimShiftButton();
        shiftClaimPage.clickOnFirstScheduledButton();
        //need to do validation

        //opt out scenario
        dashboardPage.navigateToDashboard();
        dashboardPage.clickOnSettings();
        settingsPage.clickOnToggleButton();
        settingsPage.waitInSeconds(5);
        settingsPage.clickOnOptOutBtn();
        dashboardPage.navigateToDashboard();
        dashboardPage.clickClaimShiftButton();
        shiftClaimPage.clickOnFirstScheduledButton();
        //need to do  validation
        shiftClaimPage.clickOnAgreeButton();


    }

    @AfterClass
    public void closeBrowser(){
        driver.quit();
    }
}
