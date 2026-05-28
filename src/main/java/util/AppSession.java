package util;

public class AppSession {
    private static AppSession instance;
    private String email;
    private boolean admin;

    private AppSession() {}

    public static AppSession getInstance() {
        if (instance == null) instance = new AppSession();
        return instance;
    }

    public void login(String email) {
        this.email = email;
        this.admin = "admin@gmail.com".equals(email);
    }

    public void logout() {
        this.email = null;
        this.admin = false;
    }

    public boolean isLoggedIn() { return email != null; }
    public boolean isAdmin() { return admin; }
    public String getEmail() { return email; }

    public int getFilterTeacherIdx() {
        if ("juan_antonio@gmail.com".equals(email)) return 6;
        return -1;
    }
}
