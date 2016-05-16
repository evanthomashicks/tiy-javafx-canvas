package sample;

import com.sun.org.apache.regexp.internal.RE;
import org.h2.tools.Server;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Justins PC on 5/12/2016.
 */
public class ToDoDataBase {
    public static Connection connect;
    public final static String DB_URL = "jdbc:h2:./main";

    public Connection init() throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
//        stmt.execute("CREATE TABLE IF NOT EXISTS todos (id IDENTITY, text VARCHAR, is_done BOOLEAN)");
        stmt.execute("CREATE TABLE IF NOT EXISTS todos (id IDENTITY, text VARCHAR, is_done BOOLEAN, user_id INT)");
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, username VARCHAR, fullname VARCHAR)");
//        selectToDos(conn);
        selectUsers(conn);


        // we'll add some implementation code here once we have a unit test method for it
        return conn;
    }
    public int insertToDo(Connection conn, String text,int userID) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO todos VALUES (NULL, ?, false, ?)");
        stmt.setString(1, text);
        stmt.setInt(2, userID);
        stmt.execute();
        stmt = conn.prepareStatement("SELECT * FROM todos WHERE text=?");
        stmt.setString(1,text);
        ResultSet result = stmt.executeQuery();
        result.next();
        return  result.getInt("id");
    }
    public void deleteToDo(Connection conn, String text) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM todos where text = ?");
        stmt.setString(1, text);
        stmt.execute();
    }
    public int insertUser(Connection conn, String username, String fullname) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?)");
        stmt.setString(1, username);
        stmt.setString(2, fullname);
        stmt.execute();
        stmt = conn.prepareStatement("SELECT * FROM users WHERE username=?"); //selects the last thing we added to data base
        stmt.setString(1,username); //
        ResultSet result = stmt.executeQuery(); //gets the spot in the database
        result.next();
        System.out.println("ID for new user: " + result.getInt("id"));
        return result.getInt("id"); //returns the id
    }

    public int selectUser(Connection conn,String userName) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=?");
        stmt.setString(1,userName);
        ResultSet result = stmt.executeQuery();
        result.next();
        result.getInt("id");
        return result.getInt("id");

    }
    public int selectToDo(Connection conn,String text) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos WHERE text=?");
        stmt.setString(1,text);
        ResultSet result = stmt.executeQuery();
        result.next();
        result.getInt("id");
        return result.getInt("id");
    }
    public void deleteUser(Connection conn, String username) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users where username = ?");
        stmt.setString(1, username);
        stmt.execute();
    }

    public static ArrayList<ToDoItem> selectToDos(Connection conn) throws SQLException {
        ArrayList<ToDoItem> items = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM todos");
        while (results.next()) {
            int id = results.getInt("id");
            String text = results.getString("text");
            boolean isDone = results.getBoolean("is_done");
//            items.add(new ToDoItem(id, text, isDone));
            ToDoItem toDoItem = new ToDoItem(id,text,isDone);
            items.add(toDoItem);
        }
        return items;
    }
    public static ArrayList<User> selectUsers(Connection conn) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM users");
        while (results.next()) {
            int id = results.getInt("id");
            String userName = results.getString("username");
            String fullName = results.getString("fullname");
            User user = new User(userName,fullName,id);
            users.add(user);
        }
        return users;
    }

    public static ArrayList<ToDoItem> selectToDosForUser(Connection conn, int userID) throws SQLException {
        ArrayList<ToDoItem> items = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos " +
                "INNER JOIN users ON todos.user_id = users.id " +
                "WHERE users.id = ?");
        stmt.setInt(1, userID);
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            int id = results.getInt("id");
            String text = results.getString("text");
            boolean isDone = results.getBoolean("is_done");
            items.add(new ToDoItem(id, text, isDone));
        }
        System.out.println(items);
        return items;
    }

    public void toggleToDo(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE todos SET is_done = NOT is_done WHERE id = ?");
        stmt.setInt(1,id);
        stmt.execute();
    }




}
