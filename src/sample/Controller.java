package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Scanner;

import static sample.ToDoDataBase.*;

public class Controller implements Initializable {
    @FXML
    ListView todoList;

    @FXML
    TextField todoText;

    ObservableList<ToDoItem> todoItems = FXCollections.observableArrayList();
    ArrayList<ToDoItem> savableList = new ArrayList<ToDoItem>();
    String fileName = "todos.json";
//    public final static String DB_URL = "jdbc:h2:./main";
    ToDoDataBase toDoDataBase = new ToDoDataBase();
    int currrentUseId;
    public String username;
    public Connection connect;

    @Override
    public void initialize(URL location, ResourceBundle resources) {



//
//        if (username != null && !username.isEmpty()) {
//            fileName = username + ".json";
//        }
//
//        System.out.println("Checking existing list ...");
//        ToDoItemList retrievedList = retrieveList();
//        if (retrievedList != null) {
//            for (ToDoItem item : retrievedList.todoItems) {
//                todoItems.add(item);
//            }
//        }
//
//        makeNewUser();
//        todoList.setItems(todoItems);
        try {
            connect = toDoDataBase.init();
            userLogin();
//            connect = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void makeNewUser() throws SQLException {
        String fullName;
        String userEmail;
        System.out.println("Please enter your full name: ");
        Scanner inputScanner = new Scanner(System.in);
        fullName = inputScanner.nextLine();
        System.out.println("Please enter you email address:");
        userEmail = inputScanner.next();
        toDoDataBase.insertUser(connect,userEmail,fullName);
        int userId = toDoDataBase.selectUser(connect,userEmail);
        User newUser = new User(userEmail,fullName,userId);
        toDoDataBase.selectUsers(connect).add(newUser);
        System.out.println("User: " + newUser.fullName + " " + newUser.userName + " User ID: " + newUser.userId+ " added");
        System.out.println("User list size: " +  selectUsers(connect).size());
        currrentUseId = newUser.userId;
    }

    public void userLogin() throws SQLException{
        System.out.println("Would you like to login as and existing user or make a new one?");
        System.out.println("Number of users in data base: " + selectUsers(connect).size());
        System.out.println("========(Users)========");
        System.out.println("USERS: " + selectUsers(connect).toString());
        System.out.println("Enter (new) to create new user, or type (existing) to login as a existing user");
        Scanner inputScanner = new Scanner(System.in);
        String userSelect;
        userSelect = inputScanner.nextLine();
        if (userSelect.equals("new")) {
            makeNewUser();
        }
        if  (userSelect.equals("existing")) {
            System.out.println("Please enter the Email of the user you would like to continue as");
            System.out.print("==>");
            String userTheyWant = inputScanner.nextLine();
            int idTheyWant = toDoDataBase.selectUser(connect,userTheyWant);
            currrentUseId = idTheyWant;
//            toDoDataBase.selectToDosForUser(connect,idTheyWant);
            refreshToDoForuser(currrentUseId);
//            todoList.setItems(todoItems);
        }



    }

    public void startDataBase() throws SQLException {
        System.out.println("=========(Starting database)========");
        toDoDataBase.init();
//        System.out.println("Data base size: " + selectToDos(connect).size());
        userLogin();
//        refreshToDo();
    }
//    public void refreshUsers() throws SQLException {
//        for (int count = 0; count )
//    }

//    public void refreshToDo() throws SQLException{
//        for (int count= 0; count < selectToDos(connect).size(); count++) {
//            todoItems.add(selectToDos(connect).get(count));
//        }
//        System.out.println(todoItems);
//    }
    public void refreshToDoForuser(int currrentUseId) throws SQLException {
      ArrayList<ToDoItem> selectedUsersItems = selectToDosForUser(connect,currrentUseId);
        System.out.println(selectedUsersItems);
        for (int count = 0; count < selectedUsersItems.size(); count++) {
            todoItems.add(selectedUsersItems.get(count));
        }
        todoList.setItems(todoItems);
        System.out.println(todoItems);

    }

    public void saveToDoList() {
//        if (todoItems != null && todoItems.size() > 0) {
//            System.out.println("Saving " + todoItems.size() + " items in the list");
//            savableList = new ArrayList<ToDoItem>(todoItems);
//            System.out.println("There are " + savableList.size() + " items in my savable list");
//            saveList();
//        } else {
//            System.out.println("No items in the ToDo List");
//        }
    }
    public void displayUsers() {
        System.out.println();
    }

    public void addItem() {
        try {
            System.out.println("Adding item: " + todoText.getText());
            int id = toDoDataBase.insertToDo(connect,todoText.getText(),currrentUseId); //adds items to database and returns item id
            String text = todoText.getText();
            boolean isDone = false;
            ToDoItem toDoItem = new ToDoItem(id,text,isDone); //creates  new item
            todoItems.add(toDoItem);  //adds the new items to obesrbable list
//            selectToDos(connect);
//            todoItems.add(new ToDoItem(todoText.getText()));
            todoText.setText("");
            System.out.println(todoItems);
            System.out.println(ToDoDataBase.selectToDos(connect));
            todoList.setItems(todoItems);
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        System.out.println("Adding item ...");
//        todoItems.add(new ToDoItem(todoText.getText()));
//        todoText.setText("");
    }

    public void removeItem() {
        try {
            System.out.println("Removing item: " + todoList.getSelectionModel().getSelectedItem().toString());
            ToDoItem todoItem = (ToDoItem)todoList.getSelectionModel().getSelectedItem();
            todoItem.isDone = false;
            toDoDataBase.deleteToDo(connect, (String) todoList.getSelectionModel().getSelectedItem().toString());
            System.out.println("Is it done?: " + todoItem.isDone);
            System.out.println(todoItem);
            System.out.println("Remove this: " + todoItem.text);
            todoItems.remove(todoItem);
            System.out.println("Data base size: " + selectToDos(connect).size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        ToDoItem todoItem = (ToDoItem)todoList.getSelectionModel().getSelectedItem();
//        System.out.println("Removing " + todoItem.text + " ...");
//        todoItems.remove(todoItem);
    }

    public void toggleItem() {
//        Statement stmt;
        try {
            System.out.println("Toggling item: " +todoList.getSelectionModel().getSelectedItem().toString());
            ToDoItem toDoItem = (ToDoItem)todoList.getSelectionModel().getSelectedItem(); //we get the item we want to toggle
            int id = toDoItem.id; // we get the items id
            System.out.println("Toggled items id:" + id);
            toDoDataBase.toggleToDo(connect,id);
             // we send the id to the data base so we can change it there
                    if (toDoItem != null) { // as long as the item ist null
                        toDoItem.isDone = !toDoItem.isDone; // we set the item is done to its oposite
//                        String item = toDoItem.text;
//                        boolean isdone = toDoItem.isDone;
                        todoList.setItems(null); // sets all items in the list to null
                        todoList.setItems(todoItems); //sets all the items in the list to todoitems
                        System.out.println("is it done: " + toDoItem.isDone);
                    }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        System.out.println("Toggling item ...");
//        ToDoItem todoItem = (ToDoItem)todoList.getSelectionModel().getSelectedItem();
//        if (todoItem != null) {
//            todoItem.isDone = !todoItem.isDone;
//            todoList.setItems(null);
//            todoList.setItems(todoItems);
    }

//    public void saveList() {
//        try {
//            // write JSON
//            JsonSerializer jsonSerializer = new JsonSerializer().deep(true);
//            String jsonString = jsonSerializer.serialize(new ToDoItemList(todoItems));
//
//            System.out.println("JSON = ");
//            System.out.println(jsonString);
//
//            File sampleFile = new File(fileName);
//            FileWriter jsonWriter = new FileWriter(sampleFile);
//            jsonWriter.write(jsonString);
//            jsonWriter.close();
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//    }

//    public ToDoItemList retrieveList() {
//        try {
//
//            Scanner fileScanner = new Scanner(new File(fileName));
//            fileScanner.useDelimiter("\\Z"); // read the input until the "end of the input" delimiter
//            String fileContents = fileScanner.next();
//            JsonParser ToDoItemParser = new JsonParser();
//
//            ToDoItemList theListContainer = ToDoItemParser.parse(fileContents, ToDoItemList.class);
//            System.out.println("==============================================");
//            System.out.println("        Restored previous ToDoItem");
//            System.out.println("==============================================");
//            return theListContainer;
//        } catch (IOException ioException) {
//            // if we can't find the file or run into an issue restoring the object
//            // from the file, just return null, so the caller knows to create an object from scratch
//            return null;
//        }
//    }

    

}
