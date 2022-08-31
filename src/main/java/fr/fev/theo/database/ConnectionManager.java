package fr.fev.theo.database;

import fr.fev.theo.Main;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {

    public Main pl;

    public ConnectionManager(Main pl) {
        this.pl = pl;
    }

    public  Connection connectDb() {

        try {
            Connection conn = null;
            Class.forName("com.mysql.cj.jdbc.Driver");
            if (pl.isLocal) {
                conn = DriverManager.getConnection("jdbc:mysql://localhost/portail_dsit", "root", "");
            } else {
                conn = DriverManager.getConnection("jdbc:mysql://frx003/Portail_Dsit", "db_access", "Jn@shF3V@09201216");
            }
            return conn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            pl.log("Connexion failed - Contact Administrator - Code 1");
            return null;
        }
    }
}
