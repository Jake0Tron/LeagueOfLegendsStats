package com.example.jake.LeagueStats;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SingleSummonerActivity extends AppCompatActivity implements AsyncResponse {

    HttpAsyncTask http = new HttpAsyncTask();
    private String url;
    private String sumJsonString;
    private String statJsonString;
    private ArrayList<String> stats;
    private ArrayAdapter<String> aa;
    private ArrayList<String> indivListStat;
    private ListView list;
    private TextView
            titleView,
            regionView,
            divisionView,
            winView,
            lossView,
            levelView;
    private URLBuilder urlBuilder;
    private SummonerData summoner;
    private JSONObject sumJson;
    private JSONObject statJson;
    private JSONObject leagueJSON;
    private Context context;
    private int wins, losses;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_summoner);

        this.stats = new ArrayList<>();
        // arraylist to hold stat descrips on Menu Item Click
        this.indivListStat = new ArrayList<>();

        // homemade setup function for create/resume
        build();

        // Set views with data
        this.titleView = (TextView) findViewById(R.id.single_summoner_title);
        this.titleView.setText(this.summoner.getSummonerName());
        this.regionView = (TextView) findViewById(R.id.single_summoner_region);
        this.regionView.setText(this.summoner.getRegion().toUpperCase());
        this.levelView = (TextView) findViewById(R.id.single_summoner_level);
        String levelStr = "Level ";
        levelStr +=String.valueOf(this.summoner.getSummonerLevel());
        this.levelView.setText(levelStr);
        // set in ASYNC
        this.divisionView = (TextView) findViewById(R.id.single_summoner_division);
        this.winView = (TextView) findViewById(R.id.single_summoner_losses_value);
        this.lossView = (TextView) findViewById(R.id.single_summoner_win_value);

        // array adapter setup
        this.aa = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, stats);
        // get listView
        this.list = (ListView) findViewById(R.id.single_summoner_stat_list);
        // adapt list to view
        this.list.setAdapter(aa);
        // handle click listener
        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*  Had an error saying that AlertDialogue had protected access, turns out I need a builder:
            see http://stackoverflow.com/questions/25560408/alert-dialogue-has-protected-access
        */

            // Fire alert with stats
            alertDialogBuilder = new AlertDialog.Builder(context);
            // add scrollable view to dialogue
            //http://developer.android.com/guide/topics/ui/dialogs.html
            alertDialogBuilder
                    .setView(View.inflate(getApplicationContext(), R.layout.alert_dialogue_scroll_view, null))
                    .setTitle(summoner.getSummonerName())
                    .setMessage(indivListStat.get(position))
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            // create the dialog
            alertDialog = alertDialogBuilder.create();
            // Show the AlertDialog.
            alertDialog.show();
//DEBUG
//                Toast.makeText(
//                        getApplicationContext(),
//                        indivListStat.get(position),
//                        Toast.LENGTH_LONG
//                ).show();
            }
        });

        // If loading data takes time, Fire a dialogue saying that we're getting data
        this.alertDialogBuilder = new AlertDialog.Builder(context);
        // add scrollable view to dialogue
        //http://developer.android.com/guide/topics/ui/dialogs.html
        alertDialogBuilder
                .setView(View.inflate(getApplicationContext(), R.layout.alert_dialogue_scroll_view, null))
                .setTitle("Please Wait...")
                .setMessage("Getting data from Riot's servers...");
        // create the dialog
        this.alertDialog = alertDialogBuilder.create();
        // Show the AlertDialog.
        alertDialog.show();

    }

    // CUSTOM SET UP FUNCTION
    // ADD TO THIS IF NEEDED ON CREATE OR RESUME!
    public void build(){
        // get data passed in from login:
        // sumJSON - summoner stat JSON
        // region - region
        // statJSON - statSummary JSON
        Intent i = getIntent();
        this.sumJsonString = i.getStringExtra("sumJSON");
        this.statJsonString = i.getStringExtra("statJSON");

        try {
            this.sumJson = new JSONObject(this.sumJsonString);
            this.statJson = new JSONObject(this.statJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // make new SummonerData from JSON
        this.summoner = new SummonerData(this.sumJson, i.getStringExtra("region"));
        this.context = this;
        this.http = new HttpAsyncTask();
        this.http.delegate = this;
        this.urlBuilder = new URLBuilder();

        String leagueUrl = this.urlBuilder.singleSummonerLeagueEntryURL(
                this.summoner.getRegion(),
                this.summoner.getSummonerID()
        );
        // get Summoner League data
        this.http.execute(leagueUrl);

        //FORMAT:
            /*{"playerStatSummaries":[
                 {"aggregatedStats":{"totalChampionKills":346,"totalAssists":709,"totalNeutralMinionsKilled":928,"totalTurretsKilled":60,"totalMinionKills":5636},"playerStatSummaryType":"CAP5x5","modifyDate":1427599786000,"wins":30},
                 {"aggregatedStats":{"totalChampionKills":83,"totalAssists":54,"totalNeutralMinionsKilled":384,"totalTurretsKilled":11,"totalMinionKills":711},"playerStatSummaryType":"CoopVsAI","modifyDate":1421884717000,"wins":9},
                 {"aggregatedStats":{"totalChampionKills":160,"totalAssists":111,"totalNeutralMinionsKilled":255,"totalTurretsKilled":21,"totalMinionKills":1776},"playerStatSummaryType":"CoopVsAI3x3","modifyDate":1421884717000,"wins":20},
                 {"aggregatedStats":{"totalChampionKills":137,"totalAssists":246,"totalNeutralMinionsKilled":354,"totalTurretsKilled":32,"totalMinionKills":2270},"playerStatSummaryType":"CounterPick","modifyDate":1427144067000,"wins":11},
                 {"aggregatedStats":{"averageNodeCaptureAssist":1,"maxNodeNeutralizeAssist":2,"maxChampionsKilled":3,"totalChampionKills":4,"totalAssists":20,"averageChampionsKilled":2,"averageNodeCapture":5,"averageNumDeaths":4,"maxNodeNeutralize":3,"averageTotalPlayerScore":663,"averageTeamObjective":1,"averageNodeNeutralize":2,"maxNodeCapture":6,"maxObjectivePlayerScore":747,"averageNodeNeutralizeAssist":2,"averageAssists":10,"maxTotalPlayerScore":859,"maxAssists":13,"maxCombatPlayerScore":166,"averageCombatPlayerScore":139,"maxNodeCaptureAssist":1,"totalNodeCapture":9,"totalNodeNeutralize":4,"maxTeamObjective":1,"averageObjectivePlayerScore":524},"playerStatSummaryType":"OdinUnranked","modifyDate":1421884717000,"wins":1},
                 {"aggregatedStats":{},"losses":0,"modifyDate":1385674889000,"playerStatSummaryType":"RankedTeam3x3","wins":0},
                 {"aggregatedStats":{"totalChampionKills":428,"totalAssists":763,"totalNeutralMinionsKilled":1923,"totalTurretsKilled":34,"totalMinionKills":6580},"playerStatSummaryType":"Unranked3x3","modifyDate":1421884717000,"wins":46},
                 {"aggregatedStats":{"totalChampionKills":147,"totalAssists":153,"totalNeutralMinionsKilled":129,"totalTurretsKilled":32,"totalMinionKills":2076},"playerStatSummaryType":"URF","modifyDate":1431470436000,"wins":11},
                 {"aggregatedStats":{"totalChampionKills":11,"totalAssists":33,"totalNeutralMinionsKilled":21,"totalTurretsKilled":1,"totalMinionKills":114},"playerStatSummaryType":"Hexakill","modifyDate":1439250877000,"wins":2},
                 {"aggregatedStats":{"totalChampionKills":1633,"totalAssists":3481,"totalNeutralMinionsKilled":5721,"totalTurretsKilled":238,"totalMinionKills":23453},"playerStatSummaryType":"Unranked","modifyDate":1447190632000,"wins":159},
                 {"aggregatedStats":{"totalChampionKills":274,"totalAssists":585,"totalNeutralMinionsKilled":490,"totalTurretsKilled":63,"totalMinionKills":5967},"losses":27,"modifyDate":1447291432000,"playerStatSummaryType":"RankedSolo5x5","wins":29},
                 {"aggregatedStats":{"totalChampionKills":117,"totalAssists":137,"totalNeutralMinionsKilled":309,"totalTurretsKilled":13,"totalMinionKills":1787},"playerStatSummaryType":"OneForAll5x5","modifyDate":1447727322000,"wins":7},
                 {"aggregatedStats":{"totalChampionKills":3810,"totalAssists":10848,"totalTurretsKilled":179},"playerStatSummaryType":"AramUnranked5x5","modifyDate":1447730296000,"wins":259}
             ],"summonerId":37930093}
             */

        try {
            this.statJson = new JSONObject(statJsonString);

            // get "playerStatSummaries" as array
            JSONArray playerStatSummaries = this.statJson.getJSONArray("playerStatSummaries");
            String listStatString;
            for (int x = 0; x < playerStatSummaries.length(); x++){
                // go through all player stat summaries
                JSONObject aggregatedStats = playerStatSummaries.getJSONObject(x);
                // get array of substats
                JSONObject subStats = aggregatedStats.getJSONObject("aggregatedStats");
                if (!subStats.toString().equals("{}")){
                    // remove empty subStat strings

                    // debug
                    //Log.d("GAME TYPE", aggregatedStats.getString("playerStatSummaryType"));
                    //Log.d("sinSum Sub Stat", subStats.toString());

                    // get Match type number of wins
                    int gameTypeWins = aggregatedStats.getInt("wins");
                    this.wins += gameTypeWins;
                    // get list of stats for game type
                    // TODO: change game modes to appropriate names, for now API values
                    listStatString = aggregatedStats.getString("playerStatSummaryType") + " : ";
                    String indList = "";
                    // set up string of all stats
                    for (int y = 0; y < subStats.length(); y++){
                        indList += subStats.names().get(y) + " - "
                                + subStats.getInt(subStats.names().get(y).toString()) + "\n";
                    }
                    // add to list for listview
                    indivListStat.add(indList);
                    listStatString += "Wins : " + String.valueOf(gameTypeWins);
                    stats.add(listStatString);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // called on API return
    // HANDLES RANKED STAT QUERY
    // FORMAT
//  {"47391952": [{                           ourSummonerList
//        "entries": [{                             0 entries
//            "leaguePoints": 12,                       0
//            "isFreshBlood": false,                    1
//            "isHotStreak": false,                     2
//            "division": "III",                        3
//            "isInactive": false,                      4
//            "isVeteran": false,                       5
//            "losses": 48,                             6
//            "playerOrTeamName": "Jake0Tron",          7
//            "playerOrTeamId": "47391952",             8
//            "wins": 49                                9
//        }],
//        "queue": "RANKED_SOLO_5x5",               1
//        "tier": "BRONZE"                          2
//        "name": "Alistar's Spellslingers",        3
//    }]}

    @Override
    public void processFinish(JSONObject output) {
        if (output != null) {
            this.leagueJSON = output;

            //Log.d("singSum League", leagueJSON.toString());
            JSONArray ourSummonerList;
            JSONObject entries, ourSummoner;
            String division, tier, name, fullString;
            int  leaguePoints;
            try {
                ourSummonerList = leagueJSON.getJSONArray(String.valueOf(this.summoner.getSummonerID()));

                ourSummoner = ourSummonerList.getJSONObject(0);
                JSONArray entriesList = ourSummoner.getJSONArray("entries");
                //Log.d("entriesList", entriesList.toString());

                entries = entriesList.getJSONObject(0);
                //Log.d("entries", entries.toString());
                leaguePoints = entries.getInt("leaguePoints");
                division = entries.getString("division");
                losses = entries.getInt("losses");
                wins = entries.getInt("wins");

                tier = ourSummoner.getString("tier");
                name = ourSummoner.getString("name");

                //Log.d("fullPlacement", tier + " " + division);
                fullString = name + ": " + tier + " " + division + " - " + leaguePoints + "LP";
                this.divisionView.setText(fullString);
                this.lossView.setText(String.valueOf(losses));
                this.winView.setText(String.valueOf(wins));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            // player has no RankfullString = name + ": " + tier + " " + division + " - " + leaguePoints + "LP";
            this.divisionView.setText(R.string.single_summoner_no_rank);
            this.lossView.setText("");
            this.winView.setText(String.valueOf(wins));
        }
        // close the dialog if it's still showing
        if (this.alertDialog.isShowing())
            this.alertDialog.cancel();
    }

    // ensures data is set up for previous activity
    @Override
    public void onStop(){
        super.onStop();

        // FORMAT: {"tehbean":{"id":49449174,"name":"tehbean","profileIconId":785,"summonerLevel":30,"revisionDate":1447465652000}}
        Intent i = new Intent(this, SingleSummonerActivity.class);
        // send newly acquired Stat JSON
        i.putExtra("statJSON", this.statJson.toString());
        // send single summoner data over as string
        i.putExtra("sumJSON", this.sumJsonString);
        // send region
        i.putExtra("region", this.summoner.getRegion());
    }

    // ensures data is available for views etc
    @Override
    public void onResume(){
        super.onResume();
        build();
    }
}
