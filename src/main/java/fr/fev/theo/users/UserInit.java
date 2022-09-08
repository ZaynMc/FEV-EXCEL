package fr.fev.theo.users;

import com.groupdocs.conversion.internal.c.a.d.User;
import com.opencsv.CSVReader;
import fr.fev.theo.Main;
import fr.fev.theo.database.ConnectionEnum;

import javax.swing.plaf.nimbus.State;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class UserInit {

    public Main pl;

    public UserInit(Main pl) {
        this.pl = pl;

        init();
        executePowerShell();
        readFiles();
        readQuote();
        exportDatas();
        Main.log(pl.userInfoMap.get("Bassoli, Florian").getUserPrincipalName());
    }

    private void readQuote() {
        pl.excelUtils.getUsersNamesFromExcel("IT COST FEV FRANCE AND STS.xlsx");
        pl.excelUtils.getUsersNamesFromExcel("IT COST FEV IBERIA.xlsx");
        pl.excelUtils.getUsersNamesFromExcel("IT COST FEV NA.xlsx");
    }

    private void exportDatas() {
        Main.log("Export datas...");
        Connection connection = pl.connectionManager.connectDb();

        for(UserInfo userInfo : pl.userInfoMap.values()) {

            try {

                Statement statement = connection.createStatement();

                if(userInfo.getId() > 0) {
                   statement.executeUpdate("UPDATE portaildsit_users SET users_licences_table='" + userInfo.getLicences_tables() +"', totalPrice=" + userInfo.getTotalPrice() + ",description='" + userInfo.getDepartement().replace("'", "-") + "' WHERE userPrincipalName='" + userInfo.getUserPrincipalName() + "';");
                } else {
                    statement.executeUpdate("INSERT into portaildsit_users (mailNickname, displayName, userPrincipalName, company, description, users_licences_table, totalPrice) VALUES ('" + userInfo.getStringName() + "', '"
                    + userInfo.getDisplayName() + "', '" + userInfo.getUserPrincipalName() + "', '" + userInfo.getCompany() + "','" + userInfo.getDepartement() + "','" + userInfo.getLicences_tables() + "', " + userInfo.getTotalPrice() + ");");

                }



            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void readFiles() {
        readFile("sts", "FEV STS ");
        readFile("france", "FEV France");
        readFile("northafrica", "FEV North Africa");
        readFile("iberia", "FEV Iberia");
    }

    private void executePowerShell() {
        powerShell("OU=FEV-STS,OU=User Accounts,DC=FEV,DC=COM", "sts");
        powerShell("OU=FEV-FR,OU=User Accounts,DC=FEV,DC=COM", "france");
        powerShell("OU=FEV-Northafrica,OU=User Accounts,DC=FEV,DC=COM", "northafrica");
        powerShell("OU=ES,OU=User Accounts,DC=FEV,DC=COM", "iberia");
    }


    public void powerShell(String string, String file) {
        //String command = "powershell.exe  your command";
        //Getting the version
        String command = "powershell.exe Get-ADUser -Filter * -SearchBase '" + string + "' -Properties * | Select-Object displayName, mail, description | export-csv -path " + file + ".csv -encoding utf8";

        try {
            // Executing the command
            Process powerShellProcess = Runtime.getRuntime().exec(command);
            // Getting the results
            powerShellProcess.getOutputStream().close();
            String line;
            System.out.println("Standard Output:");
            BufferedReader stdout = new BufferedReader(new InputStreamReader(
                    powerShellProcess.getInputStream()));
            while ((line = stdout.readLine()) != null) {
                System.out.println(line);
            }
            stdout.close();
            System.out.println("Standard Error:");
            BufferedReader stderr = new BufferedReader(new InputStreamReader(
                    powerShellProcess.getErrorStream()));
            while ((line = stderr.readLine()) != null) {
                System.out.println(line);
            }
            stderr.close();
            System.out.println("Done");
        } catch (IOException e) {
            throw new RuntimeException(e);
    }
    }

    public void init() {
        Main.log("-------------- User Part Start -------------- ");
        Main.log("Initialization of the UserInit");

        Connection connection = pl.connectionManager.connectDb();

        try {
            Statement statement;
            ResultSet resultSet;

            statement = connection.createStatement();
            resultSet = statement.executeQuery(ConnectionEnum.GET_USERS_TABLE.getStringName());

            while(resultSet.next()) {
                int id = resultSet.getInt(UserVariablesEnum.ID.getString());
                String mailNickName = resultSet.getString(UserVariablesEnum.MAILNICKNAME.getString());
                String displayName = resultSet.getString(UserVariablesEnum.DISPLAYNAME.getString());
                String userPrincipalName = resultSet.getString(UserVariablesEnum.USERPRINCIPALNAME.getString());
                String company = resultSet.getString(UserVariablesEnum.COMPANY.getString());
                String departement = resultSet.getString(UserVariablesEnum.DEPARTEMENT.getString());

                pl.userInfoMap.putIfAbsent(displayName, new UserInfo(id, mailNickName, displayName, userPrincipalName, company, departement));
            }

            Main.log("List of users : " + pl.userInfoMap.size());


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void readFile(String file, String company) {
        //Instantiating the CSVReader class
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(file + ".csv"));

            //Reading the contents of the csv file
            List list = reader.readAll();
            //Getting the Iterator object
            Iterator it = list.iterator();
            while(it.hasNext()) {
                String[] str = (String[]) it.next();
                String displayName = str[0];

                if(str.length >= 2 && !displayName.contains("displayName") && !displayName.contains("#TYPE")) {
                    String mail = str[1];
                    String description = str[2] == null ? "" : str[2];

                    pl.userInfoMap.putIfAbsent(displayName, new UserInfo(0, mail, displayName, company, description));

                    UserInfo userInfo = pl.userInfoMap.get(displayName);

                    if(userInfo != null) {
                        userInfo.setDepartement(description);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
