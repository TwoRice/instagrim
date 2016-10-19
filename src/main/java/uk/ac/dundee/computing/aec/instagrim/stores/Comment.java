/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.stores;

import java.util.UUID;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author Big Cheesy B
 */
public class Comment {
    
    private UUID uuid = null;
    private String user = null;
    private String comment = null;
    private Date timestamp = null;

    public UUID getUuid() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
   
    public String formatTimestamp(){
        String dateString = this.timestamp.toString();
        dateString = dateString.substring(4);
        dateString = dateString.replace("BST", "");
        
        return dateString;
    }
    
}
