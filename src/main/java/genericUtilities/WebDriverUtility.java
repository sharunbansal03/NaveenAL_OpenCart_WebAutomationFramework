package genericUtilities;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains generic methods related to Selenium WebDriver
 * 
 * @author sharu
 *
 */
public class WebDriverUtility {

	/**
	 * This method will maximize the browser window
	 * 
	 * @param driver
	 */
	public void maximizeWindow(WebDriver driver) {
		driver.manage().window().maximize();
	}

	/**
	 * This method will minimize the window
	 * 
	 * @param driver
	 */
	public void minimizeWindow(WebDriver driver) {
		driver.manage().window().minimize();
	}

	/**
	 * This method will wait for every web element for a maximum of 20 seconds to
	 * load
	 * 
	 * @param driver
	 */
	public void waitForPageToLoad(WebDriver driver) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
	}

	/**
	 * This method will wait for given element to become visible for given duration
	 * before throwing exception
	 * 
	 * @param driver
	 * @param element
	 */
	public void waitForElementToBeVisible(WebDriver driver, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * This method will wait for particular element to be clickable for 20 seconds
	 * before throwing exception
	 * 
	 * @param driver
	 * @param element
	 */
	public void waitForElementToBeClickable(WebDriver driver, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	/**
	 * This method will switch the driver control to given frame index
	 * 
	 * @param driver
	 * @param index
	 */
	public void switchToFrame(WebDriver driver, int index) {
		driver.switchTo().frame(index);
	}

	/**
	 * This method will switch the driver control to given frame id or name
	 * 
	 * @param driver
	 * @param idOrName
	 */
	public void switchToFrame(WebDriver driver, String idOrName) {
		driver.switchTo().frame(idOrName);
	}

	/**
	 * This method will switch the driver control to given frame by locator
	 * 
	 * @param driver
	 * @param ele
	 */
	public void switchToFrame(WebDriver driver, WebElement ele) {
		driver.switchTo().frame(ele);
	}

	/**
	 * This method will switch back the control to default content
	 * 
	 * @param driver
	 */
	public void switchToDefaultContent(WebDriver driver) {
		driver.switchTo().defaultContent();
	}

	/**
	 * This method will switch to alert and accept
	 * 
	 * @param driver
	 */
	public void switchToAlertAndAccept(WebDriver driver) {
		driver.switchTo().alert().accept();
	}

	/**
	 * This method will switch to alert pop up and dismiss
	 * 
	 * @param driver
	 */
	public void switchToAlertAndDismiss(WebDriver driver) {
		driver.switchTo().alert().dismiss();
	}

	/**
	 * This method will switch to alert and return text
	 * 
	 * @param driver
	 */
	public String switchToAlertAndGetText(WebDriver driver) {
		return driver.switchTo().alert().getText();
	}

	/**
	 * This method will switch to window based on partial window title
	 * 
	 * @param driver
	 * @param partialWinTitle
	 */
	public void switchToWindow(WebDriver driver, String partialWinTitle) {
		Set<String> windowHandles = driver.getWindowHandles();
		for (String winId : windowHandles) {
			String currentWindowTitle = driver.switchTo().window(winId).getTitle();
			if (currentWindowTitle.contains(partialWinTitle)) {
				break;
			}
		}
	}

	/**
	 * This method will select option by visible text
	 * 
	 * @param element
	 * @param text
	 */
	public void handleDropDown(WebElement element, String text) {
		Select sel = new Select(element);
		sel.selectByVisibleText(text);
	}

	/**
	 * This method will select option by index
	 * 
	 * @param element
	 * @param text
	 */
	public void handleDropDown(WebElement element, int index) {
		Select sel = new Select(element);
		sel.selectByIndex(index);
	}

	/**
	 * This method will select option by value
	 * 
	 * @param element
	 * @param text
	 */
	public void handleDropDown(String value, WebElement element) {
		Select sel = new Select(element);
		sel.selectByValue(value);
	}


	/**
	 * This method will move mouse curson to given element
	 * 
	 * @param driver
	 * @param ele
	 */
	public void mouseHoverAction(WebDriver driver, WebElement ele) {
		Actions act = new Actions(driver);
		act.moveToElement(ele).perform();
	}

	/**
	 * This method will perform double click on given element
	 * 
	 * @param driver
	 * @param ele
	 */
	public void doubleClickAction(WebDriver driver, WebElement ele) {
		Actions act = new Actions(driver);
		act.doubleClick(ele).perform();
	}

	/**
	 * This method will perform right click on given element
	 * 
	 * @param driver
	 * @param ele
	 */
	public void rightClickAction(WebDriver driver, WebElement ele) {
		Actions act = new Actions(driver);
		act.contextClick(ele).perform();
	}

	/**
	 * This method will perform drag and drop
	 * 
	 * @param driver
	 * @param ele
	 */
	public void dragAndDropAction(WebDriver driver, WebElement source, WebElement dest) {
		Actions act = new Actions(driver);
		act.dragAndDrop(source, dest).perform();
	}

	/**
	 * This method will scroll down to the given element
	 * 
	 * @param driver
	 * @param ele
	 */
	public void scrollDownToElement(WebDriver driver, WebElement ele) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		int y = ele.getLocation().getY();
		jse.executeScript("window.scollBy(0, " + y + ")", ele);
	}

	/**
	 * This method will capture screenshot in case of failure
	 * 
	 * @param driver
	 * @param screenshotName
	 * @return
	 * @throws IOException
	 */
	public String takeScreenshot(WebDriver driver, String screenshotName) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		File dest = new File(IConstantsUtility.ScreenshotFolderPath + "\\" + screenshotName + ".png");
		FileUtils.copyFile(src, dest);

		return dest.getAbsolutePath();
	}
}
