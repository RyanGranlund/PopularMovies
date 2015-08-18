package com.digitalinfomesh.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View detailView = inflater.inflate(R.layout.fragment_detail, container, false);


        Intent detailIntent = getActivity().getIntent();
        int pos = detailIntent.getIntExtra("moviePosition", 0);

        Toast.makeText(getActivity(), ""+pos, Toast.LENGTH_SHORT).show();

        return detailView;
    }
}
