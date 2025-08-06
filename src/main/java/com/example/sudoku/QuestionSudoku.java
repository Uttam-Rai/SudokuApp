package com.example.sudoku;

import java.util.Random;

class QuestionSudoku
{
    int fullBoard[][];
    int empty;
    int createBoard[][];
    int tempBoard[][];
    Random random;
    CompleteSudoku cs;
    SudokuSolver ss;

    public QuestionSudoku(int createBoard[][],int empty)
    {
        this.createBoard = createBoard;
        this.empty = empty;
        random = new Random();
        fullBoard = new int [9][9];
        tempBoard = new int [9][9];
        cs = new CompleteSudoku(fullBoard);
        cs.fullSudoku(0, 0);
        initialize();
    }

    void initialize()
    {
        for(int i=0; i<9; i++)
        {
            for(int j=0; j<9; j++)
            {
                createBoard[i][j] = fullBoard[i][j];
            }
        }
    }
    void createQuestionSudoku()
    {
        for(int i=0; i<empty; i++)
        {
            int j = random.nextInt(9);
            int k = random.nextInt(9);

            if(createBoard[j][k]==0)
            {
                i--;
            }
            else
            {
                createBoard[j][k] = 0;
                for(int m=0; m<9; m++)
                {
                    for(int n=0; n<9; n++)
                    {
                        tempBoard[m][n] = createBoard[m][n];
                    }
                }

                ss = new SudokuSolver(tempBoard);
                if(ss.totalPossibleSolution()!=1)
                {
                    i--;
                    createBoard[j][k] = fullBoard[j][k];
                }
            }
        }

    }
}
