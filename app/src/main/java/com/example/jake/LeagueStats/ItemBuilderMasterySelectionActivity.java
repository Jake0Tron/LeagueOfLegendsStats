package com.example.jake.LeagueStats;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class ItemBuilderMasterySelectionActivity extends AppCompatActivity {

    private String sumName;
    private String selectedChamp;
    private String region;
    private ArrayList<String> runes;
    private ArrayList<String> masteries;
    private ArrayList<String> masteryDescrips;
    private ArrayList<Bitmap> masteryPics;
    private ListView masteryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_builder_mastery_selection);

        Intent i = getIntent();
        this.sumName = i.getStringExtra("SummonerName");
        this.selectedChamp = i.getStringExtra("SelectedChamp");
        this.runes = i.getStringArrayListExtra("Runes");
        this.region = i.getStringExtra("Region");

        // TODO: Populate arrays from API CALLS/Static DB

        this.masteries = new ArrayList<>();
        this.masteryDescrips = new ArrayList<>();
        this.masteryPics = new ArrayList<>();

        this.masteries.add("Mastery 1");
        this.masteryDescrips.add("Damage to monsters/minions + 3%");
        //this.masteryPics.add(R.drawable.mastery);
        this.masteries.add("Mastery 2");
        this.masteryDescrips.add("+2% damage taken from champs, +3% damage dealt to champs");
       // this.masteryPics.add(R.drawable.mastery);
        this.masteries.add("Mastery 3");
        this.masteryDescrips.add("Time of Jungle Buffs extended by 10%");
        //this.masteryPics.add(R.drawable.mastery);

        this.masteryList = (ListView) findViewById(R.id.item_build_mastery_list);
        this.masteryList.setAdapter(new PictureTextSubTextAdapter(this, masteries, masteryDescrips, masteryPics));
    }

    // on submit masteries
    public void onSubmit(View v){
        // WHEN COMPLETE: START MASTERY SELECT
        Intent i = new Intent(this, ItemBuilderActivity.class);
        i.putExtra("Runes", runes);
        i.putExtra("Masteries", masteries);
        i.putExtra("SummonerName", sumName);
        i.putExtra("SelectedChamp", selectedChamp);
        i.putExtra("Region", region);
        startActivity(i);
        finish();
    }
}
