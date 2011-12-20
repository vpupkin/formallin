import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.openqa.selenium.server.SeleniumServer;

import com.thoughtworks.selenium.DefaultSelenium;

public class WebPagesTest {
	
	private static Logger log = Logger.getLogger(WebPagesTest.class.getName());	
	
	public static SeleniumServer server;
	
	public static void startSeleniumServer() throws IOException{
		server = null;
		try {
            ServerSocket serverSocket = new ServerSocket(4444);
            serverSocket.close();

            try {
             server = new SeleniumServer(); //false, rcc);
            } catch (Exception e) {
                System.err.println("Could not create Selenium Server because of: " + e.getMessage());
                e.printStackTrace();
            }

            try {
                server.start();
            } catch (Exception e) {
                System.err.println("Could not start Selenium Server because of: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (BindException e) {
            System.out.println("Selenium server already up, will reuse...");
        }
	}
	
	
	public static void main(String[] arg) throws IOException, ClassNotFoundException, SQLException {
		
		startSeleniumServer();
		
		DefaultSelenium selenium = new DefaultSelenium("localhost", 4444, "*iehta", "http://localhost:8080/SRI/");
        selenium.start();
        
        // Test - traduccion
        // insert
        selenium.open("traducciones/tipo_servicio.jsp?tipo=1");
        selenium.
        if(selenium.isTextPresent("Traducción")){
        	System.out.println("ok");
        }
        else {
        	System.out.println("error");
        }
        
        selenium.stop();
        if(server!=null){
        	server.stop();
        }
	}
}
