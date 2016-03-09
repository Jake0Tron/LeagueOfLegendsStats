package com.example.jake.LeagueStats;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ItemBuilderRuneSelectionActivity extends AppCompatActivity {

    private String sumName;
    private String region;
    private String selectedChamp;
    private ListView runeList;
    private ArrayList<String> runes;
    private ArrayList<String> runeDescrips;
    private ArrayList<Bitmap> runePics;
    private String[] stats;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_builder_rune_selection);

        Intent i = getIntent();
        this.selectedChamp = i.getStringExtra("SelectedChamp");
        this.sumName = i.getStringExtra("SummonerName");
        this.region = i.getStringExtra("Region");

        // TODO: Populate ArrayLists from API/Static DB

        this.runes = new ArrayList<>();
        this.runeDescrips = new ArrayList<>();
        this.runePics = new ArrayList<>();

        this.runes.add("Glyph of Attack Speed");
        this.runeDescrips.add("Attack Speed +30%");
       // this.runePics.add(R.drawable.rune);

        this.runes.add("Mark of Lifesteal");
        this.runeDescrips.add("Lifesteal +3%");
        //this.runePics.add(R.drawable.rune);

        this.runeDescrips.add("Armor Penetration +2%");
        this.runes.add("Seal of Armor Penetration");
        //this.runePics.add(R.drawable.rune);

        this.runeList = (ListView) findViewById(R.id.item_build_rune_runeList);
        this.runeList.setAdapter(new PictureTextSubTextAdapter(this, runes, runeDescrips, runePics));

        this.submit = (Button) findViewById(R.id.item_build_rune_submit_button);
    }

    public void onSubmit(View v){
        // WHEN COMPLETE: START MASTERY SELECT
        Intent i = new Intent(this, ItemBuilderMasterySelectionActivity.class);
        i.putExtra("Runes", runes);
        i.putExtra("SummonerName", sumName);
        i.putExtra("SelectedChamp", selectedChamp);
        i.putExtra("Region", region);
        startActivity(i);
        //TODO: Not sure if user should be able to go back and edit...
        finish();
    }

}
