package Persistence;

import Acquaintance.IChatHistory;
import Acquaintance.ILogin;
import Acquaintance.IManagement;
import Acquaintance.IMessage;
import Acquaintance.IPrivateChat;
import Acquaintance.IProfile;
import Acquaintance.IUser;
import Acquaintance.Nationality;
import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.postgresql.util.PSQLException;

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

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            Class.forName("org.postgresql.Driver");

            PreparedStatement st = conn.prepareStatement("Select * FROM Synchat.users WHERE users.mail = ?;");
            st.setString(1, login.gethMail());

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                if (login.gethPW().equals(rs.getString("password"))) {
                    // for converting sqlarray to int List
                    /*
                    List<Integer> tmpList = new ArrayList<>();
                    Array chats = rs.getArray("chats");
                    try {
                        Integer[] intChats = (Integer[]) chats.getArray();
                        for (int i = 0; i < intChats.length; i++) {
                            tmpList.add(intChats[i]);
                        }
                    } catch (PSQLException | NullPointerException ex) {
                    }
                     IUser returnUser = new PerUser(rs.getInt("userID"), rs.getBoolean("banned"), rs.getInt("reportcount"), tmpList);
                     */

                    PerUser returnUser = new PerUser(rs.getInt("userID"), rs.getBoolean("banned"), rs.getInt("reportcount"), null);
                    IProfile returnProfile = getProfile(returnUser.getUserID());
                    returnUser.setProfile(returnProfile);
                    ILogin tempLog = new Login(2, returnUser);
                    return tempLog;
                } else {
                    ILogin tempLog = new Login(1, null);
                    return tempLog;
                }

            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        ILogin tempLog = new Login(0, null);
        return tempLog;
    }

    private IProfile getProfile(int userID) {
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            IProfile profile = (IProfile) new PerProfile("", "", Nationality.Denmark);
            Class.forName("org.postgresql.Driver");

            PreparedStatement st = conn.prepareStatement("Select profiles.firstname, profiles.lastname, profiles.profile_text, profiles.nationality FROM Synchat.profiles WHERE profiles.userid = ?;");
            st.setInt(1, userID);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                profile.setFirstName(rs.getString("firstname"));
                profile.setLastName(rs.getString("lastname"));
                profile.setProfileText(rs.getString("profiletext"));
                String tmpNationolaty = rs.getString("nationality");
                profile.setNationality(Nationality.valueOf(tmpNationolaty));
            }

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
            int profileID = getUserIDfromDB(login.gethMail());
            createProfileBoolean = createProfile(login, profileID);

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return createBoolean && createProfileBoolean;
    }

    private boolean createProfile(ILogin login, int profileID) {

        boolean success = false;
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            PreparedStatement st0 = conn.prepareStatement("Select * FROM Synchat.profiles WHERE profiles.userid = ?;");
            st0.setInt(1, login.getUser().getUserID());
            ResultSet rs0 = st0.executeQuery();
            if (!rs0.next()) {
                PreparedStatement st1 = conn.prepareStatement("INSERT INTO SYNCHAT.profiles (firstname, lastname, nationality, userid) VALUES(?,?,?,?)");
                st1.setString(1, login.getUser().getProfile().getFirstName());
                st1.setString(2, login.getUser().getProfile().getLastName());
                st1.setString(3, login.getUser().getProfile().getNationality().toString());
                st1.setInt(4, profileID);

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

    int verifyPw(int userID, String hashedPw) {
        int returnstatus = 0;

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            Class.forName("org.postgresql.Driver");

            PreparedStatement st = conn.prepareStatement("SELECT users.password FROM SYNchat.users WHERE users.userid = ?;");
            st.setString(1, (userID + ""));

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                if (rs.getString("password").equals(hashedPw.toString())) {
                    //if passwords match retur 1 for success.
                    returnstatus = 1;
                } else if (!rs.getString("password").equals(hashedPw.toString())) {
                    returnstatus = 3;
                } else {
                    returnstatus = 2;
                }
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        //If password is incorrect retur 2 for error
        return returnstatus;

        //Return 1 = success;
        //return 2 = unknown error with verifcation;
        //return 3 = Password does no match and access is denied;
    }

    int changePw(IManagement management) {
        int passwordverification = verifyPw(management.getUserID(), management.getPw());
        int returnstatus = 2;

        switch (passwordverification) {
            case 1:
                //password success, edit database entries
                try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
                    Class.forName("org.postgresql.Driver");

                    PreparedStatement st = conn.prepareStatement("UPDATE SYNchat.users SET password = ? WHERE users.userid = ?;");
                    st.setString(1, management.getString1());
                    st.setString(2, (management.getUserID() + ""));

                    st.executeUpdate();
                    //success retrun 1.
                    returnstatus = 1;

                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case 2:
                //password verification failed
                returnstatus = 4;
                break;
            case 3:
                //Passwords did not match
                returnstatus = 3;
                break;
            default:
                returnstatus = 2;
        }

        return returnstatus;
    }

    int changeMail(IManagement management) {
        int passwordverification = verifyPw(management.getUserID(), management.getPw());
        int returnStatus = 2;

        switch (passwordverification) {
            case 1:
                //password success, edit database entries
                try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
                    Class.forName("org.postgresql.Driver");

                    PreparedStatement st = conn.prepareStatement("UPDATE SYNchat.users SET mail = ? WHERE users.userid = ?;");
                    st.setString(1, management.getString1());
                    st.setString(2, (management.getUserID() + ""));

                    st.executeUpdate();
                    //success return 1.
                    returnStatus = 1;

                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case 2:
                //password verification faliure
                returnStatus = 4;
                break;
            case 3:
                //passwords did not match
                returnStatus = 3;
                break;
            default:
                returnStatus = 2;
        }
        return returnStatus;
    }

    boolean alterProfile(IUser user) {
        Boolean updateBoolean = false;
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            Class.forName("org.postgresql.Driver");
            PreparedStatement st0 = conn.prepareStatement("UPDATE synchat.profiles SET firstname = '?', lastname = '?', nationality = '?', profiletext = '?' WHERE synchat.userid = ?;");
            st0.setString(1, user.getProfile().getFirstName());
            st0.setString(2, user.getProfile().getLastName());
            st0.setString(3, user.getProfile().getNationality().toString());
            st0.setString(4, user.getProfile().getProfileText());
            st0.setString(5, (user.getUserID() + ""));

            st0.executeUpdate();
            updateBoolean = true;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return updateBoolean;
    }

    IPrivateChat createNewPrivateChat(IPrivateChat prichat) {

        // convert the message timestamp to java.sql.Timestamp datatype
        Timestamp time = Timestamp.from(prichat.getCh().getMsgList().get(0).getTimestamp());

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {

            // creating chatid with name
            int chatID = -1;
            PreparedStatement st2 = conn.prepareStatement("INSERT INTO SYNCHAT.chatinfo (chatname) VALUES(?)", PreparedStatement.RETURN_GENERATED_KEYS);
            st2.setString(1, prichat.getName());
            
            st2.executeUpdate();
            ResultSet rs2 = st2.getGeneratedKeys();
            if (rs2.next()) {
                chatID = rs2.getInt(1);
            }


            // inserting the table with most values first, so db can assign chatID with lowest chance of duplicates
            PreparedStatement st0 = conn.prepareStatement("INSERT INTO SYNCHAT.chatmessages (chatid, message, timestamp, senderid) VALUES(?,?,?,?)");
            st0.setInt(1, chatID);
            st0.setString(2, prichat.getCh().getMsgList().get(0).getContext());
            st0.setTimestamp(3, time);
            st0.setInt(4, prichat.getCh().getMsgList().get(0).getSenderID());

            st0.executeUpdate();


            // iterating for all users participating in this chat, storing assigned chatID with the connected userIDs
            for (Integer userID : prichat.getUserIDs()) {
                PreparedStatement st1 = conn.prepareStatement("INSERT INTO SYNCHAT.userchats (chatid, userid) VALUES(?,?)");

                st1.setInt(1, chatID);
                st1.setInt(2, userID);

                st1.executeUpdate();
            }

            

            // creating new (updated) IPrivateChat for the return
            IPrivateChat newchat = loadPrivateChat(chatID, 1);
            return newchat;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * chatID for the wanted chat, loadMsgCount for amount of messages loaded (I
     * believe loadMsgCount can be higher than amount of messages in chat, with
     * no errors)
     *
     * @param chatID
     * @param loadMsgCount
     * @return
     */
    IPrivateChat loadPrivateChat(int chatID, int loadMsgCount) {
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {

            // all the variables needed to create IPrivateChat (including IMessages and IChatHistory)
            List<Integer> userIDs = new ArrayList<>();
            String name = "";
            List<IMessage> msgList = new ArrayList<>();
            int senderID;
            Timestamp time;
            String text;

            // **** denne skal laves om - sortér messages efter timestamp, og indlæs det øverste antal baseret på loadMsgCount
            PreparedStatement st3 = conn.prepareStatement("SELECT TOP ? * FROM SYNCHAT.chatmessages WHERE chatid = ? ORDER BY timestamp DESC;");
            st3.setInt(1, loadMsgCount);
            st3.setInt(2, chatID);

            ResultSet rs3 = st3.executeQuery();

            // iterating to get the last x=msgLoadedCount messages added to msgList
            while (rs3.next()) {
                text = rs3.getString("message");
                senderID = rs3.getInt("senderid");
                time = rs3.getTimestamp("timestamp");   // this is converted from Timestamp to Instant in the PerMessage-constructor, thus no conversion here

                // ****NB!* Check for hvorvidt det er en textmessage!
                IMessage msg = new PerTextMessage(senderID, time, text);
                msgList.add(msg);
            }

            // getting chatname
            PreparedStatement st4 = conn.prepareStatement("SELECT * FROM synchat.chatinfo WHERE chatid = ?");
            st4.setInt(1, chatID);
            ResultSet rs4 = st4.executeQuery();
            if (rs4.next()) {
                name = rs4.getString("chatname");
            }

            PreparedStatement st5 = conn.prepareStatement("SELECT * FROM synchat.userchats WHERE chatid = ?");
            st5.setInt(1, chatID);
            ResultSet rs5 = st5.executeQuery();

            // while-loop to interate over all users connecting to this chatid
            while (rs5.next()) {
                userIDs.add(rs5.getInt("userid"));
            }

            IChatHistory ch = new PerChatHistory(loadMsgCount, msgList);
            IPrivateChat newchat = new PerPrivateChat(userIDs, chatID, name, ch);

            return newchat;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    IPrivateChat addToPrivateChat(IPrivateChat prichat) {
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {

            // the last index of msgList to get the latest message - the one that needs to be added to db
            int msgIndex = prichat.getCh().getMsgList().size() - 1;
            // converting Instant to Timestamp
            Timestamp time = Timestamp.from(prichat.getCh().getMsgList().get(msgIndex).getTimestamp());

            // add to existing chat entry in db
            PreparedStatement st0 = conn.prepareStatement("INSERT INTO synchat.chatmessages (chatid, message, timestamp, senderid) VALUES(?,?,?,?)");
            st0.setInt(1, prichat.getChatID());
            st0.setString(2, prichat.getCh().getMsgList().get(msgIndex).getContext());
            st0.setTimestamp(3, time);
            st0.setInt(4, prichat.getCh().getMsgList().get(msgIndex).getSenderID());

            st0.executeUpdate();

            IPrivateChat newchat = loadPrivateChat(prichat.getChatID(), 1);         // I think loadMsgCount is always 1 in this case, depending on implementation of other layers?
            return newchat;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

}
