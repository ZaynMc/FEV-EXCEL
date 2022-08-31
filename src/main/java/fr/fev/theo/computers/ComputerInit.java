package fr.fev.theo.computers;

import fr.fev.theo.Main;
import fr.fev.theo.database.ConnectionEnum;
import fr.fev.theo.database.ConnectionManager;
import fr.fev.theo.tools.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.plaf.nimbus.State;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ComputerInit {

    private Main pl;            //instance of the main class to update the hashmap

    public ComputerInit(Main pl) {
        this.pl = pl;

        init();
        CalculateCost();
        getExcelFever();
        exportDatas();
        pl.excelUtils.createNewExcel();
    }

    private void exportDatas() {
        Main.log("Export datas...");
        Connection connection = pl.connectionManager.connectDb();
        for(ComputerInfo computerInfo : pl.computerInfoMap.values()) {


            try {
                Statement statement = connection.createStatement();

                if(computerInfo.getId() > 0) {

                    statement.executeUpdate("UPDATE portaildsit_computers SET " +
                            ComputerVariablesEnum.COMPANY.getStr() + "='" + computerInfo.getCompany()
                            + "', " + ComputerVariablesEnum.DEPARTEMENT.getStr() + "='" + computerInfo.getDepartement() +
                            "', " + ComputerVariablesEnum.CONTACT.getStr() + "='" + computerInfo.getContact() +
                            "', "  + ComputerVariablesEnum.LICENCES_TABLE.getStr() + "='" + computerInfo.getLicence_table() + "',totalPrice=" + computerInfo.getTotalPrice() + " WHERE " + ComputerVariablesEnum.COMPUTERNAME.getStr() + "='" + computerInfo.getComputerName() + "';");
                } else {
                    statement.executeUpdate("INSERT into portaildsit_computers (computername, company, department, contact, licences_computers_table, totalPrice) VALUES ('" + computerInfo.getComputerName()
                    + "','" + computerInfo.getCompany() + "','" + computerInfo.getDepartement() + "','" + computerInfo.getContact() + "','" + computerInfo.getLicence_table() + "', " + computerInfo.getTotalPrice() + ");");

                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        Main.log("-------------- Computer Part Finish --------------");
    }

    private void init() {
        Main.log("-------------- Computer Part Start -------------- ");
        Main.log("Initialization of the ComputerInit");
        Connection connection = pl.connectionManager.connectDb();       //connection to the database

        try {
            Statement statement;
            ResultSet rst;

            //pre requis for execute the query
            statement = connection.createStatement();
            //execution of the query to get all informations for the computer table
            rst = statement.executeQuery(ConnectionEnum.GET_COMPUTERS_TABLE.getStringName());

            while(rst.next()) {
                int id = rst.getInt(ComputerVariablesEnum.ID.getStr());                                                                 //Get id
                String stringName = rst.getString(ComputerVariablesEnum.COMPUTERNAME.getStr());                                         //get the name of the computer
                String company = rst.getString(ComputerVariablesEnum.COMPANY.getStr());                                                 //get the company
                String departement = rst.getString(ComputerVariablesEnum.DEPARTEMENT.getStr()).replace("'", "-");     //get the departement
                String contact = rst.getString(ComputerVariablesEnum.CONTACT.getStr());                                                 //get the contact

                //if the user is absent of the hashmap, I add it
                pl.computerInfoMap.putIfAbsent(stringName, new ComputerInfo(id, stringName, company, departement, contact));
            }

            Main.log("List of computers : " + pl.computerInfoMap.size());                   //debug

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void CalculateCost() {
        Main.log("Calculate cost of the computers...");                                     // message in the console to know the step

       pl.excelUtils.getComputerNamesFromExcel("IT COST FEV FRANCE AND STS.xlsx", "FEV France");            //read and add the computers from the excel file IT COST FEV FRANCE AND STS
       pl.excelUtils.getComputerNamesFromExcel("IT COST FEV IBERIA.xlsx", "FEV Iberia");                    //read and add the computers from the excel file IT COST FEV IBERIA
       pl.excelUtils.getComputerNamesFromExcel("IT COST FEV NA.xlsx", "FEV North Africa");                        //read and add the computers from the excel file IT COST FEV NORTH AFRICA

        Main.log("List of computers : " + pl.computerInfoMap.size());
    }


    private void getExcelFever() {
        Main.log("Reading fever file...");
        String fileExcel = "D:/OneDrive/FEV Group GmbH/IT Area Europe - France/3 - Parc Informatique/1 - PC/FEVER.xlsx";
        try {
            InputStream inputStream = new FileInputStream(fileExcel);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            int f =0;
            while(workbook.getSheetAt(f) != null &&
                    !workbook.getSheetAt(f).getSheetName().equalsIgnoreCase("White PC")) {

                XSSFSheet sheet = workbook.getSheetAt(f);

                Main.log(sheet.getSheetName());

                int i = 2;
                while (sheet.getRow(i) != null && sheet.getRow(i).getCell(0) != null && sheet.getRow(i).getCell(1) !=null) {

                    Row row = sheet.getRow(i);

                    String computerName = sheet.getRow(i).getCell(0).toString();
                    String company = pl.excelUtils.returnCellWithourNullError(row.getCell(1));
                    String departement = pl.excelUtils.returnCellWithourNullError(row.getCell(2));
                    String contact = pl.excelUtils.returnCellWithourNullError(row.getCell(4));

                    Main.log("" + i);

                    pl.computerInfoMap.putIfAbsent(computerName,
                            new ComputerInfo(0,
                                    computerName,
                                    company,
                                    departement,
                                    contact));

                    ComputerInfo computerInfo = pl.computerInfoMap.get(computerName);

                    if(computerInfo.getComputerName().equalsIgnoreCase("FRM1305")) {
                        Main.log(computerInfo.getComputerName());
                    }

                    if(computerInfo != null) {
                        if (!computerInfo.getCompany().contains("FEV Maroc")) computerInfo.setCompany(company);
                        computerInfo.setDepartement(departement);
                        computerInfo.setContact(contact);
                    }
                    i++;

                }
                f++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
