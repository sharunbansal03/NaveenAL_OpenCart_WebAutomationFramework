package MyAccount.Tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import ObjectRepository.AccountPage;

import genericUtilities.BaseClass_new;

public class LoginTest extends BaseClass_new{
	
	@Test
	public void loginTest() throws IOException {
		
		AccountPage ap= new AccountPage(driver);
		Assert.fail();
		Assert.assertTrue(ap.getMyAccount_Header().isDisplayed(),"[ASSERTION FAILED]: LOGIN FAILED");
		Reporter.log("[ASSERTION PASSED]: LOGIN SUCCESSFUL", true);
	}

}
