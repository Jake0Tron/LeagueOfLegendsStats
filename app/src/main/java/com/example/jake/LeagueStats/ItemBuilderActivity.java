package com.example.jake.LeagueStats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class ItemBuilderActivity extends AppCompatActivity {

    private String sumName;
    private String region;
    private String selectedChamp;
    private ArrayList<String> runes;
    private ArrayList<String> masteries;

    private int champLevel;
    private Context context;

    private NumberPicker levelSelector;
    private NumberPicker.OnValueChangeListener pickerListener;

    private RelativeLayout item0, item1, item2, item3, item4, item5, item6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_builder);

        this.context = this;
        // get sent in data
        Intent i = getIntent();
        this.selectedChamp = i.getStringExtra("SelectedChamp");
        this.runes = i.getStringArrayListExtra("Runes");
        this.masteries = i.getStringArrayListExtra("Masteries");
        this.region = i.getStringExtra("Region");
        this.levelSelector = (NumberPicker)findViewById(R.id.item_builder_level_select);
        this.levelSelector.setMinValue(1);
        this.levelSelector.setMaxValue(18);
        this.pickerListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                champLevel = newVal;
            }
        };
        this.levelSelector.setOnValueChangedListener(pickerListener);

        // get item boxes
        this.item0 = (RelativeLayout) findViewById(R.id.item_builder_item_0);
        this.item1 = (RelativeLayout) findViewById(R.id.item_builder_item_1);
        this.item2 = (RelativeLayout) findViewById(R.id.item_builder_item_2);
        this.item3 = (RelativeLayout) findViewById(R.id.item_builder_item_3);
        this.item4 = (RelativeLayout) findViewById(R.id.item_builder_item_4);
        this.item5 = (RelativeLayout) findViewById(R.id.item_builder_item_5);
        this.item6 = (RelativeLayout) findViewById(R.id.item_builder_item_6);
    }

    // When Item Selected
    // TODO: Get list of items and write to Selected Item
    public void onItemClicked(View v){
        switch (v.getId()){
            case R.id.item_builder_item_0:
                break;
            case R.id.item_builder_item_1:
                break;
            case R.id.item_builder_item_2:
                break;
            case R.id.item_builder_item_3:
                break;
            case R.id.item_builder_item_4:
                break;
            case R.id.item_builder_item_5:
                break;
            case R.id.item_builder_item_6:
                break;
            default:
                break;
        }
    }

    public void exitBuilder(View v){
        Intent i = new Intent(this, ItemBuilderMenuActivity.class);
        i.putExtra("SummonerName", sumName);
        i.putExtra("Region", region);
        startActivity(i);
        finish();   // take back to itemBuilder Menu
    }

}
