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
}
