package testcases;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import coreUtilities.utils.FileOperations;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import rest.ApiUtil;
import rest.CustomResponse;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@SuppressWarnings("unused")
public class RestAssured_TestCases {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(RestAssured_TestCases.class);

	private static String baseUrl;
	private static String username;
	private static String password;
	private static String cookieValue = null;
	private ApiUtil apiUtil;
	private int employeeStatus;
	private TestCodeValidator testCodeValidator;
	private String apiUtilPath = System.getProperty("user.dir") + "\\src\\main\\java\\rest\\ApiUtil.java";
	private String excelPath = System.getProperty("user.dir") + "\\src\\main\\resources\\TestData.xlsx";

	
	@Test(priority = 0, groups = { "PL2" }, description = "Login to the application using Selenium and retrieve the cookie.")
	public void loginWithSeleniumAndGetCookie() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

		apiUtil = new ApiUtil();
		baseUrl = apiUtil.getBaseUrl();
		username = apiUtil.getUsername();
		password = apiUtil.getPassword();

		driver.get(baseUrl + "/web/index.php/auth/login");
		Thread.sleep(3000); // Wait for page load

		// Login to the app
		driver.findElement(By.name("username")).sendKeys(username);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		Thread.sleep(9000); // Wait for login

		// Extract cookie named "orangehrm"
		Set<Cookie> cookies = driver.manage().getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("orangehrm")) {
				cookieValue = cookie.getValue();
				break;
			}
		}

		driver.quit();
		testCodeValidator = new TestCodeValidator();

		if (cookieValue == null) {
			throw new RuntimeException("orangehrm cookie not found after login");
		}
	}
	
	/**
 * Test Method: getLocalizationValid
 * 
 * Purpose:
 * This test validates the '/web/index.php/api/v2/admin/localization' API endpoint using a valid authentication cookie.
 * It ensures that the response structure is correctly parsed into a CustomResponse object and that essential fields
 * such as 'language' and 'dateFormat' are present and non-empty.
 * 
 * Steps:
 * 1. Define the API endpoint for localization.
 * 2. Send a GET request to the endpoint using the apiUtil.getLocalizationValid method, passing the valid cookie.
 * 3. Parse the response into a CustomResponse object.
 * 4. Use TestCodeValidator to confirm that the test method implementation utilizes required RestAssured keywords 
 *    (cookie, get, response) to enforce proper API testing standards.
 * 5. Validate that the CustomResponse contains all required fields as per validation logic.
 * 6. Print the response status code and body for debugging purposes.
 * 7. Retrieve the 'Languages' and 'dateFormat' lists from the CustomResponse.
 * 8. Assert that these lists are not empty, ensuring the API returned meaningful data.
 * 9. Iterate through the lists to assert that no individual element is null.
 * 10. Assert that the implementation of the test method is correct and uses RestAssured methods only.
 * 11. Assert that the response status code is 200 (OK).
 * 
 * Assertions:
 * - All required fields in CustomResponse are present and correctly parsed.
 * - 'Languages' and 'dateFormat' lists are non-empty and contain non-null values.
 * - Test method implementation adheres to RestAssured usage standards.
 * - Response status code is 200.
 * 
 * Notes:
 * - This test belongs to group "PL2" and has the highest priority (1).
 * - It relies on a valid cookieValue to authenticate the request.
 * - System.out.println statements are used to aid debugging and can be removed in production test runs.
 */


	@Test(priority = 1, groups = {
	"PL2" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/localization' endpoint using a valid authentication cookie\n"
				+ "2. Verify that the response is parsed into a CustomResponse object with language and dateFormat fields extracted from the 'data' section\n"
				+ "3. Assert that both 'language' and 'dateFormat' fields are not null and not empty\n"
				+ "4. Validate that the test method implementation uses required RestAssured keywords using the TestCodeValidator\n"
				+ "5. Assert that the response status code is 200 (OK) and all required fields are present as per validation logic")

	public void getLocalizationValid() throws IOException {

	    String endpoint = "/web/index.php/api/v2/admin/localization";
	    
	    // Send GET request
	    CustomResponse customResponse = apiUtil.getLocalizationValid(endpoint, cookieValue, null);

	    
	    // Validate customResponse structure
	    boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
	            "getLocalizationValid", List.of("cookie", "get", "response"));
	    System.out.println(isImplementationCorrect);
	    
	    // Validating customResponse Field
	    Assert.assertTrue(TestCodeValidator.validateResponseFields("getLocalizationValid", customResponse),
				"Must have all required fields in the customResponse.");
	    
	    System.out.println("Status Code: " + customResponse.getStatusCode());
	    System.out.println("customResponse Body: " + customResponse.getResponse().getBody().asString());

	    //Retrieve objects for Checking
		List<Object> Languages = customResponse.getLanguages();
		List<Object> dateFormat= customResponse.getDateFormat();
  
		
		System.out.println("Language :" + Languages + "\n" + " DateFormat : "+ dateFormat);
		
	    Assert.assertFalse(Languages.isEmpty(), "ItemId list should not be empty.");
		Assert.assertFalse(dateFormat.isEmpty(), "ItemName list should not be empty.");

	    // Checking all values of customResponses Data is not null
		for (int i = 0; i < Languages.size(); i++) {
			Assert.assertNotNull(Languages.get(i), "ItemId at index " + i + " should not be null.");
			Assert.assertNotNull(dateFormat.get(i), "ItemName at index " + i + " should not be null.");
		}
		// Checking For Implementation
	    Assert.assertTrue(isImplementationCorrect, "getLocalizationValid must be implemented using the Rest Assured methods only!");
	    assertEquals(customResponse.getStatusCode(), 200);
	    
	}


	/**
 * Test Method: getActiveLanguages
 * 
 * Purpose:
 * This test validates the '/web/index.php/api/v2/admin/i18n/languages' API endpoint for active languages
 * using a valid authentication cookie. It ensures that the response is parsed correctly into a CustomResponse object
 * and that essential fields ('id', 'name', 'code') are present and non-null.
 * 
 * Steps:
 * 1. Define the API endpoint for fetching active languages with query parameters for limit, offset, sort order, and active-only filter.
 * 2. Send a GET request to the endpoint using the apiUtil.getActiveLanguages method, passing the valid cookie.
 * 3. Parse the response into a CustomResponse object.
 * 4. Print the response status code, body, and extracted fields for debugging purposes.
 * 5. Retrieve the lists of language IDs, names, and codes from the CustomResponse.
 * 6. Assert that these lists are not empty to ensure the API returned valid data.
 * 7. Iterate through the lists to assert that no individual element is null.
 * 8. Use TestCodeValidator to confirm that the test method implementation utilizes required RestAssured keywords 
 *    (given, cookie, get, response) to enforce proper API testing standards.
 * 9. Validate that the CustomResponse contains all required fields as per validation logic.
 * 10. Assert that the implementation of the test method adheres to RestAssured usage standards.
 * 11. Assert that the response status code is 200 (OK).
 * 
 * Assertions:
 * - All required fields in CustomResponse are present and correctly parsed.
 * - 'languageId', 'languageName', and 'languageCode' lists are non-empty and contain non-null values.
 * - Test method implementation adheres to RestAssured usage standards.
 * - Response status code is 200.
 * 
 * Notes:
 * - This test belongs to group "PL2" and has a priority of 2.
 * - It relies on a valid cookieValue to authenticate the request.
 * - System.out.println statements are included for debugging and can be removed in production test runs.
 */

	
	@Test(priority = 2, groups = {
    "PL2" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/i18n/languages?limit=50&offset=0&sortOrder=ASC&activeOnly=true' endpoint using a valid cookie\n"
		        + "2. Parse the response using a CustomResponse object, extracting 'id', 'name', and 'code' fields from each entry in the 'data' array\n"
		        + "3. Print the customResponse status code, body, and extracted language fields for debugging\n"
		        + "4. Assert that the extracted lists of IDs, names, and codes are not empty and contain no null entries\n"
		        + "5. Validate that the method is implemented using required RestAssured calls using TestCodeValidator\n"
		        + "6. Assert that the response contains all expected fields and the status code is 200 (OK)")

	public void getActiveLanguages() throws IOException {
		// Endpoint 
		String endpoint = "/web/index.php/api/v2/admin/i18n/languages?limit=50&offset=0&sortOrder=ASC&activeOnly=true";
		
		// Send GET request
		CustomResponse customResponse = apiUtil.getActiveLanguages(endpoint,cookieValue, null);

		
		// Validate customResponse Structure
		System.out.println("Reading file from: " + apiUtilPath);

	    
	    System.out.println("Status Code: " + customResponse.getStatusCode());
	    System.out.println("customResponse Body: " + customResponse.getResponse().getBody().asString());

	    //Retrieve objects for Checking
		List<Object> languageId = customResponse.getLanguageId();
		List<Object> languageName= customResponse.getLanguageName();
		List<Object> languageCode= customResponse.getLanguageCode();
	
		
		System.out.println("Language Id :" + languageId + "\n" + "Language Name : "+ languageName + "\n" + "Language Code : " + languageCode );
		
	    Assert.assertFalse(languageId.isEmpty(), "ItemId list should not be empty.");
		Assert.assertFalse(languageName.isEmpty(), "ItemName list should not be empty.");
		Assert.assertFalse(languageCode.isEmpty(), "ItemName list should not be empty.");

	    // Checking all values of customResponses Data is not null
		for (int i = 0; i < languageId.size(); i++) {
			Assert.assertNotNull(languageId.get(i), "ItemId at index " + i + " should not be null.");
			Assert.assertNotNull(languageName.get(i), "ItemId at index " + i + " should not be null.");
			Assert.assertNotNull(languageCode.get(i), "ItemName at index " + i + " should not be null.");
		}

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
				"getActiveLanguages", List.of("given" , "cookie", "get", "response"));
	    System.out.println(isImplementationCorrect);
	    
	    // Validating customResponse Field
	    Assert.assertTrue(TestCodeValidator.validateResponseFields("getActiveLanguages", customResponse),
				"Must have all required fields in the customResponse.");
	    
	    // Checking For Implementation
		Assert.assertTrue(isImplementationCorrect, "getActiveLanguages must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);
	}

	/**
 * Test Method: getDashboardShortcuts
 * 
 * Purpose:
 * This test validates the '/web/index.php/api/v2/dashboard/shortcuts' API endpoint using a valid authentication cookie.
 * It ensures that the response is parsed into a CustomResponse object and that essential shortcut fields are present
 * and correctly populated.
 * 
 * Steps:
 * 1. Send a GET request to the dashboard shortcuts endpoint using apiUtil.getDashboardShortcuts with a valid cookie.
 * 2. Parse the response into a CustomResponse object, extracting shortcut lists such as:
 *    - leave.assign_leave
 *    - leave.leave_list
 *    - leave.apply_leave
 *    - leave.my_leave
 *    - time.employee_timesheet
 *    - time.my_timesheet
 * 3. Print the response status code, body, and extracted lists for debugging and verification.
 * 4. Assert that all extracted lists are not empty, ensuring the API returned meaningful shortcut data.
 * 5. Iterate through each list and assert that none of the elements are null.
 * 6. Use TestCodeValidator to confirm that the test method implementation uses required RestAssured keywords 
 *    (given, cookie, get, response).
 * 7. Validate that the CustomResponse contains all required fields according to the validation logic.
 * 8. Assert that the response status code is 200 (OK).
 * 
 * Assertions:
 * - All shortcut lists are non-empty and contain no null values.
 * - Test method implementation adheres to RestAssured usage standards.
 * - CustomResponse contains all required fields.
 * - Response status code is 200.
 * 
 * Notes:
 * - This test belongs to group "PL2" and has a priority of 3.
 * - System.out.println statements are used for debugging and can be removed in production test runs.
 * - Ensures that dashboard shortcuts are correctly exposed and usable via the API.
 */

	 @Test(priority = 3, groups = {
    "PL2" }, description = "1. Send a GET request to the '/web/index.php/api/v2/dashboard/shortcuts' endpoint with a valid cookie\n"
		        + "2. Parse the response using CustomResponse to extract relevant shortcut fields like leave.assign_leave, leave.leave_list, etc.\n"
		        + "3. Print the customResponse status code, response body, and extracted fields for verification\n"
		        + "4. Assert that all extracted lists are not empty and contain expected shortcut information\n"
		        + "5. Assert that the customResponse status code is 200 (OK)")

	public void getDashboardShortcuts() throws IOException {
		
		// Sending Endpoint
		CustomResponse customResponse = apiUtil.getDashboardShortcuts("/web/index.php/api/v2/dashboard/shortcuts", cookieValue, null);


		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("customResponse Body: " + customResponse.getResponse().getBody().asString());

		List<Object> leave_assign_leave = customResponse.leave_Assign_Level();
		List<Object> leave_leave_list = customResponse.leave_Leave_List();
		List<Object> leave_apply_leave = customResponse.leave_Apply_Leave();
		List<Object> leave_my_leave = customResponse.leave_My_Leave();
		List<Object> time_employee_timesheet = customResponse.time_Employee_Timesheet();
		List<Object> time_my_timesheet = customResponse.time_My_Timesheet();

		// Print and assert
		System.out.println("leave.assign_leave: " + leave_assign_leave);
		System.out.println("leave.leave_list: " + leave_leave_list);
		

		// Assert lists are not empty
		Assert.assertFalse(leave_assign_leave.isEmpty(), "leave_assign_leave list should not be empty.");
		Assert.assertFalse(leave_leave_list.isEmpty(), "leave_leave_list list should not be empty.");
		Assert.assertFalse(leave_apply_leave.isEmpty(), "leave_apply_leave list should not be empty.");
		Assert.assertFalse(leave_my_leave.isEmpty(), "leave_my_leave list should not be empty.");
		Assert.assertFalse(time_employee_timesheet.isEmpty(), "time_employee_timesheet list should not be empty.");
		Assert.assertFalse(time_my_timesheet.isEmpty(), "time_my_timesheet list should not be empty.");

		// Assert none of the values are null
		for (int i = 0; i < leave_assign_leave.size(); i++) {
		    Assert.assertNotNull(leave_assign_leave.get(i), "'leave_assign_leave' at index " + i + " should not be null.");
		}
		for (int i = 0; i < leave_leave_list.size(); i++) {
		    Assert.assertNotNull(leave_leave_list.get(i), "'leave_leave_list' at index " + i + " should not be null.");
		}
		for (int i = 0; i < leave_apply_leave.size(); i++) {
		    Assert.assertNotNull(leave_apply_leave.get(i), "'leave_apply_leave' at index " + i + " should not be null.");
		}
		for (int i = 0; i < leave_my_leave.size(); i++) {
		    Assert.assertNotNull(leave_my_leave.get(i), "'leave_my_leave' at index " + i + " should not be null.");
		}
		for (int i = 0; i < time_employee_timesheet.size(); i++) {
		    Assert.assertNotNull(time_employee_timesheet.get(i), "'time_employee_timesheet' at index " + i + " should not be null.");
		}
		for (int i = 0; i < time_my_timesheet.size(); i++) {
		    Assert.assertNotNull(time_my_timesheet.get(i), "'time_my_timesheet' at index " + i + " should not be null.");
		}
		
		
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
				"getDashboardShortcuts", List.of("given" , "cookie", "get", "response"));
	    System.out.println(isImplementationCorrect);
	    
	    // Validating customResponse Field
	    Assert.assertTrue(TestCodeValidator.validateResponseFields("getDashboardShortcuts", customResponse),
				"Must have all required fields in the customResponse.");
	    
	    
		Assert.assertTrue(isImplementationCorrect, "getDashboardShortcuts must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);
	}
	
	/**
 * Test Method: getAuthProviders
 * 
 * Purpose:
 * This test validates the '/web/index.php/api/v2/auth/openid-providers' API endpoint using a valid authentication cookie.
 * It ensures that the response is parsed into a CustomResponse object and that all essential OpenID provider fields
 * are present and correctly populated.
 * 
 * Steps:
 * 1. Create a new OpenID provider using a POST request (via createAuthProviderTest) to ensure that data exists for validation.
 * 2. Send a GET request to fetch OpenID providers using apiUtil.getAuthProviders with the valid cookie.
 * 3. Parse the response into a CustomResponse object, extracting lists of:
 *    - provider IDs (id)
 *    - provider names (providerName)
 *    - provider URLs (providerUrl)
 *    - provider statuses (status)
 *    - client IDs (clientId)
 * 4. Print the response status code, body, and all extracted lists for verification and debugging.
 * 5. Assert that none of the extracted lists are empty, ensuring that the API returned valid data.
 * 6. Iterate through each list and assert that none of the individual elements are null.
 * 7. Use TestCodeValidator to confirm that the test method implementation uses required RestAssured keywords
 *    (given, cookie, get, response).
 * 8. Validate that the CustomResponse contains all required fields as per the validation logic.
 * 9. Assert that the response status code is 200 (OK).
 * 
 * Assertions:
 * - All extracted OpenID provider lists are non-empty and contain no null values.
 * - Test method implementation adheres to RestAssured usage standards.
 * - CustomResponse contains all required fields.
 * - Response status code is 200.
 * 
 * Notes:
 * - This test belongs to group "PL2" and has a priority of 4.
 * - System.out.println statements are used for debugging and can be removed in production test runs.
 * - Ensures that OpenID providers are correctly exposed and usable via the API.
 */

	
	
	@Test(priority = 4, groups = {
    "PL2" }, description = "1. Create a new OpenID provider using POST to ensure data exists for validation\n"
		        + "2. Send a GET request to the '/web/index.php/api/v2/auth/openid-providers?limit=50&offset=0' endpoint with a valid cookie\n"
		        + "3. Parse the response using CustomResponse to extract fields like id, providerName, providerUrl, status, and clientId\n"
		        + "4. Print the status code, response body, and all extracted fields for verification\n"
		        + "5. Assert that none of the extracted lists are empty or contain null values\n"
		        + "6. Assert that the customResponse status code is 200 (OK)")
	public void getAuthProviders() throws IOException {
		
		
		// Creating Post Response for Tackle Failure of GET Response returning Nothing
		createAuthProviderTest();
		
		//GET API Request	
		CustomResponse customResponse = apiUtil.getAuthProviders(
				"/web/index.php/api/v2/auth/openid-providers?limit=50&offset=0", cookieValue, null);
	    
	    System.out.println("Status Code: " + customResponse.getStatusCode());
	    System.out.println("customResponse Body: " + customResponse.getResponse().getBody().asString());

	    // Retrieve objects for Checking
	    List<Object> providerIds = customResponse.getProviderIds();         // from "id"
	    List<Object> providerNames = customResponse.getProviderNames();     // from "providerName"
	    List<Object> providerUrls = customResponse.getProviderUrls();       // from "providerUrl"
	    List<Object> providerStatuses = customResponse.getProviderStatuses(); // from "status"
	    List<Object> clientIds = customResponse.getClientIds();             // from "clientId"

	    System.out.println("Provider Ids       : " + providerIds);
	    System.out.println("Provider Names     : " + providerNames);
	    System.out.println("Provider URLs      : " + providerUrls);
	    System.out.println("Provider Statuses  : " + providerStatuses);
	    System.out.println("Client IDs         : " + clientIds);

	    // Assert none of the lists are empty
	    Assert.assertFalse(providerIds.isEmpty(), "Provider ID list should not be empty.");
	    Assert.assertFalse(providerNames.isEmpty(), "Provider Name list should not be empty.");
	    Assert.assertFalse(providerUrls.isEmpty(), "Provider URL list should not be empty.");
	    Assert.assertFalse(providerStatuses.isEmpty(), "Provider Status list should not be empty.");
	    Assert.assertFalse(clientIds.isEmpty(), "Client ID list should not be empty.");

	    // Assert none of the individual values are null
	    for (int i = 0; i < providerIds.size(); i++) {
	        Assert.assertNotNull(providerIds.get(i), "Provider ID at index " + i + " should not be null.");
	        Assert.assertNotNull(providerNames.get(i), "Provider Name at index " + i + " should not be null.");
	        Assert.assertNotNull(providerUrls.get(i), "Provider URL at index " + i + " should not be null.");
	        Assert.assertNotNull(providerStatuses.get(i), "Provider Status at index " + i + " should not be null.");
	        Assert.assertNotNull(clientIds.get(i), "Client ID at index " + i + " should not be null.");
	    }

	    // Validate implementation correctness
	    boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
	            apiUtilPath, "getAuthProviders", List.of("given", "cookie", "get", "response"));
	    System.out.println(isImplementationCorrect);
	    
	    // Validating customResponse Field
	    Assert.assertTrue(TestCodeValidator.validateResponseFields("getAuthProviders", customResponse),
				"Must have all required fields in the customResponse.");
	    
	    // Checking For Implementation
		Assert.assertTrue(isImplementationCorrect, "getAuthProviders must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);
	};
	
	/**
 * Test Method: getAdminOAuthList
 * 
 * Purpose:
 * This test validates the '/web/index.php/api/v2/admin/oauth-clients' API endpoint using a valid authentication cookie.
 * It ensures that the response is correctly parsed into a CustomResponse object and that all essential OAuth client
 * fields are present and populated.
 * 
 * Steps:
 * 1. Send a GET request to the admin OAuth clients endpoint using apiUtil.getAdminOAuthList with a valid cookie.
 * 2. Parse the response into a CustomResponse object, extracting lists of:
 *    - ids
 *    - names
 *    - admin IDs
 *    - redirect URIs
 *    - enabled flags
 *    - confidential flags
 * 3. Print the response status code, body, and extracted fields for verification and debugging.
 * 4. Assert that none of the extracted lists are empty, ensuring that the API returned valid data.
 * 5. Iterate through each list and assert that none of the individual elements are null.
 * 6. Use TestCodeValidator to confirm that the test method implementation uses required RestAssured keywords
 *    (given, cookie, get, response).
 * 7. Validate that the CustomResponse contains all required fields as per the validation logic.
 * 8. Assert that the response status code is 200 (OK).
 * 
 * Assertions:
 * - All extracted OAuth client lists are non-empty and contain no null values.
 * - Test method implementation adheres to RestAssured usage standards.
 * - CustomResponse contains all required fields.
 * - Response status code is 200.
 * 
 * Notes:
 * - This test belongs to group "PL2" and has a priority of 5.
 * - System.out.println statements are used for debugging and can be removed in production test runs.
 * - Ensures that OAuth client data is correctly exposed via the API.
 */

	
	
	@Test(priority = 5, groups = {
    "PL2" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/oauth-clients?limit=50&offset=0' endpoint with a valid cookie\n"
		        + "2. Parse the response using CustomResponse to extract id, name, adminId, redirectUri, enabled, and confidential fields\n"
		        + "3. Print the status code, response body, and extracted fields\n"
		        + "4. Assert that all extracted lists are non-empty and contain non-null values\n"
		        + "5. Assert that the response status code is 200 (OK)")
	public void getAdminOAuthList() throws IOException {

		CustomResponse customResponse = apiUtil.getAdminOAuthList("/web/index.php/api/v2/admin/oauth-clients?limit=50&offset=0", cookieValue,
				null);

		System.out.println("Status Code: " + customResponse.getStatusCode());
	    System.out.println("customResponse Body: " + customResponse.getResponse().getBody().asString());

	    // Retrieve objects for Checking
		List<Object> ids = customResponse.getIds();
		List<Object> names = customResponse.getNames();
		List<Object> clientAdminIds = customResponse.getAdminIds();
		List<Object> redirectUrls = customResponse.getRedirectUris();
		List<Object> enabledFlags = customResponse.getEnabledFlags();
		List<Object> confidentialFlags = customResponse.getConfidentialFlags();

		Assert.assertFalse(ids.isEmpty(), "IDs should not be empty.");
		Assert.assertFalse(names.isEmpty(), "Names should not be empty.");
		Assert.assertFalse(clientAdminIds.isEmpty(), "Client IDs should not be empty.");
		Assert.assertFalse(redirectUrls.isEmpty(), "Redirect URIs should not be empty.");
		Assert.assertFalse(enabledFlags.isEmpty(), "Enabled flags should not be empty.");
		Assert.assertFalse(confidentialFlags.isEmpty(), "Confidential flags should not be empty.");

		for (int i = 0; i < ids.size(); i++) {
		    Assert.assertNotNull(ids.get(i), "ID at index " + i + " should not be null.");
		    Assert.assertNotNull(names.get(i), "Name at index " + i + " should not be null.");
		    Assert.assertNotNull(clientAdminIds.get(i), "Client ID at index " + i + " should not be null.");
		    Assert.assertNotNull(redirectUrls.get(i), "Redirect URL at index " + i + " should not be null.");
		    Assert.assertNotNull(enabledFlags.get(i), "Enabled flag at index " + i + " should not be null.");
		    Assert.assertNotNull(confidentialFlags.get(i), "Confidential flag at index " + i + " should not be null.");
		}

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
		        apiUtilPath, "getAdminOAuthList", List.of("given", "cookie", "get", "response"));
		System.out.println(isImplementationCorrect);

	    // Checking For Implementation
		Assert.assertTrue(TestCodeValidator.validateResponseFields("getAdminOAuthList", customResponse),
		        "Must have all required fields in the customResponse.");
		
		// Checking For Implementation
		Assert.assertTrue(isImplementationCorrect, "getAdminOAuthList must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);
	}

	/**
 * Test Method: getLDAPConfig
 * 
 * Purpose:
 * This test validates the '/web/index.php/api/v2/admin/ldap-config' API endpoint using a valid authentication cookie.
 * It ensures that the response is correctly parsed into a CustomResponse object and that all essential LDAP configuration
 * fields are present and valid.
 * 
 * Steps:
 * 1. Send a GET request to the LDAP configuration endpoint using apiUtil.getLDAPConfig with a valid cookie.
 * 2. Parse the response into a CustomResponse object, extracting the following fields:
 *    - Hostname
 *    - Port
 *    - Encryption
 *    - LDAP Implementation
 * 3. Use TestCodeValidator to ensure that the test method implementation uses required RestAssured keywords 
 *    (given, cookie, get, response).
 * 4. Print the response status code and key fields for debugging and verification.
 * 5. Assert that the response status code is 200 (OK).
 * 6. Assert that extracted fields are not null and valid:
 *    - Hostname is not null.
 *    - Port is greater than 0.
 *    - Encryption is not null.
 *    - LDAP Implementation is not null.
 * 
 * Assertions:
 * - Response status code is 200.
 * - Hostname, Encryption, and LDAP Implementation fields are non-null.
 * - Port is a valid integer greater than 0.
 * - Test method implementation adheres to RestAssured usage standards.
 * 
 * Notes:
 * - This test belongs to group "PL2" and has a priority of 6.
 * - System.out.println statements are used for debugging and can be removed in production test runs.
 * - Ensures that LDAP configuration is correctly exposed via the API.
 */

	
	@Test(priority = 6, groups = { "PL2" },
    description = "1. Send a GET request to the '/web/index.php/api/v2/admin/ldap-config' endpoint with a valid cookie\n"
		        + "2. Parse the response using CustomResponse to extract Hostname, Port, Encryption, and LDAP Implementation fields\n"
		        + "3. Print the status code, response body, and extracted fields\n"
		        + "4. Assert that all extracted lists are non-empty and contain non-null values\n"
		        + "5. Assert that the response status code is 200 (OK)")
public void getLDAPConfig() throws IOException {

    CustomResponse customResponse = apiUtil.getLDAPConfig(
        "/web/index.php/api/v2/admin/ldap-config", cookieValue, null
    );

    boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
        apiUtilPath, "getLDAPConfig", List.of("given", "cookie", "get", "response")
    );
    Assert.assertTrue(isImplementationCorrect, "Implementation is incorrect");

    // Print for debug
    System.out.println("Status Code: " + customResponse.getStatusCode());
    System.out.println("Hostname: " + customResponse.getHostname());

    // Assertions
    Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code mismatch");
    Assert.assertNotNull(customResponse.getHostname(), "Hostname should not be null");
    Assert.assertTrue(customResponse.getPort() > 0, "Port should be Integer greater than 0");
    Assert.assertNotNull(customResponse.getEncryption(), "Encryption should not be null");
    Assert.assertNotNull(customResponse.getLdapImplementation(), "LDAP Implementation should not be null");
}

/**
 * Test Method: getOptionalField
 * 
 * Purpose:
 * This test validates the '/web/index.php/api/v2/pim/optional-field' API endpoint using a valid authentication cookie.
 * It ensures that the response is correctly parsed into a CustomResponse object and that all optional PIM fields
 * are present and valid boolean values.
 * 
 * Steps:
 * 1. Send a GET request to the optional-field endpoint using apiUtil.getOptionalField with a valid cookie.
 * 2. Parse the response into a CustomResponse object, extracting the following fields:
 *    - pimShowDeprecatedFields
 *    - showSIN
 *    - showSSN
 *    - showTaxExemptions
 * 3. Use TestCodeValidator to ensure that the test method implementation uses required RestAssured keywords 
 *    (given, cookie, get, response).
 * 4. Assert that the response status code is 200 (OK).
 * 5. Assert that each extracted field is non-null and is a Boolean type.
 * 
 * Assertions:
 * - Response status code is 200.
 * - pimShowDeprecatedFields, showSIN, showSSN, and showTaxExemptions are non-null and Boolean.
 * - Test method implementation adheres to RestAssured usage standards.
 * 
 * Notes:
 * - This test belongs to group "PL2" and has a priority of 7.
 * - Ensures that optional PIM fields are correctly exposed via the API.
 * - System.out.println statements can be added for debugging but are optional.
 */


	@Test(priority = 7, groups = { "PL2" },
    description = "1. 1. Send a GET request to the '/web/index.php/api/v2/pim/optional-field' endpoint with a valid cookie\n"
		        + "2. Parse the response using CustomResponse to extract PimShowDeprecatedFields, PimShowSIN, PimShowSSN, and PimShowTaxExemptions fields\n"
		        + "3. Print the status code, response body, and extracted fields\n"
		        + "4. Assert that all extracted lists are non-empty and contain non-null values\n"
		        + "5. Assert that the response status code is 200 (OK)")
public void getOptionalField() throws IOException {

    CustomResponse customResponse = apiUtil.getOptionalField(
        "/web/index.php/api/v2/pim/optional-field", cookieValue, null
    );

    boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
        apiUtilPath, "getOptionalField", List.of("given", "cookie", "get", "response")
    );
    Assert.assertTrue(isImplementationCorrect, "Implementation is incorrect");

    // Status code assertion
    Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code mismatch");

    // Boolean field assertions
    Assert.assertNotNull(customResponse.getPimShowDeprecatedFields(), "pimShowDeprecatedFields should not be null");
    Assert.assertTrue(customResponse.getPimShowDeprecatedFields() instanceof Boolean, "pimShowDeprecatedFields should be a boolean");

    Assert.assertNotNull(customResponse.getShowSIN(), "showSIN should not be null");
    Assert.assertTrue(customResponse.getShowSIN() instanceof Boolean, "showSIN should be a boolean");

    Assert.assertNotNull(customResponse.getShowSSN(), "showSSN should not be null");
    Assert.assertTrue(customResponse.getShowSSN() instanceof Boolean, "showSSN should be a boolean");

    Assert.assertNotNull(customResponse.getShowTaxExemptions(), "showTaxExemptions should not be null");
    Assert.assertTrue(customResponse.getShowTaxExemptions() instanceof Boolean, "showTaxExemptions should be a boolean");
}
	

/**
 * Test Method: getCustomFields
 * 
 * Purpose:
 * This test validates the '/web/index.php/api/v2/pim/custom-fields' API endpoint using a valid authentication cookie.
 * It ensures that the response is correctly parsed into a CustomResponse object and that the list of custom fields
 * is present and valid.
 * 
 * Steps:
 * 1. Send a GET request to the custom-fields endpoint using apiUtil.getCustomFields with a valid cookie.
 * 2. Parse the response into a CustomResponse object, extracting the 'CustomFieldsList'.
 * 3. Use TestCodeValidator to ensure that the test method implementation uses required RestAssured keywords 
 *    (given, cookie, get, response).
 * 4. Print the response status line and status code for debugging and verification.
 * 5. Assert that the response status code is 200 (OK).
 * 6. Assert that the extracted 'CustomFieldsList' is non-null and of type List.
 * 
 * Assertions:
 * - Response status code is 200.
 * - CustomFieldsList is non-null and a List instance.
 * - Test method implementation adheres to RestAssured usage standards.
 * 
 * Notes:
 * - This test belongs to group "PL2" and has a priority of 8.
 * - System.out.println statements are used for debugging and can be removed in production test runs.
 * - Ensures that custom PIM fields are correctly exposed via the API.
 */


@Test(priority = 8, groups = { "PL2" },
    description = "1. Send a GET request to the '/web/index.php/api/v2/pim/custom-fields?limit=50&offset=0' endpoint with a valid cookie\n"
		        + "2. Parse the response using CustomResponse to extract CustomFieldsList\n"
		        + "3. Print the status code, response body, and extracted fields\n"
		        + "4. Assert that all extracted lists are non-empty and contain non-null values\n"
		        + "5. Assert that the response status code is 200 (OK)")
public void getCustomFields() throws IOException {
    CustomResponse customResponse = apiUtil.getCustomFields(
        "/web/index.php/api/v2/pim/custom-fields?limit=50&offset=0", cookieValue, null
    );

    boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
        apiUtilPath, "getCustomFields", List.of("given", "cookie", "get", "response")
    );
    Assert.assertTrue(isImplementationCorrect, "Implementation is incorrect");

    // Status code assertion
    Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code mismatch");
	System.out.println(customResponse.getStatusLine());
	System.out.println(customResponse.getStatusCode());

    // Assert the custom fields list is not null
    Assert.assertNotNull(customResponse.getCustomFieldsList(), "Custom fields list should not be null");
    Assert.assertTrue(customResponse.getCustomFieldsList() instanceof List, "Custom fields list should be a List");
}

/**
 * Test Method: getReportingMethods
 * 
 * Purpose:
 * This test validates the '/web/index.php/api/v2/pim/reporting-methods' API endpoint using a valid authentication cookie.
 * It ensures that the response is correctly parsed into a CustomResponse object and that the lists of reporting method
 * IDs and Names are present and valid.
 * 
 * Steps:
 * 1. Send a GET request to the reporting-methods endpoint using apiUtil.getReportingMethods with a valid cookie.
 * 2. Parse the response into a CustomResponse object, extracting the 'ids' and 'names' lists.
 * 3. Use TestCodeValidator to ensure that the test method implementation uses required RestAssured keywords 
 *    (given, cookie, get, response).
 * 4. Print the response status line for debugging and verification.
 * 5. Assert that the response status code is 200 (OK).
 * 6. Assert that the 'ids' and 'names' lists are non-null and not empty.
 * 
 * Assertions:
 * - Response status code is 200.
 * - IDs list is non-null and not empty.
 * - Names list is non-null and not empty.
 * - Test method implementation adheres to RestAssured usage standards.
 * 
 * Notes:
 * - This test belongs to group "PL2" and has a priority of 9.
 * - Ensures that reporting methods are correctly exposed via the API.
 * - System.out.println statements are used for debugging and can be removed in production test runs.
 */



@Test(priority = 9, groups = { "PL2" },
    description = "1. Send a GET request to the '/web/index.php/api/v2/pim/reporting-methods?limit=50&offset=0' endpoint with a valid cookie\n"
		        + "2. Parse the response using CustomResponse to extract Name,ids\n"
		        + "3. Print the status code, response body, and extracted fields\n"
		        + "4. Assert that all extracted lists are non-empty and contain non-null values\n"
		        + "5. Assert that the response status code is 200 (OK)")
public void getReportingMethods() throws IOException {

    CustomResponse customResponse = apiUtil.getReportingMethods(
        "/web/index.php/api/v2/pim/reporting-methods?limit=50&offset=0", cookieValue, null
    );

    boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
        apiUtilPath, "getReportingMethods", List.of("given", "cookie", "get", "response")
    );
    Assert.assertTrue(isImplementationCorrect, "Implementation should be correct");

    System.out.println(customResponse.getStatusLine());

    // Verify status code and status line
    Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200");

    // Verify IDs list
    Assert.assertNotNull(customResponse.getIds(), "IDs list should not be null");
    Assert.assertFalse(customResponse.getIds().isEmpty(), "IDs list should not be empty");

    // Verify Names list
    Assert.assertNotNull(customResponse.getNames(), "Names list should not be null");
    Assert.assertFalse(customResponse.getNames().isEmpty(), "Names list should not be empty");
}


	/**
 * Test Method: getTerminationReasons
 * 
 * Purpose:
 * This test validates the '/web/index.php/api/v2/pim/termination-reasons' API endpoint using a valid authentication cookie.
 * It ensures that the response is correctly parsed into a CustomResponse object and that the lists of termination reason
 * IDs and Names are present and valid.
 * 
 * Steps:
 * 1. Send a GET request to the termination-reasons endpoint using apiUtil.getTerminationReasons with a valid cookie.
 * 2. Parse the response into a CustomResponse object, extracting the 'ids' and 'names' lists.
 * 3. Use TestCodeValidator to ensure that the test method implementation uses required RestAssured keywords 
 *    (given, cookie, get, response).
 * 4. Print the response status code and body for debugging and verification.
 * 5. Assert that the response status code is 200 (OK).
 * 6. Assert that the 'ids' and 'names' lists are non-null and not empty.
 * 
 * Assertions:
 * - Response status code is 200.
 * - IDs list is non-null and not empty.
 * - Names list is non-null and not empty.
 * - Test method implementation adheres to RestAssured usage standards.
 * 
 * Notes:
 * - This test belongs to group "PL2" and has a priority of 10.
 * - Ensures that termination reasons are correctly exposed via the API.
 * - System.out.println statements can be added for debugging purposes.
 */


@Test(priority = 10, groups = { "PL2" },
    description = "1. Send a GET request to the '/web/index.php/api/v2/pim/termination-reasons?limit=50&offset=0' endpoint with a valid cookie\n"
				+ "2. Parse the response using CustomResponse to extract IDs and Names\n"
				+ "3. Print the status code, response body, and extracted fields\n"
				+ "4. Assert that all extracted lists are non-empty and contain non-null values\n"
				+ "5. Assert that the response status code is 200 (OK)")
public void getTerminationReasons() throws IOException {
    CustomResponse customResponse = apiUtil.getTerminationReasons(
        "/web/index.php/api/v2/pim/termination-reasons?limit=50&offset=0", cookieValue, null
    );

    boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
        apiUtilPath, "getTerminationReasons", List.of("given", "cookie", "get", "response")
    );
    Assert.assertTrue(isImplementationCorrect, "Implementation should be correct");

    // Verify status code
    Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200");

    // Verify IDs list
    Assert.assertNotNull(customResponse.getIds(), "IDs list should not be null");
    Assert.assertFalse(customResponse.getIds().isEmpty(), "IDs list should not be empty");

    // Verify Names list
    Assert.assertNotNull(customResponse.getNames(), "Names list should not be null");
    Assert.assertFalse(customResponse.getNames().isEmpty(), "Names list should not be empty");
}
	

/**
 * Test Method: updateOpenIdProvider
 * 
 * Purpose:
 * This test validates the PUT operation for updating an OpenID provider at the endpoint 
 * '/web/index.php/api/v2/auth/openid-providers/{id}'. It ensures that the response is correctly 
 * parsed into a CustomResponse object and that all updated fields are returned as expected.
 * 
 * Steps:
 * 1. Retrieve the OpenID provider ID using a helper method (getOpenIdProviderId).
 * 2. Generate random values for the provider's name, URL, clientId, and clientSecret.
 * 3. Build the request body JSON using the generated values.
 * 4. Send a PUT request to the endpoint with the request body using apiUtil.updateOpenIdProvider.
 * 5. Parse the response into a CustomResponse object, extracting id, name, clientId, and clientSecret fields.
 * 6. Validate that the test method implementation uses required RestAssured keywords 
 *    (given, cookie, put, response) using TestCodeValidator.
 * 7. Print or log the response status code, status line, and body for debugging purposes.
 * 8. Assert that the response status code is 200 (OK).
 * 9. Assert that all extracted fields are non-null and match the values sent in the PUT request.
 * 
 * Assertions:
 * - Status code is 200.
 * - Response ID is not null.
 * - Name matches the sent value.
 * - Client ID matches the sent value.
 * - Client Secret matches the sent value.
 * - Test method implementation adheres to RestAssured usage standards.
 * 
 * Notes:
 * - This test belongs to group "PL2" and has a priority of 11.
 * - Randomized values help ensure uniqueness and prevent conflicts during repeated runs.
 * - The test covers both API request correctness and response validation.
 */


@Test(priority = 11, groups = { "PL2" },
    description = "1. Create a body for the PUT request as {\"name\": \"\", \"url\": \"\", \"clientId\": \"\", \"clientSecret\": \"\"}"
	+ "2. Send a PUT request to the '/web/index.php/api/v2/auth/openid-providers/{id}' endpoint with the request body\n"
	+ "3. Parse the response using CustomResponse to extract id, name, clientId, and clientSecret fields\n"
	+ "4. Print the status code, response body, and extracted fields\n"
	+ "5. Assert that all extracted fields are non-null and contain expected values")
public void updateOpenIdProvider() throws IOException {
    // Get the OpenID provider ID
    int id = getOpenIdProviderId(cookieValue);

    // Generate random strings for payload
    

    String name = "Provider_" + randomString(6);
    String url = "https://"+randomString(8)+".com";
    String clientId = randomString(8);
    String clientSecret = randomString(12);

    // Build request body
    String requestBody = "{\n" +
            "  \"name\": \"" + name + "\",\n" +
            "  \"url\": \"" + url + "\",\n" +
            "  \"clientId\": \"" + clientId + "\",\n" +
            "  \"clientSecret\": \"" + clientSecret + "\"\n" +
            "}";

    // Call API
    CustomResponse customResponse = apiUtil.updateOpenIdProvider(
            "/web/index.php/api/v2/auth/openid-providers/" + id,
            cookieValue,
            requestBody
    );

    // Validate implementation
    boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
            apiUtilPath, "updateOpenIdProvider", List.of("given", "cookie", "put", "response")
    );
    Assert.assertTrue(isImplementationCorrect, "Implementation should be correct");

    // Verify status code and line
    Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200");
    //Assert.assertEquals(customResponse.getStatusLine(), "HTTP/1.1 200 OK", "Status line should match 'HTTP/1.1 200 OK'");

    // Verify response fields
    Assert.assertNotNull(customResponse.getId(), "ID should not be null");
    Assert.assertEquals(customResponse.getName(), name, "Name should match");
    Assert.assertEquals(customResponse.getClientId(), clientId, "Client ID should match");
    Assert.assertEquals(customResponse.getClientSecret(), clientSecret, "Client Secret should match");
}

/**
 * Test Method: deleteOpenIdProvider
 * 
 * Purpose:
 * Deletes an existing OpenID provider by ID and validates the deletion.
 * Ensures that the provider is first created to guarantee test data availability.
 * 
 * Steps:
 * 1. Create a new OpenID provider using a POST request to ensure there is a provider to delete.
 * 2. Retrieve the ID of the newly created provider using getOpenIdProviderId.
 * 3. Send a DELETE request to the '/web/index.php/api/v2/auth/openid-providers/{id}' endpoint using the retrieved ID.
 * 4. Parse the DELETE response into a CustomResponse object.
 * 5. Validate the test implementation using TestCodeValidator to ensure RestAssured calls are used correctly.
 * 6. Log the response status code, status line, and deleted ID for debugging.
 * 7. Assert that:
 *    - The response status code is 200 (OK).
 *    - The deleted ID matches the ID of the provider that was created and deleted.
 * 
 * Parameters:
 * - cookieValue: The authentication cookie used for authorization.
 * 
 * Returns:
 * - None (test method asserts deletion and validates response)
 * 
 * Notes:
 * - Ensures that test data exists before attempting deletion.
 * - Validates both the API implementation and the response structure.
 */


@Test(priority = 12, groups = { "PL2" },
    description = "1. Create an OpenID provider using POST to ensure data exists for deletion\n"
	+ "2. Get its ID\n"
	+ "3. Send a DELETE request to '/web/index.php/api/v2/auth/openid-providers/{id}'\n"
	+ "4. Validate the response\n"
	+ "5. Assert that the provider is deleted")
public void deleteOpenIdProvider() throws IOException {
    // Step 1: Create provider
    createOpenIdProviderWithRandomBody(cookieValue);

    // Step 2: Get its ID
    int id = getOpenIdProviderId(cookieValue);

    // Step 3: Delete provider
    CustomResponse customResponse = apiUtil.deleteOpenIdProvider(
            "/web/index.php/api/v2/auth/openid-providers",
            cookieValue,
            id
    );

    // Step 4: Validate implementation
    boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
            apiUtilPath, "deleteOpenIdProvider", List.of("given", "cookie", "delete", "response")
    );
    Assert.assertTrue(isImplementationCorrect, "Implementation should be correct");

    // Step 5: Log & assert
    logger.info("Status Code: " + customResponse.getStatusCode());
    logger.info("Status Line: " + customResponse.getStatusLine());
    logger.info("Deleted ID: " + customResponse.getDeletedId());

    Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code mismatch");
    Assert.assertEquals(customResponse.getDeletedId(), id, "Deleted ID mismatch");
}

/**
 * Test Case: Create an OAuth Client
 *
 * Steps:
 * 1. Generate random strings for 'name' and 'redirectUri' to ensure uniqueness.
 * 2. Construct the JSON request body with the following fields:
 *      - name: Randomly generated name
 *      - redirectUri: Randomly generated URI
 *      - enabled: true
 *      - confidential: false
 * 3. Send a POST request to the '/web/index.php/api/v2/admin/oauth-clients' endpoint
 *    using the constructed request body and a valid authentication cookie.
 * 4. Parse the response into a CustomResponse object to extract:
 *      - id
 *      - name
 *      - redirectUri
 *      - enabled
 *      - confidential
 * 5. Log the status code, response body, and extracted fields for verification.
 * 6. Assert that:
 *      - The test implementation uses the correct RestAssured methods (given, cookie, post, response)
 *      - Status code is 200
 *      - Extracted ID is not null
 *      - Extracted name and redirectUri match the request
 *      - Enabled is true and confidential is false
 */


@Test(priority = 13, groups = { "PL2" },
    description = "1. Generate random strings for name and redirectUri\n"
	+ "2. Create a body for the POST request as {\"name\": \"\", \"redirectUri\": \"\", \"enabled\": true, \"confidential\": false}\n"
	+ "3. Send a POST request to '/web/index.php/api/v2/admin/oauth-clients' with the request body\n"
	+ "4. Parse the response using CustomResponse to extract id, name, redirectUri, enabled, and confidential fields\n"
	+ "5. Print the status code, response body, and extracted fields\n"
	+ "6. Assert that all extracted fields are non-null and contain expected values")
public void createOAuthClient() throws IOException {
    // Generate random strings

    String name = "Client_" + UUID.randomUUID().toString().substring(0, 6);
    String redirectUri = "https://"+UUID.randomUUID().toString().substring(0, 6)+".com";

    // Build request body
    String requestBody = "{\n" +
            "  \"name\": \"" + name + "\",\n" +
            "  \"redirectUri\": \"" + redirectUri + "\",\n" +
            "  \"enabled\": true,\n" +
            "  \"confidential\": false\n" +
            "}";

    // Call API
    CustomResponse customResponse = apiUtil.createOAuthClient(
            "/web/index.php/api/v2/admin/oauth-clients",
            cookieValue,
            requestBody
    );

    // Validate implementation
    boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
            apiUtilPath, "createOAuthClient", List.of("given", "cookie", "post", "response")
    );
    Assert.assertTrue(isImplementationCorrect, "Implementation should be correct");

    // Assertions
    Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200");
    //Assert.assertEquals(customResponse.getStatusLine(), "HTTP/1.1 200 OK", "Status line should match");

    Assert.assertNotNull(customResponse.getId(), "ID should not be null");
    Assert.assertEquals(customResponse.getName(), name, "Name should match");
    Assert.assertEquals(customResponse.getRedirectUri(), redirectUri, "Redirect URI should match");
    Assert.assertTrue(customResponse.isEnabled(), "Enabled should be true");
    Assert.assertFalse(customResponse.isConfidential(), "Confidential should be false");
}

/**
 * Test Method: updateOAuthClient
 *
 * Purpose:
 * Updates an existing OAuth client using a PUT request and validates the response.
 *
 * Steps:
 * 1. Retrieve the ID of the second OAuth client using a helper method.
 * 2. Generate random values for the 'name' and 'redirectUri' fields to ensure uniqueness.
 * 3. Build the JSON request body with the generated values and fixed 'enabled' and 'confidential' flags.
 * 4. Send a PUT request to the endpoint corresponding to the OAuth client ID.
 * 5. Parse the response into a CustomResponse object for validation.
 * 6. Validate the implementation correctness using TestCodeValidator.
 * 7. Assert that:
 *      - The status code is 200.
 *      - The updated 'name' and 'redirectUri' match the values sent in the request.
 *      - The 'enabled' field is true.
 *      - The 'confidential' field is false.
 */



@Test(priority = 14, groups = { "PL2" },
    description = "1. Get the second OAuth client ID using a helper method\n"
	+ "2. Generate random values for payload\n"
	+ "3. Build the request body for the PUT request\n"
	+ "4. Call the API to update the OAuth client\n"
	+ "5. Validate the response")
public void updateOAuthClient() throws IOException {
    // Step 1: Get the client ID (hardcoded)
    int id = getSecondOAuthClientId(cookieValue); // Assuming you have a valid client ID, or you can fetch it dynamically

    // Step 2: Generate random values for payload
    String name = "Client_" + java.util.UUID.randomUUID().toString().substring(0, 6);
    String redirectUri = "https://"+java.util.UUID.randomUUID().toString().substring(0, 8)+".com";

    // Step 3: Build request body
    String requestBody = "{\n" +
            "  \"name\": \"" + name + "\",\n" +
            "  \"redirectUri\": \"" + redirectUri + "\",\n" +
            "  \"enabled\": true,\n" +
            "  \"confidential\": false\n" +
            "}";

    // Step 4: Call API
    CustomResponse customResponse = apiUtil.updateOAuthClient(
            "/web/index.php/api/v2/admin/oauth-clients/" + id,
            cookieValue,
            requestBody
    );

    // Step 5: Validate implementation
    boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
            apiUtilPath, "updateOAuthClient", List.of("given", "cookie", "put", "response")
    );
    Assert.assertTrue(isImplementationCorrect, "Implementation should be correct");

    // Step 6: Assertions
    Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200");
    Assert.assertEquals(customResponse.getName(), name, "Name should match");
    Assert.assertEquals(customResponse.getRedirectUri(), redirectUri, "Redirect URI should match");
    Assert.assertTrue(customResponse.isEnabled(), "Enabled should be true");
    Assert.assertFalse(customResponse.isConfidential(), "Confidential should be false");
}

	/**
 * Method: deleteOAuthClient
 *
 * Purpose:
 * Ensures that an OAuth client can be created and then deleted successfully,
 * validating the API response and confirming deletion.
 *
 * Steps:
 * 1. Generate random strings for name and redirectUri.
 * 2. Create a new OAuth client using the POST endpoint to ensure test data exists.
 * 3. Retrieve the last created OAuth client ID.
 * 4. Build the DELETE request payload with the retrieved client ID.
 * 5. Send a DELETE request to remove the OAuth client.
 * 6. Validate the test implementation using TestCodeValidator.
 * 7. Log the status code, status line, and deleted ID for debugging.
 * 8. Assert that:
 *      - The response status code is 200.
 *      - The deleted ID matches the ID of the client created for deletion.
 *
 * Notes:
 * - Randomized client name and redirectUri are used to avoid conflicts with existing data.
 * - This test depends on the existence of a valid cookie for authentication.
 */


	@Test(priority = 15, groups = { "PL2" },
	description = "1. Create a new OAuth client using POST to ensure data exists for deletion\n"
		        + "2. Get its ID\n"
		        + "3. Send a DELETE request to '/web/index.php/api/v2/admin/oauth-clients' with the ID in the request body\n"
		        + "4. Validate the response\n"
		        + "5. Assert that the client is deleted")
public void deleteOAuthClient() throws IOException {

	String name = "Client_" + UUID.randomUUID().toString().substring(0, 6);
    String redirectUri = "https://"+UUID.randomUUID().toString().substring(0, 6)+".com";
	// Step 1: Create OAuth client
	CustomResponse customresponse = apiUtil.createOAuthClient(
            "/web/index.php/api/v2/admin/oauth-clients",
            cookieValue,
            "{\n" +
            "  \"name\": \"" + name + "\",\n" +
            "  \"redirectUri\": \"" + redirectUri + "\",\n" +
            "  \"enabled\": true,\n" +
            "  \"confidential\": false\n" +
            "}"
    );
    // Step 1: Get the last client ID
    int id = getLastOAuthClientId(cookieValue);

    // Step 2: Build payload with the ID to delete
    String requestBody = "{ \"ids\": [" + id + "] }";

    // Step 3: Call DELETE API
    CustomResponse customResponse = apiUtil.deleteOAuthClient(
            "/web/index.php/api/v2/admin/oauth-clients",
            cookieValue,
            requestBody
    );

    // Step 4: Validate implementation
    boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
            apiUtilPath, "deleteOAuthClient", List.of("given", "cookie", "delete", "response")
    );
    Assert.assertTrue(isImplementationCorrect, "Implementation should be correct");


    // Step 6: Assert status code and deleted ID
    Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code mismatch");
    Assert.assertEquals(customResponse.getDeletedId(), id, "Deleted ID mismatch");
}











	/*------------Helper Methods------------*/

	public static int getLastOAuthClientId(String cookieValue) {
        String url = "https://opensource-demo.orangehrmlive.com/web/index.php/api/v2/admin/oauth-clients?limit=50&offset=0";

        Response response = RestAssured.given()
                .cookie("orangehrm", cookieValue)
                .header("Accept", "application/json")
            .when()
                .get(url)
            .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonPath = response.jsonPath();
        List<Integer> ids = jsonPath.getList("data.id");

        if (ids.isEmpty()) {
            throw new RuntimeException("No OAuth clients found.");
        }

        System.out.println("Fetched IDs: " + ids); // debug log
        return ids.get(ids.size() - 1); // last ID
    }

	
    public static int getSecondOAuthClientId(String cookieValue) {
        String url = "https://opensource-demo.orangehrmlive.com/web/index.php/api/v2/admin/oauth-clients?limit=50&offset=0";

        Response response = RestAssured.given()
                .cookie("orangehrm", cookieValue)
                .header("Accept", "application/json")
            .when()
                .get(url)
            .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonPath = response.jsonPath();
        List<Integer> ids = jsonPath.getList("data.id");

        if (ids.size() < 2) {
            throw new RuntimeException("Less than 2 OAuth clients found. Cannot fetch the second ID.");
        }

        System.out.println("Fetched IDs: " + ids); // debug log
        return ids.get(1); // second ID
    }





	public void createOpenIdProviderWithRandomBody(String cookieValue) {
    String endpoint = "https://opensource-demo.orangehrmlive.com/web/index.php/api/v2/auth/openid-providers";

    // Generate random strings
    String name = "name_" + UUID.randomUUID().toString().substring(0, 6);
    String url = "https://example-" + UUID.randomUUID().toString().substring(0, 6) + ".com";
    String clientId = "client_" + UUID.randomUUID().toString().substring(0, 6);
    String clientSecret = "secret_" + UUID.randomUUID().toString().substring(0, 6);

    // Build request body
    String requestBody = "{\n" +
            "  \"name\": \"" + name + "\",\n" +
            "  \"url\": \"" + url + "\",\n" +
            "  \"clientId\": \"" + clientId + "\",\n" +
            "  \"clientSecret\": \"" + clientSecret + "\"\n" +
            "}";

    // Send POST request
    Response response = RestAssured.given()
            .cookie("orangehrm", cookieValue)
            .header("Content-Type", "application/json")
            .body(requestBody)
        .when()
            .post(endpoint)
        .then()
            .extract().response();

    // Print the response
    System.out.println("Status Code: " + response.getStatusCode());
    System.out.println("Status Line: " + response.getStatusLine());
    System.out.println("Response Body:\n" + response.getBody().asPrettyString());
}


	public int getOpenIdProviderId(String cookieValue) {
    Response response = RestAssured.given()
            .relaxedHTTPSValidation()
            .cookie("orangehrm", cookieValue)
            .header("Content-Type", "application/json")
            .when()
            .get("https://opensource-demo.orangehrmlive.com/web/index.php/api/v2/auth/openid-providers?limit=50&offset=0")
            .then()
            .extract()
            .response();

    List<Map<String, Object>> dataList = response.jsonPath().getList("data");

    if (dataList != null && !dataList.isEmpty()) {
        Object idObj = dataList.get(0).get("id");
        if (idObj instanceof Number) {
            return ((Number) idObj).intValue();
        }
    }
    throw new IllegalStateException("No ID found in the OpenID providers response");
}


	
	public int getfirstlangid() {
	    String url = "https://opensource-demo.orangehrmlive.com/web/index.php/api/v2/admin/i18n/languages?limit=50&offset=0&sortOrder=ASC&activeOnly=true";

	    Response response = RestAssured
	            .given()
	            .cookie("orangehrm", cookieValue)
	            .header("Content-Type", "application/json")
	            .get(url)
	            .then()
	            .extract()
	            .response();

	    // Extract the first language ID from the JSON customResponse
	    int firstLangId = response.jsonPath().getInt("data[0].id");
	    return firstLangId;
	}


	public int getMissingLanguageIdLessThan450() {
	    String url = "https://opensource-demo.orangehrmlive.com/web/index.php/api/v2/admin/i18n/languages?limit=50&offset=0&sortOrder=ASC&activeOnly=true";

	    Response customResponse = RestAssured
	            .given()
	            .cookie("orangehrm", cookieValue)
	            .header("Content-Type", "application/json")
	            .get(url)
	            .then()	         
	            .extract()
	            .response();

	    List<Integer> existingIds = customResponse.jsonPath().getList("data.id");
//	    System.out.println("Existing IDs: " + existingIds);

	    if (existingIds == null || existingIds.isEmpty()) {
	        System.out.println("No IDs found in customResponse!");
	        return -1;
	    }

	    Set<Integer> idSet = new HashSet<>(existingIds);

	    for (int i = 1; i < 450; i++) {
	        if (!idSet.contains(i)) {
	            System.out.println("Missing ID Found: " + i);
	            return i;
	        }
	    }

	    System.out.println("No missing ID found under 450.");
	    return -1;
	}

	
	public int getPayGradeid() {
		String endpoint = "/web/index.php/api/v2/admin/pay-grades?limit=50&offset=0";
		Response response = RestAssured.given().cookie("orangehrm", cookieValue).get(baseUrl + endpoint);

		if (response.statusCode() == 200) {
			int firstId = response.jsonPath().getInt("data[0].id");
			System.out.println("First Job Title ID: " + firstId);
			return firstId;
		} else {
			System.out.println("Failed to fetch job titles. Status code: " + response.statusCode());
			return -1;
		}
	}

	public int getemploymentstatusid() {
		String endpoint = "/web/index.php/api/v2/admin/employment-statuses?limit=50&offset=0";

		Response response = RestAssured.given().cookie("orangehrm", cookieValue).get(baseUrl + endpoint);

		if (response.statusCode() == 200) {
			int firstId = response.jsonPath().getInt("data[0].id");
			System.out.println("First Job Title ID: " + firstId);
			return firstId;
		} else {
			System.out.println("Failed to fetch job titles. Status code: " + response.statusCode());
			return -1;
		}

	}

	/*----------------------------------------------Helper Function----------------------------------------------------------*/
	
	
	public void createAuthProviderTest() throws IOException {
		
		String endpoint = "/web/index.php/api/v2/auth/openid-providers";

	    Map<String, Object> body = new HashMap<>();
	    body.put("name", "Test Provider " + System.currentTimeMillis());
	    body.put("url", "https://openid.example.com");
	    body.put("status", true);
	    body.put("clientId", "client-id-" + UUID.randomUUID());
	    body.put("clientSecret", "super-secret-value");

	    // Prepare the request
	    RequestSpecification request = RestAssured.given()
	        .baseUri(baseUrl)
	        .cookie("orangehrm", cookieValue)
	        .header("Content-Type", "application/json");

	    // Add body
	    if (body != null) {
	        request.body(body);
	    }

	    // Send POST request
	    Response response = request.post(endpoint).then().extract().response();

	    // Print for debug
	    System.out.println("POST Status Code: " + response.getStatusCode());
	    System.out.println("POST Response Body: " + response.getBody().asString());

	    // Assert status code
	    Assert.assertEquals(response.getStatusCode(), 200, "Expected 201 Created status.");
	}

	String randomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return sb.toString();
    }

	
}
