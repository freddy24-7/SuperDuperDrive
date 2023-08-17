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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CredentialsTests {

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
    public void testCredentialsCreationAndDisplay() throws InterruptedException {
        //Creating a test account
        driver.get("http://localhost:" + port + "/signup");

        //Filling in signup form
        driver.findElement(By.id("inputFirstName")).sendKeys("Koen");
        driver.findElement(By.id("inputLastName")).sendKeys("Hein");
        driver.findElement(By.id("inputUsername")).sendKeys("koen");
        driver.findElement(By.id("inputPassword")).sendKeys("password10");
        driver.findElement(By.id("buttonSignUp")).click();
        doLogIn("koen", "password10");

        WebElement CredentialsButton = driver.findElement(By.id("nav-credentials-tab"));
        CredentialsButton.click();

        //Waiting for the "Add New Credentials" button to become clickable
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement addCredentialsButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("new-credentials")));

        //Clicking the button
        addCredentialsButton.click();

        //Waiting for the modal to become visible
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));

        //Locating and interacting with the form fields
        WebElement urlInput = modal.findElement(By.id("credentials-url"));
        WebElement usernameInput = modal.findElement(By.id("credentials-username"));
        WebElement passwordInput = modal.findElement(By.id("credentials-password"));
        WebElement submitButton = modal.findElement(By.id("SubmitCredential"));

        //Filling in the form fields
        urlInput.sendKeys("http://example.com");
        usernameInput.sendKeys("testuser");
        passwordInput.sendKeys("testpassword");

        //Clicking the submit button
        submitButton.click();

        //Clicking the success button
        WebElement successButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("continue")));
        successButton.click();

        //Clicking credential button
        WebElement reClickCredButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        reClickCredButton.click();

        //Verifying that the new credentials are displayed in the table
        WebElement urlCell = driver.findElement(By.id("idURL"));
        WebElement usernameCell = driver.findElement(By.id("idusername"));
        WebElement passwordCell = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("idpassword")));
        String passwordText = passwordCell.getText();

        assertEquals("http://example.com", urlCell.getText());
        assertEquals("testuser", usernameCell.getText());

        //Verifying working encryption
        assertNotEquals("testpassword", passwordText);

    }

    @Test
    public void testCredentialsEditingAndDecryption() throws InterruptedException {
        //Creating a test account
        driver.get("http://localhost:" + port + "/signup");

        //Filling in signup form
        driver.findElement(By.id("inputFirstName")).sendKeys("Koen");
        driver.findElement(By.id("inputLastName")).sendKeys("Hein");
        driver.findElement(By.id("inputUsername")).sendKeys("koen");
        driver.findElement(By.id("inputPassword")).sendKeys("password10");
        driver.findElement(By.id("buttonSignUp")).click();
        doLogIn("koen", "password10");

        WebElement CredentialsButton = driver.findElement(By.id("nav-credentials-tab"));
        CredentialsButton.click();

        //Waiting for the "Add New Credentials" button to become clickable
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement addCredentialsButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("new-credentials")));

        //Clicking the button
        addCredentialsButton.click();

        //Waiting for the modal to become visible
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));

        //Locating and interacting with the form fields
        WebElement urlInput = modal.findElement(By.id("credentials-url"));
        WebElement usernameInput = modal.findElement(By.id("credentials-username"));
        WebElement passwordInput = modal.findElement(By.id("credentials-password"));
        WebElement submitButton = modal.findElement(By.id("SubmitCredential"));

        //Filling in the form fields
        urlInput.sendKeys("http://example.com");
        usernameInput.sendKeys("testuser");
        passwordInput.sendKeys("testpassword");

        //Clicking the submit button
        submitButton.click();

        //Clicking the success button
        WebElement successButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("continue")));
        successButton.click();

        //Clicking credential button
        WebElement reClickCredButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        reClickCredButton.click();

        //Verifying that the new credentials are displayed in the table
        WebElement urlCell = driver.findElement(By.id("idURL"));
        WebElement usernameCell = driver.findElement(By.id("idusername"));
        WebElement passwordCell = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("idpassword")));
        String passwordText = passwordCell.getText();

        assertEquals("http://example.com", urlCell.getText());
        assertEquals("testuser", usernameCell.getText());

        //Verifying working encryption
        assertNotEquals("testpassword", passwordText);
        System.out.println(passwordText);

        //Waiting for the edit button to become visible
        wait = new WebDriverWait(driver, 10);
        WebElement editButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-edit")));
        editButton.click();
        Thread.sleep(5000);
        WebElement editModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));

        //Locating and interacting with the form fields in the edit modal
        WebElement urlEditInput = editModal.findElement(By.id("credentials-url"));
        WebElement usernameEditInput = editModal.findElement(By.id("credentials-username"));
        WebElement passwordEditInput = editModal.findElement(By.id("credentials-password"));

        //Extracting the values from the form fields
        String editedUrl = urlEditInput.getAttribute("value");
        String editedUsername = usernameEditInput.getAttribute("value");
        String editedPassword = passwordEditInput.getAttribute("value");

        //Verifying working decryption
        assertEquals("testpassword", editedPassword);

        //Printing or using the extracted values
        System.out.println("Edited URL: " + editedUrl);
        System.out.println("Edited Username: " + editedUsername);
        System.out.println("Edited Password: " + editedPassword);

        passwordEditInput.sendKeys("testpassword1005");
        Thread.sleep(5000);

        //Clicking the submit button
        submitButton = editModal.findElement(By.id("SubmitCredential"));
        submitButton.click();
        //Clicking the success button
        successButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("continue")));
        successButton.click();
        //Clicking credential button
        reClickCredButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        reClickCredButton.click();
        //Waiting for the edit button to become visible
        wait = new WebDriverWait(driver, 10);
        editButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-edit")));
        editButton.click();
        Thread.sleep(5000);

        //Locating and interacting with the form fields in the edit modal
        editModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));
        passwordEditInput = editModal.findElement(By.id("credentials-password"));

        //Extracting the values from the form fields
        editedPassword = passwordEditInput.getAttribute("value");
        System.out.println("Edited Password: " + editedPassword);

        //Verifying that the new password is correctly displayed
        assertEquals("testpasswordtestpassword1005", editedPassword);

        driver.quit();
    }

    @Test
    public void testDeleteAndVerifyDeleteCredential() throws InterruptedException {
        //Creating a test account
        driver.get("http://localhost:" + port + "/signup");

        //Filling in signup form
        driver.findElement(By.id("inputFirstName")).sendKeys("Koen");
        driver.findElement(By.id("inputLastName")).sendKeys("Hein");
        driver.findElement(By.id("inputUsername")).sendKeys("koen");
        driver.findElement(By.id("inputPassword")).sendKeys("password10");
        driver.findElement(By.id("buttonSignUp")).click();
        doLogIn("koen", "password10");

        WebElement CredentialsButton = driver.findElement(By.id("nav-credentials-tab"));
        CredentialsButton.click();

        //Waiting for the "Add New Credentials" button to become clickable
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement addCredentialsButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("new-credentials")));

        //Clicking the button
        addCredentialsButton.click();

        //Waiting for the modal to become visible
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));

        //Locating and interacting with the form fields
        WebElement urlInput = modal.findElement(By.id("credentials-url"));
        WebElement usernameInput = modal.findElement(By.id("credentials-username"));
        WebElement passwordInput = modal.findElement(By.id("credentials-password"));
        WebElement submitButton = modal.findElement(By.id("SubmitCredential"));

        //Filling in the form fields
        urlInput.sendKeys("http://example.com");
        usernameInput.sendKeys("testuser");
        passwordInput.sendKeys("testpassword");

        //Clicking the submit button
        submitButton.click();

        //Clicking the success button
        WebElement successButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("continue")));
        successButton.click();

        //Clicking credential button
        WebElement reClickCredButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        reClickCredButton.click();

        //Verifying that the new credentials are displayed in the table
        WebElement urlCell = driver.findElement(By.id("idURL"));
        WebElement usernameCell = driver.findElement(By.id("idusername"));
        WebElement passwordCell = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("idpassword")));
        String passwordText = passwordCell.getText();

        assertEquals("http://example.com", urlCell.getText());
        assertEquals("testuser", usernameCell.getText());

        List<WebElement> credentialsList = driver.findElements(By.id("credentialTable"));
        int initialCount = credentialsList.size();

        //Waiting for the delete button to become visible
        wait = new WebDriverWait(driver, 10);
        WebElement deleteButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentials-delete")));
        deleteButton.click();

        successButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("continue")));
        successButton.click();

        wait = new WebDriverWait(driver, 10);
        //Clicking credential button
        reClickCredButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        reClickCredButton.click();
        Thread.sleep(5000);

        List<WebElement> updatedCredentialsList = driver.findElements(By.cssSelector("#credentialsTable tbody tr"));
        int finalCount = updatedCredentialsList.size();

        // Assertion
        assertEquals(initialCount - 1, finalCount);

    }

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
}
