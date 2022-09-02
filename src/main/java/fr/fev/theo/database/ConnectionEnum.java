package fr.fev.theo.database;

public enum ConnectionEnum {

    GET_COMPUTERS_TABLE("SELECT * FROM portaildsit_computers"), GET_USERS_TABLE("SELECT * FROM portaildsit_users");

    public String stringName;

    ConnectionEnum(String stringName) {
        this.stringName = stringName;
    }

    public String getStringName() {
        return stringName;
    }
}
