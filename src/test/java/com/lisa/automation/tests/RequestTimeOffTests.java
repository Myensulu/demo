package com.lisa.automation.tests;

import com.lisa.automation.pages.DashboardPage;
import com.lisa.automation.pages.requestTimeOff.CreateNewRTOPage;
import com.lisa.automation.pages.requestTimeOff.RequestTimeOffPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.lisa.automation.common.constants.DataConstants.PERSONAL;
import static com.lisa.automation.common.constants.DataConstants.SICK;

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
        createNewRTOPage.selectVacationType(SICK);
        createNewRTOPage.enterComments("");
        createNewRTOPage.clickSubmitRequest();
        Map<Integer, List<String>> requestData = requestTimeOffPage.getRequestData();
        List<String> dataList = requestData.get(0);
        System.out.println(dataList);
    }

    @Test
    public void testMixedRequestTimeOff() throws Exception {
        dashboardPage.clickRequestTimeOffButton();
        createNewRTOPage.clickOnNewRequest();
        createNewRTOPage.enterFromDate("");
        createNewRTOPage.enterToDate("");
        createNewRTOPage.disableToggleButton();
        createNewRTOPage.selectMixedVacationTypes(Arrays.asList(SICK, PERSONAL));
        createNewRTOPage.enterComments("");
        createNewRTOPage.clickSubmitRequest();
        Map<Integer, List<String>> requestData = requestTimeOffPage.getRequestData();
        List<String> dataList = requestData.get(0);
        System.out.println(dataList);
    }

    @Test
    public void testOneTypeAndMoreDaysRequestTimeOff() throws Exception {
        dashboardPage.clickRequestTimeOffButton();
        createNewRTOPage.clickOnNewRequest();
        createNewRTOPage.enterFromDate("");
        createNewRTOPage.enterToDate("");
        createNewRTOPage.selectVacationType(SICK);
        List<String> mixedVacationDropDownData = createNewRTOPage.getMixedVacationDropDownData();
        boolean allMatch = mixedVacationDropDownData.stream().allMatch(x -> x.equals(SICK));
        Assert.assertTrue(allMatch);
        createNewRTOPage.enterComments("");
        createNewRTOPage.clickSubmitRequest();
        Map<Integer, List<String>> requestData = requestTimeOffPage.getRequestData();
        List<String> dataList = requestData.get(0);
        System.out.println(dataList);
    }
    
}
