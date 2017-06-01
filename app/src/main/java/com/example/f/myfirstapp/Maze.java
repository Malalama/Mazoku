package com.example.f.myfirstapp;

/**
 * Created by f on 31/05/2017.
 */



import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;

public class Maze implements Serializable {

    //
    //
    //
    //
    //
    //
    //

    /*************************************************************************
     * ***********************************************************************
     * 					Beginning of VARIABLES DECLARATION...
     * ***********************************************************************
     *************************************************************************/
    //Stuff with @Element and @ElementArray on top are used for XML (de-)serialization

    @Element
    private int sizeX;
    @Element
    private int sizeY;
    @Element
    private int beginX;
    @Element
    private int beginY;
    @Element
    private int finalX;
    @Element
    private int finalY;
    @ElementArray(entry="TilePoint", required=false)
    private int[][] TilesPoints;
    @ElementArray(entry="oOperator", required=false)
    private String[][] oOperators;
    @Element
    private int intVictoryScore;

    private static final long serialVersionUID = 1L;
    public static final int UP = 0, DOWN = 1, RIGHT = 2, LEFT = 3;
    private boolean[][] verticalLines, horizontalLines;
    private int currentX, currentY;
    private int[] pathX, pathY;
    private int mazeID;
    private boolean gameComplete;
    private int TotalPoints;
    private boolean[][] beenThere;

    /*************************************************************************
     * ***********************************************************************
     * 					...End of VARIABLES DECLARATION
     * ***********************************************************************
     *************************************************************************/

    //
    //
    //
    //
    //
    //
    //

    /*************************************************************************
     * ***********************************************************************
     * 					Beginning of GETTERS AND SETTERS...
     * ***********************************************************************
     *************************************************************************/

    public int getMazeWidth() {return sizeX;}
    public int getMazeHeight() {return sizeY;}
    public void setMazeWidth(int sizeX) {this.sizeX = sizeX;}
    public void setMazeHeight(int sizeY) {this.sizeY = sizeY;}
    public int getMazeID() {return mazeID;}
    public void setMazeID(int mazeID) {this.mazeID = mazeID;}
    public int[] getPathX() {return pathX;}
    public int[] getPathY() {return pathY;}
    public void setPathX(int[] x) {pathX = x;}
    public void setPathY(int[] y) {pathY = y;}
    public int getTotalPoints() {return TotalPoints;}
    public boolean[][] getBeenThere(){return beenThere;}
    public int getFinalX() {return finalX;}
    public int getFinalY() {return finalY;}
    public int getCurrentX() {return currentX;}
    public int getCurrentY() {return currentY;}
    public int getBeginX() {return beginX;}
    public int getBeginY() {return beginY;}
    public int[][] getTilesPoints() {return TilesPoints;}
    public void setTilesPoints(int[][] points) {TilesPoints = points;}
    public boolean[][] getHorizontalLines() {return horizontalLines;}
    public void setHorizontalLines(boolean[][] lines) {horizontalLines = lines;}
    public boolean[][] getVerticalLines() {return verticalLines;}
    public void setVerticalLines(boolean[][] lines) {verticalLines = lines;}
    public void setOperators(String[][] oOperators) {this.oOperators = oOperators;}
    public String[][] getOperators(){return oOperators;}
    public void setVictoryScore(int VictoryScore){intVictoryScore = VictoryScore;}
    public int getVictoryScore(){return intVictoryScore;}


    public void setBeenThereOneCell(int x, int y, boolean val){this.beenThere[x][y] = val;}

    public boolean getCellInPath(int x, int y){
        boolean b = false;

        for(int l =0; l < pathX.length;l++){
            if (pathX[l] == x && pathY[l] == y) b = true;
        }

        return b;
    }

    public int getPositionOfCellInPath(int x, int y){
        int i = 0;

        for(int l =0; l < pathX.length;l++){
            if (pathX[l] == x && pathY[l] == y) i = l;
        }

        return i;
    }

    public boolean getBeenThereOneCell(int x, int y){
        return this.beenThere[x][y];
    }

    public void setStartPosition(int x, int y) {
        currentX = x;
        currentY = y;
        beginX = x;
        beginY = y;
    }


    public void setFinalPosition(int x, int y) {
        finalX = x;
        finalY = y;
    }


    /*************************************************************************
     * ***********************************************************************
     * 					...End of GETTERS AND SETTERS
     * ***********************************************************************
     *************************************************************************/

    //
    //
    //
    //
    //
    //
    //

    /*************************************************************************
     * ***********************************************************************
     * 					Beginning of CONSTRUCTORS...
     * ***********************************************************************
     *************************************************************************/

    public Maze(){}


    /*************************************************************************
     * ***********************************************************************
     * 					...End of CONSTRUCTORS...
     * ***********************************************************************
     *************************************************************************/

    //
    //
    //
    //
    //
    //
    //

    /*************************************************************************
     * ***********************************************************************
     * 					Beginning of UTILS...
     * ***********************************************************************
     *************************************************************************/

    /**
     * Reallocates an array with a new size, and copies the contents
     * of the old array to the new array.
     * @param oldArray  the old array, to be reallocated.
     * @param newSize   the new array size.
     * @return          A new array with the same contents.
     */
    private static Object resizeArray (Object oldArray, int newSize) {

        //We get the current size of the array we want to resize
        int oldSize = java.lang.reflect.Array.getLength(oldArray);

        //We get the type of what is stored in the array
        Class elementType = oldArray.getClass().getComponentType();

        //We create a new array with the same type and with the new size
        Object newArray = java.lang.reflect.Array.newInstance(elementType, newSize);

        //We get the smaller of the current size and the new size
        int preserveLength = Math.min(oldSize, newSize);
        //System.out.println("preserveLength = " + preserveLength);
        //We copy the old array into the new one
        if (preserveLength > 0) System.arraycopy(oldArray, 0, newArray, 0, preserveLength);

        //We return the new array
        return newArray;

    }

    /**
     * Print information about Cells
     * Useful when debugging the XML (de-) serialization
     */
    public void PrintCells(){
        for (int row = 0; row < TilesPoints.length; row ++)
            for (int col = 0; col < TilesPoints[row].length; col++)
                System.out.println("cells[" + row + "][" + col + "] points = " + TilesPoints[row][col]);
        System.out.println("intVictoryScore = " + intVictoryScore);
        System.out.println("sizeX = " + sizeX);
        System.out.println("sizeY = " + sizeY);
        System.out.println("beginX = " + beginX);
        System.out.println("beginY = " + beginY);
        System.out.println("finalX = " + finalX);
        System.out.println("finalY = " + finalY);
    }

    /**
     * TO REMOVE
     * Initializes the array beenThere, based on a grid dimension (works only for squares)
     * @param GridDimension
     */
    public void setBeenThere(int GridDimension){
        switch (GridDimension) {
            case 2:  beenThere = new boolean[][]{{false ,false}, {false ,false}};
                break;
            case 3:  beenThere = new boolean[][]{{false ,false, false}, {false ,false, false}, {false ,false, false}};
                break;
            case 4:  beenThere = new boolean[][]{{false ,false, false, false}, {false ,false, false, false}, {false ,false, false, false}, {false ,false, false, false}};
                break;
            default:
                break;
        }
    }

    /**
     * TO IMPROVE
     * Initializes the array beenThere, based on a grid dimensions (works for rectangles)
     * @param GridDimensionX
     * @param GridDimensionY
     */
    public void setBeenThere(int GridDimensionX, int GridDimensionY){

        if (GridDimensionX == GridDimensionY){

            switch (GridDimensionX) {
                case 2:  beenThere = new boolean[][]{{false ,false}, {false ,false}};
                    break;
                case 3:  beenThere = new boolean[][]{{false ,false, false}, {false ,false, false}, {false ,false, false}};
                    break;
                case 4:  beenThere = new boolean[][]{{false ,false, false, false}, {false ,false, false, false}, {false ,false, false, false}, {false ,false, false, false}};
                    break;
                default:
                    break;
            }
        }
        else {

            if ((GridDimensionX == 2 && GridDimensionY == 3) || (GridDimensionX == 3 && GridDimensionY == 2)) {
                beenThere = new boolean[][]{{false ,false}, {false ,false}, {false ,false}};
                //beenThere = new boolean[][]{{false ,false, false}, {false ,false, false}};
            }
        }
    }
    public void setBeenThereAllFalse(int GridDimensionX, int GridDimensionY){

        for (int i = 0; i< GridDimensionX; i++){
            for (int j = 0; j< GridDimensionY; j++){
                beenThere[i][j] = false;
            }
        }

    }


    /*************************************************************************
     * ***********************************************************************
     * 					...End of UTILS...
     * ***********************************************************************
     *************************************************************************/

    //
    //
    //
    //
    //
    //
    //

    /*************************************************************************
     * ***********************************************************************
     * 					Beginning of CLASS METHODS...
     * ***********************************************************************
     *************************************************************************/


    /**
     * Calculates the number of points
     * Using the path, the array of tiles points
     * and the array of operators (+, -, *, /)
     */
    public void update_nb_points(){
        //We count the number of points the path made
        TotalPoints = 0;


        for(int l =0; l < pathX.length;l++){

            //We exclude the start point
            if (!(pathX[l] == 0 && pathY[l]==0)){


                //TotalPoints = TotalPoints + TilesPoints[pathX[l]][pathY[l]];
                System.out.println("oOperators[pathX[l]][pathY[l]]: " + oOperators[pathX[l]][pathY[l]]);
                if (oOperators[pathX[l]][pathY[l]].equals("+")){
                    System.out.println("oOperators[pathX[l]][pathY[l]] == '+'");
                    //TotalPoints = TotalPoints + TilesPoints[pathX[l]][pathY[l]];
                    TotalPoints = TotalPoints + TilesPoints[pathY[l]][pathX[l]];
                }
                else if (oOperators[pathX[l]][pathY[l]].equals("-")){
                    System.out.println("oOperators[pathX[l]][pathY[l]] == '-'");
                    TotalPoints = TotalPoints - TilesPoints[pathX[l]][pathY[l]];
                }
                else if (oOperators[pathX[l]][pathY[l]].equals("*")){
                    System.out.println("oOperators[pathX[l]][pathY[l]] == '*'");
                    TotalPoints = TotalPoints * TilesPoints[pathX[l]][pathY[l]];
                }
                else if (oOperators[pathX[l]][pathY[l]].equals("/")){
                    TotalPoints = TotalPoints / TilesPoints[pathX[l]][pathY[l]];
                }

            }


        }

    }

    /**
     * Updates the path of chosen by the player
     * For each new cell visited, we check if the player is not coming back on his/her way
     * @param newX - X position of the new cell visited
     * @param newY - Y position of the new cell visited
     */
    public void update_path(int newX, int newY){

        System.out.println("Calling update_path with newX = " + newX + " and newY = " + newY);

        int l;
        l = pathX.length;

        System.out.println("current pathX.length = " + l);


        if ((l > 1) && (newX == pathX[l-2] && newY == pathY[l-2])) {


            //We are coming back on our feet
            System.out.println("We are coming back on our feet");
            pathX = (int[]) resizeArray(pathX, l-1);
            pathY = (int[]) resizeArray(pathY, l-1);

        }
        //else if (newX == beginX && newY == beginY) {
        else if (getCellInPath(newX, newY)){
            //We clicked on the starting cell: reinitialize the array
            System.out.println("We are coming back to the an cell already in the path");

            int p;
            p = getPositionOfCellInPath(newX, newY);

            pathX = (int[]) resizeArray(pathX, p+1);
            pathY = (int[]) resizeArray(pathY, p+1);
            //setBeenThereAllFalse(sizeX,sizeY);
            //this.beenThere[newX][newY] = true;
            currentX = newX;
            currentY = newY;




        }
        else {

            //We are NOT coming back on our feet
            System.out.println("We are NOT coming back on our feet");
            pathX = (int[]) resizeArray(pathX, l+1);
            pathY = (int[]) resizeArray(pathY, l+1);

            pathX[l]= newX;
            pathY[l]= newY;
        }


        l = pathX.length;

        System.out.println("new pathX.length = " + l);



        //We reset all the elements of the beenThere array to false
        setBeenThere(3);
        if (l>0){
            for (int i = 0; i < l; i++) {
                beenThere[pathX[i]][pathY[i]]= true;
            }
        }

    }


    /**
     * Updates the current X-Y position based on a direction
     * Calls "isGameComplete" if there was a move (-> could probably be done somewhere else)	 *
     * @param direction
     * @return
     */
    public boolean move(int direction) {
        boolean moved = false;


        if(direction == UP) {
            if(currentY != 0) {
                //currentY--;
                currentY = currentY - 1;
                moved = true;
            }
        }
        if(direction == DOWN) {
            if(currentY != sizeY-1) {
                //currentY++;
                currentY = currentY + 1;
                moved = true;
            }
        }
        if(direction == RIGHT) {

            if(currentX != sizeX-1) {
                //currentX++;
                currentX = currentX + 1;
                moved = true;
            }
        }
        if(direction == LEFT) {
            if(currentX != 0) {
                //currentX--;
                currentX = currentX - 1;
                moved = true;
            }
        }


        if(moved) {

            isGameComplete();

        }
        return moved;
    }

    /**
     * If the player reached the end,
     * updates the number of points and check if they are <= to the victory score.
     * POTENTIALLY TO IMPROVE: check the whole path for the ending position, not only the current position.
     * Is it possible that the player goes too fast and we don't get the time to check if he/she won??
     * @return boolean - victory or not
     */
    public boolean isGameWon(){

        if (currentX == finalX && currentY == finalY){
            update_nb_points();

            System.out.println("IsGameWon --- getTotalPoints() = " + getTotalPoints());
            System.out.println("IsGameWon --- getVictoryScore() = " + getVictoryScore());

            if(getTotalPoints() <= getVictoryScore()){
                return true;
            }
            else return false;
        }
        else return false;

    }

    /**
     * TO CLEAN
     * Returns the values of isGameComplete()
     * USELESS
     * @return
     */

    public boolean isCurrentCellEndCell(){
        if (currentX == finalX && currentY == finalY){
            return true;
        }
        else return false;
    }

    public boolean isGameComplete() {
        gameComplete = isGameWon();
        return gameComplete;
    }






    /*************************************************************************
     * ***********************************************************************
     * 					...End of CLASS METHODS...
     * ***********************************************************************
     *************************************************************************/








}
