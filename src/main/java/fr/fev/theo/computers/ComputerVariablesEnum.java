package fr.fev.theo.computers;

public enum ComputerVariablesEnum {

    ID("id"),
    COMPUTERNAME("computername"),
    COMPANY("company"),
    DEPARTEMENT("department"),
    CONTACT("contact"),
    LICENCES_TABLE("licences_computers_table");

    private String str;

    ComputerVariablesEnum(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}
