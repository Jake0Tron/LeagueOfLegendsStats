package com.example.jake.LeagueStats;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class InGameSingleSummonerActivity extends AppCompatActivity {

    private Integer id;
    private String sumName;
    private String region;
    private ArrayList<String> items;
    private ArrayList<String> itemDescrips;
    private ArrayList<Bitmap> itemPics;
    private ListView list;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game_single_summoner);

        // get data passed in from login (SummonerName)
        Intent i = getIntent();
        this.sumName = i.getStringExtra("SummonerName");
        this.region = i.getStringExtra("Region");
        this.id = i.getIntExtra("SelectedSummoner", -1);
        // TODO: QUERY SERVER FOR SELECTED IN GAME PLAYER STATS WITH ID

        this.title = (TextView) findViewById(R.id.in_game_single_summoner_title);
        this.title.setText(sumName);
        // fill array with stats

        // DEBUG
        this.items = new ArrayList<>(6);
        //this.items.add("Rylai's Crystal Sceptre");
        //this.items.add("Luden's Echo");
        //this.items.add("Lich Bane");
        //this.items.add("Boots of Mobility");

        this.itemPics = new ArrayList<>(6);
        //this.itemPics.add(R.drawable.item);
        //this.itemPics.add(R.drawable.item);
        //this.itemPics.add(R.drawable.item);
        //this.itemPics.add(R.drawable.item);

        // get list
        this.list = (ListView) findViewById(R.id.in_game_single_summoner_item_list);

        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: HANDLE INDIVIDUAL ITEM SELECTION HERE
            }
        });

        this.list.setAdapter(new PictureTextSubTextAdapter(this, items, itemDescrips, itemPics));
    }

    public void refreshSummonerStats(View v){
        Toast.makeText(this, "Refreshing stats for " + sumName + "...", Toast.LENGTH_SHORT).show();
    }

}
