package com.example.jake.LeagueStats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class InGameSummonersActivity extends AppCompatActivity {

    private String sumName;
    private String region;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game_summoners);

        Intent i = getIntent();
        this.sumName = i.getStringExtra("SummonerName");
        this.region = i.getStringExtra("Region");
        Toast.makeText(this, sumName, Toast.LENGTH_SHORT).show();
    }

    public void getInGameStatsForSelectedChamp(View v){

        // get the view ID
        int id = v.getId();

        // TODO: Handle individual clicks on summoner

        switch (id) {
            // BLUE
            case R.id.in_game_blue_champ_name_0:
                break;
            case R.id.in_game_blue_champ_name_1:
                break;
            case R.id.in_game_blue_champ_name_2:
                break;
            case R.id.in_game_blue_champ_name_3:
                break;
            case R.id.in_game_blue_champ_name_4:
                break;
            // Pictures
            case R.id.in_game_blue_champ_pic_0:
                break;
            case R.id.in_game_blue_champ_pic_1:
                break;
            case R.id.in_game_blue_champ_pic_2:
                break;
            case R.id.in_game_blue_champ_pic_3:
                break;
            case R.id.in_game_blue_champ_pic_4:
                break;
            // RED
            case R.id.in_game_red_champ_name_0:
                break;
            case R.id.in_game_red_champ_name_1:
                break;
            case R.id.in_game_red_champ_name_2:
                break;
            case R.id.in_game_red_champ_name_3:
                break;
            case R.id.in_game_red_champ_name_4:
                break;
            //Pictures
            case R.id.in_game_red_champ_pic_0:
                break;
            case R.id.in_game_red_champ_pic_1:
                break;
            case R.id.in_game_red_champ_pic_2:
                break;
            case R.id.in_game_red_champ_pic_3:
                break;
            case R.id.in_game_red_champ_pic_4:
                break;

            default:
                break;
        }

        Intent i = new Intent(this, InGameSingleSummonerActivity.class);
        i.putExtra("SummonerName", this.sumName);
        i.putExtra("SelectedSummoner", id);
        startActivity(i);

    }

}
