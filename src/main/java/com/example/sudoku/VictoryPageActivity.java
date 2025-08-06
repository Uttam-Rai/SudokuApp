package com.example.sudoku;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import de.dlyt.yanndroid.sudoku.R;

public class VictoryPageActivity extends Activity
{
    TextView tvScore, tvTime, tvDifficulty, tvBestTime;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_victory_page);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        tvScore = findViewById(R.id.tvScore);
        tvTime = findViewById(R.id.tvTime);
        tvBestTime = findViewById(R.id.tvBestTime);
        tvDifficulty = findViewById(R.id.tvDifficulty);

        tvScore.setText(""+getIntent().getIntExtra("score",0));
        int diff = getIntent().getIntExtra("difficulty",30);

        if(diff==10)
        {
            tvDifficulty.setText("Easy");
        }
        else if(diff==30)
        {
            tvDifficulty.setText("Medium");
        }
        else
        {
            tvDifficulty.setText("Hard");
        }

        tvTime.setText(timeConversion(getIntent().getLongExtra("time",0)));
        tvBestTime.setText(timeConversion(getIntent().getLongExtra("bestTime",0)));
    }

    private String timeConversion(long time)
    {
        Long sec = (long)(time/1000) % 60;
        Long min = (long)(time/(1000*60)) % 60;
        Long hr = (long)(time/(1000*60*60)) % 24;

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
        String t = h+" : "+m+" : "+s;
        return t;
    }
}