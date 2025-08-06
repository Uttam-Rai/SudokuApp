package com.example.sudoku;

import java.util.ArrayList;
import java.util.List;

public class SudokuSolver
{
    List<List<Integer>> list1;
    List<List<Integer>> list2;
    List<List<Integer>> list3;
    int board[][];
    int tempBoard[][];
    int totalSolution;

    public SudokuSolver(int board[][])
    {
        this.board = board;
        list1 = new ArrayList<List<Integer>>();
        list2 = new ArrayList<List<Integer>>();
        list3 = new ArrayList<List<Integer>>();
        tempBoard = new int[9][9];
        totalSolution = 0;
        intialize();
    }

    void intialize()
    {
        for (int i = 0; i < 9; i++)
        {
            System.arraycopy(board[i], 0, tempBoard[i], 0, 9);
        }

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

    boolean solveSudoku(int i,int j,List<List<Integer>> set1,List<List<Integer>> set2,List<List<Integer>> set3)
    {
        List<List<Integer>> list1 = new ArrayList<List<Integer>>(set1);
        List<List<Integer>> list2 = new ArrayList<List<Integer>>(set2);
        List<List<Integer>> list3 = new ArrayList<List<Integer>>(set3);

        if(i==9)
        {
            if(totalSolution==0)
            {
                for (int k = 0; k < 9; k++)
                {
                    System.arraycopy( tempBoard[k], 0, board[k], 0, 9);
                }
            }
            totalSolution++;
            return true;
        }
        else
        {
            int i2 = (j==8) ? i+1 : i;
            int j2 = (j==8) ? 0 : j+1;

            if(tempBoard[i][j]!=0)
            {
                return solveSudoku(i2,j2,list1,list2,list3);
            }
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
                tempBoard[i][j] = k;
                solveSudoku(i2,j2,list1,list2,list3);
                tempBoard[i][j] = 0;
                list1.get(i).remove(list1.get(i).size()-1);
                list2.get(j).remove(list2.get(j).size()-1);
                list3.get(l).remove(list3.get(l).size()-1);
            }
            return false;
        }
    }

    int totalPossibleSolution()
    {
        solveSudoku(0, 0, list1, list2, list3);
        return totalSolution;
    }

}
