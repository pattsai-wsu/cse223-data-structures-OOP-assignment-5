import java.util.LinkedList;

// NAME: Patrick Tsai
// CLASS: CSE223 M-F 10 AM
// DUE DATE: 6/05/2020 8 AM
// PROGRAM NAME: NetDot

public class Box {
  int row;
  int col;
  int boxWin;
  LinkedList<Integer> boxLL;
 
  public Box(int i, int j) {           //constructor - box uses linked list to keep track of side
    row=i;                             //has a "win" field to keep track of play win
    col=j;
    boxWin=0;
    boxLL=new LinkedList<Integer>();   //LL sides
    boxLL.add(0);                      //initially all set to 0
    boxLL.add(0);
    boxLL.add(0);
    boxLL.add(0);
  }

  public int addLine(String id) {     //addLine method - an id (eg top) is given
    switch(id) {                      //if the associated box does not have a line there
      case "top":                     //set a 1 in linked list
        if (boxLL.get(0)==1) {        //if already set to 1, then do nothing return 0 for invalid move
          return(1);
        }
        else {
          boxLL.set(0,1);
          return(0);
        }
      case "left":
        if (boxLL.get(1)==1) {
          return(1);
        }
        else {
          boxLL.set(1,1);
          return(0);
        }
      case "right":
        if (boxLL.get(2)==1) {
          return(1);
        }
        else {
          boxLL.set(2,1);
          return(0);
        }
      case "bottom":
        if (boxLL.get(3)==1) {
          return(1);
        }
        else {
          boxLL.set(3,1);
          return(0);
        }
    }
  return(0);
  }

  public String toString() {          //this was used for testing purposes to see the print out of box wins   
    String output=" " + row + "," + col + " who's box =" + boxWin;
    return (output);
  }
}

