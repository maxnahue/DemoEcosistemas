package com.demo.pageObjects;

import com.demo.base.ConfiguracionBase;
import org.apache.log4j.Logger;
import org.apache.maven.shared.utils.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class SauceDemo {
    static Logger log = ConfiguracionBase.log;
    private static Object driver;
    WebDriver lDriver;
    ConfiguracionBase base = new ConfiguracionBase();



    @FindBy(id = "user-name")
    WebElement userName;
    @FindBy(id = "password")
    WebElement password;
    @FindBy(xpath ="//*[@id=\"login_button_container\"]/div/form/input[3]")
    WebElement login;
    @FindBy(xpath = "//div[@id='inventory_filter_container']/select")
    WebElement comboOrdena;
    @FindBy(xpath = "//*[@id=\"inventory_filter_container\"]/select/option[1]")
    WebElement orderAtoZ;
    @FindBy(xpath = "//*[@id=\"inventory_filter_container\"]/select/option[2]")
    WebElement orderZtoA;
    @FindBy(xpath = "//*[@id=\"inventory_filter_container\"]/select/option[3]")
    WebElement orderPriceLowtoHigh;
    @FindBy(xpath = "//*[@id=\"inventory_filter_container\"]/select/option[4]")
    WebElement orderPriceHightoLow;
    @FindBy(xpath = "//div[@id='inventory_container']/div/div/div[3]/button")
    WebElement mochila;
    @FindBy(xpath= "/html/body/div/div[2]/div[1]/div[2]/a/svg")
    WebElement carrito;
    @FindBy(xpath = "//*[@id=\"cart_contents_container\"]/div/div[2]/a[2]")
    WebElement checkout;
    @FindBy(id="first-name")
    WebElement checkoutNombre;
    @FindBy(id="last-name")
    WebElement checkoutApellido;
    @FindBy(id="postal-code")
    WebElement checkoutCodigoPostal;
    @FindBy(xpath = "//*[@id=\"checkout_info_container\"]/div/form/div[2]/input")
    WebElement checkoutContinue;
    @FindBy(xpath ="//*[@id=\"checkout_summary_container\"]/div/div[2]/div[8]/a[2]")
    WebElement checkoutFinalizarCompra;
    @FindBy(xpath = "//div[@id='menu_button_container']/div/div[3]/div/button")
    WebElement hamburguesa;
    @FindBy(id = "logout_sidebar_link")
    WebElement logout;


    public SauceDemo(WebDriver driver) {
        this.lDriver = driver;
        PageFactory.initElements(driver, this);
    }




    public void loginSauce(String usuario, String contrasena) throws IOException {
        userName.sendKeys(usuario);
        password.sendKeys(contrasena);
        login.click();
    }

    public void ordenarProductos() throws IOException {
        comboOrdena.click();
        orderAtoZ.click();
        comboOrdena.click();
        orderZtoA.click();
        comboOrdena.click();
        orderPriceLowtoHigh.click();
        comboOrdena.click();
        orderPriceHightoLow.click();
        comboOrdena.click();
        orderAtoZ.click();
    }

    public void compraMochila() throws InterruptedException {
        mochila.click();
        Thread.sleep(2000);
        lDriver.findElement(By.cssSelector("path")).click();
        //carrito.click();
    }

    public void checkout (String nombre, String apellido, String codigoPostal){
        checkout.click();
        checkoutNombre.click();
        checkoutNombre.sendKeys(nombre);
        checkoutApellido.click();
        checkoutApellido.sendKeys(apellido);
        checkoutCodigoPostal.click();
        checkoutCodigoPostal.sendKeys(codigoPostal);
    }

    public void finalizaCompra(){
        checkoutContinue.click();
        checkoutFinalizarCompra.click();
    }

    public void logoutSauce() throws InterruptedException {
        hamburguesa.click();
        Thread.sleep(1000);
        logout.click();
    }


}
