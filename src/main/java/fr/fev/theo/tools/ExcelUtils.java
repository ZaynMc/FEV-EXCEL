package fr.fev.theo.tools;

import fr.fev.theo.Main;
import fr.fev.theo.computers.ComputerInfo;
import fr.fev.theo.users.UserInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExcelUtils {

    Main pl;

    public ExcelUtils(Main pl) {
        this.pl = pl;
    }

    public List<String> readLineExcel(String file, int excel, String line) {

        return null;
    }

    public String returnCellWithourNullError(Cell cell) {
        if(cell == null) {
            return "";
        }
        return cell.toString();
    }

    public XSSFWorkbook readExcelFile(String fileExcel) {
        Main.log("Reading " + fileExcel + " file...");
        try {
            InputStream inputStream = new FileInputStream(fileExcel);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            return workbook;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getComputerNamesFromExcel(String file, String st) {
        int fr_sts = 2;
        XSSFWorkbook workbook = pl.excelUtils.readExcelFile(file);
        XSSFSheet sheet = workbook.getSheetAt(0);

        while(sheet.getRow(fr_sts) != null) {
            Row row = sheet.getRow(fr_sts);
            String stringCell = row.getCell(2).toString();
            //If the cell strarting with the word "Client"
            String computer = pl.stringUtils.getwordInString(stringCell, new String[]{"FRM", "DEM", "ESM", "DEM", "FRW", "MAM"}, " ");
            if(stringCell.startsWith("Client") && computer.length() > 1) {

                pl.computerInfoMap.putIfAbsent(computer, new ComputerInfo(0, computer, st, "DSIT", "Computer not found, put in DSIT department by default"));

                String licence = row.getCell(7).toString();
                String licencePrice = row.getCell(8).toString();
                double totalPrice = Double.parseDouble(licencePrice);

                ComputerInfo computerInfo = pl.computerInfoMap.get(computer);

                if(computerInfo != null && !licence.contains("IT CID - ")) {
                    computerInfo.addToLicence("<tr><td>" + computer + "</td><td>" + licence + "</td><td>" + licencePrice  +" €</td></tr>");
                    computerInfo.addToLicences(computerInfo.getLicences() + ";");
                    computerInfo.addToPrice(totalPrice);
                    computerInfo.setCompany(st);
                }
            }
            fr_sts++;
        }
    }

    public void getUsersNamesFromExcel(String file) {
        int i = 2;
        XSSFWorkbook workbook = pl.excelUtils.readExcelFile(file);
        XSSFSheet sheet = workbook.getSheetAt(0);

        while(sheet.getRow(i) != null) {
            Row row = sheet.getRow(i);
            String username = row.getCell(1).toString().toLowerCase(Locale.ROOT);
            String licenceName = row.getCell(7).toString();
            String licencePrice = row.getCell(8).toString();

            double totalPrice = Double.parseDouble(licencePrice);
            if(licenceName.contains("user")) {

                UserInfo userInfo = pl.userInfoMap.get(UserInfo.getUserPrincipalNameWithDisplayName(username));

                if(userInfo != null) {
                    Main.log("oui oui");
                    userInfo.addToLicence("<tr><td>" + licenceName + "</td><td>" + totalPrice  +" €</td></tr>");
                    userInfo.addToPrice(totalPrice);
                }
            }
            i++;

        }


    }

    public void createNewExcel() {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("Test");

        FileOutputStream out = null;

        int i = 1;

        for(ComputerInfo computerInfo : pl.computerInfoMap.values()) {


                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue(computerInfo.getComputerName());
                row.createCell(1).setCellValue(computerInfo.getContact());
                row.createCell(2).setCellValue(computerInfo.getTotalPrice());
                i++;
        }
        try {
            out = new FileOutputStream("test"+ ".xlsx");
            workbook.write(out);
            out.close();
            workbook.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
