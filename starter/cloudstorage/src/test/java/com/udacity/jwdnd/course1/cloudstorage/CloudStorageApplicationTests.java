package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {


	@LocalServerPort
	private int port;

	private WebDriver driver;

	private WebDriverWait wait;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
		assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 * If this test is failing, please ensure that you are handling redirecting users
	 * back to the login page after a successful sign up.
	 * Read more about the requirement in the rubric:
	 * https://review.udacity.com/#!/rubrics/2724/view
	 */
	@Test
	public void testRedirection() {
		driver.get("http://localhost:" + port + "/signup");

		// Fill in signup form
		driver.findElement(By.id("inputFirstName")).sendKeys("Johnny");
		driver.findElement(By.id("inputLastName")).sendKeys("Doen");
		driver.findElement(By.id("inputUsername")).sendKeys("johndoen");
		driver.findElement(By.id("inputPassword")).sendKeys("password2");
		driver.findElement(By.id("buttonSignUp")).click();

		// Wait for redirection to login page
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.urlToBe("http://localhost:" + port + "/login"));
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 * If this test is failing, please ensure that you are handling bad URLs
	 * gracefully, for example with a custom error page.
	 * Read more about custom error pages at:
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		driver.get("http://localhost:" + port + "/signup");

		// Fill in signup form
		driver.findElement(By.id("inputFirstName")).sendKeys("Petra");
		driver.findElement(By.id("inputLastName")).sendKeys("Doe");
		driver.findElement(By.id("inputUsername")).sendKeys("petradoe");
		driver.findElement(By.id("inputPassword")).sendKeys("password3");
		driver.findElement(By.id("buttonSignUp")).click();
		doLogIn("petradoe", "password3");

		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		assertTrue(driver.getPageSource().contains("404"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code.
	 * Read more about file size limits here:
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() throws IOException {

		driver.get("http://localhost:" + port + "/signup");

		// Find and interact with form elements
		WebElement firstNameInput = driver.findElement(By.id("inputFirstName"));
		WebElement lastNameInput = driver.findElement(By.id("inputLastName"));
		WebElement usernameInput = driver.findElement(By.id("inputUsername"));
		WebElement passwordInput = driver.findElement(By.id("inputPassword"));
		WebElement signUpButton = driver.findElement(By.id("buttonSignUp"));

		// Fill in the form and submit
		firstNameInput.sendKeys("Petra");
		lastNameInput.sendKeys("Doe");
		usernameInput.sendKeys("petradoe");
		passwordInput.sendKeys("password3");
		signUpButton.click();
		doLogIn("petradoe", "password3");

		//Creating a 5 MB file
		String fileName = "largeUpload5MB.txt";
		createLargeFile(fileName, 5);

		//Trying to upload the 5 MB file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 10);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));

		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
		driver.quit();
	}

	@Test
	public void testUnauthenticatedAccess() {
		//Testing the login page
		driver.get("http://localhost:" + this.port + "/login");
		assertEquals("Login", driver.getTitle());

		//Testing the signup page
		driver.get("http://localhost:" + this.port + "/signup");
		assertEquals("Sign Up", driver.getTitle());

		//Attempting to access other protected pages directly
		driver.get("http://localhost:" + this.port + "/home");
		assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/files");
		assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/notes");
		assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/credentials");
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testSignupLoginLogout() {
		//Signing up a new user
		driver.get("http://localhost:" + this.port + "/signup");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		//Filling out sign-up form
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));

		inputFirstName.sendKeys("Hein");
		inputLastName.sendKeys("Doe");
		inputUsername.sendKeys("Hein");
		inputPassword.sendKeys("Doen");
		buttonSignUp.click();

		//Logging in with the created user
		driver.get("http://localhost:" + this.port + "/login");
		webDriverWait.until(ExpectedConditions.titleContains("Login"));

		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		WebElement loginButton = driver.findElement(By.id("login-button"));

		loginUserName.sendKeys("Hein");
		loginPassword.sendKeys("Doen");
		loginButton.click();

		//Verifying that the home page is accessible after logging in
		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		assertEquals("Home", driver.getTitle());

		//Logging out
		WebElement logoutButton = driver.findElement(By.id("logout-button"));
		logoutButton.click();

		//Verifying that the home page is no longer accessible after logging out
		driver.get("http://localhost:" + this.port + "/home");
		assertEquals("Login", driver.getTitle());
	}

	private void createLargeFile(String fileName, long fileSizeInMB) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(fileName)) {
			for (int i = 0; i < fileSizeInMB * 1024 * 1024; i++) {
				fos.write(0); // Write a byte to fill up the file
			}
		}
	}
}
