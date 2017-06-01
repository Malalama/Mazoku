package com.example.f.myfirstapp;

/**
 * Created by f on 31/05/2017.
 */


import android.content.Context;

import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;



public class MazeCreator{

//
//	We do the XML game de-serialization in the Constructor
//	And we have another function to retrieve a particular game based on a number
//

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

    private Maze[] mazes;

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

    public Maze[] getMazes() {return this.mazes;}
    public Maze getMazeN(int n){return this.mazes[n];}
    public void setMazes(Maze[] mazes) {this.mazes = mazes;}

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

    /**
     * TO IMPROVE
     * This constructor deserializes an XML file into a list of Maze objects
     * Probably not a good thing to do. Would be preferable to use a method for this
     * Also, add a parameter for the XML file name, not hard coded
     * @param context
     * @throws IOException
     */
    public MazeCreator(Context context) throws IOException{

        //1) Get stuff from Asset folder
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        inputStream = context.getResources().openRawResource(R.raw.game2); //assetManager.open("game2.xml");

        //2) De-serialization using the SimpleXMLFramework
        Serializer serializer = new Persister();
        Mazes mazes = null;
        try {
            mazes = serializer.read(Mazes.class, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setMazes(mazes.getMazes());

    }

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
     * 					Beginning of CLASS METHODS...
     * ***********************************************************************
     *************************************************************************/

    /**
     * Based on a Maze number, returns a Maze object. (So far so good)
     * Now, WHY don't we just use the GETTER "public Maze getMazeN(int n)" ???
     * @param mazeNo
     * @param context
     * @return
     * @throws IOException
     */
    public Maze getMaze(int mazeNo, Context context) throws IOException {

        Maze maze = null;

        int GridDimensionX = 0;
        int GridDimensionY = 0;
        boolean[][] vLines;
        boolean[][] hLines;

        int[] pathX = new int[]{0};
        int[] pathY = new int[]{0};
        int[][] pPoints = null;

        String[][] oOperators;

        maze = new Maze();


        Maze maze2 = null;
        maze2 = getMazeN(mazeNo);

        maze2.PrintCells();

        pPoints = maze2.getTilesPoints();
        oOperators = maze2.getOperators();

        System.out.println("After mazes oOperators");

        GridDimensionX = maze2.getMazeWidth();
        GridDimensionY = maze2.getMazeHeight();
        System.out.println("After getMazeHeight");

        maze.setStartPosition(maze2.getBeginX(), maze2.getBeginY());
        maze.setFinalPosition(maze2.getFinalX(), maze2.getFinalY());
        maze.setVictoryScore(maze2.getVictoryScore());

        System.out.println("After setVictoryScore");





        if (GridDimensionX == GridDimensionY){

            switch (GridDimensionX){
                case 2:
                    vLines = new boolean[][]{{false ,false}, {false ,false}};
                    hLines = new boolean[][]{{false ,false}, {false ,false}};
                    break;

                case 3:
                    vLines = new boolean[][]{{false ,false, false}, {false ,false, false}, {false ,false, false}};
                    hLines = new boolean[][]{{false ,false, false}, {false ,false, false}, {false ,false, false}};
                    break;

                default:
                    vLines = new boolean[][]{{false ,false}, {false ,false}};
                    hLines = new boolean[][]{{false ,false}, {false ,false}};
                    break;
            }
        }
        else {

            //if (GridDimensionX == 2 && GridDimensionY == 3){
            vLines = new boolean[][]{{false ,false}, {false ,false}, {false ,false}};
            hLines = new boolean[][]{{false ,false}, {false ,false}, {false ,false}};


            //vLines = new boolean[][]{{false ,false, false}, {false ,false, false}};
            //hLines = new boolean[][]{{false ,false, false}, {false ,false, false}};

            //}
        }

        maze.setMazeID(mazeNo);
        maze.setTilesPoints(pPoints);
        maze.setVerticalLines(vLines);
        maze.setHorizontalLines(hLines);
        maze.setOperators(oOperators);
        maze.setPathX(pathX);
        maze.setPathY(pathY);
        maze.setBeenThere(GridDimensionX, GridDimensionY);
        maze.setMazeWidth(GridDimensionX);
        maze.setMazeHeight(GridDimensionY);

        return maze;
    }


    /*************************************************************************
     * ***********************************************************************
     * 					...End of CLASS METHODS...
     * ***********************************************************************
     *************************************************************************/

}
