package com.lisa.automation.tests;

import com.lisa.automation.pages.DashboardPage;
import com.lisa.automation.pages.RequestTimeOffPage;
import com.lisa.automation.pages.SettingsPage;
import com.lisa.automation.pages.ShiftClaimPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class RequestTimoffTests extends Base {

    private DashboardPage dashboardPage;
    private RequestTimeOffPage requestTimeOffPage;


    @BeforeClass
    public void setUpAndLogin() throws Exception {
        String customRegHash = dashboardPage.getCustomRegHash();
        loginAndVerifySMSCode();
        dashboardPage = new DashboardPage(driver);
        requestTimeOffPage = new RequestTimeOffPage(driver);
    }

    @Test
    public void testRequestTimeOff() throws Exception {
        dashboardPage.clickRequestTimeOffButton();
        requestTimeOffPage.clickOnNewRequest();
        requestTimeOffPage.enterFromDate("");
        requestTimeOffPage.enterToDate("");
        requestTimeOffPage.selectVacationType("sick");
        requestTimeOffPage.enterComments("");
        requestTimeOffPage.clickSubmitRequest();
        Map<Integer, List<String>> requestData = requestTimeOffPage.getRequestData();
        List<String> dataList = requestData.get(0);
        System.out.println(dataList);
    }
}
