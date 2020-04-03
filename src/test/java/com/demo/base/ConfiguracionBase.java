package com.demo.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.demo.listener.WebEventListener;
import com.demo.utilities.ExcelReader;
import com.demo.utilities.ExtentManager;
import com.demo.utilities.TestUtil;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import javax.mail.MessagingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

public class ConfiguracionBase {


    public static final Logger log = Logger.getLogger(ConfiguracionBase.class.getName());
    public static WebDriver driver;
    public static Properties config = new Properties();
    public static Properties or = new Properties();
    public static Properties bd = new Properties();
    public static FileInputStream fis;
    public static ExcelReader excel = new ExcelReader(System.getProperty("user.dir") + "/src/test/resources/excel/testData.xlsx");
    public static WebDriverWait wait;
    public static WebElement element;
    public static ExtentReports extent;
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();
    public static String reportPath;
    public static String attachmentPath;

    public static String url;
    private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<ExtentTest>();
    //final String filePath = System.getProperty("user.dir") + "/src/test/resources/reports/report.html";
    public EventFiringWebDriver Event_driver;
    public WebEventListener eventListener;
    public ITestResult result;


    //@BeforeSuite(alwaysRun=true)
    public void setUp() throws IOException {

        if (driver == null) {
            try {
                fis = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/properties/config.properties");
                log.debug("Loaded Config properties");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                log.error("Failed to read Config properties");
            }
            try {
                config.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Failed to load Config properties");
            }
            try {
                fis = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/properties/or.properties");
                log.debug("Loaded OR properties");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                log.error("Failed to read OR properties");
            }
            try {
                or.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Failed to read OR properties");
            }

            if (config.getProperty("browser").equalsIgnoreCase("firefox")) {
                System.out.println("Se busca la ruta donde se encuentra el driver");
                System.setProperty("webdriver.gecko.driver",
                        System.getProperty("user.dir") + "/src/test/resources/executables/geckodriver.exe");
                System.out.println("Configuracion Firefox");
                FirefoxOptions opts = new FirefoxOptions();
                opts.addArguments("-private");
                opts.addPreference("browser.download.dir", System.getProperty("user.dir") + "/Descargas");
                opts.addPreference("browser.download.folderList", 2);
                opts.addPreference("browser.download.useDownloadDir", true);
                opts.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                driver = new FirefoxDriver(opts);
                log.debug("FirefoxDriver Started");
            } else if (config.getProperty("browser").equalsIgnoreCase("chrome")) {
                log.debug("Se busca el driver en la ruta especifica");
                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/src/test/resources/executables/chromedriver.exe");
                driver = new ChromeDriver();
                Event_driver = new EventFiringWebDriver(driver);
                eventListener = new WebEventListener();
                log.debug("ChromeDriver Started");
                //
            } else if (config.getProperty("browser").equalsIgnoreCase("ie")) {
                log.debug("Se busca el driver en la ruta especifica");
                System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "/src/test/resources/executables/IEDriverServer.exe");
                driver = new InternetExplorerDriver();
                Event_driver = new EventFiringWebDriver(driver);
                eventListener = new WebEventListener();
                log.debug("InternetExplorerDriver Started");
            }

            if (Event_driver != null && eventListener != null) {
                System.out.println("Driver Cargado");
            } else {
                System.out.println("Problemas con Driver");
            }

        }
    }


    @BeforeSuite
    public void beforeSuite() {
        reportPath = TestUtil.dir_Report() + "/Report_" + TestUtil.DateSystem();
        extent = ExtentManager.createInstance(reportPath);
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportPath);
        extent.attachReporter(htmlReporter);
    }

    @BeforeClass
    public synchronized void beforeClass() {
        ExtentTest parent = extent.createTest(getClass().getName());
        parentTest.set(parent);
    }

    @BeforeMethod
    public synchronized void beforeMethod(Method method) {
        ExtentTest child = parentTest.get().createNode(method.getName());
        test.set(child);
        System.out.println(method.getName());
    }

    @AfterMethod
    public synchronized void afterMethod(ITestResult result) throws InterruptedException {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.get().fail(result.getThrowable());
//        else if (result.getStatus() == ITestResult.SKIP)
//        	((ExtentTest) test.get().skip(result.getThrowable());
            driver.close();
            Thread.sleep(1000);
            System.out.println("Fallo el test");
        } else {
            test.get().pass("Test passed");
            System.out.println("Test exitoso");
        }
        extent.flush();
    }

    public void getUrl(String url) {
        log.info("navigating to :-" + url);
        driver.get(url);
        driver.manage().window().maximize();
    }


    @AfterSuite(alwaysRun = true)
    public void endTest() throws MessagingException, InterruptedException {
        driver.close();
        Thread.sleep(2000);
        attachmentPath = TestUtil.zip(TestUtil.dir_Report());
        //  mail.sendMail(TestConfig.server, TestConfig.from, TestConfig.to, TestConfig.subject, TestConfig.messageBody, attachmentPath, TestConfig.attachmentName);
    }

    public void iniciarSitio2(String nombreSitio) throws IOException {
        setUp();
        getUrl(nombreSitio);
    }
}