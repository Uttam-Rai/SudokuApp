package com.example.sudoku;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import de.dlyt.yanndroid.sudoku.R;


public class ContinueGameActivity extends Activity implements Runnable
{
    LinearLayout llBoard;
    TextView cell[][][][];
    TextView tvScore, tvTimer, tvDifficulty, tvMistakes, tvAllowedMistakes;
    String gameDuration;
    Long startTime,pauseTime, pauseStart, continueTime;
    boolean gameOver;
    int score, mistakes, empty;
    SharedPreferences share;
    SharedPreferences continueShare;
    int llId[][] = {{R.id.ll11,R.id.ll12,R.id.ll13},
            {R.id.ll21,R.id.ll22,R.id.ll23},
            {R.id.ll31,R.id.ll32,R.id.ll33}};

    int blockId[][] = {{R.id.block11,R.id.block12,R.id.block13},
            {R.id.block21,R.id.block22,R.id.block23},
            {R.id.block31,R.id.block32,R.id.block33}};

    int celId[][] = {{R.id.cell11,R.id.cell12,R.id.cell13},
            {R.id.cell21,R.id.cell22,R.id.cell23},
            {R.id.cell31,R.id.cell32,R.id.cell33}};

    int btNumId[] = {R.id.btNum1,R.id.btNum2,R.id.btNum3,
            R.id.btNum4,R.id.btNum5,R.id.btNum6,
            R.id.btNum7,R.id.btNum8,R.id.btNum9};

    int btCountId[] = {R.id.btCount1, R.id.btCount2, R.id.btCount3,
            R.id.btCount4, R.id.btCount5, R.id.btCount6,
            R.id.btCount7, R.id.btCount8, R.id.btCount9};

    Button btNum[];
    Button btCount[];
    TextView tvSelected;
    int selectedI,selectedJ;
    int hint;
    List<TextView> tvAdjacent;
    int board[][];
    int fullBoard[][];
    List<Integer> availableNum;
    ImageView imgHint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_game);

        board = new int[9][9];
        fullBoard = new int[9][9];

        cell = new TextView[3][3][3][3];
        llBoard = findViewById(R.id.llBoard);
        View inBoard = findViewById(R.id.inBoard);
        imgHint = findViewById(R.id.imgHint);
        tvScore = findViewById(R.id.tvScore);
        tvTimer = findViewById(R.id.tvTimer);
        pauseTime = 0L;
        pauseStart = 0L;
        gameOver = false;
        tvDifficulty = findViewById(R.id.tvDifficulty);
        tvMistakes = findViewById(R.id.tvMistakes);
        tvAllowedMistakes = findViewById(R.id.tvAllowedMistakes);
        tvAdjacent = new ArrayList<>();
        btNum = new Button[9];
        btCount = new Button[9];
        board= new int[9][9];

        continueShare = getSharedPreferences("continue",MODE_PRIVATE);
        empty = continueShare.getInt("empty",30);
        share = getSharedPreferences(""+empty,MODE_PRIVATE);
        tvAllowedMistakes.setText(""+empty);
        hint = continueShare.getInt("hint",0);
        score = continueShare.getInt("score",0);
        mistakes = continueShare.getInt("mistake",0);
        tvScore.setText("SCORE : "+score);
        tvMistakes.setText(""+mistakes);
        continueTime = continueShare.getLong("time",0);

        if(empty==10)
        {
            tvDifficulty.setText("Easy");
        }
        else if(empty==30)
        {
            tvDifficulty.setText("Medium");
        }
        else
        {
            tvDifficulty.setText("Hard");
        }

        availableNum = new ArrayList<>();
        StringTokenizer stBoard = new StringTokenizer(continueShare.getString("board",""),",");
        StringTokenizer stFull = new StringTokenizer(continueShare.getString("fullBoard",""),",");

        for(int i=0; i<9; i++)
        {
            for(int j=0; j<9; j++)
            {
                board[i][j] = Integer.parseInt(stBoard.nextToken());
                fullBoard[i][j] = Integer.parseInt(stFull.nextToken());
            }
        }

        for(int i=0; i<9; i++)
        {
            btNum[i] = findViewById(btNumId[i]);
            btCount[i] = findViewById(btCountId[i]);
            availableNum.add(i+1);

            int finalI = i;
            btNum[i].setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int n = Integer.parseInt(btCount[finalI].getText().toString());
                    if(n>0)
                    {
                        if(tvSelected!=null)
                        {
                            if(!tvSelected.getText().toString().equals(""))
                            {
                                int nn = Integer.parseInt(tvSelected.getText().toString());
                                int mm = Integer.parseInt(btCount[nn-1].getText().toString()) + 1;
                                btCount[nn-1].setText(""+mm);
                                if(mm==0)
                                {
                                    availableNum.add(nn);
                                }
                            }
                            n = Integer.parseInt(btCount[finalI].getText().toString())-1;
                            btCount[finalI].setText(""+n);
                            if(n==0)
                            {
                                availableNum.remove(Integer.valueOf(finalI+1));
                            }
                            tvSelected.setText(""+(finalI+1));
                            if(fullBoard[selectedI][selectedJ]==finalI+1)
                            {
                                score++;
                                tvScore.setText("SCORE : "+score);
                                board[selectedI][selectedJ] = finalI+1;
                                tvSelected = null;
                                Toast.makeText(getApplicationContext(), "Correct position...!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                score--;
                                mistakes++;
                                tvMistakes.setText(""+mistakes);
                                tvScore.setText("SCORE : "+score);
                                if(mistakes==empty)
                                {
                                    gameOver = true;
                                    Toast.makeText(getApplicationContext(), "You Loose", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), HomePageActivity.class);
                                    startActivity(i);
                                }

                            }
                            if(availableNum.isEmpty())
                            {
                                boolean nonZero = true;
                                for(int ii=0; ii<9; ii++)
                                {
                                    for(int jj=0; jj<9; jj++)
                                    {
                                        if(board[ii][jj]==0)
                                        {
                                            nonZero = false;
                                            break;
                                        }
                                    }
                                }
                                if(nonZero)
                                {
                                    gameOver = true;
                                    gameDuration = tvTimer.getText().toString();
                                    Long thisTime = System.currentTimeMillis() - startTime - pauseTime + continueTime;
                                    Long bestTime = share.getLong("bestTime",Long.MAX_VALUE);
                                    SharedPreferences.Editor edit = share.edit();
                                    edit.putLong("bestTime",Math.min(thisTime,bestTime));
                                    edit.apply();
                                    Intent i = new Intent(getApplicationContext(),VictoryPageActivity.class);
                                    i.putExtra("difficulty",empty);
                                    i.putExtra("score",score);
                                    i.putExtra("time",thisTime);
                                    i.putExtra("bestTime",share.getLong("bestTime",0));
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }
                    }
                }
            });
        }

        if(inBoard instanceof LinearLayout)
        {
            LinearLayout innerLayout = (LinearLayout) inBoard;

            for(int i=0; i<3; i++)
            {
                for(int j=0; j<3; j++)
                {
                    LinearLayout ll = innerLayout.findViewById(llId[i][j]);
                    View block = innerLayout.findViewById(blockId[i][j]);

                    if(block instanceof LinearLayout)
                    {
                        for(int k=0; k<3; k++)
                        {
                            for(int l=0; l<3; l++)
                            {
                                cell[i][j][k][l] = block.findViewById(celId[k][l]);
                                int finalI = i;
                                int finalJ = j;
                                int finalK = k;
                                int finalL = l;
                                cell[i][j][k][l].setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        int ik = (finalI*3) + finalK;
                                        int jl = (finalJ*3) + finalL;

                                        if(board[ik][jl]!=0)
                                        {
                                            return;
                                        }
                                        if(tvSelected!=null)
                                        {
                                            tvSelected.setBackground(getDrawable(R.drawable.cell));
                                        }
                                        if(tvSelected==cell[finalI][finalJ][finalK][finalL])
                                        {
                                            try
                                            {
                                                int n = Integer.parseInt(tvSelected.getText().toString());
                                                btCount[n-1].setText(""+(Integer.parseInt(btCount[n-1].getText().toString())+1));
                                            }
                                            catch (Exception e)
                                            {
                                                // Do nothing
                                            }
                                            tvSelected.setText("");
                                            tvSelected = null;
                                            while(!tvAdjacent.isEmpty())
                                            {
                                                tvAdjacent.remove(0).setBackground(getDrawable(R.drawable.cell));
                                            }
                                            return;
                                        }
                                        while(!tvAdjacent.isEmpty())
                                        {
                                            tvAdjacent.remove(0).setBackground(getDrawable(R.drawable.cell));
                                        }
                                        for(int ii=0; ii<3; ii++)
                                        {
                                            for(int jj=0; jj<3; jj++)
                                            {
                                                cell[ii][finalJ][jj][finalL].setBackground(getDrawable(R.drawable.cell_adjecent));
                                                cell[finalI][ii][finalK][jj].setBackground(getDrawable(R.drawable.cell_adjecent));
                                                cell[finalI][finalJ][ii][jj].setBackground(getDrawable(R.drawable.cell_adjecent));
                                                tvAdjacent.add(cell[finalI][finalJ][ii][jj]);
                                                tvAdjacent.add(cell[ii][finalJ][jj][finalL]);
                                                tvAdjacent.add(cell[finalI][ii][finalK][jj]);
                                            }
                                        }
                                        cell[finalI][finalJ][finalK][finalL].setBackground(getDrawable(R.drawable.cell_on_select));
                                        tvSelected = cell[finalI][finalJ][finalK][finalL];
                                        selectedI = (3*finalI) + finalK;
                                        selectedJ = (3*finalJ) + finalL;
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
        imgHint.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(hint==0)
                {
                    return;
                }
                if(tvSelected!=null)
                {
                    score--;
                    tvScore.setText("Score : "+score);
                    if(!tvSelected.getText().toString().equals(""))
                    {
                        int n = Integer.parseInt(tvSelected.getText().toString());
                        int m = Integer.parseInt(btCount[n-1].getText().toString())+1;
                        btCount[n-1].setText(""+m);
                        if(m==1)
                        {
                            availableNum.add(n);
                        }
                    }
                    tvSelected.setText(""+fullBoard[selectedI][selectedJ]);
                    board[selectedI][selectedJ] = fullBoard[selectedI][selectedJ];
                    int n = Integer.parseInt(btCount[board[selectedI][selectedJ]-1].getText().toString())-1;
                    btCount[board[selectedI][selectedJ]-1].setText(""+n);
                    if(n==0)
                    {
                        availableNum.remove(Integer.valueOf(board[selectedI][selectedJ]));
                    }
                    if(availableNum.isEmpty())
                    {
                        boolean nonZero = true;
                        for(int ii=0; ii<9; ii++)
                        {
                            for(int jj=0; jj<9; jj++)
                            {
                                if(board[ii][jj]==0)
                                {
                                    nonZero = false;
                                    break;
                                }
                            }
                        }
                        if(nonZero)
                        {
                            gameOver = true;
                            gameDuration = tvTimer.getText().toString();
                            Long thisTime = System.currentTimeMillis() - startTime - pauseTime + continueTime;
                            Long bestTime = share.getLong("bestTime",Long.MAX_VALUE);
                            SharedPreferences.Editor edit = share.edit();
                            edit.putLong("bestTime",Math.min(thisTime,bestTime));
                            edit.apply();
                            Intent i = new Intent(getApplicationContext(),VictoryPageActivity.class);
                            i.putExtra("difficulty",empty);
                            i.putExtra("score",score);
                            i.putExtra("time",thisTime);
                            i.putExtra("bestTime",share.getLong("bestTime",Long.MAX_VALUE));
                            startActivity(i);
                            finish();
                        }
                    }
                    hint--;
                }
            }
        });

        for(int i=0; i<9; i++)
        {
            for(int j=0; j<9; j++)
            {
                if(board[i][j] != 0)
                {
                    int n = Integer.parseInt(btCount[board[i][j]-1].getText().toString())-1;
                    if(n==0)
                    {
                        availableNum.remove(Integer.valueOf(board[i][j]));
                    }
                    btCount[board[i][j]-1].setText(""+n);
                    int blockRow = i / 3;
                    int blockCol = j / 3;
                    int cellRow = i % 3;
                    int cellCol = j % 3;
                    cell[blockRow][blockCol][cellRow][cellCol].setText(""+board[i][j]);
                }
            }
        }
        startTime = System.currentTimeMillis();
        new Thread(this).start();
    }

    private void updateTimerText(long timePeriod)
    {
        int sec = (int)(timePeriod/1000) % 60;
        int min = (int)(timePeriod/(1000*60)) % 60;
        int hr = (int)(timePeriod/(1000*60*60)) % 24;

        String h = ""+hr;
        String m = ""+min;
        String s = ""+sec;

        if(hr<10)
        {
            h = "0"+h;
        }
        if(min<10)
        {
            m = "0"+m;
        }
        if(sec<10)
        {
            s = "0"+s;
        }
        tvTimer.setText(h+" : "+m+" : "+s);
    }

    @Override
    public void run()
    {
        while(!Thread.currentThread().isInterrupted())
        {
            updateTimerText(System.currentTimeMillis() - startTime - pauseTime + continueTime);
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        pauseStart = System.currentTimeMillis();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        pauseTime += System.currentTimeMillis() - pauseStart;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        SharedPreferences.Editor edit = continueShare.edit();
        if(!gameOver)
        {
            StringBuilder sbBoard = new StringBuilder();
            StringBuilder sbFull = new StringBuilder();
            StringBuilder sbCount = new StringBuilder();
            for (int i=0; i<9; i++)
            {
                sbCount.append(btCount[i].getText().toString()).append(",");
                for (int j=0; j<9; j++)
                {
                    sbBoard.append(board[i][j]).append(",");
                    sbFull.append(fullBoard[i][j]).append(",");
                }
            }
            long thisTime = System.currentTimeMillis() - startTime - pauseTime + continueTime;
            edit.putString("board",sbBoard.toString());
            edit.putString("fullBoard",sbFull.toString());
            edit.putInt("empty",empty);
            edit.putInt("mistake",mistakes);
            edit.putInt("score",score);
            edit.putInt("hint",hint);
            edit.putLong("time",thisTime);
            edit.apply();
        }
        else
        {
            edit.putString("board","0");
            edit.putInt("empty",0);
            edit.putString("fullBoard","0");
            edit.putInt("mistake",0);
            edit.putInt("score",0);
            edit.putInt("hint",0);
            edit.putLong("time",0);
            edit.apply();
        }
    }
}