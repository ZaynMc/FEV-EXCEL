package fr.fev.theo.users;

public enum UserVariablesEnum {

    ID("id"),
    MAILNICKNAME("mailNickName"),
    DISPLAYNAME("displayName"),
    USERPRINCIPALNAME("userPrincipalName"),
    COMPANY("company"),
    DEPARTEMENT("description");

    private String string;

    UserVariablesEnum(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
