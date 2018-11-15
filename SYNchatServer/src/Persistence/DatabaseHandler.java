package Persistence;

import Acquaintance.IFriends;
import Acquaintance.ILogin;
import Acquaintance.IManagement;
import Acquaintance.IProfile;
import Acquaintance.IUser;
import Acquaintance.Nationality;
import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseHandler {

    static String url = "jdbc:postgresql://tek-mmmi-db0a.tek.c.sdu.dk:5432/si3_2018_group_9_db";
    static String dbUsername = "si3_2018_group_9";
    static String dbPassword = "copt22*viols";

    /**
     * A template for a sql querry, change the return type and use of prerared
     * statement as needed
     */
    /*
    private void queryTemplate(){
        try(Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)){
            Class.forName("org.postgresql.Driver");
            
            Statement st = conn.createStatement();
            String sql = "Do sql here";

            ResultSet rs = st.executeQuery(sql);
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     */
    ILogin Login(ILogin login) {
        //User only for initializing purposes
        PerUser user = new PerUser(-1, true, -1, null);

        List<Integer> tmpList = new ArrayList<Integer>();

     //  int userid = -1;
        String password = "";
        int countChats = -1;

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            Class.forName("org.postgresql.Driver");

            PreparedStatement st1 = conn.prepareStatement("SELECT users.userid, users.password FROM synchat.users WHERE users.mail = ?;");
            st1.setString(1, login.gethMail());

            ResultSet rs1 = st1.executeQuery();
            while (rs1.next()) {
                user.setUserID(rs1.getInt("userid"));
                password = rs1.getString("password");
                
            }
            if (login.gethPW().equals(password)) {
                PreparedStatement st2 = conn.prepareStatement("SELECT * FROM synchat.users "
                        + "WHERE users.userid = ?;");

                
                
                //   st2.setString(1, login.gethMail());
                st2.setInt(1, user.getUserID());

                ResultSet rs2 = st2.executeQuery();
                while (rs2.next()) {

                    user.setBanned(rs2.getBoolean("banned"));
                    user.setReports(rs2.getInt("reportcount"));
                    user.setUserID(rs2.getInt("userid"));
                }

                PreparedStatement st3 = conn.prepareStatement("SELECT user_chats.chatid FROM synchat.user_chats WHERE user_chats.userid = ?;");

                st3.setInt(1, user.getUserID());
                ResultSet rs3 = st3.executeQuery();
                
                    while (rs3.next()) {
                        tmpList.add(rs3.getInt("chatid"));
                    }
                    user.setChats(tmpList);
                    System.out.println("users userID: " + user.getUserID());
                    login.setUser(user);
                
                    
                    IProfile returnProfile = getProfile(user.getUserID());
                    IFriends returnFriends = getFriends(user.getUserID());
                    
                    user.setProfile(returnProfile);
                    user.setFriends(returnFriends);
                    
                   // ILogin tempLog = new Login(2, returnUser);
                    login.setLoginvalue(2);
                    System.out.println("About to return login with: " + login.getUser().getUserID());
                    return login;
                

            } else {

                login.setLoginvalue(1);
                return login;
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        login.setLoginvalue(0);
        return login;
    }

    private IProfile getProfile(int userID) {
        
        
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            IProfile profile = (IProfile) new PerProfile("", "", Nationality.Denmark);
            Class.forName("org.postgresql.Driver");

            PreparedStatement st = conn.prepareStatement("Select profiles.firstname, profiles.lastname, profiles.profile_text, profiles.nationality, profiles.picture_reference FROM synchat.profiles WHERE profiles.userid = ?;");
            st.setInt(1, userID);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                profile.setFirstName(rs.getString("firstname"));
                profile.setLastName(rs.getString("lastname"));
                profile.setProfileText(rs.getString("profile_text"));
                String tmpNationolaty = rs.getString("nationality");
                profile.setPicture(rs.getString("picture_reference"));
                
                profile.setNationality(Nationality.valueOf(tmpNationolaty));
            }
            
            return profile;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    boolean createUser(ILogin login) {
        boolean createBoolean = false;
        boolean createProfileBoolean = false;
       // Map<Integer, String> friendList;
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            PreparedStatement st0 = conn.prepareStatement("Select * FROM Synchat.users WHERE users.mail = ?;");
            st0.setString(1, login.gethMail());
            ResultSet rs0 = st0.executeQuery();
            if (!rs0.next()) {
                PreparedStatement st1 = conn.prepareStatement("INSERT INTO SYNCHAT.users (mail, password) VALUES(?,?)");
                st1.setString(1, login.gethMail());
                st1.setString(2, login.gethPW());

                st1.executeUpdate();
                createBoolean = true;
            }
            int userID = getUserIDfromDB(login.gethMail());
            createProfileBoolean = createProfile(login, userID);
            //friendList = getFriends(userID);
            

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (createBoolean && createProfileBoolean);
    }
    
    
      

    private boolean createProfile(ILogin login, int profileID) {

        boolean success = false;
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            PreparedStatement st0 = conn.prepareStatement("Select * FROM Synchat.profiles WHERE profiles.userid = ?;");
            st0.setInt(1, login.getUser().getUserID());
            ResultSet rs0 = st0.executeQuery();
            if (!rs0.next()) {
                PreparedStatement st1 = conn.prepareStatement("INSERT INTO SYNCHAT.profiles (firstname, lastname, nationality, userid, profile_text, picture_reference) VALUES(?,?,?,?,?,?)");
                st1.setString(1, login.getUser().getProfile().getFirstName());
                st1.setString(2, login.getUser().getProfile().getLastName());
                st1.setString(3, login.getUser().getProfile().getNationality().toString());
                st1.setInt(4, profileID);
                st1.setString(5, "");
                st1.setString(6, "src/Assets/Avatar_0.png");

                st1.executeUpdate();
                success = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return success;
    }

    private int getUserIDfromDB(String hashedMail) {
        int userID = -1;

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            PreparedStatement st0 = conn.prepareStatement("Select * FROM Synchat.users WHERE users.mail = ?;");
            st0.setString(1, hashedMail);
            ResultSet rs0 = st0.executeQuery();
            if (rs0.next()) {
                userID = rs0.getInt("userid");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userID;
    }

    boolean verify(IManagement management, int userID) {
        
        boolean returnstatus = false;

        if(management.getAction() == 0){
            System.out.println("Checking pw");
            try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            Class.forName("org.postgresql.Driver");

            PreparedStatement st = conn.prepareStatement("SELECT users.password FROM SYNchat.users WHERE users.userid = ?;");
            st.setInt(1, (userID));

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                if (rs.getString("password").equals(management.getPw())) {
                    //if passwords match return 1 for success.
                    returnstatus = true;
                } else {
                    returnstatus = false;
                }
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        else if(management.getAction() == 1){
            System.out.println("Checking mail");
            try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            Class.forName("org.postgresql.Driver");

            PreparedStatement st = conn.prepareStatement("SELECT users.mail FROM SYNchat.users WHERE users.userid = ?;");
            st.setInt(1, (userID));

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                if (rs.getString("mail").equals(management.getMail())) {
                    //if passwords match return 1 for success.
                    returnstatus = true;
                } else {
                    returnstatus = false;
                }
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        
       
        return returnstatus;
    }

    boolean changePw(IManagement management, int userID) {
      //  int passwordverification = verifyPw(userID, management.getPw());
        boolean returnstatus = false;

    
                //password success, edit database entries
                try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
                    Class.forName("org.postgresql.Driver");

                    PreparedStatement st = conn.prepareStatement("UPDATE SYNchat.users SET password = ? WHERE users.userid = ?;");
                    st.setString(1, management.getPw());
                    st.setInt(2, (userID));

                    st.executeUpdate();
                    returnstatus = true;

                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
           
        return returnstatus;
    }

    boolean changeMail(IManagement management, int userID) {
        boolean returnStatus = false;

      
                //password success, edit database entries
                try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
                    Class.forName("org.postgresql.Driver");

                    PreparedStatement st = conn.prepareStatement("UPDATE SYNchat.users SET mail = ? WHERE users.userid = ?;");
                    st.setString(1, management.getMail());
                    st.setString(2, (userID + ""));

                    st.executeUpdate();
                    //success return 1.
                    returnStatus = true;

                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
        return returnStatus;
    }

    boolean alterProfile(IUser user) {
        Boolean updateBoolean = false;
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            Class.forName("org.postgresql.Driver");
            PreparedStatement st0 = conn.prepareStatement("UPDATE synchat.profiles SET firstname = '?', lastname = '?', nationality = '?', profiletext = '?', picture_reference = '?' WHERE synchat.userid = ?;");
            st0.setString(1, user.getProfile().getFirstName());
            st0.setString(2, user.getProfile().getLastName());
            st0.setString(3, user.getProfile().getNationality().toString());
            st0.setString(4, user.getProfile().getProfileText());
            st0.setInt(5, (user.getUserID()));
            st0.setString(6, user.getProfile().getPicture());

            st0.executeUpdate();
            updateBoolean = true;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return updateBoolean;
    }
    
    private IFriends getFriends(int userID){
        
      
        List<Integer> friends = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {

            PreparedStatement st1 = conn.prepareStatement("SELECT friend_lists.friendid FROM synchat.friend_lists WHERE userid = ?;");
            st1.setInt(1, userID);

            ResultSet rs = st1.executeQuery();

//            if(rs.next() == false){
//                friends.put(userID, "initialFriend");
//            }
            
            while(rs.next()){
             //   friends.addFriend(rs.getInt("friendid"),rs.getString("friendName"));
                friends.add(rs.getInt("friendid"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         PerFriends returnFriends = new PerFriends(friends);
        
        return returnFriends;
    }


    boolean addFriend(int userID, int newFriendID) {
        Boolean createBoolean = false;
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            PreparedStatement st0 = conn.prepareStatement("INSERT INTO SYNCHAT.friend_lists (userid, friendid) VALUES(?,?)");
            st0.setInt(1, userID);
            st0.setInt(2, newFriendID);
            st0.executeUpdate();
            PreparedStatement st1 = conn.prepareStatement("INSERT INTO SYNCHAT.friend_lists (userid, friendid) VALUES(?,?)");
            st1.setInt(1, newFriendID);
            st1.setInt(2, userID);
            st1.executeUpdate();
            createBoolean = true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return createBoolean;
    }

    boolean removeFriend(int userID, int oldFriendID) {
        Boolean createBoolean = false;
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            PreparedStatement st0 = conn.prepareStatement("DELETE FROM SYNCHAT.friend_lists WHERE userid = ? AND friendid = ?");
            st0.setInt(1, userID);
            st0.setInt(2, oldFriendID);
            st0.executeUpdate();
            PreparedStatement st1 = conn.prepareStatement("DELETE FROM SYNCHAT.friend_lists WHERE userid = ? AND friendid = ?");
            st1.setInt(1, oldFriendID);
            st1.setInt(2, userID);
            st1.executeUpdate();
            createBoolean = true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return createBoolean;
    }
}
