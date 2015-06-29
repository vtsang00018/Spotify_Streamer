package com.example.vincent.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * A placeholder fragment containing a simple view.
 */
public class TopTracksFragment extends Fragment {

    public TopTracksFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);

        String artistID = getActivity().getIntent().getStringExtra(MainActivityFragment.ID_KEY);




        return rootView;
    }
}
