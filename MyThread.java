import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MyThread extends Thread{
  NetDot parent;     // this is the object we belong to
  int id;
  int nameInit=1;
  int plyTurnThOut;
  int xThOut;
  int yThOut;
  String plyrName1;
  String plyrName2;
  PrintWriter pw;
  Scanner sc;
  ServerSocket ss;
  Socket sock;
  
public MyThread(int i, String conIP) {
  id=i;

  if (id==1) {
    
    try {
      ss=new ServerSocket(1234);
    }
    catch (Exception e) {
      System.out.println("error");
      return;
    }

  // wait for a connetion request
  System.out.println("Waiting for a connection request");
  try {
    sock=ss.accept();
  }
  catch (Exception e) {
    System.out.println("error at socket accepting connection");
    return;
  }
  
  System.out.println("Success, socekt at " + sock);
  
  try {
    pw=new PrintWriter(sock.getOutputStream());
    sc=new Scanner(sock.getInputStream());
  }
  catch(Exception e) {
    System.out.println("error at socket accepting connection");
    return;	  
  }
  
  }
  if (id==2) {
    try{
      sock=new Socket(conIP,1234);
    	
      pw=new PrintWriter(sock.getOutputStream());
      sc=new Scanner(sock.getInputStream());
    }
    catch (Exception e) {
      System.out.println("Error connection IP");
      System.exit(0);
    }
  }

}

  public void run() {
     while (sc.hasNextLine()) {
      String temp=sc.nextLine();

      if (nameInit==1) {
      System.out.print("Hi, my name is ");
    
      if (id==1) {
    	  if (temp.equals("") || temp.equals("Enter Name 2")) {
            temp="Client";
            System.out.print(temp);
            System.out.print(" I'm the Client\n");
    	  }
    	  else {
            System.out.print(temp);
            System.out.print(" I'm the Client\n");
    	  }
    	  if (parent.txtPlayer_1.getText().equals("") || parent.txtPlayer_1.getText().equals("Enter Name 1")) {
    	    parent.txtPlayer_1.setText("Server");
    	  }
    	  parent.txtPlayer_2.setText(temp);
      }
      if (id==2) {
    	  if (temp.equals("") || temp.equals("Enter Name 1")) {
            temp="Server";
            System.out.print(temp);
            System.out.print(" I'm the Server\n");
    	  }
    	  else {
            System.out.print(temp);
            System.out.print(" I'm the Server\n");
    	  }
    	  if (parent.txtPlayer_2.getText().equals("") || parent.txtPlayer_2.getText().equals("Enter Name 2")) {
    	    parent.txtPlayer_2.setText("Client");
    	  }
    	  parent.txtPlayer_1.setText(temp);
      }
      parent.setInitialThIn();
      }
      
      if (temp.equals("Q")) {
          parent.sendQuit();
          System.out.println("Player has selected quit, Goodbye");
    	  System.exit(0);
      }
      
      if (nameInit==0) {
      System.out.println(temp);
    		  try {
    		    String arrOfTemp[] = temp.split(",", 0);
                plyTurnThOut=Integer.parseInt(arrOfTemp[0]);
                xThOut=Integer.parseInt(arrOfTemp[1]);
                yThOut=Integer.parseInt(arrOfTemp[2]);
//                System.out.println(plyTurnThOut);                
//                System.out.println(xThOut);                
//                System.out.println(yThOut);                
    		  }
    		  catch(Exception e) {
                  System.out.println("Error");
    		  }
    		  
              parent.clickUpdate(plyTurnThOut,xThOut,yThOut);
      }
      
      nameInit=0;     
     
     }
  }

  public void saveParent(NetDot myParent) {
    parent=myParent;
  }
}
