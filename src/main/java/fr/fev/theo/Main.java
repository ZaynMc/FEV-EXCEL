package fr.fev.theo;

import fr.fev.theo.computers.ComputerInfo;
import fr.fev.theo.computers.ComputerInit;
import fr.fev.theo.database.ConnectionManager;
import fr.fev.theo.tools.ExcelUtils;
import fr.fev.theo.tools.StringUtils;
import fr.fev.theo.users.UserInfo;
import fr.fev.theo.users.UserInit;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static Main INSTANCE;

    public boolean isLocal = true;                                                 //boolean to know each database reach

    public Map<String, UserInfo> userInfoMap = new HashMap<>();                    //hashmap for the users list
    public Map<String, ComputerInfo> computerInfoMap = new HashMap<>();            //hashmap for the computers list

    public ConnectionManager connectionManager = new ConnectionManager(this);   //class for the communication with the database
    public ExcelUtils excelUtils = new ExcelUtils(this);
    public StringUtils stringUtils = new StringUtils(this);

    public static void main(String[] args) throws IOException {
        INSTANCE = new Main();

        Main.log("IsLocal : " + Main.INSTANCE.isLocal);

        new ComputerInit(INSTANCE);
        new UserInit(INSTANCE);
    }

    public static void log(String string) {
        System.out.println(string);
    }

}

