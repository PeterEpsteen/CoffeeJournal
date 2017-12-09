package com.example.peter.coffeejournal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrewNotesFragment extends Fragment {

    TextView notesTv, grindTv, titleTv;
    String notes = "";
    String grind = "Medium";
    String name = "Coffee";

    public BrewNotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_brew_notes, container, false);
        notesTv = rootView.findViewById(R.id.notes_text_view);
        grindTv = rootView.findViewById(R.id.brew_grind_notes_text_view);
        titleTv = rootView.findViewById(R.id.brew_title_notes_text_view);
        notesTv.setText(notes);
        titleTv.setText(name);
        grindTv.setText(grind);
        return rootView;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setGrind(String grind) {
        this.grind = grind;
    }

    public void setTitle(String name) {
        this.name = name;
    }
}
