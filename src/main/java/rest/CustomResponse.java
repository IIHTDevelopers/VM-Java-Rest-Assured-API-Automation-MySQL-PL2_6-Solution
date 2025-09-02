package rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.restassured.response.Response;

public class CustomResponse {
	private Response response;
	private int statusCode;
	private String status;
	// Variable of Test Case 1
	private List<Object> languages;
	private List<Object> dateFormat;
	// Variable of Test Case 2
	private List<Object> languageId;
	private List<Object> languageName;
	private List<Object> languageCode;
	// Variable of Test Case 3
	private List<Object> leaveAssignLeave;
	private List<Object> leaveLeaveList;
	private List<Object> leaveApplyLeave;
	private List<Object> leaveMyLeave;
	private List<Object> timeEmployeeTimesheet;
	private List<Object> timeMyTimesheet;
	// Variables of Test Case 4
	private List<Object> providerIds;
    private List<Object> providerNames;
    private List<Object> providerUrls;
    private List<Object> providerStatuses;
    private List<Object> clientIds;
    // Variables for Test Case 5
    private List<Object> ids;
    private List<Object> names;
    private List<Object> clientAdminIds;
    private List<Object> redirectUris;
    private List<Object> enabledFlags;
    private List<Object> confidentialFlags;
private String statusLine;
private boolean enable;
private String hostname;
private int port;
private String encryption;
private String ldapImplementation;
private boolean bindAnonymously;
private String bindUserDN;
private boolean hasBindUserPassword;
private boolean pimShowDeprecatedFields;
private boolean showSIN;
private boolean showSSN;
private boolean showTaxExemptions;
private List<Map<String, Object>> customFieldsList;
private List<Object> idList;
private List<Object> nameList;

private int id;
private String name;
private String clientId;
private String clientSecret;

public CustomResponse(Response response,
                      int statusCode,
                      String statusLine,
                      boolean pimShowDeprecatedFields,
                      boolean showSIN,
                      boolean showSSN,
                      boolean showTaxExemptions) {
    this.response = response;
    this.statusCode = statusCode;
    this.statusLine = statusLine;
    this.pimShowDeprecatedFields = pimShowDeprecatedFields;
    this.showSIN = showSIN;
    this.showSSN = showSSN;
    this.showTaxExemptions = showTaxExemptions;
}

// Getters
public boolean isPimShowDeprecatedFields() { return pimShowDeprecatedFields; }
public boolean isShowSIN() { return showSIN; }
public boolean isShowSSN() { return showSSN; }
public boolean isShowTaxExemptions() { return showTaxExemptions; }



	// public CustomResponse(Response response, int statusCode, String status, List<Integer> ids, List<String> names) {
	// 	this.response = response;
	// 	this.statusCode = statusCode;
	// 	this.status = status;
	// 	this.ids = ids;
	// 	this.names = names;
	// }
	
	public CustomResponse(Response response, int statusCode, String status,List<Object> languages, List<Object> dateFormat) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.languages = languages;
		this.dateFormat = dateFormat;
	}
	
	
	public CustomResponse(Response response, int statusCode, String status,List<Object> languageId, List<Object> languageName , List<Object> languageCode) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.languageId = languageId;
		this.languageName = languageName;
		this.languageCode = languageCode;
	}
	
	public CustomResponse(Response response, int statusCode, String status,List<Object> leaveAssignLeave,List<Object> leaveLeaveList,
			List<Object> leaveApplyLeave,List<Object> leaveMyLeave,List<Object> timeEmployeeTimesheet,List<Object> timeMyTimesheet) {
		
	    this.response = response;
	    this.statusCode = statusCode;
	    this.status = status;
	    this.leaveAssignLeave = leaveAssignLeave;
	    this.leaveLeaveList = leaveLeaveList;
	    this.leaveApplyLeave = leaveApplyLeave;
	    this.leaveMyLeave = leaveMyLeave;
	    this.timeEmployeeTimesheet = timeEmployeeTimesheet;
	    this.timeMyTimesheet = timeMyTimesheet;
	}
	
	public CustomResponse(Response response, int statusCode, String status,
            List<Object> providerIds, List<Object> providerNames,
            List<Object> providerUrls, List<Object> providerStatuses,
            List<Object> clientIds) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.providerIds = providerIds;
		this.providerNames = providerNames;
		this.providerUrls = providerUrls;
		this.providerStatuses = providerStatuses;
		this.clientIds = clientIds;
		}
	
	
	public CustomResponse(Response response, int statusCode, String status, OAuthClientInfo clientInfo) {
	    this.response = response;
	    this.statusCode = statusCode;
	    this.status = status;
	    this.ids = clientInfo.getIds();
	    this.names = clientInfo.getNames();
	    this.clientAdminIds = clientInfo.getClientIds();
	    System.out.println("Cleint Id is : " + clientAdminIds);
	    this.redirectUris = clientInfo.getRedirectUris();
	    this.enabledFlags = clientInfo.getEnabledFlags();
	    this.confidentialFlags = clientInfo.getConfidentialFlags();
	}

	public CustomResponse(Response response,
                      int statusCode,
                      String statusLine,
                      boolean enable,
                      String hostname,
                      int port,
                      String encryption,
                      String ldapImplementation,
                      boolean bindAnonymously,
                      String bindUserDN,
                      boolean hasBindUserPassword) {
    this.response = response;
    this.statusCode = statusCode;
    this.statusLine = statusLine;
    this.enable = enable;
    this.hostname = hostname;
    this.port = port;
    this.encryption = encryption;
    this.ldapImplementation = ldapImplementation;
    this.bindAnonymously = bindAnonymously;
    this.bindUserDN = bindUserDN;
    this.hasBindUserPassword = hasBindUserPassword;
}

public CustomResponse(Response rawResponse, int statusCode, String statusLine, List<Map<String, Object>> customFieldsList) {
	this.response = rawResponse;
	this.statusCode = statusCode;
	this.statusLine = statusLine;
	this.customFieldsList = customFieldsList;
}

public CustomResponse(Response response, int statusCode, String statusLine,
                      int id, String name, String clientId, String clientSecret, List<Object> redirectUris) {
    // store in fields
    this.id = id;
    this.name = name;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.redirectUris = redirectUris;
}








	// Getting Responses , StatusCode , Status Line
	public Response getResponse() {
		return response;
	}
	
	public int getStatusCode() {
		return statusCode;
	}

	public String getStatus() {
		return status;
	}
	
	// Get Methods For Test Case 2
	public List<Object> getLanguages() {
		return languages;
	}

	public List<Object> getDateFormat() {
		return dateFormat;
	}
	
	// Get Methods For Test Case 2
	public List<Object> getLanguageId() {
		return languageId;
	}
	public List<Object> getLanguageName() {
		return languageName;
	}
	public List<Object> getLanguageCode() {
		return languageCode;
	}
		
	// Get Methods For Test Case 3	
	public List<Object> leave_Assign_Level() {
	    return leaveAssignLeave;
	}
	public List<Object> leave_Leave_List() {
	    return leaveLeaveList;
	}
	public List<Object> leave_Apply_Leave() {
	    return leaveApplyLeave;
	}
	public List<Object> leave_My_Leave() {
	    return leaveMyLeave;
	}
	public List<Object> time_Employee_Timesheet() {
	    return timeEmployeeTimesheet;
	}
	public List<Object> time_My_Timesheet() {
	    return timeMyTimesheet;
	}
	
	
	// Get Methods For Test Case 4
    public List<Object> getProviderIds() {
        return providerIds;
    }

    public List<Object> getProviderNames() {
        return providerNames;
    }

    public List<Object> getProviderUrls() {
        return providerUrls;
    }

    public List<Object> getProviderStatuses() {
        return providerStatuses;
    }

    public List<Object> getClientIds() {
        return clientIds;
    }
	
    // Get Methods For Test Case 5
    public List<Object> getIds() {
        return ids;
    }

    public List<Object> getNames() {
        return names;
    }

    public List<Object> getAdminIds() {
        return clientAdminIds;
    }

    public List<Object> getRedirectUris() {
        return redirectUris;
    }

    public List<Object> getEnabledFlags() {
        return enabledFlags;
    }

    public List<Object> getConfidentialFlags() {
        return confidentialFlags;
    }

	 public List<Map<String, Object>> getCustomFieldsList() {
        return customFieldsList;
    }

	public List<Object> getIdList() {
        return idList;
    }

    public List<Object> getNameList() {
        return nameList;
    }

    private List<Integer> extractIds(Response response, String jsonPath) {
    return response.jsonPath().getList(jsonPath, Integer.class);
	}

	private List<String> extractNames(Response response, String jsonPath) {
		return response.jsonPath().getList(jsonPath, String.class);
	}


    

    public Response getRawResponse() {
        return response;
    }

    public String getStatusLine() { return statusLine; }
    public boolean isEnable() { return enable; }
    public String getHostname() { return hostname; }
    public int getPort() { return port; }
    public String getEncryption() { return encryption; }
    public String getLdapImplementation() { return ldapImplementation; }
    public boolean isBindAnonymously() { return bindAnonymously; }
    public String getBindUserDN() { return bindUserDN; }
    public boolean isHasBindUserPassword() { return hasBindUserPassword; }
	public Boolean getPimShowDeprecatedFields() { return pimShowDeprecatedFields; }
	public Boolean getShowSIN() { return showSIN; }
	public Boolean getShowSSN() { return showSSN; }
	public Boolean getShowTaxExemptions() { return showTaxExemptions; }
	public Integer getId() { return id; }
	public String getName() { return name; }
	public String getClientId() { return clientId; }
	public String getClientSecret() { return clientSecret; }


	
    private Response rawResponse;
    private int deletedId;

    public CustomResponse(Response rawResponse, int statusCode, String statusLine, int deletedId) {
        this.rawResponse = rawResponse;
        this.statusCode = statusCode;
        this.statusLine = statusLine;
        this.deletedId = deletedId;
    }
    public int getDeletedId() {
        return deletedId;
    }

	
	public void setIds(List<Object> ids) {
    this.ids = ids;
}

public void setNames(List<Object> names) {
    this.names = names;
}




    private String url;
    

    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

	public void setId(int id) {
    this.id = id;
}

public void setName(String name) {
    this.name = name;
}

Boolean statuss;

public void setStatus(Boolean statuss) {
    this.statuss = statuss;
}

public void setClientId(String clientId) {
    this.clientId = clientId;
}

public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
}

public void setRedirectUris(List<Object> redirectUris) {
    this.redirectUris = redirectUris;
}

public void setDeletedId(Integer deletedId) {
    this.deletedId = deletedId;
}

private String redirectUri;
private boolean enabled;
private boolean confidential;

// Setters
public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
}

public void setEnabled(boolean enabled) {
    this.enabled = enabled;
}

public void setConfidential(boolean confidential) {
    this.confidential = confidential;
}

public String getRedirectUri() {
    return redirectUri;
}

public boolean isEnabled() {
    return enabled;
}

public boolean isConfidential() {
    return confidential;
}


    private String body;

    // You can add other fields if needed
    // For example, the deleted ID, name, etc.
    

    // Constructor for DELETE response
    public CustomResponse(io.restassured.response.Response response, int statusCode, String statusLine, String body) {
        this.statusCode = statusCode;
        this.statusLine = statusLine;
        this.body = body;

        // Parse ID from response if available
            if (body != null && !body.isEmpty() && body.contains("\"data\"")) {
                io.restassured.path.json.JsonPath jsonPath = response.jsonPath();
                // Assuming data array has at least one object with "id"
                if (jsonPath.getList("data.id").size() > 0) {
                    this.id = jsonPath.getInt("data[0].id");
                }
            }
        
    }

    
    
    public String getBody() {
        return body;
    }

    






	
	
	/*----------------------------------- Helper Function --------------------------------------------*/
 
	
}