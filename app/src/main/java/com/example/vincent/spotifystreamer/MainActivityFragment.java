package com.example.vincent.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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
    ArtistAdapter mArtistAdapter;
    // list view of the artists
    ListView artistListView;
    // List of Artists
    ArrayList<ArtistInfo> artistList;
    // Default image if artist image isn't available
    String DEFAULT_IMAGE = "http://cdn.photoaffections.com/images/icon-profile.png";
    // Intent Put Extra Artist ID Key
    public static final String ID_KEY = "spotifystreamer.artist.ID";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // get the list view displaying the list of artists
        artistListView = (ListView)rootView.findViewById(R.id.artist_listview);

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

        // a filler Artist
        ArrayList<ArtistInfo> test = new ArrayList<>();
        ArtistInfo temp = new ArtistInfo("Maroon 5", DEFAULT_IMAGE, "1");
        test.add(temp);
        // Create the adapter
        mArtistAdapter = new ArtistAdapter(
                getActivity().getBaseContext(),
                R.layout.list_item_artists,
                test);
        // set the adapter to create the list views
        artistListView.setAdapter(mArtistAdapter);
        artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArtistInfo aInfo = mArtistAdapter.getItem(position);
                String artistID = aInfo.getArtistId();

                Intent intent = new Intent(getActivity(), TopTracks.class);
                intent.putExtra(ID_KEY, artistID);
                startActivity(intent);
            }
        });

        return rootView;
    }

    /**
     * Connects to the spotify api using the wrapper
     */
    public void connectToSpotify(){
        // connect to the spotify API with the wrapper
        SpotifyApi api = new SpotifyApi();
        // initialize the spotify service to get the data
        mSpotifyService = api.getService();
    }

    /**
     * FetchArtistTask retrieves artist data in the backgroud
     * Takes in a string (artist name) and returns an list of artist data
     */
    public class FetchArtistTask extends AsyncTask<String, Void, ArrayList<ArtistInfo>> {

        @Override
        protected ArrayList<ArtistInfo> doInBackground(String... params) {
            try {
                ArrayList<ArtistInfo> artistList = new ArrayList<>();
                ArtistsPager results = mSpotifyService.searchArtists(params[0]);
                List<Artist> artistsFound = results.artists.items;

                if (artistsFound != null) {
                    for (Artist artist : artistsFound) {
                        // retrieve the parameters for each artist
                        String artistImageURL;

                        if(artist.images.size() != 0) {
                            artistImageURL = artist.images.get(0).url;
                        } else {
                            artistImageURL = DEFAULT_IMAGE;
                        }
                        String artistName = artist.name;
                        String artistID = artist.id;

                        // load artist object with the data and insert into the list
                        ArtistInfo aInfo = new ArtistInfo(artistName, artistImageURL, artistID);
                        artistList.add(aInfo);

                        Log.d("ARTISTS", "ARTISTS: " + artist.name);
                    }
                    return artistList;

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
        protected void onPostExecute(ArrayList<ArtistInfo> artistInfoArrayList) {

            if(artistInfoArrayList != null){
                artistList = artistInfoArrayList;
                mArtistAdapter.clear();
                for(ArtistInfo artistInfo : artistInfoArrayList){
                    mArtistAdapter.add(artistInfo);
                }
            }
        }
    }
}
