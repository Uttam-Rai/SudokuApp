package com.example.sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CompleteSudoku
{
    int board[][];
    Random random;
    List<List<Integer>> row;
    List<List<Integer>> col;
    List<List<Integer>> block;

    public CompleteSudoku(int board[][])
    {
        this.board = board;
        random = new Random();
        row = new ArrayList<List<Integer>>();
        col = new ArrayList<List<Integer>>();
        block = new ArrayList<List<Integer>>();
        initialize();
    }

    void initialize()
    {
        for(int i=0; i<9; i++)
        {
            row.add(new ArrayList<Integer>());
            col.add(new ArrayList<Integer>());
            block.add(new ArrayList<Integer>());
        }
    }

    boolean fullSudoku(int i,int j)
    {
        if(i==9)
        {
            return true;
        }
        int ii = (j==8) ? i+1 : i;
        int jj = (j==8) ? 0 : j+1;

        if(board[i][j]!=0)
        {
            return fullSudoku(ii, jj);
        }
        List<Integer> list = new ArrayList<Integer>();
        for(int l=1; l<10; l++)
        {
            if(isValid(i, j, l))
            {
                list.add(l);
            }
        }
        while(!list.isEmpty())
        {
            int index = random.nextInt(list.size());
            int k = (i/3)+(int)(j/3)*3;
            int num = list.remove(index);
            row.get(i).add(num);
            col.get(j).add(num);
            block.get(k).add(num);
            board[i][j] = num;
            if(fullSudoku(ii, jj))
            {
                return true;
            }
            board[i][j] = 0;
            row.get(i).remove(Integer.valueOf(num));
            col.get(j).remove(Integer.valueOf(num));
            block.get(k).remove(Integer.valueOf(num));
        }
        return false;
    }

    boolean isValid(int i,int j,int num)
    {
        int k = (i/3)+(int)(j/3)*3;
        return (!row.get(i).contains(num) && !col.get(j).contains(num) && !block.get(k).contains(num));
    }
}
