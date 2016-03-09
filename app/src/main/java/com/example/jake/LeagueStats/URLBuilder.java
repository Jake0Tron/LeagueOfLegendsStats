package com.example.jake.LeagueStats;

import android.util.Log;

/**
 * Created by Jake on 11/15/2015.
 * Handles all URL Construction for queries to RIOT API
 * Limited to functionality of this application (will not handle every query provided by riot)
 */
public class URLBuilder {

    private static String apiKeyString = "api_key=4eb27f7f-ea35-476b-9269-81ea251e424f";

    public URLBuilder() {
    }
    // version URL
    public String getVersionDataURL(){
        String url = "https://ddragon.leagueoflegends.com/realms/na.json";
        //{
            // "n":
            // {
                // "item":"5.24.2",
                // "rune":"5.24.2",
                // "mastery":"5.24.2",
                // "summoner":"5.24.2",
                // "champion":"5.24.2",
                // "profileicon":"5.24.2",
                // "map":"5.24.2",
                // "language":"5.24.2"
            // },
            // "v":"5.24.2",
            // "l":"en_US",
            // "cdn":"http:\/\/ddragon.leagueoflegends.com\/cdn",
            // "dd":"5.24.2",
            // "lg":"5.24.2",
            // "css":"5.24.2",
            // "profileiconmax":28,
            // "store":null
        // }

    return url;
    }


    // builds URL for single summoner
    public String singleSummonerURL(String summonerName, String region) {
        //"https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/jake0tron?api_key=4eb27f7f-ea35-476b-9269-81ea251e424f";
        return "https://" + region + ".api.pvp.net/api/lol/" + region + "/v1.4/summoner/by-name/" + summonerName + "?" + apiKeyString;
    }

    // HANDLE PLATFORM ID by region
    public String singleSummonerInGameURL(long summonerId, String region){
        // https://na.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/NA1/38363660?api_key=4eb27f7f-ea35-476b-9269-81ea251e424f
        return "https://"+region+".api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/NA1/38363660?api_key=4eb27f7f-ea35-476b-9269-81ea251e424f";

    }

    public String singleSummonerStatSummaryURL(String region, long summonerId) {
        //"https://na.api.pvp.net/api/lol/na/v1.3/stats/by-summoner/49449174/summary?season=SEASON2015&api_key=4eb27f7f-ea35-476b-9269-81ea251e424f"
        return "https://" + region + ".api.pvp.net/api/lol/na/v1.3/stats/by-summoner/" + summonerId + "/summary?season=SEASON2015&" + apiKeyString;
    }

    public String singleSummonerLeagueEntryURL(String region, long summonerId) {
        //"https://       na     .api.pvp.net/api/lol/na/v2.5/league/by-summoner/      47391952    /entry?    api_key=4eb27f7f-ea35-476b-9269-81ea251e424f"
        return "https://" + region + ".api.pvp.net/api/lol/na/v2.5/league/by-summoner/" + summonerId + "/entry?" + apiKeyString;
    }

    public String getAllChampionIdsURL(boolean rotation, String region){
        // rotation (Free To Play) will return fewer champions
         if (rotation){
            //https://na.api.pvp.net/api/lol/na/v1.2/champion?freeToPlay=true&api_key=4eb27f7f-ea35-476b-9269-81ea251e424f
            return "https://" + region + ".api.pvp.net/api/lol/na/v1.2/champion?freeToPlay=true&" + apiKeyString;
        }else{
            //https://na.api.pvp.net/api/lol/na/v1.2/champion?api_key=4eb27f7f-ea35-476b-9269-81ea251e424f
             return "https://" + region + ".api.pvp.net/api/lol/na/v1.2/champion?" + apiKeyString;
        }
     }

    public String getAllChampionStaticDataURL(String version){
        // default English

        return "http://ddragon.leagueoflegends.com/cdn/"+version+"/data/en_US/champion.json";
    }
    
    // pulls individual champion Image
    public String getIndividualChampImageURL(String champion, String version){

        //Log.d("URLBUILDER",champion);

        // remove spaces, apostrophes
        //      http://ddragon.leagueoflegends.com/cdn/5.23.1/img/champion/Aatrox.png
        String x = "http://ddragon.leagueoflegends.com/cdn/"+version+"/img/champion/"+ champion.replace(" ", "").replace("'", "").replace(".","") + ".png";
        Log.d("URLBuild", x);
        return "http://ddragon.leagueoflegends.com/cdn/"+version+"/img/champion/"+ champion.replace(" ", "").replace("'", "").replace(".","") + ".png";
    }

    public String getSummonerProfileIconURL(long id, String version){
        //http://ddragon.leagueoflegends.com/cdn/5.22.3/img/profileicon/588.png

        return "http://ddragon.leagueoflegends.com/cdn/"+version+"/img/profileicon/" + id + ".png";
    }

    public String getIndividualSkinImageURL(String name){
        // TODO: handle skins
        return null;
    }

    // public String getStringForURL(String region, long summonerId){
    //}
}
