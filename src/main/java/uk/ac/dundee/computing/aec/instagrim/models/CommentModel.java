package uk.ac.dundee.computing.aec.instagrim.models;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import java.util.Date;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;
import java.util.Iterator;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.stores.Comment;

/**
 * Model which communicates to the database for all operations on the comment table
 * @author Big Cheesy B
 */
public class CommentModel {

    Cluster cluster;
    
    public CommentModel(){
        
    }
    
    public void setCluster(Cluster cluster){       
        this.cluster=cluster;      
    }
    
    /**
     * Method to add a comment to the comment table and the uuid for that comment to the
     * appropriate pic in the pic table
     * @param picID - ID for the picture the comment is being added to
     * @param user - Username for the user who posted the comment
     * @param comment - The content of the comment
     */
    public void postNewComment(UUID picID, String user, String comment){
        Session session = cluster.connect("instagrim");
        //Generates a new UUID for the comment
        UUID uuid = Convertors.getTimeUUID();
    
        PreparedStatement ps_insertComment = session.prepare("insert into comment (commentid, user, comment, comment_added) values (?,?,?,?)");
        String insertCommentID = "update pics set comments = comments + {" + uuid + "} where picid = ?";
        PreparedStatement ps_insertCommentID = session.prepare(insertCommentID);
        BoundStatement bs_insertComment = new BoundStatement(ps_insertComment);
        BoundStatement bs_insertCommentID = new BoundStatement(ps_insertCommentID);        
        
        //Gets a timestamp for the comment
        Date postTime = new Date();
        session.execute(bs_insertComment.bind(uuid, user, comment, postTime));
        session.execute(bs_insertCommentID.bind(picID));          
    }
    
    /**
     * Method to retrieve all the comments for a picture and return them as a linked list
     * @param picID - ID for the picture to retrieve all the comments from
     * @return - a linked list of all the comment UUIDs
     */
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
    
    /**
     * Method which takes a list of comment UUIDs and retrieves all the comment data for each comment ID
     * from the comment table and adds each as a comment object to a linked list
     * @param lsCommentIDs - List of comment UUIDs
     * @return - List of Comment objects
     */
    public LinkedList<Comment> getCommentsFromID(LinkedList<UUID> lsCommentIDs){
        Session session = cluster.connect("instagrim");
        LinkedList<Comment> lsComments = new LinkedList<>();
        
        PreparedStatement ps_selectComments = session.prepare("select user, comment, comment_added from comment where commentid = ?");
        BoundStatement bs_selectComments = new BoundStatement(ps_selectComments);
        ResultSet rs_selectComments = null;
        
        //Runs the select statement for each UUID in the lsCommentIDs list
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
