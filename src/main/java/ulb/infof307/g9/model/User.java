package ulb.infof307.g9.model;

/**
 * The type User.
 */
public class User {

    private final String username;
    private String password;

    /**
     * Instantiates a new User.
     *
     * @param username the username
     */
    public User(String username) {
        this.username = username;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof User)) {
            return false;
        }

        return ((User) obj).getUsername().equals(getUsername()) && ((User) obj).getPassword().equals(getPassword());
    }
}

