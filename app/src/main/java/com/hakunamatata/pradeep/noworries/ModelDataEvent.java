package com.hakunamatata.pradeep.noworries;

import java.util.Date;

/**
 * Created by pradeep on 12/5/16.
 */

public class ModelDataEvent {


    private String userName;
    private Date datePosted;
    private String eventTitle;
    private String eventDetails;
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
    public String getEventTitle() {
        return eventTitle;
    }
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
    public String getEventDetails() {
        return eventDetails;
    }
    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }


}
