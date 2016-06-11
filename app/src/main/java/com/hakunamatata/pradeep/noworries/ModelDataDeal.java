package com.hakunamatata.pradeep.noworries;

/**
 * Created by pradeep on 13/5/16.
 */
import java.util.Date;

public class ModelDataDeal {


    private String userName;
    private Date datePosted;
    private String dealTitle;
    private String dealDetails;
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Date getDatePosted() {
        return datePosted;
    }
    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }
    public String getDealTitle() {
        return dealTitle;
    }
    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
    }
    public String getDealDetails() {
        return dealDetails;
    }
    public void setDealDetails(String dealDetails) {
        this.dealDetails = dealDetails;
    }


}
