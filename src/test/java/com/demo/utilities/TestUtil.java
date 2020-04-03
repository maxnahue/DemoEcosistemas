package com.demo.utilities;

import com.demo.base.ConfiguracionBase;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TestUtil extends ConfiguracionBase {
    private static int month;
    private static int year;
    private static int sec;
    private static int min;
    private static int date;
    private static int day;

    // make zip of reports
    public static String zip(String filepath) {
        String pathZip = null;
        try {
            //pathZip=filepath.substring(0,58);
            pathZip = System.getProperty("user.dir");
            File inFolder = new File(filepath);
            System.out.println("Creating ZIP in: " + filepath);
            File outFolder = new File("Reports.zip");
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFolder)));
            BufferedInputStream in = null;
            byte[] data = new byte[1024];
            String[] files = inFolder.list();
            for (int i = 0; i < files.length; i++) {
                in = new BufferedInputStream(new FileInputStream
                        (inFolder.getPath() + "/" + files[i]), 1000);
                out.putNextEntry(new ZipEntry(files[i]));
                int count;
                while ((count = in.read(data, 0, 1000)) != -1) {
                    out.write(data, 0, count);
                }
                out.closeEntry();
            }
            out.flush();
            out.close();

            System.out.println("-----------" + pathZip + "//Reports.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pathZip + "//Reports.zip";
    }

    public static String dir_Report() {
        String path;
        boolean success = false;
        try {
            path = System.getProperty("user.dir") + "/src/test/resources/Reportes/" + Date();
            File directory = new File(path);
            if (directory.exists()) {
                System.out.println("Ya se creo una carpeta de reportes con el dia de la fecha, revisar en resources/Reportes");
            } else if (!directory.exists()) {
                success = directory.mkdir();
                if (success) {
                    System.out.println("Se creo una nueva carpeta a dia de la fecha donde se generaran reportes. resources/Reportes");
                } else {
                    System.out.println("Directorio de reportes: /src/test/resources/Reportes");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No se creo nuevo directorio de reportes " + e.getMessage());
            path = null;
        }
        return path;
    }

    public static String DateSystem() {
        Calendar cal = new GregorianCalendar();
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        sec = cal.get(Calendar.SECOND);
        min = cal.get(Calendar.MINUTE);
        date = cal.get(Calendar.DATE);
        day = cal.get(Calendar.HOUR_OF_DAY);

        return year + "-" + (month + 1) + "-" + date + "-" + day + min + sec;
    }

    public static String Date() {
        Calendar cal = new GregorianCalendar();
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        date = cal.get(Calendar.DATE);

        return year + "-" + (month + 1) + "-" + date;
    }

    public static String decode(String str) {
        byte[] valueDecoded = Base64.decodeBase64(str);
        return new String(valueDecoded);
    }

    @DataProvider(name = "data")
    public Object[][] getData(Method m) {
        String sheetName = m.getDeclaringClass().getSimpleName();
        String mtcId = m.getName();

        int rows = excel.getRowCount(sheetName);
        int cols = excel.getColumnCount(sheetName);
        Object[][] data = new Object[1][1];
        Hashtable<String, String> table = null;
        int index = 0;
        for (int i = 2; i < rows + 1; i++) {
            if (mtcId.equalsIgnoreCase(excel.getCellData(sheetName, 0, i))) {
                index = i;
                break;
            } else if (i == rows) {
                System.out.println("No se encontro el TC_ID " + mtcId + " en el excel de data");
            }
        }
        table = new Hashtable<String, String>();
        for (int colNum = 0; colNum < cols; colNum++) {
            table.put(excel.getCellData(sheetName, colNum, 1), excel.getCellData(sheetName, colNum, index));
        }
        data[0][0] = table;
        return data;
    }


}
