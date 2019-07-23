package daryadelan.sandogh.zikey.com.daryadelan.data;

import daryadelan.sandogh.zikey.com.daryadelan.model.User;

public class UserInstance {

    private static   UserInstance ourInstance = null;

    public static UserInstance getInstance() {
        if(ourInstance==null)
            ourInstance=new UserInstance();

        return ourInstance;
    }

    private UserInstance() {
    }

   private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void clear(){
        ourInstance=null;
        user=null;
    }
}
