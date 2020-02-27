// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

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

  public void logon()
  {
    try 
    {
      this.client.connect();
      System.out.println("Successfully Logged on to:" + this.host + ":" + this.port);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!");
    }

  }

  public void logoff()
  {
    try 
    {
      this.client.disconnect();
      System.out.println("Successfully Logged off from " + this.host + ":" + this.port);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't close connection!");

      System.exit(0);
    }

    return;
  }

  public void help()
  {
    System.out.println("\n-----------------------------------------------------------------------------------------------");
    System.out.println("#logon   -> Opens a connection to specified host and port");
    System.out.println("#logoff  -> Closes any active connection");
    System.out.println("#sethost -> Sets host name of the server to be connected to (cannont set while connection active)");
    System.out.println("#gethost -> Displays host name of the server to be connected to (cannont set while connection active)");
    System.out.println("#setport -> Sets port number to be used for the connection (cannont set while connection active)");
    System.out.println("#setport -> Displays port number to be used for the connection (cannont set while connection active)");
    System.out.println("#help    -> Displays this msg");
    System.out.println("#quit    -> Closes all connections and Exits the program");
    System.out.println("-----------------------------------------------------------------------------------------------");
    return;
  }

  public void stop()
  {
    //System.out.println("Command Recived: Quit");

    try 
    {
      this.client.disconnect();
      System.out.println("Exiting Program");
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't close connection!");

      System.exit(0);
    }
    //client.quit();

    return;
  }

  public void setPort(int _port)
  {
    

    if(!client.isConnected())
    {
      System.out.println("Setting Port to: " + _port);
      client.setPort(_port);
    }
    else
    {
      System.out.println("Error. Please Logoff to set port");
    }
    
    

    return;
  }

  public void getPort()
  {
    
    System.out.println("Port is set to: " + client.getPort());
    
    return;
  }



  public void setHost(String _host)
  {

    if(!client.isConnected())
    {
      System.out.println("Setting host to: " + _host);
      client.setHost(_host);
    }
    else
    {
      System.out.println("Error. Please Logoff to set host");
    }
    

    return;
  }


  public void getHost()
  {
    
    System.out.println("Host is set to: " + client.getHost());

    return;
  }


  public void start() 
  {
    System.out.println("Enter msg to send or #help for a list of commands");

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


        Scanner sc = new Scanner(message);

        String command = sc.next();

        
        //int index = message.indexOf('#');
        // Check for command character
        if (command.indexOf('#') > -1) 
        {
          // Which Command?
          switch (command.toLowerCase()) 
          {
            case "#logon":
              this.logon();
              break;
            case "#logoff":
              this.logoff();
              break;
            case "#setport":
              this.setPort(sc.nextInt());
              break;
            case "#getport":
              this.getPort();
              break;
            case "#sethost":
              this.setHost(sc.next());
              break;
            case "#gethost":
              this.getHost();
              break;
            case "#help":
              this.help();
              break;
            case "#quit":
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
            System.out.println("Error. You must login to send msg");
          }
        }


        //int newPort = sc.nextInt();
        
        //System.out.println("Command Recived: Setport = " + newPort);
        //client.setPort(newPort);

        
        /*
        
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
            case "setport":
            this.setport(command);
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
        //*/
        sc.close();
      }
      //System.out.println("Exiting Program");      
      
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
