/**
 * To represent the a User using the system
 */
public abstract class User {  // not going to make instances of this class, so abstract
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {      // Name can be changed
        this.name = name;
    }
}
