package com.example.jake.LeagueStats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ItemBuilderMenuActivity extends AppCompatActivity {
    private String sumName;
    private String region;
    private boolean rotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_builder_menu);
        // in case we need to get anything sent in from prev activity
        Intent i = getIntent();
        this.sumName = i.getStringExtra("SummonerName");
        this.region = i.getStringExtra("Region");
        this.rotation = i.getBooleanExtra("Rotation", false);

    }

    public void startItemBuilder(View v){

        Intent i = new Intent(this, ChampionSelectActivity.class);
        i.putExtra("SummonerName", sumName);
        i.putExtra("ItemBuilder", true);    // start ItemBuilder
        i.putExtra("Rotation", rotation);
        i.putExtra("Region", region);
        startActivity(i);
    }

}
