package com.example.f.myfirstapp;

/**
 * Created by f on 31/05/2017.
 */

import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;

@Root
public class Mazes {

    @ElementArray(entry="Maze", required=false)
    private Maze[] mazes;

    Mazes(){}

    public Mazes(Maze[] mazes) {
        this.mazes = mazes;
    }

    public Maze[] getMazes(){
        return this.mazes;
    }

}