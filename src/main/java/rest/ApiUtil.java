package rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiUtil {
	private static final Logger logger = LoggerFactory.getLogger(ApiUtil.class);
	private static final Set<Integer> usedNumbers = new HashSet<>();
	private static final Random random = new Random();
	private static String BASE_URL;
	Properties prop;

	/**
	 * Retrieves the base URL from the configuration properties file.
	 *
	 * <p>
	 * This method loads the properties from the file located at
	 * <code>{user.dir}/src/main/resources/config.properties</code> and extracts the
	 * value associated with the key <code>base.url</code>. The value is stored in
	 * the static variable <code>BASE_URL</code> and returned.
	 *
	 * @return the base URL string if successfully read from the properties file;
	 *         {@code null} if an I/O error occurs while reading the file.
	 */
	public String getBaseUrl() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			BASE_URL = prop.getProperty("base.url");
			return BASE_URL;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retrieves the username from the configuration properties file.
	 *
	 * <p>
	 * This method reads the properties from the file located at
	 * <code>{user.dir}/src/main/resources/config.properties</code> and returns the
	 * value associated with the key <code>username</code>.
	 *
	 * @return the username as a {@code String} if found in the properties file;
	 *         {@code null} if an I/O error occurs while reading the file.
	 */
	public String getUsername() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			return prop.getProperty("username");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getPassword() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			return prop.getProperty("password");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retrieves the password from the configuration properties file.
	 *
	 * <p>
	 * This method loads the properties from the file located at
	 * <code>{user.dir}/src/main/resources/config.properties</code> and returns the
	 * value associated with the key <code>password</code>.
	 *
	 * @return the password as a {@code String} if found in the properties file;
	 *         {@code null} if an I/O error occurs while reading the file.
	 */
	public static String generateUniqueName(String base) {
		int uniqueNumber;
		do {
			uniqueNumber = 1000 + random.nextInt(9000);
		} while (usedNumbers.contains(uniqueNumber));

		usedNumbers.add(uniqueNumber);
		return base + uniqueNumber;
	}

	/**
	 * Sends a GET request to the specified localization endpoint using a session cookie and retrieves localization data.
	 *
	 * <p>This method targets the <code>/admin/localization</code> endpoint to fetch language and date format settings.
	 * It uses RestAssured to build the request, sets <code>Content-Type</code> to <code>application/json</code>,
	 * and includes the <code>orangehrm</code> session cookie for authentication.
	 *
	 * <p>The response is parsed to extract <code>data.language</code> and <code>data.dateFormat</code> values,
	 * which are then wrapped into lists and returned inside a {@link CustomResponse} object.
	 *
	 * @param endpoint     the relative URL to the localization API endpoint
	 * @param cookieValue  the value of the <code>orangehrm</code> session cookie for authentication
	 * @param body         a map for request body parameters (typically <code>null</code> for GET requests)
	 * @return a {@link CustomResponse} containing the response, status details, language, and date format
	 */

	public CustomResponse getLocalizationValid(String endpoint, String cookieValue, Map<String, String> body) {
		// write your code here

		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");
				
		Response response = request.get(BASE_URL + endpoint).then().extract().response();
		
		JsonPath jsonPath = response.jsonPath();
		
		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		String language = jsonPath.getString("data.language");
		String strDateFormat = jsonPath.getString("data.dateFormat");

		// Wrap them into lists
		List<Object> languages = Collections.singletonList(language);
		List<Object> dateFormat = Collections.singletonList(strDateFormat);
		
	    // Return wrapped CustomResponse with extracted fields
		return new CustomResponse(response, statusCode, status, languages, dateFormat);
	}


	/**
	 * Sends a GET request to the given endpoint to retrieve a list of active languages.
	 *
	 * <p>This method uses RestAssured to construct the request with the required <code>orangehrm</code> session cookie 
	 * and <code>Content-Type: application/json</code> header. Although GET requests typically do not include a body, 
	 * an optional request body may be attached if provided.
	 *
	 * <p>The response is parsed to extract <code>id</code>, <code>name</code>, and <code>code</code> from each language entry 
	 * in the <code>data</code> array. These fields are wrapped into lists and returned in a {@link CustomResponse} object.
	 *
	 * @param endpoint     the relative URL for the active languages API endpoint
	 * @param cookieValue  the session cookie value used for authentication
	 * @param body         an optional map of request body parameters (can be <code>null</code>)
	 * @return a {@link CustomResponse} containing the response object and extracted fields
	 */

	public CustomResponse getActiveLanguages(String endpoint, String cookieValue, Map<String, String> body) {
		
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");
		
		// Only add the body if it's not null
		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();
	
		JsonPath jsonPath = response.jsonPath();
		
		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		String strlanguageId = jsonPath.getString("data.id");
		String strlanguageName = jsonPath.getString("data.name");
		String strlanguageCode = jsonPath.getString("data.code");

		// Wrap them into lists
		List<Object> languageId = Collections.singletonList(strlanguageId);
		List<Object> languageName = Collections.singletonList(strlanguageName);
		List<Object> languageCode = Collections.singletonList(strlanguageCode);
		
	    // Return wrapped CustomResponse with extracted fields
		return new CustomResponse(response, statusCode, status, languageId, languageName , languageCode);

	}

	/**
	 * Sends a GET request to the specified dashboard shortcuts endpoint using a session cookie.
	 *
	 * <p>This method constructs a GET request using RestAssured, sets the <code>Content-Type</code> 
	 * to <code>application/json</code>, and includes a session cookie named <code>orangehrm</code>. 
	 * Although not standard, an optional request body can be attached if provided.
	 *
	 * <p>The method extracts boolean values from keys such as <code>leave.assign_leave</code>, 
	 * <code>leave.leave_list</code>, <code>leave.apply_leave</code>, <code>leave.my_leave</code>,
	 * <code>time.employee_timesheet</code>, and <code>time.my_timesheet</code> from the <code>data</code> object 
	 * in the response using bracket notation due to the presence of dots in key names.
	 *
	 * <p>These values are wrapped in lists and returned via a {@link CustomResponse} object for validation and assertion.
	 *
	 * @param endpoint     the relative endpoint for dashboard shortcuts, appended to the base URL
	 * @param cookieValue  the value of the <code>orangehrm</code> session cookie for authentication
	 * @param body         optional request body parameters (may be <code>null</code>)
	 * @return a {@link CustomResponse} containing the response and extracted shortcut data fields
	 */
	public CustomResponse getDashboardShortcuts(String endpoint, String cookieValue, Map<String, String> body) {
		
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");
	
		// Only add the body if it's not null
		if (body != null) {
			request.body(body);
		}
		
		Response response = request.get(BASE_URL + endpoint).then().extract().response();
		
		
		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();
		
		JsonPath jsonPath = response.jsonPath();

		// Access fields with dots in key names using bracket notation
		Boolean assignLeave = jsonPath.getBoolean("data['leave.assign_leave']");
		Boolean leaveList = jsonPath.getBoolean("data['leave.leave_list']");
		Boolean applyLeave = jsonPath.getBoolean("data['leave.apply_leave']");
		Boolean myLeave = jsonPath.getBoolean("data['leave.my_leave']");
		Boolean empTimesheet = jsonPath.getBoolean("data['time.employee_timesheet']");
		Boolean myTimesheet = jsonPath.getBoolean("data['time.my_timesheet']");

		// Wrap into lists
		List<Object> leaveAssignLeave = Collections.singletonList(assignLeave);
		List<Object> leaveLeaveList = Collections.singletonList(leaveList);
		List<Object> leaveApplyLeave = Collections.singletonList(applyLeave);
		List<Object> leaveMyLeave = Collections.singletonList(myLeave);
		List<Object> timeEmployeeTimesheet = Collections.singletonList(empTimesheet);
		List<Object> timeMyTimesheet = Collections.singletonList(myTimesheet);

	    // Return wrapped CustomResponse with extracted fields
		return new CustomResponse(response, statusCode, status,
		    leaveAssignLeave, leaveLeaveList, leaveApplyLeave,
		    leaveMyLeave, timeEmployeeTimesheet, timeMyTimesheet);
	   
		
	}

	/**
	 * Sends a GET request to fetch authentication provider details from the specified endpoint.
	 *
	 * <p>
	 * This method uses RestAssured to construct a GET request, sets the 
	 * <code>Content-Type</code> header to <code>application/json</code>, and includes 
	 * an <code>orangehrm</code> session cookie for authentication. If a request body is provided, 
	 * it will be added to the request, although including a body in a GET request is not standard practice.
	 *
	 * <p>
	 * The response is parsed to extract the following fields from the <code>data</code> array
	 *
	 * @param endpoint     the relative path of the endpoint to which the request is sent
	 * @param cookieValue  the value of the <code>orangehrm</code> session cookie
	 * @param body         a map representing the request body, or <code>null</code> if not needed
	 * @return a {@link CustomResponse} containing status details and extracted response data
	 */
	public CustomResponse getAuthProviders(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		// Only add the body if it's not null
		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();
	
		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();

		// Extract lists from "data" array
		List<Object> providerIds = jsonPath.getList("data.id");
		List<Object> providerNames = jsonPath.getList("data.providerName");
		List<Object> providerUrls = jsonPath.getList("data.providerUrl");
		List<Object> providerStatuses = jsonPath.getList("data.status");
		List<Object> clientIds = jsonPath.getList("data.clientId");

		// Return wrapped CustomResponse with extracted fields
		return new CustomResponse(response, statusCode, status,
		        providerIds, providerNames, providerUrls,
		        providerStatuses, clientIds);
		
	};

	/**
	 * Sends a GET request to fetch a list of OAuth clients from the admin endpoint.
	 *
	 * <p>
	 * This method constructs a GET request using RestAssured, sets the 
	 * <code>Content-Type</code> header to <code>application/json</code>, and includes 
	 * an <code>orangehrm</code> session cookie for authorization. If a request body is 
	 * provided, it is attached to the request. Note that sending a body with a GET request 
	 * is non-standard and may be ignored or rejected by some servers.
	 *
	 * <p>
	 * The response is parsed to extract OAuth client attributes.
	 * These are wrapped into an {@link OAuthClientInfo} object and returned inside a {@link CustomResponse}.
	 *
	 * @param endpoint     the API endpoint for retrieving OAuth client data
	 * @param cookieValue  the session cookie value for authentication
	 * @param body         the optional request body, or <code>null</code> if not needed
	 * @return a {@link CustomResponse} containing status info and an {@link OAuthClientInfo} payload
	 */


	public CustomResponse getAdminOAuthList(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		// Only add the body if it's not null
		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();
		
		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();

		List<Object> ids = jsonPath.getList("data.id");
	    List<Object> names = jsonPath.getList("data.name");
	    List<Object> clientIds = jsonPath.getList("data.clientId");
	    System.out.println("Cleint Id is : " + clientIds);
	    List<Object> redirectUris = jsonPath.getList("data.redirectUri");
	    List<Object> enabledFlags = jsonPath.getList("data.enabled");
	    List<Object> confidentialFlags = jsonPath.getList("data.confidential");

	    // There Are Multiple CustomResponse Become error prone so creating other constructor for holding all Variables Types : - OAuthClientInfo
	    OAuthClientInfo clientInfo = new OAuthClientInfo(ids, names, clientIds, redirectUris, enabledFlags, confidentialFlags);
	    // Return wrapped CustomResponse with extracted fields
	    return new CustomResponse(response, statusCode, status, clientInfo);

	}
	
    /**
 * Method: getLDAPConfig
 * 
 * Purpose:
 * Sends a GET request to the specified LDAP configuration endpoint and parses the response
 * into a CustomResponse object containing relevant LDAP configuration fields.
 * 
 * Parameters:
 * - endpoint: The API endpoint to send the GET request to (e.g., "/web/index.php/api/v2/admin/ldap-config").
 * - cookieValue: The authentication cookie value used to authorize the request.
 * - requestBody: Optional request body (not typically used for GET requests; defaults to an empty string if null).
 * 
 * Steps:
 * 1. Build the GET request using RestAssured with the following configuration:
 *    - relaxed HTTPS validation to ignore SSL certificate issues.
 *    - Cookie authentication using the provided cookieValue.
 *    - Content-Type header set to "application/json".
 *    - Optional request body (empty string if null).
 * 2. Send the GET request to the full URL (BASE_URL + endpoint).
 * 3. Extract the Response object from the request.
 * 4. Parse the JSON response to extract LDAP configuration fields:
 *    - enable (Boolean) from "data.enable"
 *    - hostname (String) from "data.hostname"
 *    - port (int) from "data.port"
 *    - encryption (String) from "data.encryption"
 *    - ldapImplementation (String) from "data.ldapImplementation"
 *    - bindAnonymously (Boolean) from "data.bindAnonymously"
 *    - bindUserDN (String) from "data.bindUserDN"
 *    - hasBindUserPassword (Boolean) from "data.hasBindUserPassword"
 * 5. Return a new CustomResponse object containing the raw response, status code, status line,
 *    and all parsed LDAP configuration fields.
 * 
 * Returns:
 * - CustomResponse containing:
 *    - Response object
 *    - HTTP status code
 *    - HTTP status line
 *    - Parsed LDAP configuration fields as listed above
 * 
 * Notes:
 * - Designed specifically for GET requests to the LDAP configuration endpoint.
 * - Uses relaxed HTTPS validation; suitable for environments with self-signed certificates.
 * - The requestBody parameter is optional and generally not required for GET requests.
 */

	
	public CustomResponse getLDAPConfig(String endpoint, String cookieValue, String requestBody) {
    Response response = RestAssured
        .given()
            .relaxedHTTPSValidation()
            .cookie("orangehrm", cookieValue)
            .header("Content-Type", "application/json")
            .body(requestBody != null ? requestBody : "")
        .when()
            .get(BASE_URL + endpoint)
        .then()
            .extract().response();

    // Create CustomResponse with parsed data
    return new CustomResponse(
		response,
		response.getStatusCode(),
		response.getStatusLine(),
		response.jsonPath().getBoolean("data.enable"),
        response.jsonPath().getString("data.hostname"),
        response.jsonPath().getInt("data.port"),
        response.jsonPath().getString("data.encryption"),
        response.jsonPath().getString("data.ldapImplementation"),
        response.jsonPath().getBoolean("data.bindAnonymously"),
        response.jsonPath().getString("data.bindUserDN"),
        response.jsonPath().getBoolean("data.hasBindUserPassword")
	);
	}

    /**
 * Method: getOptionalField
 * 
 * Purpose:
 * Sends a GET request to the specified optional-field endpoint and parses the response
 * into a CustomResponse object containing relevant PIM optional field settings.
 * 
 * Parameters:
 * - endpoint: The API endpoint to send the GET request to.
 * - cookieValue: The authentication cookie used for authorization.
 * - Body: Optional request body (not used for GET requests).
 * 
 * Steps:
 * 1. Build the GET request using RestAssured with:
 *    - relaxed HTTPS validation
 *    - Cookie authentication
 *    - Content-Type header set to "application/json"
 * 2. Send the GET request to BASE_URL + endpoint.
 * 3. Extract the response.
 * 4. Parse the JSON response to extract the boolean fields:
 *    - pimShowDeprecatedFields
 *    - showSIN
 *    - showSSN
 *    - showTaxExemptions
 * 5. Return a new CustomResponse containing the raw response, status code, status line, and all parsed fields.
 * 
 * Returns:
 * - CustomResponse containing the HTTP response and parsed PIM optional field values.
 * 
 * Notes:
 * - Designed specifically for GET requests to the optional-field endpoint.
 * - The Body parameter is unused and defaults to null.
 * - Uses relaxed HTTPS validation to avoid SSL certificate issues.
 */


	public CustomResponse getOptionalField(String endpoint, String cookieValue,String Body) {
    Response response = RestAssured
        .given()
            .relaxedHTTPSValidation()
            .cookie("orangehrm", cookieValue)
            .header("Content-Type", "application/json")
        .when()
            .get(BASE_URL + endpoint)
        .then()
            .extract().response();

    return new CustomResponse(
        response,
        response.getStatusCode(),
        response.getStatusLine(),
        response.jsonPath().getBoolean("data.pimShowDeprecatedFields"),
        response.jsonPath().getBoolean("data.showSIN"),
        response.jsonPath().getBoolean("data.showSSN"),
        response.jsonPath().getBoolean("data.showTaxExemptions")
    );
}

/**
 * Method: getCustomFields
 * 
 * Purpose:
 * Sends a GET request to the specified custom-fields endpoint and parses the response
 * into a CustomResponse object containing a list of custom PIM fields.
 * 
 * Parameters:
 * - endpoint: The API endpoint to send the GET request to (e.g., "/web/index.php/api/v2/pim/custom-fields?limit=50&offset=0").
 * - cookieValue: The authentication cookie used for authorization.
 * - requestBody: Optional request body (not used for GET requests; can be null).
 * 
 * Steps:
 * 1. Build the GET request using RestAssured with:
 *    - Base URI set to BASE_URL
 *    - Cookie authentication using cookieValue
 *    - Content-Type header set to "application/json"
 * 2. Send the GET request to the specified endpoint.
 * 3. Extract the response.
 * 4. Parse the JSON response and extract the 'data' array into a List of Maps,
 *    where each Map contains:
 *      - id
 *      - fieldName
 *      - fieldType
 *      - screen
 * 5. Store the parsed list in a CustomResponse object along with the raw response,
 *    status code, and status line.
 * 
 * Returns:
 * - CustomResponse containing the HTTP response and a list of custom PIM fields.
 * 
 * Notes:
 * - Designed specifically for GET requests to the custom-fields endpoint.
 * - Ensures that all relevant custom field information is extracted for further validation.
 * - The requestBody parameter is unused and defaults to null.
 */

public CustomResponse getCustomFields(String endpoint, String cookieValue, String requestBody) {
    Response response = RestAssured.given()
            .baseUri(BASE_URL)
            .cookie("orangehrm", cookieValue)
            .header("Content-Type", "application/json")
            .when()
            .get(endpoint)
            .then()
            .extract()
            .response();

    // Parse JSON and extract list fields
    List<Map<String, Object>> customFieldsList = new ArrayList<>();

    
        List<Map<String, Object>> dataList = response.jsonPath().getList("data");
        if (dataList != null) {
            for (Map<String, Object> item : dataList) {
                Map<String, Object> fieldMap = new HashMap<>();
                fieldMap.put("id", item.get("id"));
                fieldMap.put("fieldName", item.get("fieldName"));
                fieldMap.put("fieldType", item.get("fieldType"));
                fieldMap.put("screen", item.get("screen"));
                customFieldsList.add(fieldMap);
            }
        }

    // Pass parsed values to CustomResponse (ensure CustomResponse has matching constructor)
    return new CustomResponse(
            response,
            response.getStatusCode(),
            response.getStatusLine(),
            customFieldsList // Store the extracted list
    );
}

/**
 * Method: getReportingMethods
 * 
 * Purpose:
 * Sends a GET request to the specified reporting-methods endpoint and parses the response
 * into a CustomResponse object containing lists of reporting method IDs and Names.
 * 
 * Parameters:
 * - endpoint: The API endpoint to send the GET request to (e.g., "/web/index.php/api/v2/pim/reporting-methods?limit=50&offset=0").
 * - cookieValue: The authentication cookie used for authorization.
 * - requestBody: Optional request body (not used for GET requests; can be null).
 * 
 * Steps:
 * 1. Build the GET request using RestAssured with:
 *    - Base URI set to BASE_URL
 *    - Cookie authentication using cookieValue
 *    - Content-Type header set to "application/json"
 * 2. Send the GET request to the specified endpoint.
 * 3. Extract the response and reconstruct the status line if it is null.
 * 4. Parse the JSON 'data' array into two lists:
 *      - ids: List of reporting method IDs
 *      - names: List of reporting method Names
 * 5. Create a CustomResponse object using the parsed lists, status code, and status line.
 * 6. Manually set the ids and names in CustomResponse to ensure they are correctly stored.
 * 
 * Returns:
 * - CustomResponse containing the HTTP response and the extracted lists of IDs and Names.
 * 
 * Notes:
 * - Designed specifically for GET requests to the reporting-methods endpoint.
 * - Ensures all reporting methods data is safely parsed even if the response status line is null.
 * - The requestBody parameter is unused and defaults to null.
 */


public CustomResponse getReportingMethods(String endpoint, String cookieValue, String requestBody) {
    Response response = RestAssured.given()
            .baseUri(BASE_URL)
            .cookie("orangehrm", cookieValue)
            .header("Content-Type", "application/json")
            .when()
            .get(endpoint)
            .then()
            .extract().response();

    // Safely reconstruct status line
    String statusLine = response.getStatusLine();
    if (statusLine == null) {
        String reason = response.getStatusCode() == 200 ? "OK" : "";
        statusLine = "HTTP/1.1 " + response.getStatusCode() + " " + reason;
    }

    // Initialize lists
    List<Object> ids = new ArrayList<>();
    List<Object> names = new ArrayList<>();

    // Iterate through each item in data array
    List<Map<String, Object>> dataList = response.jsonPath().getList("data");
    if (dataList != null) {
        for (Map<String, Object> item : dataList) {
            ids.add(item.get("id"));
            names.add(item.get("name"));
        }
    }

    // Call existing constructor (languages/dateFormat)
    CustomResponse cr = new CustomResponse(
            response,
            response.getStatusCode(),
            statusLine,
            ids,    // stored in languages
            names   // stored in dateFormat
    );

    // Manually set real ids/names fields
    cr.setIds(ids);
    cr.setNames(names);

    return cr;
}


/**
 * Method: getTerminationReasons
 * 
 * Purpose:
 * Sends a GET request to the specified termination-reasons endpoint and parses the response
 * into a CustomResponse object containing lists of termination reason IDs and Names.
 * 
 * Parameters:
 * - endpoint: The API endpoint to send the GET request to (e.g., "/web/index.php/api/v2/pim/termination-reasons?limit=50&offset=0").
 * - cookieValue: The authentication cookie used for authorization.
 * - requestBody: Optional request body (not used for GET requests; can be null).
 * 
 * Steps:
 * 1. Build the GET request using RestAssured with:
 *    - Base URI set to BASE_URL
 *    - Cookie authentication using cookieValue
 *    - Content-Type header set to "application/json"
 * 2. Send the GET request to the specified endpoint.
 * 3. Extract the response and reconstruct the status line if it is null.
 * 4. Parse the JSON 'data' array into two lists:
 *      - ids: List of termination reason IDs
 *      - names: List of termination reason Names
 * 5. Create a CustomResponse object using the parsed lists, status code, and status line.
 * 6. Manually set the ids and names in CustomResponse to ensure they are correctly stored.
 * 
 * Returns:
 * - CustomResponse containing the HTTP response and the extracted lists of IDs and Names.
 * 
 * Notes:
 * - Designed specifically for GET requests to the termination-reasons endpoint.
 * - Ensures all termination reason data is safely parsed even if the response status line is null.
 * - The requestBody parameter is unused and defaults to null.
 */


public CustomResponse getTerminationReasons(String endpoint, String cookieValue, String requestBody) {
    Response response = RestAssured.given()
            .baseUri(BASE_URL)
            .cookie("orangehrm", cookieValue)
            .header("Content-Type", "application/json")
            .when()
            .get(endpoint)
            .then()
            .extract().response();

    String statusLine = response.getStatusLine();
    if (statusLine == null) {
        String reason = response.getStatusCode() == 200 ? "OK" : "";
        statusLine = "HTTP/1.1 " + response.getStatusCode() + " " + reason;
    }

    List<Object> ids = new ArrayList<>();
    List<Object> names = new ArrayList<>();
    List<Map<String, Object>> dataList = response.jsonPath().getList("data");
    if (dataList != null) {
        for (Map<String, Object> item : dataList) {
            ids.add(item.get("id"));
            names.add(item.get("name"));
        }
    }

    // Call existing constructor (languages/dateFormat) to avoid duplicate method issue
    CustomResponse cr = new CustomResponse(
            response,
            response.getStatusCode(),
            statusLine,
            ids,        // will be stored in languages
            names       // will be stored in dateFormat
    );

    // Now manually set real ids/names fields
    cr.setIds(ids);
    cr.setNames(names);

    return cr;
}

/**
 * Method: updateOpenIdProvider
 * 
 * Purpose:
 * Sends a PUT request to update an existing OpenID provider at the specified endpoint. 
 * Parses the response and request body into a CustomResponse object, ensuring all relevant 
 * fields are safely extracted and available for validation.
 * 
 * Parameters:
 * - endpoint: The API endpoint to send the PUT request to (e.g., "/web/index.php/api/v2/auth/openid-providers/{id}").
 * - cookieValue: The authentication cookie used for authorization.
 * - requestBody: The JSON request body containing fields like name, URL, clientId, and clientSecret.
 * 
 * Steps:
 * 1. Build the PUT request using RestAssured with:
 *    - Base URI set to BASE_URL
 *    - Cookie authentication using cookieValue
 *    - Content-Type header set to "application/json"
 *    - Request body as provided
 * 2. Send the PUT request to the endpoint and extract the response.
 * 3. Reconstruct the status line if it is null to avoid null pointer issues.
 * 4. Parse response fields safely:
 *      - respId, respProviderName, respProviderUrl, respStatus, respClientId
 * 5. Parse request body for fallback values in case response fields are missing:
 *      - reqName, reqClientId, reqClientSecret, reqRedirectUris
 * 6. Create a CustomResponse object, initially with empty lists for placeholder fields.
 * 7. Set all relevant fields in the CustomResponse, using response values if available,
 *    otherwise fallback to request body values.
 * 
 * Returns:
 * - CustomResponse containing:
 *    - HTTP response object
 *    - Status code and status line
 *    - Updated provider details including ID, name, URL, status, clientId, clientSecret, and redirectUris
 * 
 * Notes:
 * - Ensures that all fields are safely populated, even if the response is incomplete.
 * - Logs the raw response for debugging purposes.
 * - Designed specifically for updating OpenID providers via PUT requests.
 */


public CustomResponse updateOpenIdProvider(String endpoint, String cookieValue, String requestBody) {
    // Send PUT request
    Response response = RestAssured.given()
            .baseUri(BASE_URL)
            .cookie("orangehrm", cookieValue)
            .header("Content-Type", "application/json")
            .body(requestBody)
        .when()
            .put(endpoint)
        .then()
            .extract().response();

    // Log raw response for debugging
    System.out.println("Raw response: " + response.asString());

    // Safely reconstruct status line
    String statusLine = response.getStatusLine();
    if (statusLine == null) {
        String reason = response.getStatusCode() == 200 ? "OK" : "";
        statusLine = "HTTP/1.1 " + response.getStatusCode() + " " + reason;
    }

    // Safely parse response fields
    Integer respId = response.jsonPath().get("data.id");
    String respProviderName = response.jsonPath().getString("data.providerName");
    String respProviderUrl = response.jsonPath().getString("data.providerUrl");
    Boolean respStatus = response.jsonPath().get("data.status");
    String respClientId = response.jsonPath().getString("data.clientId");

    // Parse request body for fallback values
    io.restassured.path.json.JsonPath reqJson = io.restassured.path.json.JsonPath.from(requestBody);
    String reqName = reqJson.getString("name");
    String reqClientId = reqJson.getString("clientId");
    String reqClientSecret = reqJson.getString("clientSecret");
    List<Object> reqRedirectUris = reqJson.getList("redirectUris") != null ? reqJson.getList("redirectUris") : new ArrayList<>();

    // Create CustomResponse with placeholder lists
    CustomResponse cr = new CustomResponse(
            response,
            response.getStatusCode(),
            statusLine,
            new ArrayList<>(),
            new ArrayList<>()
    );

    // Set all fields safely
    cr.setId(respId != null ? respId : -1);
    cr.setName(respProviderName != null ? respProviderName : reqName);
    cr.setUrl(respProviderUrl);
    cr.setStatus(respStatus != null ? respStatus : false);
    cr.setClientId(respClientId != null ? respClientId : reqClientId);
    cr.setClientSecret(reqClientSecret);
    cr.setRedirectUris(reqRedirectUris);

    return cr;
}



public CustomResponse getReportingMethods(String endpoint, String cookieValue) {
    Response response = RestAssured.given()
            .baseUri(BASE_URL)
            .cookie("orangehrm", cookieValue)
            .header("Content-Type", "application/json")
            .when()
            .get(endpoint)
            .then()
            .extract().response();

    // Reconstruct status line if null
    String statusLine = response.getStatusLine();
    if (statusLine == null) {
        String reason = response.getStatusCode() == 200 ? "OK" : "";
        statusLine = "HTTP/1.1 " + response.getStatusCode() + " " + reason;
    }

    // Initialize lists
    List<Object> ids = new ArrayList<>();
    List<Object> names = new ArrayList<>();

    // Fill IDs
    List<Object> jsonIds = response.jsonPath().getList("data.id");
    if (jsonIds != null) {
        for (Object id : jsonIds) {
            if (id != null) {
                ids.add(id);
            }
        }
    }

    // Fill Names
    List<Object> jsonNames = response.jsonPath().getList("data.name");
    if (jsonNames != null) {
        for (Object name : jsonNames) {
            if (name != null) {
                names.add(name);
            }
        }
    }

    // Return custom response
    return new CustomResponse(
            response,
            response.getStatusCode(),
            statusLine,
            ids,
            names
    );
}

/**
 * Deletes an OpenID provider by ID using a DELETE request.
 * 
 * Steps:
 * 1. Build a payload containing the ID of the OpenID provider to delete.
 *    The payload structure is: { "ids": [id] }
 * 2. Send a DELETE request to the specified endpoint with:
 *    - Base URL from BASE_URL
 *    - Authentication cookie
 *    - Content-Type set to application/json
 *    - Payload in the request body
 * 3. Extract the response using RestAssured.
 * 4. Log the raw response for debugging purposes.
 * 5. Safely reconstruct the status line if missing (default to "HTTP/1.1 <statusCode> OK" for 200).
 * 6. Parse the response to extract the deleted ID from the "data" array.
 * 7. Create a CustomResponse object using an existing constructor.
 * 8. Set the deleted ID in the CustomResponse using setDeletedId().
 * 
 * Parameters:
 * @param endpoint    API endpoint for deletion (e.g., "/web/index.php/api/v2/auth/openid-providers")
 * @param cookieValue Authentication cookie
 * @param id          ID of the OpenID provider to delete
 * 
 * Returns:
 * @return CustomResponse containing:
 *         - Raw Response
 *         - Status code
 *         - Status line
 *         - Deleted ID (via setDeletedId)
 *         - Placeholder empty lists for other fields
 * 
 * Notes:
 * - Ensures safe extraction even if the response has no data array.
 * - Designed to integrate with existing CustomResponse structure for consistency.
 */


public CustomResponse deleteOpenIdProvider(String endpoint, String cookieValue, int id) {
    // Build payload
    Map<String, Object> payload = new HashMap<>();
    payload.put("ids", Collections.singletonList(id));

    Response response = RestAssured.given()
            .baseUri(BASE_URL)
            .cookie("orangehrm", cookieValue)
            .header("Content-Type", "application/json")
            .body(payload)
        .when()
            .delete(endpoint)
        .then()
            .extract().response();

    // Log raw response
    System.out.println("Raw response: " + response.asString());

    // Safely reconstruct status line
    String statusLine = response.getStatusLine();
    if (statusLine == null) {
        String reason = response.getStatusCode() == 200 ? "OK" : "";
        statusLine = "HTTP/1.1 " + response.getStatusCode() + " " + reason;
    }

    // Extract deleted ID from response
    List<Integer> deletedIds = response.jsonPath().getList("data");
    Integer deletedId = (deletedIds != null && !deletedIds.isEmpty()) ? deletedIds.get(0) : null;

    // Return CustomResponse (use existing constructor and setters)
    CustomResponse cr = new CustomResponse(
            response,
            response.getStatusCode(),
            statusLine,
            new ArrayList<>(),
            new ArrayList<>()
    );
    cr.setDeletedId(deletedId);

    return cr;
}

/**
 * Method: createOAuthClient
 *
 * Purpose:
 * Sends a POST request to create a new OAuth client and parses the response
 * into a CustomResponse object with extracted fields.
 *
 * Steps:
 * 1. Send a POST request to the specified endpoint with the given request body
 *    and authentication cookie.
 * 2. Log the raw response for debugging purposes.
 * 3. Safely reconstruct the HTTP status line if it's null.
 * 4. Extract relevant fields from the response JSON:
 *      - id
 *      - name
 *      - clientId
 *      - redirectUri
 *      - enabled
 *      - confidential
 * 5. Create a CustomResponse object using the status code, status line,
 *    and empty placeholder lists.
 * 6. Set the parsed fields into the CustomResponse object:
 *      - id (defaults to -1 if null)
 *      - name
 *      - clientId
 *      - redirectUri
 *      - enabled (defaults to false if null)
 *      - confidential (defaults to false if null)
 * 7. Return the populated CustomResponse object for further validation in tests.
 */

public CustomResponse createOAuthClient(String endpoint, String cookieValue, String requestBody) {
    Response response = RestAssured.given()
            .baseUri(BASE_URL)
            .cookie("orangehrm", cookieValue)
            .header("Content-Type", "application/json")
            .body(requestBody)
        .when()
            .post(endpoint)
        .then()
            .extract().response();

    // Log raw response
    System.out.println("Raw response: " + response.asString());

    // Safely reconstruct status line
    String statusLine = response.getStatusLine();
    if (statusLine == null) {
        String reason = response.getStatusCode() == 200 ? "OK" : "";
        statusLine = "HTTP/1.1 " + response.getStatusCode() + " " + reason;
    }

    // Parse response
    Integer respId = response.jsonPath().get("data.id");
    String respName = response.jsonPath().getString("data.name");
    String respClientId = response.jsonPath().getString("data.clientId");
    String respRedirectUri = response.jsonPath().getString("data.redirectUri");
    Boolean respEnabled = response.jsonPath().get("data.enabled");
    Boolean respConfidential = response.jsonPath().get("data.confidential");

    // Create CustomResponse
    CustomResponse cr = new CustomResponse(
            response,
            response.getStatusCode(),
            statusLine,
            new ArrayList<>(),
            new ArrayList<>()
    );

    // Set fields
    cr.setId(respId != null ? respId : -1);
    cr.setName(respName);
    cr.setClientId(respClientId);
    cr.setRedirectUri(respRedirectUri);
    cr.setEnabled(respEnabled != null && respEnabled);
    cr.setConfidential(respConfidential != null && respConfidential);

    return cr;
}

/**
 * Method: updateOAuthClient
 *
 * Purpose:
 * Sends a PUT request to update an existing OAuth client and returns a CustomResponse object
 * containing the parsed response fields.
 *
 * Steps:
 * 1. Send a PUT request to the specified endpoint with the given cookie and request body.
 * 2. Log the raw response for debugging purposes.
 * 3. Safely reconstruct the HTTP status line in case it is null in the response.
 * 4. Extract response fields:
 *      - id
 *      - name
 *      - clientId
 *      - redirectUri
 *      - enabled
 *      - confidential
 * 5. Create a CustomResponse object with the response, status code, and status line.
 * 6. Set the parsed fields in the CustomResponse object.
 * 7. Return the populated CustomResponse object.
 *
 * Notes:
 * - Null safety checks are included to avoid null pointer exceptions.
 * - Boolean fields are set to false if null in the response.
 */


public CustomResponse updateOAuthClient(String endpoint, String cookieValue, String requestBody) {
    Response response = RestAssured.given()
            .baseUri(BASE_URL)
            .cookie("orangehrm", cookieValue)
            .header("Content-Type", "application/json")
            .body(requestBody)
        .when()
            .put(endpoint)
        .then()
            .extract().response();

    // Log raw response
    System.out.println("Raw response: " + response.asString());

    // Safely reconstruct status line
    String statusLine = response.getStatusLine();
    if (statusLine == null) {
        String reason = response.getStatusCode() == 200 ? "OK" : "";
        statusLine = "HTTP/1.1 " + response.getStatusCode() + " " + reason;
    }

    // Extract response fields
    Integer respId = response.jsonPath().get("data.id");
    String respName = response.jsonPath().getString("data.name");
    String respClientId = response.jsonPath().getString("data.clientId");
    String respRedirectUri = response.jsonPath().getString("data.redirectUri");
    Boolean respEnabled = response.jsonPath().get("data.enabled");
    Boolean respConfidential = response.jsonPath().get("data.confidential");

    // Create CustomResponse
    CustomResponse cr = new CustomResponse(
            response,
            response.getStatusCode(),
            statusLine,
            new ArrayList<>(),
            new ArrayList<>()
    );

    // Set fields
    cr.setId(respId != null ? respId : -1);
    cr.setName(respName);
    cr.setClientId(respClientId);
    cr.setRedirectUri(respRedirectUri);
    cr.setEnabled(respEnabled != null && respEnabled);
    cr.setConfidential(respConfidential != null && respConfidential);

    return cr;
}

/**
 * Sends a DELETE request to remove one or more OAuth clients and returns a CustomResponse.
 *
 * Steps:
 * 1. Builds the DELETE request with the given endpoint, cookie, and request body (containing IDs to delete).
 * 2. Sends the request and extracts the raw Response.
 * 3. Logs the raw response for debugging purposes.
 * 4. Reconstructs the HTTP status line if it is missing from the response.
 * 5. Extracts the list of deleted IDs from the JSON response.
 * 6. Creates a CustomResponse object using the response, status code, status line, and placeholder lists.
 * 7. Sets the first deleted ID (if available) in the CustomResponse object.
 * 8. Returns the populated CustomResponse object.
 *
 * Notes:
 * - Assumes only one ID is deleted per call for simplicity.
 * - Logging the raw response helps debug potential API failures.
 */


public CustomResponse deleteOAuthClient(String endpoint, String cookieValue, String requestBody) {
    Response response = RestAssured.given()
            .baseUri(BASE_URL)
            .cookie("orangehrm", cookieValue)
            .header("Content-Type", "application/json")
            .body(requestBody)
        .when()
            .delete(endpoint)
        .then()
            .extract().response();

    // Log raw response for debugging
    System.out.println("DELETE OAuth Client Response: " + response.asString());

    // Safely reconstruct status line if null
    String statusLine = response.getStatusLine();
    if (statusLine == null) {
        String reason = response.getStatusCode() == 200 ? "OK" : "";
        statusLine = "HTTP/1.1 " + response.getStatusCode() + " " + reason;
    }

    // Extract deleted IDs from response
    List<Integer> deletedIds = response.jsonPath().getList("data");

    // Create CustomResponse using existing constructor (lists used for placeholder)
    CustomResponse cr = new CustomResponse(
            response,
            response.getStatusCode(),
            statusLine,
            new ArrayList<>(),
            new ArrayList<>()
    );

    // Set deleted ID safely
    if (deletedIds != null && !deletedIds.isEmpty()) {
        cr.setDeletedId(deletedIds.get(0));  // Assuming only one ID is deleted per call
    }

    return cr;
}




}