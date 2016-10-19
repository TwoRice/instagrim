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
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;
import java.util.Iterator;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.stores.Comment;

/**
 *
 * @author Big Cheesy B
 */
public class CommentModel {

    Cluster cluster;
    
    public CommentModel(){
        
    }
    
    public void setCluster(Cluster cluster){       
        this.cluster=cluster;      
    }
    
    public void postNewComment(UUID picID, String user, String comment){
        Session session = cluster.connect("instagrim");
        UUID uuid = Convertors.getTimeUUID();
    
        PreparedStatement ps_insertComment = session.prepare("insert into comment (commentid, user, comment, comment_added) values (?,?,?,?)");
        String insertCommentID = "update pics set comments = comments + {" + uuid + "} where picid = ?";
        PreparedStatement ps_insertCommentID = session.prepare(insertCommentID);
        BoundStatement bs_insertComment = new BoundStatement(ps_insertComment);
        BoundStatement bs_insertCommentID = new BoundStatement(ps_insertCommentID);        
        
        Date postTime = new Date();
        session.execute(bs_insertComment.bind(uuid, user, comment, postTime));
        session.execute(bs_insertCommentID.bind(picID));
              
    }
    
    public LinkedList<UUID> getCommentsFromPic(UUID picID){
        Session session = cluster.connect("instagrim");
        LinkedList<UUID> lsComments = new LinkedList<>();

        PreparedStatement ps_selectComments = session.prepare("select comments from pics where picid = ?");
        BoundStatement bs_selectComments = new BoundStatement(ps_selectComments);
        ResultSet rs_selectComments = null;

        rs_selectComments = session.execute(bs_selectComments.bind(picID));

        if(rs_selectComments.isExhausted()){
            return null;
        }
        else{
            for(Row row : rs_selectComments){
                Set<UUID> sComments = row.getSet("comments", UUID.class);
                for(Iterator<UUID> i = sComments.iterator(); i.hasNext();){
                    UUID commentID = i.next();
                    lsComments.add(commentID);
                }
            } 
            return lsComments;   
        }
    }
    
    public LinkedList<Comment> getCommentsFromID(LinkedList<UUID> lsCommentIDs){
        Session session = cluster.connect("instagrim");
        LinkedList<Comment> lsComments = new LinkedList<>();
        
        PreparedStatement ps_selectComments = session.prepare("select user, comment, comment_added from comment where commentid = ?");
        BoundStatement bs_selectComments = new BoundStatement(ps_selectComments);
        ResultSet rs_selectComments = null;
        
        for(Iterator<UUID> i = lsCommentIDs.iterator(); i.hasNext();){
            UUID commentID = i.next();
            rs_selectComments = session.execute(bs_selectComments.bind(commentID));
            
            if(rs_selectComments.isExhausted()){
                System.out.println("Comment could not be found");
            }
            else{
                for(Row row : rs_selectComments){
                    Comment comment = new Comment();
                    String user = row.getString("user");
                    String commentText = row.getString("comment");
                    Date timestamp = row.getTimestamp("comment_added");
                    
                    System.out.println("inserting " + commentID);
                    comment.setUUID(commentID);
                    comment.setUser(user);
                    comment.setComment(commentText);
                    comment.setTimestamp(timestamp);
                    
                    lsComments.add(comment);
                    
                }
            }
        }       
        return lsComments;     
    }
    
}
