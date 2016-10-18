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
    
    public LinkedList<Comment> getCommentsFromPic(UUID picID){
        Session session = cluster.connect("instagrim");
        LinkedList<Comment> lsComments = new LinkedList<>();

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
                    Comment comment = new Comment();
                    UUID commentID = i.next();
                    comment.setUUID(commentID);
                    lsComments.add(comment);
                }
            } 
            return lsComments;   
        }
    }
    
}
