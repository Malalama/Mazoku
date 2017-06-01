package com.example.f.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Handler;
import android.os.SystemClock;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    private GridView g;

    public Log log;

    //For the timer
    private Button startButton;
    private Button pauseButton;
    private Button nextButton;
    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    //End of --- For the timer

    private int mazeID;
    Maze maze;
    List<String> items = new ArrayList<String>();
    boolean[][] beenThere;
    boolean dragging = false;
    private Context context;
    boolean gameWon = false;
    boolean gameLost = false;
    boolean firstMoveHappened = false;
    long timePenalties = 0L;


    //FC - Crossfade stuff - 2014 08 13
    private View mSplashView;
    private View mGameView;
    private int mShortAnimationDuration;
    //End of FC - Crossfade stuff - 2014 08 13


    @Override
    public void onCreate(Bundle icicle) {


        Log.d("aaa", "bbb");

        //super(context);
        this.context = (Activity)context;

        super.onCreate(icicle);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        g = (GridView)findViewById(R.id.myGrid);
        //g.setBackgroundColor(Color.WHITE);
        g.setVerticalSpacing(1);
        g.setHorizontalSpacing(1);

        mazeID++;
        prepareGame();

        addListenerToGrid();



        //For the timer
        timerValue = (TextView) findViewById(R.id.timerValue);
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);
            }
        });
        pauseButton = (Button) findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);
            }
        });
        //End of --- For the timer

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (gameWon){
                    mazeID++;
                    prepareGame();
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                }

            }
        });




        mGameView = findViewById(R.id.root);
        //mSplashView = findViewById(R.id.SplashTextView);
        // Initially hide the content view.
        //mGameView.setVisibility(View.GONE);
        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = 1000; //getResources().getInteger(android.R.integer.config_shortAnimTime);


    }



    ///**
     //* Cross-fades between {@link #mContentView} and {@link #mLoadingView}.
     //*/

    @SuppressLint("NewApi")
    private void CrossFader() {
        // Decide which view to hide and which to show.
        final View showView = mGameView;
        final View hideView = mSplashView;

        // Set the "show" view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        showView.setAlpha(0f);
        showView.setVisibility(View.VISIBLE);

        // Animate the "show" view to 100% opacity, and clear any animation listener set on
        // the view. Remember that listeners are not limited to the specific animation
        // describes in the chained method calls. Listeners are set on the
        // ViewPropertyAnimator object for the view, which persists across several
        // animations.
        showView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        // Animate the "hide" view to 0% opacity. After the animation ends, set its visibility
        // to GONE as an optimization step (it won't participate in layout passes, etc.)
        hideView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hideView.setVisibility(View.GONE);
                    }
                });
    }



    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            //FC
            updatedTime += timePenalties;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);

            timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));

            customHandler.postDelayed(this, 0);

        }
    };


    private Maze CreateMaze(MazeCreator mazecreator){
        try {
            return mazecreator.getMaze(mazeID, getApplicationContext());
        }
        catch (IOException e) {e.printStackTrace(); return null;}
    }

    private void prepareGame() {
        // We need a list of strings for the list items



        MazeCreator mazecreator = null;


        try {mazecreator = new MazeCreator(getApplicationContext());} catch (IOException e) {e.printStackTrace();}

        //final Maze maze = CreateMaze(mazecreator);
        maze = CreateMaze(mazecreator);

        int[][] TilesPoints = maze.getTilesPoints();
        items.clear();
        for(int i=0;i<3;i++) {
            for(int j=0;j<3;j++) {
                items.add(Integer.toString(TilesPoints[i][j]));
            }
        }
        items.set(0, "Start"); // = "B";
        items.set(8, "Finish");

        firstMoveHappened = false;
        gameWon = false;

        //We draw our Grid View
        drawGridView(maze, items);




    }

    public int getXFromPosition(int p){
        switch (p){
            case 0: return 0;
            case 1: return 1;
            case 2: return 2;
            case 3: return 0;
            case 4: return 1;
            case 5: return 2;
            case 6: return 0;
            case 7: return 1;
            case 8: return 2;
            default: return 0;
        }
    }
    public int getYFromPosition(int p){
        switch (p){
            case 0: return 0;
            case 1: return 0;
            case 2: return 0;
            case 3: return 1;
            case 4: return 1;
            case 5: return 1;
            case 6: return 2;
            case 7: return 2;
            case 8: return 2;
            default: return 0;
        }
    }

    public void drawGridView(final Maze maze, List<String> items){

        ArrayAdapter<String> notes = new ArrayAdapter<String>(getBaseContext(), R.layout.my_item_view, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int x_pos = getXFromPosition(position);
                int y_pos = getYFromPosition(position);
                int[][] TilesPoints = maze.getTilesPoints();

                beenThere = maze.getBeenThere();
                //System.out.println("Position (" + position + ")  ->  XY (" + x_pos + y_pos + ")");



                if (beenThere[x_pos][y_pos]==true) {
                    if (gameWon){
                        view.setBackgroundColor(Color.argb(255, 102, 204, 0));
                    }
                    else if (gameLost){

                        view.setBackgroundColor(Color.argb(255, 255, 0, 51));
                    }
                    else{
                        view.setBackgroundColor(Color.LTGRAY);
                    }
                }

                return view;
            }
        };

        g.setAdapter(notes);

    }


    public void addListenerToGrid() {



       /* g.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });*/



        g.setOnTouchListener(new AdapterView.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent me) {




                int action = me.getActionMasked();
                float touchX = me.getX();
                float touchY = me.getY();
                int position = g.pointToPosition((int) touchX, (int) touchY);

                //We get the current (X,Y) position on our maze
                int currentX = maze.getCurrentX();
                int currentY = maze.getCurrentY();

                int totalCellWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        (float) 120, getResources().getDisplayMetrics());
                int totalCellHeight = totalCellWidth;

                int cellX = 0;
                int cellY = 0;
                boolean moved;

                //As soon as the game is won, we disable the moves
                if (gameWon == false){

                    switch (me.getAction() & MotionEvent.ACTION_MASK) {

                        //Touch gesture started: the screen was previously not touched and now is.
                        //... So, the player has one finger on a cell of our maze
                        case MotionEvent.ACTION_DOWN:

                            gameLost = false;

                            if (firstMoveHappened == false){
                                firstMoveHappened = true;
                                startTime = SystemClock.uptimeMillis();
                                customHandler.postDelayed(updateTimerThread, 0);
                            }

                            System.out.println("MotionEvent.ACTION_DOWN");

                            cellX = (int)Math.floor(touchX/totalCellWidth);
                            cellY = (int)Math.floor(touchY/totalCellHeight);


                            System.out.println("Previous: " + currentX + "," + currentY + " and New: " + cellX + "," + cellY);

                            //maze.setBeenThereOneCell(cellX, cellY, !beenThere[cellX][cellY]);


                            if (((cellX - currentX == 1 || cellX - currentX == -1) && (cellY == currentY))
                                    || ((cellY - currentY == 1 || cellY - currentY == -1) && (cellX == currentX))){

                                System.out.println("The new cell is a neighbor of the previous one");

                                moved = false;

                                if (cellX-currentX ==  1) moved = maze.move(Maze.RIGHT);
                                if (cellX-currentX == -1) moved = maze.move(Maze.LEFT);
                                if (cellY-currentY ==  1) moved = maze.move(Maze.DOWN);
                                if (cellY-currentY == -1) moved = maze.move(Maze.UP);

                                //If we moved to a different cell, we redraw the view
                                if(moved) {

                                    System.out.println("Yep, it moved");

                                    beenThere[cellX][cellY] = true;
                                    maze.update_path(cellX,  cellY);

                                    currentX = maze.getCurrentX();
                                    currentY = maze.getCurrentY();
                                    beenThere[currentX][currentY] = true;

                                    drawGridView(maze, items);
                                }



                                //If we reached the last cell
                                if(maze.isCurrentCellEndCell()){


                                    //If the game is complete, we count the number of points and show the finish dialog if needed
                                    if(maze.isGameComplete()) {
                                        System.out.println("The Game is Finished!");
                                        maze.update_nb_points();
                                        gameWon = maze.isGameWon();

                                        timeSwapBuff += timeInMilliseconds;
                                        customHandler.removeCallbacks(updateTimerThread);
                                        drawGridView(maze, items);
                                    }
                                    else {
                                        gameLost = true;
                                        drawGridView(maze, items);
                                        //Penatly for losing :)
                                        timePenalties += 10000;
                                    }


                                }

                            }
                            //Else if the cell pressed/selected is on the path
                            else if (maze.getCellInPath(cellX, cellY)){
                                System.out.println("Have we been there yet (" + cellX + "," + cellY + "): " + maze.getBeenThereOneCell(cellX, cellY));

                                // We rebuild the path
                                System.out.println("If the cell pressed/selected is on the path");
                                maze.update_path(cellX, cellY);

                                // Then we redraw the view
                                beenThere[cellX][cellY] = true;
                                maze.update_path(cellX,  cellY);

                                currentX = maze.getCurrentX();
                                currentY = maze.getCurrentY();
                                beenThere[currentX][currentY] = true;

                                drawGridView(maze, items);

                            }

                            if(cellX == currentX && cellY == currentY) {

                                System.out.println("Dragging...");

                                //touch gesture in the cell where the ball is
                                dragging = true;
                            }

                            break;

                        case MotionEvent.ACTION_UP:

                            System.out.println("case MotionEvent.ACTION_UP");

                            //touch gesture completed
                            dragging = false;
                            //return true;

                            break;

                        case MotionEvent.ACTION_MOVE:

                            gameLost = false;

                            System.out.println("case MotionEvent.ACTION_MOVE");

                            if(dragging) {

                                System.out.println("DRAGGING");

                                cellX = (int)Math.floor(touchX/totalCellWidth);
                                cellY = (int)Math.floor(touchY/totalCellHeight);

                                if((cellX != currentX && cellY == currentY)
                                        || (cellY != currentY && cellX == currentX)) {
                                    //either X or Y changed
                                    moved = false;

                                    if (cellX-currentX ==  1) moved = maze.move(Maze.RIGHT);
                                    if (cellX-currentX == -1) moved = maze.move(Maze.LEFT);
                                    if (cellY-currentY ==  1) moved = maze.move(Maze.DOWN);
                                    if (cellY-currentY == -1) moved = maze.move(Maze.UP);

                                    //If we moved to a different cell, we redraw the view
                                    if(moved) {
                                        gameWon = maze.isGameWon();
                                        System.out.println("MOVED");

                                        beenThere[cellX][cellY] = true;
                                        maze.update_path(cellX,  cellY);
                                        drawGridView(maze, items);
                                    }

                                    //If we reached the last cell
                                    if(maze.isCurrentCellEndCell()){


                                        //If the game is complete, we count the number of points and show the finish dialog if needed
                                        if(maze.isGameComplete()) {
                                            System.out.println("The Game is Finished!");
                                            maze.update_nb_points();
                                            gameWon = maze.isGameWon();

                                            timeSwapBuff += timeInMilliseconds;
                                            customHandler.removeCallbacks(updateTimerThread);
                                            drawGridView(maze, items);
                                        }
                                        else {
                                            gameLost = true;
                                            drawGridView(maze, items);
                                            //Penatly for losing :)
                                            timePenalties += 10000;
                                        }


                                    }

                                }
                                return true;


                            }
                            break;

                        default: break;


                    }
                }
                return true;
            }
        });
    }


    /**
     * Informs the user if he/she won
     * TO DO: think about a more graphically pleasant way to congratulate the player
     */
/*	void showFinishDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		//builder.setTitle(context.getText(R.string.finished_title) + " --- Points: " + maze.getTotalPoints());

		if (maze.getTotalPoints() == maze.getVictoryScore()){
			builder.setTitle("Congrats, you won (Yours: " + maze.getTotalPoints() + " = Victory: " + maze.getVictoryScore());
		}
		else if (maze.getTotalPoints() < maze.getVictoryScore()){
			builder.setTitle("Congrats, you beat the game creator (Yours: " + maze.getTotalPoints() + " < Victory: " + maze.getVictoryScore());
		}
		else {
			builder.setTitle("Not the best score, try again (Yours: " + maze.getTotalPoints() + " > Victory: " + maze.getVictoryScore());
		}

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.finish, null);
		builder.setView(view);
		final AlertDialog finishDialog = builder.create();
		View closeButton =view.findViewById(R.id.closeGame);
		closeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View clicked) {
				if(clicked.getId() == R.id.closeGame) {
					finishDialog.dismiss();
					((Activity)context).finish();
				}
			}
		});
		finishDialog.show();
	}*/

}
