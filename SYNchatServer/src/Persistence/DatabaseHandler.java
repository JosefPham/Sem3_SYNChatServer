package Persistence;

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

                PreparedStatement st3 = conn.prepareStatement("SELECT userchats.chatid FROM synchat.userchats WHERE userchats.userid = ?;");

                st3.setInt(1, user.getUserID());
                ResultSet rs3 = st3.executeQuery();
                
                    while (rs3.next()) {
                        tmpList.add(rs3.getInt("chatid"));
                    }
                    user.setChats(tmpList);
                    System.out.println("users userID: " + user.getUserID());
                    login.setUser(user);
                
                    
                    IProfile returnProfile = getProfile(user.getUserID());
                    user.setProfile(returnProfile);
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

            PreparedStatement st = conn.prepareStatement("Select profiles.firstname, profiles.lastname, profiles.profile_text, profiles.nationality FROM synchat.profiles WHERE profiles.userid = ?;");
            st.setInt(1, userID);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                profile.setFirstName(rs.getString("firstname"));
                profile.setLastName(rs.getString("lastname"));
                profile.setProfileText(rs.getString("profile_text"));
                String tmpNationolaty = rs.getString("nationality");
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
                PreparedStatement st1 = conn.prepareStatement("INSERT INTO SYNCHAT.profiles (firstname, lastname, nationality, userid, profile_text) VALUES(?,?,?,?,?)");
                st1.setString(1, login.getUser().getProfile().getFirstName());
                st1.setString(2, login.getUser().getProfile().getLastName());
                st1.setString(3, login.getUser().getProfile().getNationality().toString());
                st1.setInt(4, profileID);
                st1.setString(5, "");

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

}
