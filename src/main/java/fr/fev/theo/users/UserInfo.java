package fr.fev.theo.users;

import fr.fev.theo.Main;

import java.util.Locale;

public class UserInfo {

    private int id;                     //id of user table
    private String stringName;          // ******@fev.com
    private String displayName;         //Name, Surname
    private String userPrincipalName;   // *****@***.***
    private String company;             //Company of the user
    private String departement;         //Departement of the user
    private String licences_tables;
    private double totalPrice;

    public UserInfo(int id, String stringName, String displayName, String userPrincipalName, String company, String departement) {
        this.id = id;
        this.stringName = stringName;
        this.displayName = displayName;
        this.userPrincipalName = userPrincipalName;
        this.company = company;
        this.departement = departement;

        this.licences_tables = "";
        this.totalPrice = 0;
    }

    public UserInfo(int id, String stringName, String displayName, String company, String departement) {
        this.id = id;
        this.stringName = stringName.toLowerCase(Locale.ROOT).replace("fev.local", "fev.com");
        this.displayName = displayName;
        this.userPrincipalName = this.stringName.replace("@fev.com", "");
        this.company = company;
        this.departement = departement.replace("'", "-");

        this.licences_tables = "";
        this.totalPrice = 0;

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

    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    public String getLicences_tables() {
        return licences_tables;
    }

    public static String getUserPrincipalNameWithDisplayName(String userPrincipalName) {
        for(UserInfo userInfo : Main.INSTANCE.userInfoMap.values()) {
            if(userInfo.getUserPrincipalName().equalsIgnoreCase(userPrincipalName)) {
                return userInfo.getDisplayName();
            }
        }
        return "";
    }

    public void addToLicence(String string){
        this.licences_tables = this.licences_tables + string;
    }

    public void addToPrice(double price)  {
        this.totalPrice = this.totalPrice + price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
