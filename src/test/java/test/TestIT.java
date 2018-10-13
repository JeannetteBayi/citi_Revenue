/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.File;
import java.net.URL;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.junit.Assert;
//import org.junit.Rule;
//import org.junit.contrib.java.lang.system.SystemOutRule;
import org.openqa.selenium.By;

/**
 *
 * @author jmukamana
 */
@WarpTest
@RunWith(Arquillian.class)
@RunAsClient
public class TestIT {
    //   @Rule
    //  public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    private static final String WEBAPP_SRC = "src/main/webapp";
    private static final String WEB_INF_SRC = "src/main/webapp/WEB-INF";
    private static final String WEB_RESOURCES = "src/main/webapp/resources";
    @Drone
    private WebDriver browser;
    @ArquillianResource
    private URL deploymentUrl;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies().resolve().withTransitivity().asFile();

        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "com.mobitill")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("jboss-web.xml", "jboss-web.xml")
                .addAsWebInfResource("web.xml", "web.xml")
                .addAsWebInfResource(new File(WEB_INF_SRC, "template.xhtml"))
                .addAsWebResource(new File(WEBAPP_SRC, "index.xhtml"))
                .addAsWebResource(new File("src/main/webapp/demo", "home.xhtml"), "demo/home.xhtml")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                        .importDirectory(WEB_RESOURCES).as(GenericArchive.class), "resources")
                .addAsLibraries(files);
        System.out.println(war.toString(true));
        return war;
    }
    @FindBy(tagName = "li")                     // 2. injects a first element with given tag name
    private WebElement facesMessage;

    ///tax register
    @FindBy(id = "taxRegisterbtn")
    private WebElement taxRegisterbtn;
    @FindBy(id = "trPanel")
    private WebElement trPanel;
    @FindBy(id = "trMerchantPIN")
    private WebElement trMerchantPIN;
    @FindBy(id = "btnTaxRegisterSave")
    private WebElement btnTaxRegisterSave;
    @FindBy(id = "trmessages")
    private WebElement trmessages;

    //find taxe register
    @FindBy(id = "findTaxRegisterbtn")
    private WebElement findTaxRegisterbtn;
    @FindBy(id = "findtrPanel")
    private WebElement findtrPanel;
    @FindBy(id = "Searchtr")
    private WebElement Searchtr;
    @FindBy(id = "taxregDeviceId")
    private WebElement taxregDeviceId;
    @FindBy(id = "findtrmessages")
    private WebElement findtrmessages;

    //transactions
    @FindBy(id = "transactionsPanel")
    private WebElement transactionsPanel;
    @FindBy(id = "Transactionsbtn")
    private WebElement Transactionsbtn;
    @FindBy(id = "transmessages")
    private WebElement transmessages;
    @FindBy(id = "searchtransaction")
    private WebElement searchtransaction;
    @FindBy(id = "merchantbtn")
    private WebElement merchantbtn;
    @FindBy(id = "panelMerchant")
    private WebElement panelMerchant;
    @FindBy(id = "txtMerchantName")
    private WebElement txtMerchantName;
    @FindBy(id = "txtMerchantPIN")
    private WebElement txtMerchantPIN;
    @FindBy(id = "txtMerchantPhone")
    private WebElement txtMerchantPhone;
    @FindBy(id = "txtMerchantEmailAddress")
    private WebElement txtMerchantEmailAddress;
    @FindBy(id = "btnMerchantCreateNew")
    private WebElement btnMerchantCreateNew;
    @FindBy(id = "messages")
    private WebElement messages;

    @Test
    @InSequence(1)
    public final void browserTest() throws Exception {
        browser.get(deploymentUrl.toExternalForm() + "index");
        WebElement loginImage = browser.findElement(By.id("loginImage"));
        guardHttp(loginImage).click();
        Assert.assertEquals("navigate to home page ", deploymentUrl.toExternalForm() + "demo/home", browser.getCurrentUrl());
    }

    @Test
    @InSequence(2)
    public final void browserMerchantCreate() throws Exception {
        if (!browser.getCurrentUrl().equals(deploymentUrl.toExternalForm() + "demo/home")) {
            browser.get(deploymentUrl.toExternalForm() + "demo/home");
        }
        guardAjax(merchantbtn).click();
        waitAjax().until().element(panelMerchant).is().present();
        Assert.assertTrue("merchant panel displayed", panelMerchant.isDisplayed());
        WebElement txtMerchantLocation = browser.findElement(By.id("txtMerchantLocation"));
        WebElement btnMerchantSave = browser.findElement(By.id("btnMerchantSave"));
        txtMerchantName.sendKeys("test merchant");
        txtMerchantLocation.sendKeys("kampala");
        txtMerchantPhone.sendKeys("11111163");
        txtMerchantEmailAddress.sendKeys("demo@yahoo.com");
        guardAjax(btnMerchantSave).click();
        Assert.assertEquals("create merchant ", "Merchant Created", facesMessage.getText().trim());
    }

//    @Test
//    @InSequence(3)
//    public final void browserEditMerchant() throws Exception {
//        Warp
//                .initiate(new Activity() {
//                    @Override
//                    public void perform() {
//                        browser.get(deploymentUrl.toExternalForm() + "demo/home");
//                    }
//                })
//                .observe(request().method().equal(HttpMethod.GET))
//                .inspect(new Inspection() {
//                    private static final long serialVersionUID = 1L;
//
//                    @Inject
//                    HomeManagedBean hmb;
//
//                    @BeforePhase(Phase.RENDER_RESPONSE)
//                    public void beforeRenderResponse() {
//                        System.out.println("beforeRenderResponse:===================");
//                    }
//
//                    @AfterPhase(Phase.RENDER_RESPONSE)
//                    public void afterRenderResponse() {
//                        System.out.println("afterRenderResponse:");
//                    }
//
//                    @BeforePhase(Phase.RESTORE_VIEW)
//                    public void beforeRestoreView() {
//                        System.out.println("beforeRestoreView:===================");
//                    }
//
//                    @AfterPhase(Phase.RESTORE_VIEW)
//                    public void afterRestoreView() {
//                        System.out.println("afterRestoreView:");
//                        int max = hmb.getMerchants().size();
//                        Random rand = new SecureRandom();
//                        hmb.setMerchantSelected(hmb.getMerchants().get(rand.nextInt(max)));
//                        hmb.onMerchantSelect();
//                    }
//
//                });}
    
    //selecting a table row
    @Test
    @InSequence(5)
    public final void selectMerchantTableRow() throws Exception {
        WebElement tableElement = browser.findElement(By.xpath("//table/tbody[@id='merchantTable_data']"));
        List<WebElement> allRows = tableElement.findElements(By.xpath(".//tr"));
        System.out.println(" size " + allRows.size());
        int max = allRows.size();
        Random rand = new SecureRandom();
        if (max > 0) {
            guardAjax(allRows.get(rand.nextInt(max))).click();
        }
    }
    //merchant transactions
    @Test
    @InSequence(6)
    public final void merchantTransactionsNullRecords() throws Exception {
        if (!browser.getCurrentUrl().equals(deploymentUrl.toExternalForm() + "demo/home")) {
            browser.get(deploymentUrl.toExternalForm() + "demo/home");
        }
        WebElement merchantTransacttions = browser.findElement(By.id("merchantTrans"));
        guardAjax(merchantTransacttions).click();
        WebElement merchantPanel = browser.findElement(By.id("merchantPanel"));
        waitAjax().until().element(merchantPanel).is().present();
        WebElement merchantAccordion = browser.findElement(By.id("merchantAccordion"));
        waitAjax().until().element(merchantAccordion).is().present();
        WebElement salesTab = browser.findElement(By.id("salesTab"));
        guardAjax(salesTab).click();
        WebElement Purchases = browser.findElement(By.id("Purchases"));
        guardAjax(Purchases).click();
        WebElement merchantSalesTable = browser.findElement(By.id("merchantSalesTable"));
        Assert.assertEquals("merchant transactions ", "No records found.", merchantSalesTable.getText().trim());
    }
    
    //tax register
    @Test
    @InSequence(7)
    public final void browserTaxRegister() throws Exception {
        if (!browser.getCurrentUrl().equals("https://127.0.0.1:8443/citi/demo/home")) {
            browser.get(deploymentUrl.toExternalForm() + "demo/home");
        }
        guardAjax(taxRegisterbtn).click();
        waitAjax().until().element(trPanel).is().present();
        trMerchantPIN.sendKeys("123445678090");
        guardAjax(btnTaxRegisterSave).click();
        Assert.assertEquals("Create Tax Register", "Tax Register Created", trmessages.getText().trim());
    }

    //find tax register
    @Test
    @InSequence(8)
    public final void browserFindTaxRegister() throws Exception {
        if (!browser.getCurrentUrl().equals("https://127.0.0.1:8443/citi/demo/home")) {
            browser.get(deploymentUrl.toExternalForm() + "demo/home");
        }
        guardAjax(findTaxRegisterbtn).click();
        waitAjax().until().element(findtrPanel).is().present();
        taxregDeviceId.sendKeys("K0669380053");
        guardAjax(Searchtr).click();
        Assert.assertNotEquals(" Tax register ", "Tax register not found", findtrmessages.getText().trim());
    }

    //transactions
    @Test
    @InSequence(10)
    public final void browserTranasctions() throws Exception {
        if (!browser.getCurrentUrl().equals("https://127.0.0.1:8443/citi/demo/home")) {
            browser.get(deploymentUrl.toExternalForm() + "demo/home");
        }
        guardAjax(Transactionsbtn).click();
        waitAjax().until().element(transactionsPanel).is().present();
        guardAjax(searchtransaction).click();
        Assert.assertEquals("create merchant ", "", transmessages.getText().trim());
    }

    @Test
    @InSequence(9)
    public final void browserMerchantCreateNew() throws Exception {
        if (!browser.getCurrentUrl().equals("https://127.0.0.1:8443/citi/demo/home")) {
            browser.get(deploymentUrl.toExternalForm() + "demo/home");
        }
        guardAjax(merchantbtn).click();
        waitAjax().until().element(panelMerchant).is().present();
        txtMerchantName.sendKeys("test merchant");
        txtMerchantPhone.sendKeys("85115074");
        txtMerchantPIN.sendKeys("09876543210");
        txtMerchantEmailAddress.sendKeys("jannekista3@gmail.com");
        guardAjax(btnMerchantCreateNew).click();
        Assert.assertEquals("create  new merchant ", " Merchant Created", messages.getText().trim());
    }

    @Test
    @InSequence(10)
    public final void sleep() throws Exception {
        Thread.sleep(10000);
    }
}
