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
    
    public boolean IsValidUser(String username, String Password){
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
        if (rs_usernameCheck.isExhausted()) {
            
            PreparedStatement ps_emailCheck = session.prepare("select password from userprofiles where email =? allow filtering");
            BoundStatement bs = new BoundStatement(ps_emailCheck);
            ResultSet rs_emailCheck = session.execute(bs.bind(username));
            
            if(!rs_emailCheck.isExhausted()){
                for(Row row : rs_emailCheck){ 
                    
                    String StoredPass = row.getString("password");
                    if (StoredPass.compareTo(EncodedPassword) == 0){return true;}
                    
                }
            }              
        } else {
            for (Row row : rs_usernameCheck) {
                
                String StoredPass = row.getString("password");
                if (StoredPass.compareTo(EncodedPassword) == 0){return true;}
                
            }
        }
       
    return false;  
    }
       public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    
}
