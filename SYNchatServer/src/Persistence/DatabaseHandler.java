package Persistence;

import Acquaintance.ILogin;
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

    int changePw(int userID, String hashedPw) {
        int passwordverification = verifyPw(userID, hashedPw);
        int returnstatus = 2;

        if (passwordverification == 1) {
            try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
                Class.forName("org.postgresql.Driver");

                PreparedStatement st = conn.prepareStatement("UPDATE SYNchat.users SET password = ? WHERE users.userid = ?;");
                st.setString(1, hashedPw);
                st.setString(2, (userID + ""));

                st.executeUpdate();
                //success retrun 1.
                returnstatus = 1;

            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (passwordverification == 3) {
            //if return is 3, access denied due to wrong password
            returnstatus = 3;
        } else if(passwordverification == 2) {
            returnstatus = 4;
        } else
            returnstatus = 2;
        //if 1 in return, success. if 2 in return, unknown error.
        return returnstatus;
        
        //return 1 = success;
        //return 2 = unknown error;
        //return 3 = passwordverification failed on password match;
        //return 4 = verification failiure;
    }

    int changeMail(int userID, String hashedPw, String hashedMail) {
        int passwordverification = verifyPw(userID, hashedPw);
        int returnStatus = 2;

        if (passwordverification == 1) {
            try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
                Class.forName("org.postgresql.Driver");

                PreparedStatement st = conn.prepareStatement("UPDATE SYNchat.users SET mail = ? WHERE users.userid = ?;");
                st.setString(1, hashedMail);
                st.setString(2, (userID + ""));

                st.executeUpdate();
                //success return 1.
                returnStatus = 1;

            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (passwordverification == 3) {
            //if retrun is 3, access denied due to wrong password.
            returnStatus = 3;
        } else if(passwordverification == 2) {
            returnStatus = 4;
        } else 
            returnStatus = 2;
        //For 1 in return, success. if 2 in return, unknown error.
        return returnStatus;
        
        //return 1 = success;
        //return 2 = unknown error;
        //return 3 = passwordverification failed on password match;
        //return 4 = verification failiure;
    }

}
