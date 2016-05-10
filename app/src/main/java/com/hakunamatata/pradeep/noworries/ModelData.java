package com.hakunamatata.pradeep.noworries;

/**
 * Created by pradeep on 19/4/16.
 */
public class ModelData {

    private String loginStatus;
    private int loginUserId;
    private String registerStatus;
    private int registerUserId;
    private String loginErrorMsg;
    private String registerErrorMsg;

    private int userId;
    private String locationName;
    private int locationId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public int getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(int userId) {
        this.loginUserId = userId;
    }

    public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }

    public int getRegisterUserId() {
        return registerUserId;
    }

    public void setRegisterUserId(int registerUserId) {
        this.registerUserId = registerUserId;
    }

    public String getLoginErrorMsg() {
        return loginErrorMsg;
    }

    public void setLoginErrorMsg(String loginErrorMsg) {
        this.loginErrorMsg = loginErrorMsg;
    }

    public String getRegisterErrorMsg() {
        return registerErrorMsg;
    }

    public void setRegisterErrorMsg(String registerErrorMsg) {
        this.registerErrorMsg = registerErrorMsg;
    }
}
