package sample;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by Justins PC on 5/12/2016.
 */
public class ToDoDataBaseTest {
    ToDoDataBase todoDatabase;

    @Before
    public void setUp() throws Exception {
        todoDatabase = new ToDoDataBase();
        todoDatabase.init();


    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testStartUp() throws Exception {
        // test to make sure we can access the new database
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        PreparedStatement todoQuery = conn.prepareStatement("SELECT * FROM todos");
        ResultSet results = todoQuery.executeQuery();
        assertNotNull(results);


    }
    @Test
    public void testRemoveItem() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main"); //connect to database
        String firstToDoText = "UnitTest-ToDo1";
        String username = "unittester@tiy.com";
        String fullName = "Unit Tester";
        int userID = todoDatabase.insertUser(conn, username, fullName);
        todoDatabase.insertToDo(conn,firstToDoText,userID);
        ArrayList<ToDoItem> todos = todoDatabase.selectToDos(conn);
        todoDatabase.deleteToDo(conn,firstToDoText);
        assertEquals(todos.size(),1,0);
        todoDatabase.deleteToDo(conn,firstToDoText);
        todoDatabase.deleteUser(conn, username);


    }
    @Test
    public void testSelectAllToDos() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main"); //connect to database
        String firstToDoText = "UnitTest-ToDo1"; //makes new todo item text
        String secondToDoText = "UnitTest-ToDo2";
        String username = "unittester@tiy.com";
        String fullName = "Unit Tester";
        int userID = todoDatabase.insertUser(conn, username, fullName);

        todoDatabase.insertToDo(conn, firstToDoText,userID); //adds the todo items to the data base useing are insert method
        todoDatabase.insertToDo(conn, secondToDoText,userID);

        ArrayList<ToDoItem> todos = todoDatabase.selectToDos(conn); // adds the todos to the array list useing select todos method
        System.out.println("Found " + todos.size() + " todos in the database"); // prints out the ammount of items in the array
        System.out.print(todos);

        assertTrue("There should be at least 2 todos in the database (there are " +
                todos.size() + ")", todos.size() > 1); //checks to make sure that the two to do items were added to the data base

        todoDatabase.deleteToDo(conn, firstToDoText); //deletes both to do items in order to return the data base to its regular state.
        todoDatabase.deleteToDo(conn, secondToDoText);
        // make sure we remove the test user we added earlier
        todoDatabase.deleteUser(conn, username);
    }

//    @Test
//    public void testInsertToDo() throws Exception {
//        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
//        String todoText = "UnitTest-ToDo";
//        todoDatabase.insertToDo(conn, todoText);
//        // make sure we can retrieve the todo we just created
//        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos where text = ?");
//        stmt.setString(1, todoText);
//        ResultSet results = stmt.executeQuery();
//        assertNotNull(results);
//        // count the records in results to make sure we get what we expected
//        int numResults = 0;
//        while (results.next()) {
//            numResults++;
//        }
//        assertEquals(1, numResults);
//    }
    @Test
    public void  testInsertToDo() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String todoText = "UnitTest-ToDo";

        // adding a call to insertUser, so we have a user to add todos for
        String username = "unittester@tiy.com";
        String fullName = "Unit Tester";
        int userID = todoDatabase.insertUser(conn, username, fullName);

        todoDatabase.insertToDo(conn, todoText, userID);

        // make sure we can retrieve the todo we just created
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos where text = ?");
        stmt.setString(1, todoText);
        ResultSet results = stmt.executeQuery();
        assertNotNull(results);
        // count the records in results to make sure we get what we expected
        int numResults = 0;
        while (results.next()) {
            numResults++;
        }

        assertEquals(1, numResults);

        todoDatabase.deleteToDo(conn, todoText);
        // make sure we remove the test user we added earlier
        todoDatabase.deleteUser(conn, username);

        // make sure there are no more records for our test todo
        results = stmt.executeQuery();
        numResults = 0;
        while (results.next()) {
            numResults++;
        }
        assertEquals(0, numResults);
    }
    @Test
    public void testInsertUser() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main"); //connect to server
        String userName = "justinthomas@gmail.com"; // creates new text
        String fullName = "Justin Thomas";
        int userId = todoDatabase.insertUser(conn, userName,fullName); // inserts todo text into the data base
        // make sure we can retrieve the todo we just created
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users where username = ?");
        stmt.setString(1, userName);
        int userIdFromTest;
        ResultSet results = stmt.executeQuery();
        assertNotNull(results);
        results.next();
        userIdFromTest = results.getInt("id"); //get the id from the id coloum

        // count the records in results to make sure we get what we expected
//        int numResults = 0;
//        while (results.next()) {
//            numResults++;
//        }
        System.out.println("ID returned from userinsert: " + userId);
//        assertEquals(1, numResults);
        assertEquals(userIdFromTest,userId);
        todoDatabase.deleteUser(conn,userName);
        // make sure there are no more records for our test todo
        results = stmt.executeQuery();
//        numResults = 0;
//        while (results.next()) {
//            numResults++;
//        }
//        assertEquals(0, numResults);

    }
    @Test
    public void testSelectUser() throws Exception {

    }
//    @Test
//    public void testUserMakeNewUser() throws Exception {
//        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
//        Controller controller = new Controller();
////        User newUser = controller.makeNewUser();
//        int userId = todoDatabase.insertUser(conn,newUser.fullName,newUser.fullName);
//        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users where username = ?");
//        stmt.setString(1, newUser.userName);
//        int userIdFromTest;
//        ResultSet results = stmt.executeQuery();
//        assertNotNull(results);
//        results.next();
//        userIdFromTest = results.getInt("id"); //get the id from the id coloum
//
//        // count the records in results to make sure we get what we expected
////        int numResults = 0;
////        while (results.next()) {
////            numResults++;
////        }
//        System.out.println("ID returned from userinsert: " + userId);
////        assertEquals(1, numResults);
//        assertEquals(userIdFromTest,userId);
//        todoDatabase.deleteUser(conn,newUser.userName);
//        // make sure there are no more records for our test todo
//        results = stmt.executeQuery();
//    }

    @Test
    public void testInsertToDoForUser() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main"); //we connect ot he SQL data base
        String todoText = "UnitTest-ToDo"; //we create the text for are todo items
        String todoText2 = "UnitTest-ToDo2";

        // adding a call to insertUser, so we have a user to add todos for
        String username = "unittester@tiy.com"; //creating a user name for user1
        String fullName = "Unit Tester";  //creating a full name for user 1
        int userID = todoDatabase.insertUser(conn, username, fullName); //creating user 1

        String username2 = "unitester2@tiy.com"; //creating a user name for user 2
        String fullName2 = "Unit Tester 2"; // creating a fullname for user 2
        int userID2 = todoDatabase.insertUser(conn, username2, fullName2); //creating user 2

        todoDatabase.insertToDo(conn, todoText, userID); //inserts a todo for user 1
        todoDatabase.insertToDo(conn, todoText2, userID2); //inserts a todo for user 2

        // make sure each user only has one todo item
        ArrayList<ToDoItem> todosUser1 = todoDatabase.selectToDosForUser(conn, userID); ///creates a new array list for users 1s todos
        ArrayList<ToDoItem> todosUser2 = todoDatabase.selectToDosForUser(conn, userID2); //creates a new array list for user 2s todos

        assertEquals(1, todosUser1.size()); //compare the exspected amount of todo items which is one equal to the size of the arraylist
        assertEquals(1, todosUser2.size());

        // make sure each todo item matches
        ToDoItem todoUser1 = todosUser1.get(0); //gets the first item in the array which is the item  we added
        assertEquals(todoText, todoUser1.text); //compares the text of the item to the text we created when we  added it to the array
        ToDoItem todoUser2 = todosUser2.get(0);
        assertEquals(todoText2, todoUser2.text);
        // we delete all todo items and users to make sure the database returns to normal
        todoDatabase.deleteToDo(conn, todoText); //deleteing first item
        todoDatabase.deleteToDo(conn, todoText2); //deleteing second item
        // make sure we remove the test user we added earlier
        todoDatabase.deleteUser(conn, username); //deleteing first user
        todoDatabase.deleteUser(conn, username2); //deleting second user

    }


}