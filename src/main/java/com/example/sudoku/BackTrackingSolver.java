package com.example.sudoku;

import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.dlyt.yanndroid.sudoku.R;

public class BackTrackingSolver
{
    List<List<Integer>> list1;
    List<List<Integer>> list2;
    List<List<Integer>> list3;
    int board[][];
    int totalSolution;
    TextView cell[][][][];
    Context cont;

    public BackTrackingSolver(int board[][],TextView cell[][][][],Context cont) throws InterruptedException {
        this.cell = cell;
        this.board = board;
        this.cont = cont;
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        totalSolution = 0;
        intialize();
        solveSudoku(0,0,list1,list2,list3);
    }

    void intialize()
    {
        for(int i=0; i<9; i++)
        {
            list3.add(new ArrayList<Integer>());
            list2.add(new ArrayList<Integer>());
            list1.add(new ArrayList<Integer>());
        }

        for(int i=0; i<9; i++)
        {
            for(int j=0; j<9; j++)
            {
                if(board[i][j]!=0)
                {
                    list1.get(i).add(board[i][j]);
                    int k=(i/3)*3+(j/3);
                    list3.get(k).add(board[i][j]);
                }
                if(board[j][i]!=0)
                {
                    list2.get(i).add(board[j][i]);
                }
            }
        }
    }

    boolean solveSudoku(int i,int j,List<List<Integer>> set1,List<List<Integer>> set2,List<List<Integer>> set3) throws InterruptedException
    {

        List<List<Integer>> list1 = new ArrayList<List<Integer>>(set1);
        List<List<Integer>> list2 = new ArrayList<List<Integer>>(set2);
        List<List<Integer>> list3 = new ArrayList<List<Integer>>(set3);

        if(i==9)
        {
            return true;
        }
        else
        {
            int i2 = (j==8) ? i+1 : i;
            int j2 = (j==8) ? 0 : j+1;

            int blockRow = i / 3;
            int blockCol = j / 3;
            int cellRow = i % 3;
            int cellCol = j % 3;

            if(board[i][j]!=0)
            {
                return solveSudoku(i2,j2,list1,list2,list3);
            }
            Thread.sleep(1000);
            for(int k=1; k<=9; k++)
            {
                int l=(i/3)*3+(j/3);
                if(list1.get(i).contains(k) || list2.get(j).contains(k) || list3.get(l).contains(k))
                {
                    continue;
                }
                list1.get(i).add(k);
                list2.get(j).add(k);
                list3.get(l).add(k);
                cell[blockRow][blockCol][cellRow][cellCol].setBackground(cont.getDrawable(R.drawable.cell_on_select));
                cell[blockRow][blockCol][cellRow][cellCol].setText(""+k);
                board[i][j] = k;
                solveSudoku(i2,j2,list1,list2,list3);
                Thread.sleep(1000);
                board[i][j] = 0;
                cell[blockRow][blockCol][cellRow][cellCol].setText("");
                cell[blockRow][blockCol][cellRow][cellCol].setBackground(cont.getDrawable(R.drawable.cell));
                list1.get(i).remove(list1.get(i).size()-1);
                list2.get(j).remove(list2.get(j).size()-1);
                list3.get(l).remove(list3.get(l).size()-1);
            }
            return false;
        }
    }
}
