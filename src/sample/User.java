package sample;

/**
 * Created by Justins PC on 5/16/2016.
 */
public class User {
    String userName;
    String fullName;
    int userId;

    public User(String uName,String fName,int iD) {
        this.userName = uName;
        this.fullName = fName;
        this.userId = iD;
    }
    @Override
    public String toString() {
        return "Full name:" + fullName + "||" + "Email:" + userName + "||" + "UserID: "  + userId;
    }
}
