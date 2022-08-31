package fr.fev.theo.computers;

public class ComputerInfo {

    private int id;
    private String computerName;
    private String company;
    private String departement;
    private String contact;

    private String licences;
    private String licence_table;

    private double totalPrice;


    public ComputerInfo(int id, String computerName, String company, String departement, String contact) {
        this.id = id;
        this.computerName = computerName;
        this.company = company;
        this.departement = departement;
        this.contact = contact;

        this.licence_table = "";
        this.totalPrice = 0;

        //FEV Maroc -> FEV North Africa
        if(company.equalsIgnoreCase("FEV MAROC")) {
            setCompany("FEV North Africa");
        }
    }

    public int getId() {
        return id;
    }

    public String getComputerName() {
        return computerName;
    }

    public String getCompany() {
        return company;
    }

    public ComputerInfo setCompany(String company) {
        this.company = company;
        return this;
    }

    public String getDepartement() {
        return departement;
    }

    public ComputerInfo setDepartement(String departement) {
        this.departement = departement;
        return this;
    }

    public String getContact() {
        return contact;
    }

    public ComputerInfo setContact(String contact) {
        this.contact = contact;
        return this;
    }

    public void addToLicence(String string){
        this.licence_table = this.licence_table +=string;
    }

    public String getLicence_table() {
        return licence_table;
    }

    public void addToPrice(double price)  {
        this.totalPrice = this.totalPrice += price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void addToLicences(String licence) {
        this.licences += licences;
    }

    public String getLicences() {
        return licences;
    }
}
