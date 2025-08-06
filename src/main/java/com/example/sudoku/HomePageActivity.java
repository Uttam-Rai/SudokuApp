package com.example.sudoku;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import de.dlyt.yanndroid.sudoku.R;

public class HomePageActivity extends Activity {
    Button btContinueGame, btNewGame, btBackTracking;
    SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btContinueGame = findViewById(R.id.btContinueGame);
        btNewGame = findViewById(R.id.btNewGame);
        btBackTracking = findViewById(R.id.btBackTracking);

        share = getSharedPreferences("continue",MODE_PRIVATE);

        btNewGame.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showPopupMenu(v);
            }
        });

        btBackTracking.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showPopupBack(v);
            }
        });

        btContinueGame.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(share.getInt("empty",0)==0)
                {
                    Toast.makeText(getApplicationContext(), "No continue Game", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent i = new Intent(getApplicationContext(),ContinueGameActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private void showPopupMenu(View v)
    {
        PopupMenu pop = new PopupMenu(getApplicationContext(),v);
        pop.getMenuInflater().inflate(R.menu.popup_difficulty,pop.getMenu());


        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                Intent i = new Intent(getApplicationContext(), NewGameActivity.class);

                switch (item.getTitle().toString())
                {
                    case "Easy" :
                        i.putExtra("empty",10);
                        startActivity(i);
                        return true;

                    case "Medium" :
                        i.putExtra("empty",30);
                        startActivity(i);
                        return true;

                    case "Hard" :
                        i.putExtra("empty",50);
                        startActivity(i);
                        return true;

                    default: return false;
                }
            }
        });
        pop.show();
    }
    private void showPopupBack(View v)
    {
        PopupMenu pop = new PopupMenu(getApplicationContext(),v);
        pop.getMenuInflater().inflate(R.menu.popup_difficulty,pop.getMenu());


        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                Intent i = new Intent(getApplicationContext(), BackTrackingActivity.class);
                switch (item.getTitle().toString())
                {
                    case "Easy" :
                        i.putExtra("empty",10);
                        startActivity(i);
                        return true;

                    case "Medium" :
                        i.putExtra("empty",30);
                        startActivity(i);
                        return true;

                    case "Hard" :
                        i.putExtra("empty",50);
                        startActivity(i);
                        return true;

                    default: return false;
                }
            }
        });
        pop.show();
    }
}