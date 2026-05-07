package model;

public class StandardUser extends User {

    private Vault vault;

    public StandardUser(String username, String masterPassword) {
        super(username, masterPassword);
        this.vault = new Vault();
    }

    @Override
    public boolean hasPermission() {
        return false;
    }

    public Vault getVault() {
        return vault;
    }
}
