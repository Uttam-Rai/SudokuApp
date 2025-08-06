package com.example.sudoku;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.dlyt.yanndroid.sudoku.R;

public class BackTrackingActivity extends Activity implements Runnable
{
    LinearLayout llBoard;
    TextView cell[][][][];
    Thread th;
    BackTrackingSolver bts;

    int llId[][] = {{R.id.ll11,R.id.ll12,R.id.ll13},
            {R.id.ll21,R.id.ll22,R.id.ll23},
            {R.id.ll31,R.id.ll32,R.id.ll33}};

    int blockId[][] = {{R.id.block11,R.id.block12,R.id.block13},
            {R.id.block21,R.id.block22,R.id.block23},
            {R.id.block31,R.id.block32,R.id.block33}};

    int celId[][] = {{R.id.cell11,R.id.cell12,R.id.cell13},
            {R.id.cell21,R.id.cell22,R.id.cell23},
            {R.id.cell31,R.id.cell32,R.id.cell33}};

    SharedPreferences share;
    int empty;
    int board[][] = new int[9][9];
    QuestionSudoku qs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_tracking);

        th = new Thread(this);

        cell = new TextView[3][3][3][3];
        llBoard = findViewById(R.id.llBoard);
        View inBoard = findViewById(R.id.inBoard);
        board = new int[9][9];

        empty = getIntent().getIntExtra("empty", 30);
        qs = new QuestionSudoku(board, empty);
        qs.createQuestionSudoku();

        if (inBoard instanceof LinearLayout)
        {
            LinearLayout innerLayout = (LinearLayout) inBoard;

            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    LinearLayout ll = innerLayout.findViewById(llId[i][j]);
                    View block = innerLayout.findViewById(blockId[i][j]);

                    if (block instanceof LinearLayout)
                    {
                        for (int k = 0; k < 3; k++)
                        {
                            for (int l = 0; l < 3; l++)
                            {
                                cell[i][j][k][l] = block.findViewById(celId[k][l]);
                                int finalI = i;
                                int finalJ = j;
                                int finalK = k;
                                int finalL = l;
                            }
                        }
                    }
                }
            }
        }

        for(int i=0; i<9; i++)
        {
            for(int j=0; j<9; j++)
            {
                if(board[i][j] != 0)
                {
                    int blockRow = i / 3;
                    int blockCol = j / 3;
                    int cellRow = i % 3;
                    int cellCol = j % 3;
                    cell[blockRow][blockCol][cellRow][cellCol].setText(""+board[i][j]);
                }
            }
        }
        th.start();
    }

    @Override
    public void run()
    {
        try
        {
            bts = new BackTrackingSolver(board,cell,getApplicationContext());
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}