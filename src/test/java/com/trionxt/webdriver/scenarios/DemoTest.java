package com.trionxt.webdriver.scenarios;

import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.trionxt.constants.AppConstants;
import com.trionxt.util.CommonsUtil;
import com.trionxt.util.EventsUtil;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class DemoTest extends CommonsUtil {

	public DemoTest() {
		this.getAccessTicket();
	}

	String accessToken;

	@BeforeSuite
	public void setup() {
		System.setProperty("webdriver.chrome.driver", "H:\\Supermon Data\\mySuperMon_Automation\\chromedriver\\chromedriver.exe");
		System.out.print("Inside setUp....... ");
		String browserName = readApplicationProperties("browserForTestRun");
		initializeDriver(browserName);
		driver.manage().window().maximize();
		loadApplicationByURL();

	}
	

	@Test(priority = 1)
	public void home() throws InterruptedException {
		waitForUserInterfaceToLoad(AppConstants.DEFAULT_WAIT_TIME);

//			driver.navigate().refresh();
		EventsUtil.clickWebElementr(By.xpath(readLocatorsProperties("Home_Page")));
		waitForUserInterfaceToLoad(AppConstants.DEFAULT_WAIT_TIME);
	}

	@Test(priority = 2)
	public void blog() throws InterruptedException {
		waitForUserInterfaceToLoad(AppConstants.DEFAULT_WAIT_TIME);

		EventsUtil.clickWebElementr(By.xpath(readLocatorsProperties("Shop_Page")));
		waitForUserInterfaceToLoad(AppConstants.DEFAULT_WAIT_TIME);
	}

	@Test(priority = 3)
	public void contact() throws InterruptedException {
		waitForUserInterfaceToLoad(AppConstants.DEFAULT_WAIT_TIME);

		EventsUtil.clickWebElementr(By.xpath(readLocatorsProperties("About_Page")));
		waitForUserInterfaceToLoad(AppConstants.DEFAULT_WAIT_TIME);

	}

	@Test(priority = 4)
	public void login2() throws InterruptedException {
		waitForUserInterfaceToLoad(AppConstants.DEFAULT_WAIT_TIME);

		EventsUtil.clickWebElementr(By.xpath(readLocatorsProperties("Contact_Page")));
		waitForUserInterfaceToLoad(AppConstants.DEFAULT_WAIT_TIME);
	}

	@Test(priority = 5)
	public void login3() throws InterruptedException {
		waitForUserInterfaceToLoad(AppConstants.DEFAULT_WAIT_TIME);

		EventsUtil.clickWebElementr(By.xpath(readLocatorsProperties("Shop_Page")));
		EventsUtil.clickWebElementr(By.xpath(readLocatorsProperties("product")));

		waitForUserInterfaceToLoad(AppConstants.DEFAULT_WAIT_TIME);
	}

	@AfterSuite
	public void close() throws InterruptedException {
		Response report = RestAssured.given().header("Authorization", "Bearer " + accessToken)
				.header("applicationIdentifier", readApplicationProperties("applicationIdentifier"))
				.get(readApplicationProperties("Supermon_url") + "devaten/data/generateReport");

//			System.out.println(report.getBody().asString());
		try (FileWriter file = new FileWriter("report/response.json")) {
			// File Writer creates a file in write mode at the given location
			file.write(report.getBody().asString());
			// write function is use to write in file,
			// here we write the Json object in the file
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread.sleep(2000);
		// waitForUserInterfaceToLoad(Constant.DEFAULT_WAIT_TIME);
		driver.close();
	}

	@Parameters({ "TestName" })
	@BeforeTest
	public void callStartRecording(String testName) throws InterruptedException {
		Response res = RestAssured.given().header("Authorization", "Bearer " + accessToken)
				.header("applicationIdentifier", readApplicationProperties("applicationIdentifier"))
				.get(readApplicationProperties("Supermon_url") + "devaten/data/startRecording?usecaseIdentifier="
						+ testName);
//			System.out.println(res.getStatusCode());
		Thread.sleep(2500);
	}

	@Parameters({ "TestName" })
	@AfterTest
	public void callStopRecording(String testName) throws InterruptedException {
		Response res = RestAssured.given().header("Authorization", "Bearer " + accessToken)
				.header("applicationIdentifier", readApplicationProperties("applicationIdentifier"))
				.get(readApplicationProperties("Supermon_url") + "devaten/data/stopRecording?usecaseIdentifier="
						+ testName + "&inputSource=batFile");

//			System.out.println(res.getStatusCode());
		Thread.sleep(2500);
	}

	public void getAccessTicket() {
		System.out.println("call oauth");
		Response response = RestAssured.given().auth().preemptive()
				.basic("performanceDashboardClientId", "ljknsqy9tp6123")
				.contentType("application/x-www-form-urlencoded").log().all().formParam("grant_type", "password")
				.formParam("username", readApplicationProperties("username"))
				.formParam("password", readApplicationProperties("password")).when()
				.post(readApplicationProperties("Supermon_url") + "oauth/token");

		JSONObject jsonObject = new JSONObject(response.getBody().asString());
		accessToken = jsonObject.get("access_token").toString();
		System.out.println(accessToken);
	}
}
