// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import client.*;
import common.*;

//import java.util.Scanner; 

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  String host;
  int port;


  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String _host, int _port) 
  {
    this.host = _host;
    this.port = _port;

    try 
    {

      this.client = new ChatClient(host, port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't create connection!");
    }
  }
  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */

  public Boolean logon()
  {
    Boolean success = false;

    try 
    {
      this.client.connect();
      success = true;
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!");
    }

    return success;
  }

  public void logoff()
  {
    System.out.println("Command Recived: Log Off");
    try 
    {
      this.client.disconnect();
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't close connection!");
      
      System.exit(0);
    }

    return;
  }


  public void stop()
  {
    System.out.println("Command Recived: Quit");
    client.quit();

    return;
  }


  public void start() 
  {
    try
    {
      BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
      String message;

      Boolean running = true;
      //Boolean loggedIn = false;

      while (running) 
      {
        //
        message = fromConsole.readLine();

        // Check for command character
        int index = message.indexOf('#');

        // Is Msg command?
        if (index > -1) {

          // Yes: Remove '#'
          String command = message.substring(index + 1, message.length());

          // Which Command?
          switch (command.toLowerCase()) 
          {
            case "logon":
              this.logon();
              break;
            case "logoff":
              this.logoff();
              break;
            case "quit":
              this.stop();  
              running = false;
              break;
            default:
              System.out.println("Invalid Command: " + command);
              break;
          }
        }
        else
        {
          // No: Is client Conneced?
          if(client.isConnected())
          {
            // Yes: Send Message to server
            client.handleMessageFromClientUI(message);
          }
          else
          {
            // No: Diplay Error Msg
            System.out.println("You must login to send msg");
          }
        }
      }
      System.out.println("Exiting Program");      
      
    } 
    catch (Exception ex) 
    {
      System.out.println("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String _host = "";
    int _port = 0;  //The port number

    try
    {
      _host = args[0];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      _host = "localhost";
    }

    try
    {
      _port = Integer.parseInt(args[1]);
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      _port = DEFAULT_PORT;
    }

    // Crear
    ClientConsole chat = new ClientConsole(_host, _port);

    chat.start();  // Start for console & Wait for data
    
  }
}
//End of ConsoleChat class
