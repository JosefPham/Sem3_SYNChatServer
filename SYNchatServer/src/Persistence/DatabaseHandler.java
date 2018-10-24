package Persistence;

import Acquaintance.ILogin;
import Acquaintance.IUser;
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
        ILogin returnLogin = null;

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            Class.forName("org.postgresql.Driver");

            Statement st = conn.createStatement();
            String sql = "Select * FROM Synchat.users WHERE users.email = '" + login.gethMail() + "';";

            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                if (rs.first()) {
                    if (login.gethPW().equals(rs.getString("password"))) {

                        // for converting sqlarray to int List
                        List<Integer> tmpList = new ArrayList<>();
                        Array chats = rs.getArray(2);
                        Integer[] intChats = (Integer[]) chats.getArray();
                        for (int i = 0; i < intChats.length; i++) {
                            tmpList.add(intChats[i]);
                        }

                        IUser returnUser = new PerUser(rs.getInt("userID"), rs.getString("tmpname"), rs.getBoolean("banned"), rs.getInt("reportcount"), tmpList);
                        returnLogin = new PerLogin(2, returnUser);
                    } else {
                        returnLogin = new PerLogin(1, null);
                    }
                } else {
                    returnLogin = new PerLogin(0, null);
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return returnLogin;
    }

    IUser createUser(ILogin login, IUser user) {
        IUser returnUser = null;
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {

            // Statement 1 - create person
            PreparedStatement st1 = conn.prepareStatement("INSERT INTO SYNCHAT.users (mail, password, tmpName)) "
                    + "VALUES('" + login.gethMail() + "', '" + login.gethPW() + "', '" + user.getTmpName() + "'");
            st1.executeUpdate();

            Statement st2 = conn.createStatement();
            String sql = "Select * FROM Synchat.users WHERE users.email = '" + login.gethMail() + "';";

            ResultSet rs = st2.executeQuery(sql);
            while(rs.next()){
                
            List<Integer> tmpList = new ArrayList<>();
            Array chats = rs.getArray(2);
            Integer[] intChats = (Integer[]) chats.getArray();
            for (int i = 0; i < intChats.length; i++) {
                tmpList.add(intChats[i]);
            }

           returnUser = new PerUser(rs.getInt("userID"), rs.getString("tmpname"), rs.getBoolean("banned"), rs.getInt("reportcount"), tmpList);
            }
            } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return returnUser;
    }
}
