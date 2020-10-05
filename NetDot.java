import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JSeparator;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

// NAME: Patrick Tsai
// CLASS: CSE223 M-F 10 AM
// DUE DATE: 6/05/2020 8 AM
// PROGRAM NAME: NetDot
// -------------- MAKE SURE TO TYPE THE SERVER YOU WANT TO CONNECT TO AS A CLIENT ----------
// --------------- localhost or 127.0.0.1 work ------------ THERE IS NO DEFAULT ADDRESS ----
// SUMMARY - The game of DOTS (game play in description) between two players using a threaded connection
// This assignment was really difficult!
// DESCRIPTON - This is the game of dots. A board is created, using the row and col variables, for the number of rows
// and number of columns to be used on the board. You can change these variables in the Dots.java file. The Players
// enter their names. Players alternate selecting lines between dots. Once a box is completed (lines on all sides) it is
// assigned a winner - the user who placed the line on the box last to complete the box. The player's initial is placed
// in the middle of the box to signify a win. The winner of the box gets another turn immediately following the completed
// box. The game is over when all the boxes are full. And the winner is announced in the Game info area. Users can reset
// the game at any time. Users must hit the start button before beginning the game.

public class NetDot extends JFrame {
  private static final long serialVersionUID = 1L;
  private JPanel contentPane;
  int playerTurn;	
  int row=8;                                    //row variable - used to set board dimensions
  int col=8;                                    //col variable - used to set board simensions
  int cellHeight=50;
  int cellWidth=50;
  int startVar=0;
  int getScoreReturnVal=0;
  int ServerClient=1;
  int whoMe;
  String connectIP;
  String plyr1NameThIn;
  String plyr2NameThIn;
  JTextField txtPlayer_1;
  JTextField txtPlayer_2;
  JLabel lblCurrentTurnWho;   
  JLabel lblPlayerOneScore;
  JLabel lblPlayerTwoScore;
  JLabel lblSettings;
  JLabel lblSettings2;
  JLabel prevMoveAnValid;
  JLabel prevMoveAnPlyr;
  JLabel linePlace1;
  JLabel linePlace2;
  JLabel boxScore1;
  JLabel boxScore2;
  JButton btnReset;
  JRadioButton rdbtnClient;
  JRadioButton rdbtnServer;
  JTextField txtConnection;
  MyThread nt; 
  GamePanel panel;
  NetDot netDotIn;
  
  private final ButtonGroup buttonGroup = new ButtonGroup();

  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          NetDot frame = new NetDot();
          frame.setVisible(true);
        } catch (Exception e) {
	  e.printStackTrace();
        }
      }
    });
 }

  public NetDot() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 850, 550);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    contentPane.setLayout(null);

    panel = new GamePanel(row,col,cellHeight,cellWidth);                   //building the game board
    //save our identity in the panel
    panel.saveParent(this);
    netDotIn=this;
    panel.addMouseListener(new MouseAdapter() {                                      //check for a click on the game board
    @Override
      public void mouseClicked(MouseEvent mCl) {
        if (startVar!=0 && panel.playerTurn==ServerClient) {
//          System.out.println("mouse clicked at " + mCl.getX() + ", " + mCl.getY());
        	
            int xSend=mCl.getX();
            int ySend=mCl.getY();
            int prevPlyrTurn=panel.playerTurn;
            String prevPlyrName;

			nt.pw.println(ServerClient + "," + xSend + "," + ySend);
            nt.pw.flush();
			
            if (prevPlyrTurn==1) {
              prevPlyrName=panel.plyOneName;
            }
            else {
              prevPlyrName=panel.plyTwoName;
            }    

	    panel.addClick(xSend,ySend);
            if (panel.lineThere==1) {                                                //checks if line already exits
              prevMoveAnValid.setText("Invalid, line already exists");               //invalid if line already exists
              linePlace1.setText("Line fail at - " + panel.whereLine1);
              linePlace2.setText("aka " + panel.whereLine2);
              boxScore1.setText("");
              boxScore2.setText("");
              panel.resetLineThere();
            }
            else if (panel.lineThere==0) {                                           //valid line if does not already exist
              prevMoveAnValid.setText("Valid");
              linePlace1.setText("Line success - " + panel.whereLine1);
              linePlace2.setText("" + panel.whereLine2);
              boxScore1.setText("" + panel.whereBox1);
              boxScore2.setText("" + panel.whereBox2);
            }
                
            getScoreReturnVal=panel.getScore();
//            System.out.println("" + getScoreReturnVal);
            panel.setPlyrInitials();

            if (panel.setPlyrInitials()==0) {                                        //checks if initials are the same
              lblSettings.setText("Settings : Game in Progress");          //method in gameboard
              lblSettings2.setText("");                              //sets player 1 initial to 1
            }                                                                        //and player 2 initial to 2
            else {                                                                   //if initials are the same
              lblSettings.setText("Settings : Game in Progress");
              lblSettings2.setText("");
            }
                
            if (getScoreReturnVal==1) {
              lblSettings.setText("Settings : Game Over");
              lblCurrentTurnWho.setText("Game Over");

	      if (panel.plyrOneScore>panel.plyrTwoScore) {
                lblSettings2.setText("Player 1 - " + panel.plyOneName + " WINS");
              }
              else if (panel.plyrOneScore<panel.plyrTwoScore) {
                lblSettings2.setText("Player 2 - " + panel.plyTwoName + " WINS");
              }
	      else {
                lblSettings2.setText("Tie, you both WIN");
              }
            }
            else {
              lblCurrentTurnWho.setText("Player " + panel.getTurn());
            }

            lblPlayerOneScore.setText("Player 1 (" + panel.plyOneInitial + ") : " + panel.plyrOneScore);
            lblPlayerTwoScore.setText("Player 2 (" + panel.plyTwoInitial + ") : " + panel.plyrTwoScore);
            prevMoveAnPlyr.setText("Click by Player " + prevPlyrTurn + " - " + prevPlyrName);
            btnReset.setText("Quit");
            
            panel.repaint();
	      }
        }
      });

      panel.setBounds(50, 50, col*50+5, row*50+5);
      contentPane.add(panel);
		
      btnReset = new JButton("Start");
      btnReset.addMouseListener(new MouseAdapter() {                                    //listener set for the start button
      @Override                                                                         //must click start button to begin
        public void mouseClicked(MouseEvent resetG) {                                   //the game
          int startValReturn=panel.setStart(startVar);
          startVar=startValReturn;
          panel.setPlyrInitials();

          if (startValReturn==0) {
            
            nt.pw.println("Q");
            nt.pw.flush();
/*
        	panel.resetGame();
            btnReset.setText("Start");
	        txtPlayer_1.setText("Enter Name 1");
            txtPlayer_2.setText("Enter Name 2");
            lblCurrentTurnWho.setText("Player 1");
            lblPlayerOneScore.setText("Player 1 (1) : " + panel.plyrOneScore);
            lblPlayerTwoScore.setText("Player 2 (2) : " + panel.plyrTwoScore);
            lblSettings.setText("Settings : Enter Player Names");
            lblSettings2.setText("Press Start Button to Begin");
            prevMoveAnValid.setText("");
            prevMoveAnPlyr.setText("");
            linePlace1.setText("");
            linePlace2.setText("");
            boxScore1.setText("");
            boxScore2.setText("");
*/
          }
          else if (startValReturn==1) {
            if (ServerClient==1) {
//              panel.startServer();
              nt=new MyThread(1,"none");
              nt.saveParent(netDotIn);
              nt.start();
              nt.pw.println(txtPlayer_1.getText());
              nt.plyrName1=txtPlayer_1.getText();
              nt.pw.flush();
              //              System.out.println("I am Server");
	      }
	      else if (ServerClient==2) {
	    	connectIP=txtConnection.getText();
            nt=new MyThread(2,connectIP);
            nt.saveParent(netDotIn);
            nt.start();
            nt.pw.println(txtPlayer_2.getText());
            nt.plyrName2=txtPlayer_2.getText();
            nt.pw.flush();
//	    	System.out.println("I am Client");
	      }
            panel.setPlyrInitials();
            btnReset.setText("Quit");
            if (panel.setPlyrInitials()==0) {
              lblSettings.setText("Settings : Game in Progress");
              lblSettings2.setText("");
            }
            else {
              lblSettings.setText("Settings : Game in Progress");
              lblSettings2.setText("");
            }
          }    
          panel.repaint();
        }
      });

	btnReset.setBounds(525, 10, 165, 25);
        contentPane.add(btnReset);

        JLabel lblPlayer_1 = new JLabel("Player 1");
        lblPlayer_1.setBounds(525, 55, 60, 15);
        contentPane.add(lblPlayer_1);

        txtPlayer_1 = new JTextField();
        txtPlayer_1.setEnabled(true);
        txtPlayer_1.addKeyListener(new KeyAdapter() {                                           //key listener set for the name
          @Override                                                                             //fields and sets the initials
          public void keyReleased(KeyEvent e) {
            panel.setPlyrInitials();

//          if (panel.setPlyrInitials()==0) {
//            lblSettings.setText("Settings : Warning same first initial");
//          }

            lblPlayerOneScore.setText("Player 1 (" + panel.plyOneInitial + ") : " + panel.plyrOneScore);
            lblPlayerTwoScore.setText("Player 2 (" + panel.plyTwoInitial + ") : " + panel.plyrTwoScore);
            panel.repaint();
          }
        });
        txtPlayer_1.setText("Enter Name 1");
        txtPlayer_1.setBounds(595, 55, 114, 19);
        contentPane.add(txtPlayer_1);
        txtPlayer_1.setColumns(10);

        JLabel lblPlayer_2 = new JLabel("Player 2");
        lblPlayer_2.setBounds(525, 80, 60, 15);
        contentPane.add(lblPlayer_2);
	
        txtPlayer_2 = new JTextField();
        txtPlayer_2.setEnabled(false);
        txtPlayer_2.addKeyListener(new KeyAdapter() {
        @Override
          public void keyReleased(KeyEvent e) {
            panel.setPlyrInitials();

//          if (panel.setPlyrInitials()==0) {
//            lblSettings.setText("Settings : Warning same first initial");
//          }

            lblPlayerOneScore.setText("Player 1 (" + panel.plyOneInitial + ") : " + panel.plyrOneScore);
            lblPlayerTwoScore.setText("Player 2 (" + panel.plyTwoInitial + ") : " + panel.plyrTwoScore);
            panel.repaint();
          }
        });
    
        txtPlayer_2.setText("Enter Name 2");
        txtPlayer_2.setBounds(595, 80, 114, 19);
        contentPane.add(txtPlayer_2);
        txtPlayer_2.setColumns(10);
	
        txtConnection = new JTextField();
        txtConnection.setEnabled(false);
        txtConnection.setText("Server Address");
        txtConnection.setBounds(595, 105, 114, 19);
        contentPane.add(txtConnection);
        txtConnection.setColumns(10);

        lblSettings = new JLabel("Settings : Enter Player Names");
        lblSettings.setBounds(525, 135, 370, 15);
        contentPane.add(lblSettings);
	
        lblSettings2 = new JLabel("Press Start Button to Begin");
        lblSettings2.setBounds(525, 155, 370, 15);
        contentPane.add(lblSettings2);
	
        JLabel lblScore = new JLabel("Score - > ");
        lblScore.setBounds(10, 10, 95, 15);
        contentPane.add(lblScore);

        lblPlayerOneScore = new JLabel("Player 1 (1) : 0");
        lblPlayerOneScore.setBounds(100, 10, 125, 15);
        contentPane.add(lblPlayerOneScore);

        lblPlayerTwoScore = new JLabel("Player 2 (2) : 0");
        lblPlayerTwoScore.setBounds(245, 10, 125, 15);
        contentPane.add(lblPlayerTwoScore);

        JLabel lblGameNews = new JLabel("GAME INFO");
        lblGameNews.setBounds(525, 190, 115, 15);
        contentPane.add(lblGameNews);

        JLabel lblCurrentTurn = new JLabel("Current Turn >> ");
        lblCurrentTurn.setBounds(525, 210, 145, 15);
        contentPane.add(lblCurrentTurn);
		
        lblCurrentTurnWho = new JLabel("Player 1");
        lblCurrentTurnWho.setBounds(655, 210, 300, 15);
        contentPane.add(lblCurrentTurnWho);

        JLabel lblPreviousMove = new JLabel("Previous Click Analysis :");
        lblPreviousMove.setBounds(525, 235, 184, 15);
        contentPane.add(lblPreviousMove);

        prevMoveAnPlyr = new JLabel("");
        prevMoveAnPlyr.setBounds(545, 270, 350, 15);
        contentPane.add(prevMoveAnPlyr);

        prevMoveAnValid = new JLabel("");
        prevMoveAnValid.setBounds(545, 310, 250, 15);
        contentPane.add(prevMoveAnValid);

        linePlace1 = new JLabel("");
        linePlace1.setBounds(535, 340, 350, 15);
        contentPane.add(linePlace1);

        linePlace2 = new JLabel("");
        linePlace2.setBounds(610, 360, 350, 15);
        contentPane.add(linePlace2);

        boxScore1 = new JLabel("");
        boxScore1.setBounds(535, 375, 350, 15);
        contentPane.add(boxScore1);

        boxScore2 = new JLabel("");
        boxScore2.setBounds(535, 395, 350, 15);
        contentPane.add(boxScore2);

        JSeparator separator1 = new JSeparator();
        separator1.setBounds(535, 185, 250, 2);
        contentPane.add(separator1);

        JSeparator separator2 = new JSeparator();
        separator2.setBounds(535, 230, 250, 2);
        contentPane.add(separator2);
        
        rdbtnServer = new JRadioButton("Server");
        rdbtnServer.addMouseListener(new MouseAdapter() {
        @Override
          public void mouseClicked(MouseEvent arg0) {
            ServerClient=1;
            txtPlayer_1.setEnabled(true);
            txtPlayer_2.setEnabled(false);
            txtConnection.setEnabled(false);
            btnReset.setText("Start");
          }
        });
        rdbtnServer.setSelected(true);
        buttonGroup.add(rdbtnServer);
        rdbtnServer.setBounds(730, 55, 149, 23);
        contentPane.add(rdbtnServer);

        rdbtnClient = new JRadioButton("Client");
        rdbtnClient.addMouseListener(new MouseAdapter() {
        @Override
          public void mouseClicked(MouseEvent e) {
            ServerClient=2;
            txtPlayer_1.setEnabled(false);
            txtPlayer_2.setEnabled(true);
            txtConnection.setEnabled(true);
            btnReset.setText("Connect");
          }
        });
	buttonGroup.add(rdbtnClient);
        rdbtnClient.setBounds(730, 80, 149, 23);
        contentPane.add(rdbtnClient);
        
    }
 
  public void setInitialThIn () {
            panel.setPlyrInitials();
  }
  public void sendQuit() {
	  nt.pw.println("Q");
	  nt.pw.flush();
  }
  
    public void clickUpdate(int plyTurnThIn, int xThIn, int yThIn) {
        	int xFromTh=xThIn;
        	int yFromTh=yThIn;

        	panel.playerTurn=plyTurnThIn;
            int prevPlyrTurn=panel.playerTurn;
            String prevPlyrName;

        	if (prevPlyrTurn==1) {
              prevPlyrName=panel.plyOneName;
            }
            else {
              prevPlyrName=panel.plyTwoName;
            }    
        	
//        	System.out.println("Before clickUpdate addClick");
        	panel.addClick(xFromTh, yFromTh);
//        	System.out.println("After clickUpdate addClick");

        	if (panel.lineThere==1) {                                                //checks if line already exits
              prevMoveAnValid.setText("Invalid, line already exists");               //invalid if line already exists
              linePlace1.setText("Line fail at - " + panel.whereLine1);
              linePlace2.setText("aka " + panel.whereLine2);
              boxScore1.setText("");
              boxScore2.setText("");
              panel.resetLineThere();
            }
            else if (panel.lineThere==0) {                                           //valid line if does not already exist
              prevMoveAnValid.setText("Valid");
              linePlace1.setText("Line success - " + panel.whereLine1);
              linePlace2.setText("" + panel.whereLine2);
              boxScore1.setText("" + panel.whereBox1);
              boxScore2.setText("" + panel.whereBox2);
            }
                
            getScoreReturnVal=panel.getScore();
//            System.out.println("" + getScoreReturnVal);
            panel.setPlyrInitials();

            if (panel.setPlyrInitials()==0) {                                        //checks if initials are the same
              lblSettings.setText("Settings : Warning same first initial");          //method in gameboard
              lblSettings2.setText("Game in Progress");                              //sets player 1 initial to 1
            }                                                                        //and player 2 initial to 2
            else {                                                                   //if initials are the same
              lblSettings.setText("Settings : Game in Progress");
              lblSettings2.setText("");
            }
                
            if (getScoreReturnVal==1) {
              lblSettings.setText("Settings : Game Over");
              lblCurrentTurnWho.setText("Game Over");

	      if (panel.plyrOneScore>panel.plyrTwoScore) {
                lblSettings2.setText("Player 1 - " + panel.plyOneName + " WINS");
              }
              else if (panel.plyrOneScore<panel.plyrTwoScore) {
                lblSettings2.setText("Player 2 - " + panel.plyTwoName + " WINS");
              }
	      else {
                lblSettings2.setText("Tie, you both WIN");
              }
            }
            else {
              lblCurrentTurnWho.setText("Player " + panel.getTurn());
            }

            lblPlayerOneScore.setText("Player 1 (" + panel.plyOneInitial + ") : " + panel.plyrOneScore);
            lblPlayerTwoScore.setText("Player 2 (" + panel.plyTwoInitial + ") : " + panel.plyrTwoScore);
            prevMoveAnPlyr.setText("Click by Player " + prevPlyrTurn + " - " + prevPlyrName);
            btnReset.setText("Quit");
            panel.repaint();
        }
  }