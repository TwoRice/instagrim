/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.models;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.Iterator;
import java.util.UUID;
import java.util.LinkedList;
import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 * Model which communicates with the database for all operations in the User table
 * @author Administrator
 */
public class User {
    Cluster cluster;
    public User(){
        
    }
    
    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
    
    /**
     * Checks if a username already exists in the database
     * @param username - username to check
     * @return  - whether or not the username already exists
     */
    public boolean isValidUserName(String username){
        
        Session session = cluster.connect("instagrim");
        ResultSet rs_usernameExists = null;
        
        //Selects all usernames from the database
        rs_usernameExists = session.execute("select login from userprofiles");
        
        if(!rs_usernameExists.isExhausted()){
            for(Row row : rs_usernameExists){
                //Converts each username to lower case
                String storedUsername = row.getString("login").toLowerCase();
                //Checks if both usernames coverted to lower case are the same
                username = username.toLowerCase();
                
                if(username.equals(storedUsername)){
                    return false;
                }
            }
        }
        
        return true;              
    }
    
    /**
     * Checks if an email address already exists in the database
     * @param email - email address to check
     * @return - whether or not the username already exists
     */
    public boolean isValidEmail(String email){
        
        Session session = cluster.connect("instagrim");
        ResultSet rs_emailExists = null;
        
        //Selects all emails from the database
        rs_emailExists = session.execute("select email from userprofiles");
        
        if(!rs_emailExists.isExhausted()){
            for(Row row : rs_emailExists){
                //Converts each email to lower casse
                String storedEmail = row.getString("email").toLowerCase();
                //Checks if both emails coverted to lower case are the same
                email = email.toLowerCase();
                
                if(email.equals(storedEmail)){
                    return false;
                }
            }
        }
        
        return true; 
        
    }
    
    /**
     * Adds a new user to the database
     * @param username - username for the new user
     * @param Password - password for the new user
     * @param firstName - new user's first name
     * @param lastName - new user's last name
     * @param email - new user's email address
     * @return - whether or not the user was added successfully
     */
    public boolean RegisterUser(String username, String Password, String firstName, String lastName, String email){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        //Encodes the password
        try {
            EncodedPassword= sha1handler.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("insert into userprofiles (login,password,first_name,last_name,email) Values(?,?,?,?,?)");
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute(boundStatement.bind(username,EncodedPassword, firstName, lastName, email));
        //We are assuming this always works.  Also a transaction would be good here !
        
        return true;
    }
    
    /**
     * Checks if a username exists in the database and if the password matches up
     * @param username - username to check
     * @param Password - password to check
     * @return - whether or not this is a valid login
     */
    public boolean loginWithUsername(String username, String Password){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        //Encodes the password
        try {
            EncodedPassword= sha1handler.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim");
        PreparedStatement ps_usernameCheck = session.prepare("select password from userprofiles where login =?");
        ResultSet rs_usernameCheck = null;
        BoundStatement boundStatement = new BoundStatement(ps_usernameCheck);
        rs_usernameCheck = session.execute(boundStatement.bind(username));
        //Checks the encoded passwords against each other if the username exists
        if(!rs_usernameCheck.isExhausted()) {
            for (Row row : rs_usernameCheck) {
                
                String StoredPass = row.getString("password");
                if (StoredPass.compareTo(EncodedPassword) == 0){return true;}
                
            }
        }
       
        return false;  
    }
    
    /**
     * Checks if an email exists in the database and if the password matches up
     * @param email - email to check
     * @param password - password to check
     * @return 
     */
    public String loginWithEmail(String email, String password){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        //Encodes the password
        try{
        EncodedPassword= sha1handler.SHA1(password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return null;
        }
        Session session = cluster.connect("instagrim");
        PreparedStatement ps_emailCheck = session.prepare("select password, login from userprofiles where email =? allow filtering");
        BoundStatement bs = new BoundStatement(ps_emailCheck);
        ResultSet rs_emailCheck = session.execute(bs.bind(email));

        //Checks the encoded passwords against each other if the email exists
        if(!rs_emailCheck.isExhausted()){
            for(Row row : rs_emailCheck){ 

                String username = row.getString("login");
                String StoredPass = row.getString("password");
                if (StoredPass.compareTo(EncodedPassword) == 0){return username;}

            }
        }
        return null;   
    }
    
    /**
     * Retrieves a set of username that a particular user follows
     * @param username - The follower's username
     * @return - Set of usernames
     */
    public Set<String> getFollowing(String username){
        Session session = cluster.connect("instagrim");
        Set<String> sFollowing = null;
        
        PreparedStatement ps_selectFollowing = session.prepare("select following from userprofiles where login = ?");
        BoundStatement bs_selectFollowing = new BoundStatement(ps_selectFollowing);
        ResultSet rs_selectFollowing;
        
        rs_selectFollowing = session.execute(bs_selectFollowing.bind(username));
        
        if(!rs_selectFollowing.isExhausted()){
            for(Row row : rs_selectFollowing){
                 sFollowing = row.getSet("following", String.class);
            }
        }
        
        return sFollowing;   
    }
    
    /**
     * Checks if a user has another user in their following set
     * @param following - The username to check for in the set
     * @param follower - The username to grab the set for
     * @return - Whether or not the username appears in the user's following set
     */
    public boolean checkIfFollowing(String following, String follower){
        
        //Gets the set of following for the user
        Set<String> sFollowing = getFollowing(follower);
        //Iterates through each username in the set and checks if it matches
        for(Iterator<String> i = sFollowing.iterator(); i.hasNext();){
            if(i.next().equals(following)){
                return true;
            }
        }
            
        return false;
    }
    
    /**
     * Adds a username to a user's following set
     * @param following - username to add to the set
     * @param follower  - The username for the set to be added to
     */
    public void followUser(String following, String follower){
        Session session = cluster.connect("instagrim");
        String email = getEmailForUser(follower, session);
        
        String followUser = "update userprofiles set following = following + {'" + following + "'} where login = ? and email = ?";
        PreparedStatement ps_followUser = session.prepare(followUser);
        BoundStatement bs_followUser = new BoundStatement(ps_followUser);
        
        session.execute(bs_followUser.bind(follower, email)); 
    }
    
    /**
     * Removes a username from a user's following set
     * @param following - username to be removed from set
     * @param follower - username for the set to be removed from
     */
    public void unFollowUser(String following, String follower){
        Session session = cluster.connect("instagrim");
        String email = getEmailForUser(follower, session);
        
        String followUser = "update userprofiles set following = following - {'" + following + "'} where login = ? and email = ?";
        PreparedStatement ps_followUser = session.prepare(followUser);
        BoundStatement bs_followUser = new BoundStatement(ps_followUser);
        
        session.execute(bs_followUser.bind(follower, email)); 
    }
    
    /**
     * Sets the profile pic field for a user to the uuid for their profile picture
     * @param user - username for the user to set picture for
     * @param picid - UUID for the profile picture
     */
    public void setProfilePic(String user, UUID picid){
        Session session = cluster.connect("instagrim");
        String email = getEmailForUser(user, session);
        
        PreparedStatement ps_insertProfilePic = session.prepare("update userprofiles set profile_pic = ? where login = ? and email = ?");
        BoundStatement bs_insertProfilePic = new BoundStatement(ps_insertProfilePic);
        
        session.execute(bs_insertProfilePic.bind(picid,user,email));
    }
    
    /**
     * Gets the UUID for a user's profile picture
     * @param username - username to get the profile picture from
     * @return - Picture object with the UUID for the profile picture
     */
    public Pic getProfilePic(String username){
       Session session = cluster.connect("instagrim");
       Pic profilePic = new Pic();
       
       PreparedStatement ps_selectProfilePic = session.prepare("select profile_pic from userprofiles where login =?");
       ResultSet rs_selectProfilePic = null;
       BoundStatement bs = new BoundStatement(ps_selectProfilePic);
       
       rs_selectProfilePic = session.execute(bs.bind(username));
       
       if(rs_selectProfilePic.isExhausted()){
           System.out.println("No Profile Picture");
           return null;
       }
       else{
           for(Row row : rs_selectProfilePic){;
               UUID uuid = row.getUUID("profile_pic");
               if(uuid == null){
                   return null;
               }
               System.out.println("Profile Pic UUID : " + uuid.toString());
               profilePic.setUUID(uuid);
           }
       }
       
       return profilePic;      
   }
    
    /**
     * Takes a search string and returns all user's who's usernames contain the string
     * @param searchString - string to be searched
     * @return - List of users who match the search string
     */
    public LinkedList<LoggedIn> searchUsers(String searchString){
        Session session = cluster.connect("instagrim");
        LinkedList<LoggedIn> lsUsers = new LinkedList<>();
        ResultSet rs_searchResults;
        
        rs_searchResults = session.execute("select login, profile_pic from userprofiles");
        
        if(rs_searchResults.isExhausted()){
            return null;
        }
        else{
            for(Row row : rs_searchResults){
                String username = row.getString("login").toLowerCase();
                UUID profilePic = row.getUUID("profile_pic");
                if(username.toLowerCase().contains(searchString)){
                    LoggedIn user = new LoggedIn();
                    user.setUsername(username);
                    user.setProfilePic(profilePic);
                    lsUsers.add(user);
                }
            }
        }
        
        return lsUsers;       
    }
       
    /**
     * Gets the email address for a user
     * @param user - username for the user
     * @param session
     * @return - email address for the user
     */
    public String getEmailForUser(String user, Session session){      
        PreparedStatement ps_selectEmail = session.prepare("select email from userprofiles where login = ?");
        BoundStatement bs_selectEmail = new BoundStatement(ps_selectEmail);
        ResultSet rs_selectEmail = null;
        String email = null;
        
        rs_selectEmail = session.execute(bs_selectEmail.bind(user));
        
        if(rs_selectEmail.isExhausted()){
            System.out.println("User does not exist");
        }
        else{
            for(Row row : rs_selectEmail){
                 email = row.getString("email");
            }
        }
        
        return email;
  }

    
}
