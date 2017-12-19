package com.example.peter.coffeejournal;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class BrewFragment extends Fragment implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {


    private OnFragmentInteractionListener mListener;
    private FloatingActionButton addBrewButton;
    private GridView gv;
    private ListView lv;
    private DBOperator mDBOperator;
    private BrewAdapter ba;
    private ArrayList<BrewRecipe> brewRecipeArrayList;
    private String sortByCurrent;
    private Spinner sortSpinner;

    public BrewFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static BrewFragment newInstance(String param1, String param2) {
        BrewFragment fragment = new BrewFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_brew, container, false);
        lv = rootView.findViewById(R.id.brew_list_view);
        mDBOperator = new DBOperator(this.getContext());
        brewRecipeArrayList = mDBOperator.getBrewRecipes();
        ba = new BrewAdapter(rootView.getContext(), brewRecipeArrayList);
        sortByCurrent = "Name";
        sortBrewsBy(sortByCurrent);
        lv.setOnItemClickListener(this);
        registerForContextMenu(lv);
        sortSpinner = rootView.findViewById(R.id.sort_by_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.brew_sort_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(this);
        return rootView;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            TextView tv = info.targetView.findViewById(R.id.brew_title_text_view);
            String brewName = tv.getText().toString();
            switch (item.getItemId()) {
                case R.id.menu_edit:
                    Intent myIntent = new Intent(getContext(), AddBrew.class);
                    myIntent.putExtra("Brew Name", brewName);
                    startActivityForResult(myIntent, 1);
                    return true;
                case R.id.menu_delete:
                    Log.i("Info", "You would be deleting this brew: " + brewName);
                    mDBOperator.deleteBrew(brewName);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(this).attach(this).commit();
                    return true;
                default:
                    return super.onContextItemSelected(item);

            }
        }
        return false;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tv = view.findViewById(R.id.brew_title_text_view);
        String brewName = tv.getText().toString();
        Intent myIntent = new Intent(view.getContext(), BrewRecipeActivity.class);
        myIntent.putExtra("Brew Name", brewName);
        startActivity(myIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String sortBy = parent.getItemAtPosition(position).toString();
        if(!sortBy.equals(sortByCurrent))
            sortBrewsBy(sortBy);
        sortByCurrent = sortBy;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity().finish();
        startActivity(getActivity().getIntent());
    }

    public void sortBrewsBy(String sortBy) {
        Log.i("Sort", "Sort by called in brew fragment");
        switch (sortBy) {
            case "Name":
                Collections.sort(brewRecipeArrayList, new Comparator<BrewRecipe>() {
                    @Override
                    public int compare(BrewRecipe o1, BrewRecipe o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                break;
            case "Method":
                Collections.sort(brewRecipeArrayList, new Comparator<BrewRecipe>() {
                    @Override
                    public int compare(BrewRecipe o1, BrewRecipe o2) {
                        return o1.getBrewMethod().compareTo(o2.getBrewMethod());
                    }
                });
                break;

        }

        lv.setAdapter(ba);
        ba.notifyDataSetChanged();
    }
}
