package com.demo.testCases;

import com.demo.base.ConfiguracionBase;
import com.demo.pageObjects.SauceDemo;
import com.demo.utilities.TestUtil;
import org.apache.log4j.Logger;
import org.apache.maven.shared.utils.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

public class SuiteDemo extends ConfiguracionBase {
    public static final Logger log = Logger.getLogger(SuiteDemo.class.getName());
    private SauceDemo sauce;

    private static void takeScreenshot(String fileName) throws IOException {
        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file, new File("./Evidencias/Demo/" + fileName + ".jpg"));
    }

    @BeforeMethod(alwaysRun = true)
    public void inicio() throws IOException, InterruptedException {
        sauce = new SauceDemo(driver);
        iniciarSitio2("https://www.saucedemo.com/");
        Thread.sleep(1500);//
    }

    @Test(enabled = true, dataProviderClass = TestUtil.class, dataProvider = "data", priority = 1)
        public void TC_01_Login_Exitoso(Hashtable<String, String> data) throws Exception {
            sauce = new SauceDemo(driver);
            sauce.loginSauce(data.get("user"),data.get("pass"));
            takeScreenshot("TC_01_Login_Exitoso");
            sauce.logoutSauce();
    }

    @Test(enabled = true, dataProviderClass = TestUtil.class, dataProvider = "data", priority = 1)
        public void TC_02_Login_Fail(Hashtable<String, String> data) throws Exception {
            sauce = new SauceDemo(driver);
            sauce.loginSauce(data.get("user"),data.get("pass"));
            takeScreenshot("TC_02_Login_Fail");
    }

    @Test(enabled = true, dataProviderClass = TestUtil.class, dataProvider = "data", priority = 1)
        public void TC_03_Compra_Producto(Hashtable<String, String> data) throws Exception {
            sauce = new SauceDemo(driver);
            sauce.loginSauce(data.get("user"),data.get("pass"));
            sauce.ordenarProductos();
            sauce.compraMochila();
            sauce.checkout(data.get("nombre"),data.get("apellido"),data.get("codigoPostal"));
            sauce.finalizaCompra();
            takeScreenshot("TC_03_Compra_Producto");
            sauce.logoutSauce();
    }

    @Test(enabled = true, dataProviderClass = TestUtil.class, dataProvider = "data", priority = 1)
    public void TC_04_Logout(Hashtable<String, String> data) throws Exception {
        sauce = new SauceDemo(driver);
        sauce.loginSauce(data.get("user"),data.get("pass"));
        sauce.logoutSauce();
        takeScreenshot("TC_04_Logout");
    }

}





