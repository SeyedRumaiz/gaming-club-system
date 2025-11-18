public abstract class Person {  // not going to make instances of this class, so abstract
    private String name;
    private String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {      // Name can be changed
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {        // Email can be changed
        this.email = email;
    }
}
