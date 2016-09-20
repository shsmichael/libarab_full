package com.example.michaelg.myapplication.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelg.myapplication.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TriviaFragment extends Fragment {
    private final String TAG =this.getClass().getSimpleName();

    public TriviaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview= inflater.inflate(R.layout.fragment_trivia, container, false);
        getActivity().setTitle(R.string.menu_trivia);
        return myview;
    }

}
