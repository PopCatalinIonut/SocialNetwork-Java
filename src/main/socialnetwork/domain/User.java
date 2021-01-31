package main.socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private List<User> friends = new ArrayList<>();
    private static long lastId=0;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.setId(++lastId);
    }

    public User(Long ID, String firstName, String lastName){
        this.firstName=firstName;
        this.lastName=lastName;
        this.setId(ID);
        lastId=ID;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName(){ return firstName+" "+lastName;}
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void addFriend(User user){ friends.add(user);}

    public void deleteFriend(User user){ friends.remove(user);}
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof User) {
            User that = (User) obj;
            return getFirstName().equals(that.getFirstName()) &&
                    getLastName().equals(that.getLastName()) &&
                    getFriends().equals(that.getFriends());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }
}