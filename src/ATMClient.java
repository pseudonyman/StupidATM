import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
   @author Jonas Dahl
   @version 1.0
   @date 2014-11-28
*/
public class ATMClient {
    private static int portNumber = 8989;
    private static String hostName = "127.0.0.1";
    private static String lang = "svSE";
    
    public static void main(String[] args) throws IOException {
    	Language t = new Language(lang);
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        BufferedReader stdIn = null;
        try {
            socket = new Socket(hostName, portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (UnknownHostException e) {
            System.err.println(t.t("Don't know about host") + " " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println(t.t("Couldn't get I/O for the connection to") + " " + hostName);
            System.exit(1);
        }

        String line;
        boolean cont = true;
        while (cont) {
        	System.out.println(t.t("Right now, you can choose between (1) login, (2) withdraw, (3) show balance, (4) quit.")); // TODO
        	line = stdIn.readLine();
        	if (line == null) 
        		break;
        	
        	int choice = Integer.parseInt(line);
        	String response;
        	switch (choice) {	// TODO Everything, constants for cases, nicer error handling osv.. =D
        	case 1: //LOGIN
        		System.out.print("Card number: ");	// Ask for card number
        		String cardNumber = stdIn.readLine();
        		System.out.print("Pin code: ");	// Ask for pin code
        		String pinCode = stdIn.readLine();
        		out.println("L");	// Send an "L" for login
        		out.println(cardNumber);  // Send card number
        		out.println(pinCode);	  // Send pin code
        		response = in.readLine();
        		if (response.startsWith("E")) {
        			System.out.println("An error occurred.");
        		} else {
        			System.out.println("You are now logged in.");
        		}
        		break;
        	case 2: // Withdraw
        		System.out.print("Two-digit code: ");	// Ask for code
        		String code = stdIn.readLine();
        		System.out.print("Amount: ");	// Ask for amount
        		String amount = stdIn.readLine();
        		out.println("W");	// Send a "W" for withdrawal
        		out.println(code);  // Send code
        		out.println(amount);	  // Send amount
        		response = in.readLine();
        		if (response.startsWith("E")) {
        			System.out.println("An error occurred.");
        		} else {
        			System.out.println("You do now have " + ((double)Integer.parseInt(amount)/100) + " in cash.");
        		}
        		break;
        	case 3: // Balance
        		out.println("B");	// Send a "B" for balance
        		response = in.readLine();
        		if (response.startsWith("E")) {
        			System.out.println(t.t("An error occurred."));
        		} else {
        			System.out.println(t.t("You have") + " " + ((double)Integer.parseInt(response)/100) + " " + t.t("on the account."));
        		}
        		break;
        	case 4: // Balance
        		out.println("Q");	// Send an "Q" for byebye
        		cont = false;
        		break;
        	}
        }
    }
    
    public static void printMenu(BufferedReader in, PrintWriter out) {
    	System.out.println();
    }
}   
