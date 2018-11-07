package Persistence;

import Acquaintance.ILogin;
import Acquaintance.IManagement;
import Acquaintance.IProfile;
import Acquaintance.IUser;
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
                    List<Integer> tmpList = new ArrayList<>();
                    Array chats = rs.getArray("chats");
                    try {
                        Integer[] intChats = (Integer[]) chats.getArray();
                        for (int i = 0; i < intChats.length; i++) {
                            tmpList.add(intChats[i]);
                        }
                    } catch (PSQLException | NullPointerException ex) {
                    }

                    IUser returnUser = new PerUser(rs.getInt("userID"), rs.getString("tmpname"), rs.getBoolean("banned"), rs.getInt("reportcount"), tmpList);
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

    Boolean createUser(ILogin login) {
        Boolean createBoolean = false;
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            PreparedStatement st0 = conn.prepareStatement("Select * FROM Synchat.users WHERE users.mail = ?;");
            st0.setString(1, login.gethMail());
            ResultSet rs0 = st0.executeQuery();
            if (!rs0.next()) {
                PreparedStatement st1 = conn.prepareStatement("INSERT INTO SYNCHAT.users (mail, password, tmpName) VALUES(?,?,?)");
                st1.setString(1, login.gethMail());
                st1.setString(2, login.gethPW());
                st1.setString(3, login.getUser().getTmpName());

                st1.executeUpdate();
                createBoolean = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return createBoolean;
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

}

}
