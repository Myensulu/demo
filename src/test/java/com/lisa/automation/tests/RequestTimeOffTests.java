package com.lisa.automation.tests;

import com.lisa.automation.pages.DashboardPage;
import com.lisa.automation.pages.requestTimeOff.CreateNewRTOPage;
import com.lisa.automation.pages.requestTimeOff.RequestTimeOffPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class RequestTimeOffTests extends Base {

    private DashboardPage dashboardPage;
    private RequestTimeOffPage requestTimeOffPage;
    private CreateNewRTOPage createNewRTOPage;


    @BeforeClass
    public void setUpAndLogin() throws Exception {
        String customRegHash = dashboardPage.getCustomRegHash();
        loginAndVerifySMSCode();
        dashboardPage = new DashboardPage(driver);
        requestTimeOffPage = new RequestTimeOffPage(driver);
        createNewRTOPage = new CreateNewRTOPage(driver);
    }

    @Test
    public void testRequestTimeOff() throws Exception {
        dashboardPage.clickRequestTimeOffButton();
        createNewRTOPage.clickOnNewRequest();
        createNewRTOPage.enterFromDate("");
        createNewRTOPage.enterToDate("");
        createNewRTOPage.selectVacationType("sick");
        createNewRTOPage.enterComments("");
        createNewRTOPage.clickSubmitRequest();
        Map<Integer, List<String>> requestData = requestTimeOffPage.getRequestData();
        List<String> dataList = requestData.get(0);
        System.out.println(dataList);
    }
}
