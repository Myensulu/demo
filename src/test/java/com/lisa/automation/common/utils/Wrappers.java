package com.lisa.automation.common.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Wrappers {

    private final WebDriver webDriver;
    private int defaultTime = 60;

    public Wrappers(WebDriver webDriver, int... time) {
        this.webDriver = webDriver;
        if (time.length > 0) {
            defaultTime = time[0];
        }
    }

    /**
     * Generic wait method.
     */
    private boolean waitForElement(Object objLocator, int timeOut) {
        try {
            Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withTimeout(Duration.ofSeconds(timeOut))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);
            if (objLocator instanceof By)
                return wait.until((element) -> element.findElements((By) objLocator).size() > 0);
            else if (objLocator instanceof WebElement)
                return ((WebElement) objLocator).isDisplayed();
            else
                throw new NoSuchElementException("Given element is null");
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Waits till the element appears on the dom. If element not
     * visible on dom after timeout, throws NoSuchElement Exception
     */
    public void waitForElementVisible(By objLocator, int timeOut) {
        if (!waitForElement(objLocator, timeOut)) {
            throw new NoSuchElementException(String.format("%s unable to find the element on page", objLocator));
        }
    }

    /**
     * Executes the Javascript
     */
    public Object executeScript(String script) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        return js.executeScript(script);
    }

    /**
     * Waits till the element does not appears on the dom or given timeout.
     * If the element is still visible on dom, throws Exception
     */
    public void waitForElementInvisible(By objLocator, int timeOut) throws Exception {
        boolean waitForNotVisible = new WebDriverWait(webDriver, timeOut)
                .until(ExpectedConditions.invisibilityOfElementLocated(objLocator));
        if (!waitForNotVisible) {
            throw new Exception(String.format("%s able to see on the element", objLocator));
        }
    }

    /**
     * Returns the webelement if found on dom after waiting for a given time, otherwise
     * throws TimeoutException, Exception
     */
    public WebElement getWebElement(Object objLocator) {
        WebElement element = null;
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver)
                .withTimeout(Duration.ofSeconds(defaultTime))
                .pollingEvery(Duration.ofMillis(500)).ignoring(NoSuchElementException.class)
                .ignoring(ElementClickInterceptedException.class);
            element = (WebElement) wait
                    .until((Function<WebDriver, WebElement>) driver -> driver.findElement((By) objLocator));
        } catch (TimeoutException e) {
            throw new TimeoutException("Element Not identified by " + objLocator.toString() + " was not found after "
                    + defaultTime + " seconds with 1 second interval");
        }
        return element;
    }

    /**
     * Returns the list of webelement if found on dom, otherwise returns an empty list
     */
    public List<WebElement> getWebElements(By objLocator) {
        List<WebElement> listOfEle;
        if (waitForElement(objLocator, defaultTime)) {
            listOfEle = new ArrayList<>(webDriver.findElements(objLocator));
        } else {
            listOfEle = new ArrayList<>();
        }
        return listOfEle;
    }

    /**
     * Clears and enters the text on the element
     */
    public void clearAndEnterText(Object objLocator, String text) throws NoSuchElementException {
        WebElement element;
        if (objLocator instanceof By)
            element = getWebElement((By) objLocator);
        else if (objLocator instanceof WebElement)
            element = (WebElement) objLocator;
        else
            throw new NoSuchElementException("Given element is null");
        element.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END));
        for (int i = 0; i < text.length(); i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            element.sendKeys(String.valueOf(text.charAt(i)));
        }
    }

    /**
     * Performs Click action on the element. If not found, throws NoSuchElement
     * Exception
     */
    public void clickOnElement(Object objLocator) {
        if (objLocator instanceof By)
            getWebElement((By) objLocator).click();
        else if (objLocator instanceof WebElement)
            ((WebElement) objLocator).click();
        else
            throw new NoSuchElementException("Given element is null");
    }

    /**
     * Changes the control on the new window
     */
    public void switchToWindow(String windowName) {
        webDriver.switchTo().window(windowName);
    }

    /**
     * Returns the window name where control exists
     */
    public String getWindowName() {
        return webDriver.getWindowHandle();
    }

    /**
     * Returns the list of window names, opened during session
     */
    public List<String> getWindowNames() {
        return new ArrayList<>(webDriver.getWindowHandles());
    }

    /**
     * Returns the text from the element
     */
    public String getText(By objLocator) {
        return getWebElement(objLocator).getText();
    }

    /**
     * Halt execution of the script based on given time
     */
    public void waitInSeconds(int timeToWait) {
        try{Thread.sleep(timeToWait * 1000);}catch (InterruptedException e){};
    }

    /**
     * Waits till element appears on the dom or given timeout and throws Interrupted Exception if not
     * appears
     */
    public String waitForElementToAppear(By locator, int timeOut) {
        int count = 1;
        while (count <= timeOut) {
            if (webDriver.findElements(locator).size() > 0) {
                break;
            }
            waitInSeconds(1);
            count++;
        }
        if (count == timeOut + 1) {
            return "Unable to find given element after waiting " + timeOut + "seconds";
        }
        waitInSeconds(2);
        return "";
    }

    /**
     * Waits till element not appears on the dom or given timeout and throws Interrupted Exception if
     * not appears
     */
    public String waitForElementToDisappear(By locator, int timeOut) throws InterruptedException {
        int count = 1;
        while (count <= timeOut) {
            if (webDriver.findElements(locator).size() == 0) {
                break;
            }
            Thread.sleep(1000);
            count++;
        }
        if (count == timeOut + 1) {
            return "Still see the given element after waiting " + timeOut + "seconds";
        }
        return "";
    }

    /**
     * Verify the element is selected or not
     */
    public boolean isSelected(By locator) {
        return getWebElement(locator).isSelected();
    }

    /**
     * Verify the element is displayed or not
     */
    public boolean isDisplayed(By locator) {
        return getWebElement(locator).isDisplayed();
    }

    /**
     * Verify the element is enabled or not
     */
    public boolean isEnabled(By locator) {
        return getWebElement(locator).isEnabled();
    }
}
