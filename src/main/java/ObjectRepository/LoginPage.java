package ObjectRepository;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

public class LoginPage {
	// Step 1: Declare WebElements
	@FindBy(id = "input-email")
	private WebElement email_TxtField;

	@FindBy(id = "input-password")
	private WebElement password_TxtField;
	
	@FindBy(css = "input[value='Login']")
	private WebElement login_Btn;
	
	// Step 2: Initialize WebElements
	public LoginPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	// Step 3: Utilize WebElements
	public WebElement getEmail_TxtField() {
		return email_TxtField;
	}

	public WebElement getPassword_TxtField() {
		return password_TxtField;
	}

	public WebElement getLogin_Btn() {
		return login_Btn;
	}
	

	// Step 4: Business Logic or Generic Methods
	
	/**
	 * This method will login into app with given email and password
	 * 
	 * @param email
	 * @param pwd
	 */
	public void loginToApp(String email,String pwd) {
		email_TxtField.sendKeys(email);
		password_TxtField.sendKeys(pwd);
		login_Btn.click();
		Reporter.log("STEP: ENTERED LOGIN CREDENTIALS ", true);
	}

}
