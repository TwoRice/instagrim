package uk.ac.dundee.computing.aec.instagrim.lib;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.*;

public final class Keyspaces {

    public Keyspaces() {

    }

    public static void SetUpKeySpaces(Cluster c) {
        try {
            //Add some keyspaces here
            String createkeyspace = "create keyspace if not exists instagrim  WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}";
            String CreatePicTable = "CREATE TABLE if not exists instagrim.Pics ("
                    + "user varchar,\n"
                    + "picid uuid,\n"
                    + "interaction_time timestamp,\n"
                    + "title varchar,\n"
                    + "image blob,\n"
                    + "thumb blob,\n"
                    + "processed blob,\n"
                    + "imagelength int,\n"
                    + "thumblength int,\n"
                    + "processedlength int,\n"
                    + "type  varchar,\n"
                    + "name  varchar,\n"
                    + "comments set<uuid>,\n"
                    + " PRIMARY KEY (picid)\n"
                    + ")";
            String CreateCommentTable = "CREATE TABLE if not exists instagrim.comment ("
                    + "commentid uuid,\n"
                    + "user varchar,\n"
                    + "comment text,\n"
                    + "comment_added timestamp,\n"
                    + "PRIMARY KEY (commentid)\n"
                    + ");";
            String Createuserpiclist = "CREATE TABLE if not exists instagrim.userpiclist (\n"
                    + "picid uuid,\n"
                    + "user varchar,\n"
                    + "pic_added timestamp,\n"
                    + "private boolean,\n"
                    + "PRIMARY KEY (user,pic_added)\n"
                    + ") WITH CLUSTERING ORDER BY (pic_added desc);";
            String CreateUserProfile = "CREATE TABLE if not exists instagrim.userprofiles (\n"
                    + "      login text,\n"
                    + "      password text,\n"
                    + "      first_name text,\n"
                    + "      last_name text,\n"
                    + "      email text,\n"
                    + "      profile_pic uuid,\n"
                    + "      following set<text>,\n"
                    + "      PRIMARY KEY(login,email)\n"
                    + "  );";
            Session session = c.connect();
            try {
                PreparedStatement statement = session
                        .prepare(createkeyspace);
                BoundStatement boundStatement = new BoundStatement(
                        statement);
                ResultSet rs = session
                        .execute(boundStatement);
                System.out.println("created instagrim ");
            } catch (Exception et) {
                System.out.println("Can't create instagrim " + et);
            }

            //now add some column families 
            System.out.println("" + CreatePicTable);

            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreatePicTable);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create pic table " + et);
            }
            System.out.println("" + Createuserpiclist);
            
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateCommentTable);
                session.execute(cqlQuery);
            }
            catch (Exception et){
                System.out.println("Can't create comment table " + et);
            }
            System.out.println("" + CreateCommentTable);

            try {
                SimpleStatement cqlQuery = new SimpleStatement(Createuserpiclist);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create user pic list table " + et);
            }
            System.out.println("" + CreateUserProfile);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateUserProfile);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create user profiles table " + et);
            }
            session.close();

        } catch (Exception et) {
            System.out.println("Other keyspace or coulm definition error" + et);
        }

    }
}
