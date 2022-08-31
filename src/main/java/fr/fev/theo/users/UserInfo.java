package fr.fev.theo.users;

public class UserInfo {

    private int id;                 //id of user table
    private String stringName;      // ******@fev.com
    private String displayName;     //Name, Surname
    private String company;         //Company of the user
    private String departement;     //Departement of the user
    public void UserInfo(int id, String stringName, String displayName, String company, String departement, String phoneNumber) {
        this.id = id;
        this.stringName = stringName;
        this.displayName = displayName;
        this.company = company;
        this.departement = departement;
    }

    public int getId() {
        return id;
    }

    public String getStringName() {
        return stringName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCompany() {
        return company;
    }

    public UserInfo setCompany(String company) {
        this.company = company;
        return this;
    }

    public String getDepartement() {
        return departement;
    }

    public UserInfo setDepartement(String departement) {
        this.departement = departement;
        return this;
    }
}
