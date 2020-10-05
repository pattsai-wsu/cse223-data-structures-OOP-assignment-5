import java.awt.Graphics;
import java.util.Scanner;

import javax.swing.JPanel;

// NAME: Patrick Tsai
// CLASS: CSE223 M-F 10 AM
// DUE DATE: 6/05/2020 8 AM
// PROGRAM NAME: NetDot

public class GamePanel extends JPanel{
  private static final long serialVersionUID = 1L;
  NetDot parent;     // this is the object we belong to
  int row;
  int col;
  int totalGameBoxes;
  int cellWidth;
  int cellHeight;
  int plyrOneScore;
  int plyrTwoScore;
  int playerTurn=1;
  int lineThere=0;
  int fullBoxTop=0;
  int fullBoxLeft=0;
  int fullBoxRight=0;
  int fullBoxBottom=0;
  Box[][] board;
  char plyOneInitial;
  char plyTwoInitial;
  String[] parsed = null;
  String plyOneName;
  String plyTwoName;
  String whereBox1;
  String whereBox2;
  String whereLine1;
  String whereLine2;
	
  public GamePanel(int rowSet, int colSet, int cellHeightSet, int cellWidthSet) {
    row=rowSet;
    col=colSet;
    cellHeight=cellHeightSet;
    cellWidth=cellWidthSet;
  
    board=new Box[row][col];

    for (int i=0;i<row;i++) {
      for (int j=0;j<col;j++) {
        board[i][j]=new Box(i,j);
      }
    }
  }
	
  public void paint(Graphics g) {
    super.paint(g);
    drawBoard(g,row,col);
    if (parent==null) return;
  }

  public void drawBoard(Graphics g, int rowTotal, int colTotal) {
//    super.paint(g);     // not sure if I need this if I called super.paint in the paint Method
    int x,y;

    for (int i=0;i<rowTotal;i++) {
      for (int j=0;j<colTotal;j++) {
        int topLine=board[i][j].boxLL.get(0);
        int leftLine=board[i][j].boxLL.get(1);
        int rightLine=board[i][j].boxLL.get(2);
        int bottomLine=board[i][j].boxLL.get(3);

        x=j*cellWidth;
        y=i*cellHeight;

        if (topLine==1) {
          g.drawLine(x, y, x+cellWidth, y);
        }
        if (leftLine==1) {
          g.drawLine(x, y, x, y+cellHeight);	    	 
        }
        if (rightLine==1) {
          g.drawLine(x+cellWidth, y, x+cellWidth, y+cellHeight);
        }
        if (bottomLine==1) {
          g.drawLine(x, y+cellHeight, x+cellWidth, y+cellHeight);	    	 
        }

        if (topLine==1 && leftLine==1 && rightLine==1 && bottomLine==1) {
          int boxWinner=board[i][j].boxWin;
          String boxWinStr="";

          if (boxWinner==1) {
            boxWinStr="" + plyOneInitial;
          }
          if (boxWinner==2) {
            boxWinStr="" + plyTwoInitial;
          }
 
          if (boxWinner!=0) {
            int xCoor=x+23;
            int yCoor=y+27;

            g.drawString(boxWinStr, xCoor, yCoor);
          }
        }

        g.fillOval(x,y,2,2);
        g.fillOval(x+cellWidth,y,2,2);
        g.fillOval(x,y+cellHeight,2,2);
        g.fillOval(x+cellWidth,y+cellHeight,2,2);
      }
    }  
  }
  
  public int addClick(int xInAdd, int yInAdd) {
    String placement="none";
    whereBox1="";
    whereBox2="";
    whereLine1="";
    whereLine2="";

    int playerTurnInAdd=playerTurn;
//    System.out.println("playerTurnInAdd equals =" + playerTurnInAdd);
    
    int rowInAdd=(yInAdd/50);
    if (rowInAdd>=row) {                //takes care if user clicks outside of board (right side)
      rowInAdd=row-1;
    }
//    System.out.println("row equals =" + rowInAdd);

    int colInAdd=(xInAdd/50);
      if (colInAdd>=col) {              //takes care if user clicks outside of board (below)
        colInAdd=col-1;
      }
//    System.out.println("col equals =" + colInAdd);

    int top=rowInAdd*cellHeight;
    int bottom=top+cellHeight;
    int left=colInAdd*cellWidth;
    int right=left+cellWidth;
    int newRowInAdd;
    int newColInAdd;
 
    top=yInAdd-top;
    bottom=bottom-yInAdd;
    left=xInAdd-left;
    right=right-xInAdd;
 
    try {
      if (top<bottom && top<=right && top<=left) {
        placement="top";
        lineThere=board[rowInAdd][colInAdd].addLine(placement);
        fullBoxTop=checkSides(rowInAdd, colInAdd, playerTurnInAdd);
        whereLine1="row [" + rowInAdd + "] col [" + colInAdd + "] " + placement;
        if (fullBoxTop==1) {
          whereBox1="Full box score at row [" + rowInAdd + "] col [" + colInAdd + "]";
        }
        if (rowInAdd>0) {
          newRowInAdd=rowInAdd-1;
          board[rowInAdd-1][colInAdd].addLine("bottom");
          fullBoxBottom=checkSides(rowInAdd-1, colInAdd, playerTurnInAdd);
          whereLine2="aka - row [" + newRowInAdd + "] col [" + colInAdd + "] bottom";
          if (fullBoxBottom==1) {
            whereBox2="Full box score at row [" + newRowInAdd + "] col [" + colInAdd + "]";   	  
          }
        }
      }

      else if (bottom<top && bottom<=left && bottom<=right) {
        placement="bottom";
        lineThere=board[rowInAdd][colInAdd].addLine(placement);
        fullBoxBottom=checkSides(rowInAdd, colInAdd, playerTurnInAdd);
        whereLine1="row [" + rowInAdd + "] col [" + colInAdd + "] " + placement;
        if (fullBoxBottom==1) {
          whereBox1="Full box score at row [" + rowInAdd + "] col [" + colInAdd + "]";
        }
        if (rowInAdd<row-1) {
          newRowInAdd=rowInAdd+1;
          board[rowInAdd+1][colInAdd].addLine("top");
          fullBoxTop=checkSides(rowInAdd+1, colInAdd, playerTurnInAdd);
          whereLine2="aka - row [" + newRowInAdd + "] col [" + colInAdd + "] top";
          if (fullBoxTop==1) {
            whereBox2="Full box score at row [" + newRowInAdd + "] col [" + colInAdd + "]";   	  
          }
        }
      }

      else if (left<=top && left<=bottom && left<right) {
        placement="left";
        lineThere=board[rowInAdd][colInAdd].addLine(placement);
        fullBoxLeft=checkSides(rowInAdd, colInAdd, playerTurnInAdd);
        whereLine1="row [" + rowInAdd + "] col [" + colInAdd + "] " + placement;
        if (fullBoxLeft==1) {
          whereBox1="Full box score at row [" + rowInAdd + "] col [" + colInAdd + "]";
        }
        if (colInAdd>0) {
          newColInAdd=colInAdd-1;
          board[rowInAdd][colInAdd-1].addLine("right");
          fullBoxRight=checkSides(rowInAdd, colInAdd-1, playerTurnInAdd);
          whereLine2="aka - row [" + rowInAdd + "] col [" + newColInAdd + "] right";
          if (fullBoxRight==1) {
            whereBox2="Full box score at row [" + rowInAdd + "] col [" + newColInAdd + "]";   	  
          }
        }
      }

      else if (right<=top && right<=bottom && right<left) {
        placement="right";
        lineThere=board[rowInAdd][colInAdd].addLine(placement);
        fullBoxRight=checkSides(rowInAdd, colInAdd, playerTurnInAdd);
        whereLine1="row [" + rowInAdd + "] col [" + colInAdd + "] " + placement;
        if (fullBoxRight==1) {
          whereBox1="Full box score at row [" + rowInAdd + "] col [" + colInAdd + "]";
        }
        if (colInAdd<col-1) {
          newColInAdd=colInAdd+1;
          board[rowInAdd][colInAdd+1].addLine("left");
          fullBoxLeft=checkSides(rowInAdd, colInAdd+1, playerTurnInAdd);
          whereLine2="aka - row [" + rowInAdd + "] col [" + newColInAdd + "] left";
          if (fullBoxLeft==1) {
            whereBox2="Full box score at row [" + rowInAdd + "] col [" + newColInAdd + "]";   	  
          }
        }
      }
  
      if (fullBoxTop==1 || fullBoxLeft==1 || fullBoxRight==1 || fullBoxBottom==1 || lineThere==1) {
        fullBoxTop=fullBoxLeft=fullBoxRight=fullBoxBottom=0;
        return(playerTurn);
      }
 
      playerTurn=turnNext(playerTurnInAdd);
      return(playerTurn);
      }
      catch (Exception e) {
        return(0);
      }
  }
  
  public void resetLineThere() {
    lineThere=0;
  }

  public int checkSides(int rowInSide, int colInSide, int playerTurnInSides) {
    int checkSideTopVal=board[rowInSide][colInSide].boxLL.get(0);
    int checkSideLeftVal=board[rowInSide][colInSide].boxLL.get(1);
    int checkSideRightVal=board[rowInSide][colInSide].boxLL.get(2);
    int checkSideBottomVal=board[rowInSide][colInSide].boxLL.get(3);

    if (checkSideTopVal==1 && checkSideLeftVal==1 && checkSideRightVal==1 && checkSideBottomVal==1) {
      if(board[rowInSide][colInSide].boxWin==0) {
        board[rowInSide][colInSide].boxWin=playerTurnInSides;
      return(1);                     //only return 1 if the box was not previously won
      }
    }
  return(0);
  }

  public void printBoxes() {         //used for testing - prints out each box with it's linked list of sides
    for (int i=0;i<row;i++) {
      for (int j=0;j<col;j++) {
        System.out.println("dot at placement [" + i + "][" + j + "] = " + board[i][j]);    
        System.out.println("sides LL = " + board[i][j].boxLL);
      }
    }	  
  }

  public int getScore() {
    int bothScores=0;
    totalGameBoxes=row*col;     //uses the row and col variables to calculate the number of boxes on the board
    plyrOneScore=0;
    plyrTwoScore=0;
    int whoWinBox;

    for (int i=0;i<row;i++) {   //counts each users' score by running through the boxes win field
      for (int j=0;j<col;j++) {
        whoWinBox=board[i][j].boxWin;
        if (whoWinBox==1) {
          plyrOneScore++;            	              	  
        }
        if (whoWinBox==2) {
          plyrTwoScore++;            	              	  
        }
      }
    }
                                //if the player scores add to the total number of boxes the game is over
    bothScores=plyrOneScore + plyrTwoScore;
    if (totalGameBoxes==bothScores) {
      return(1);                //returns 1 if that happens
    }

  return(0);
  }

  public String getTurn() {     //returns string stating the user's turn w their name
    if (playerTurn==1) {
      return playerTurn + " - " + plyOneName;		  
    }
    else {
      return playerTurn + " - " + plyTwoName;
    }
  }

  public int turnNext(int playerTurnNext) {
    if (playerTurnNext==1) {     //basically a toggle that switches after each turn
      return (2);
    }
    else if (playerTurnNext==2) {
      return(1);
    }
    else {
      return(3);
    }
  }

  public void resetGame() {       //resets all the box sides to 0 and the win field to 0
    for (int i=0;i<row;i++) {
      for (int j=0;j<col;j++) {
        board[i][j].boxLL.set(0,0);       
        board[i][j].boxLL.set(1,0);       
        board[i][j].boxLL.set(2,0);       
        board[i][j].boxLL.set(3,0);       
        board[i][j].boxWin=0;       
      }
    }

    getScore();
    playerTurn=1;
  return;
  }

  public int setStart(int startValIn) {
    int startValOut;              //this method sets a value for the start/reset button
    if (startValIn==0) {
      startValOut=1;
    }
    else {
      startValOut=0;
    }
  return(startValOut);
  }
  
  public int setPlyrInitials() {
    plyOneName=parent.txtPlayer_1.getText();
    plyTwoName=parent.txtPlayer_2.getText();
    
    try {                          //in case user deletes the field and there is no entry
      plyOneInitial=plyOneName.charAt(0);
    }
    catch (Exception e) {
      plyOneInitial='1';
    }

    try {
      plyTwoInitial=plyTwoName.charAt(0);
    }
    catch (Exception e) {
      plyTwoInitial='2';
    }


    if (plyOneInitial==plyTwoInitial) {
      plyOneInitial='1';
      plyTwoInitial='2';
    return(0);
    }

  return(1);
  }
  
  public void parse(Scanner coordInTo) {
	  for(int i=1;i<4;i++) {
		  while(coordInTo.hasNext()) {
			  parsed[i]=coordInTo.next();
		  }
	  }
  }

  public int convertToInt(String strIn) {
	  int returnVal=0;
	  try {
		returnVal=Integer.parseInt(strIn);
	  }
	  catch(Exception e) {
		  System.out.println("Error, could not convert to int");
	  }
	  return(returnVal);
  }
  
  public void saveParent(NetDot myParent) {
    parent=myParent;
  }
}

