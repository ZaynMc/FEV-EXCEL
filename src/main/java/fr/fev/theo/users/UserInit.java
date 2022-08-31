package fr.fev.theo.users;

import fr.fev.theo.Main;

import java.sql.Connection;

public class UserInit {

    public Main pl;

    public UserInit(Main pl) {
        this.pl = pl;

        init();
        readFile();
    }

    public void init() {
        Main.log("Init of the users");

        Connection connection = pl.connectionManager.connectDb();
    }

    public void readFile() {

    }
}
