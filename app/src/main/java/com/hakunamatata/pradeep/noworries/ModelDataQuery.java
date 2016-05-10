package com.hakunamatata.pradeep.noworries;

import java.util.Date;

/**
 * Created by pradeep on 8/5/16.
 */
public class ModelDataQuery {


    private String userName;
    private String datePosted;
    private String queryTitle;
    private String queryContent;
    private String imageLocation;

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getDatePosted() {
        return datePosted;
    }
    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }
    public String getQueryTitle() {
        return queryTitle;
    }
    public void setQueryTitle(String queryTitle) {
        this.queryTitle = queryTitle;
    }
    public String getQueryContent() {
        return queryContent;
    }
    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }
    public String getImageLocation() {
        return imageLocation;
    }
    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }


}
