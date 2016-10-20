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
import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 *
 * @author Administrator
 */
public class User {
    Cluster cluster;
    public User(){
        
    }
    
    public boolean isValidUserName(String username){
        
        Session session = cluster.connect("instagrim");
        ResultSet rs_usernameExists = null;
        
        rs_usernameExists = session.execute("select login from userprofiles");
        
        if(!rs_usernameExists.isExhausted()){
            for(Row row : rs_usernameExists){
                String storedUsername = row.getString("login").toLowerCase();
                username = username.toLowerCase();
                
                if(username.equals(storedUsername)){
                    return false;
                }
            }
        }
        
        return true;              
    }
    
    public boolean isValidEmail(String email){
        
        Session session = cluster.connect("instagrim");
        ResultSet rs_emailExists = null;
        
        rs_emailExists = session.execute("select email from userprofiles");
        
        if(!rs_emailExists.isExhausted()){
            for(Row row : rs_emailExists){
                String storedEmail = row.getString("email").toLowerCase();
                email = email.toLowerCase();
                
                if(email.equals(storedEmail)){
                    return false;
                }
            }
        }
        
        return true; 
        
    }
    
    public boolean RegisterUser(String username, String Password, String firstName, String lastName, String email){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
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
    
    public boolean loginWithUsername(String username, String Password){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
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
        if(!rs_usernameCheck.isExhausted()) {
            for (Row row : rs_usernameCheck) {
                
                String StoredPass = row.getString("password");
                if (StoredPass.compareTo(EncodedPassword) == 0){return true;}
                
            }
        }
       
        return false;  
    }
    
    public String loginWithEmail(String email, String password){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
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

        if(!rs_emailCheck.isExhausted()){
            for(Row row : rs_emailCheck){ 

                String username = row.getString("login");
                String StoredPass = row.getString("password");
                if (StoredPass.compareTo(EncodedPassword) == 0){return username;}

            }
        }
        return null;   
    }
    
    public boolean checkIfFollowing(String following, String follower){
        Session session = cluster.connect("instagrim");
        
        PreparedStatement ps_selectFollowing = session.prepare("select following from userprofiles where login = ?");
        BoundStatement bs_selectFollowing = new BoundStatement(ps_selectFollowing);
        ResultSet rs_selectFollowing;
        
        rs_selectFollowing = session.execute(bs_selectFollowing.bind(follower));
        
        if(!rs_selectFollowing.isExhausted()){
            for(Row row : rs_selectFollowing){
                Set<String> sFollowing = row.getSet("following", String.class);
                for(Iterator<String> i = sFollowing.iterator(); i.hasNext();){
                    if(i.next().equals(following)){
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public void followUser(String following, String follower){
        Session session = cluster.connect("instagrim");
        String email = getEmailForUser(follower, session);
        
        String followUser = "update userprofiles set following = following + {'" + following + "'} where login = ? and email = ?";
        PreparedStatement ps_followUser = session.prepare(followUser);
        BoundStatement bs_followUser = new BoundStatement(ps_followUser);
        
        session.execute(bs_followUser.bind(follower, email)); 
    }
    
       public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
       
    public void unFollowUser(String following, String follower){
        Session session = cluster.connect("instagrim");
        String email = getEmailForUser(follower, session);
        
        String followUser = "update userprofiles set following = following - {'" + following + "'} where login = ? and email = ?";
        PreparedStatement ps_followUser = session.prepare(followUser);
        BoundStatement bs_followUser = new BoundStatement(ps_followUser);
        
        session.execute(bs_followUser.bind(follower, email)); 
    }
    
    public void setProfilePic(String user, UUID picid){
        Session session = cluster.connect("instagrim");
        String email = getEmailForUser(user, session);
        
        PreparedStatement ps_insertProfilePic = session.prepare("update userprofiles set profile_pic = ? where login = ? and email = ?");
        BoundStatement bs_insertProfilePic = new BoundStatement(ps_insertProfilePic);
        
        session.execute(bs_insertProfilePic.bind(picid,user,email));
    }
    
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
