package com.hakunamatata.pradeep.noworries;

import java.util.Date;

/**
 * Created by pradeep on 9/5/16.
 */
public class ModelDataTrivia {


    private String userName;
    private Date datePosted;
    private String description;
    private String imageLocation;
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImageLocation() {
        return imageLocation;
    }
    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

}
