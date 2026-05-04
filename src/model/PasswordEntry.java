package model;

public class PasswordEntry {

    private String siteName;
    private String username;
    private String password;
    private String category;

    public PasswordEntry(String siteName, String username, String password, String category) {
        this.siteName = siteName;
        this.username = username;
        this.password = password;
        this.category = category;
    }

    public String getSiteName() { return siteName; }
    public String getUsername() { return username; }
    public String getCategory() { return category; }

    public String getPassword() {
        // TODO: decrypt before returning
        return password;
    }

    public void setPassword(String newPwd) {
        // TODO: encrypt before storing
        this.password = newPwd;
    }
}
