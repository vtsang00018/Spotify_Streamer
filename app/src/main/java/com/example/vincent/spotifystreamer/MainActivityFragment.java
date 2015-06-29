package com.example.vincent.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    // spotify service to query for data
    SpotifyService mSpotifyService;
    // the search query that users input
    String artistSearchQuery;
    // the adapter for the artist listView
    ArrayAdapter<String> artistAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // get the list view displaying the list of artists
        ListView artistListView = (ListView)rootView.findViewById(R.id.artist_listview);

        // initialize connection to spotify api
        connectToSpotify();

        // get the search edit text
        final EditText editText = (EditText) rootView.findViewById(R.id.search_edit_text);
        // set an action listener to listen for user queries for artists
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                // if users entered a query...
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    // store the user's query as a string
                    artistSearchQuery = editText.getText().toString();
                    // obtain the artist list in an asynctask
                    FetchArtistTask artistTask = new FetchArtistTask();
                    artistTask.execute(artistSearchQuery);
                    handled = true;
                }
                return handled;
            }
        });
        // TODO: GET IMAGE ICONS TO SHOW RIGHT ICON

        // temporary data
        String[] tempData = {"Coldplay", "Taylor Swift", "Parachute"};
        ArrayList<String> artistData = new ArrayList<>(Arrays.asList(tempData));

        if(artistData != null) {
            artistAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    R.layout.list_item_artists,
                    R.id.artist_name_text,
                    artistData
            );
            artistListView.setAdapter(artistAdapter);
        }

        return rootView;
    }

    public void connectToSpotify(){
        // connect to the spotify API with the wrapper
        SpotifyApi api = new SpotifyApi();
        // initialize the spotify service to get the data
        mSpotifyService = api.getService();
    }


    public class FetchArtistTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            try {
                ArrayList<String> artistNameList = new ArrayList<>();
                ArtistsPager results = mSpotifyService.searchArtists(params[0]);
                List<Artist> artistsFound = results.artists.items;

                if (artistsFound != null) {
                    for (Artist artist : artistsFound) {
                        artistNameList.add(artist.name);
                        Log.d("ARTISTS", "ARTISTS: " + artist.name);
                    }
                    return listToArray(artistNameList);

                } else {
                    Toast.makeText(getActivity().getBaseContext(), "No Results Found",
                            Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Log.d("exception", e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            if(strings != null){
                artistAdapter.clear();
                for(String artist : strings){
                    artistAdapter.add(artist);
                }
            }
        }

        private String[] listToArray(ArrayList<String> list){
            String[] arrayList = new String[list.size()];
            arrayList = list.toArray(arrayList);
            return arrayList;
        }
    }
}
