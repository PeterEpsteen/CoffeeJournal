package com.example.peter.coffeejournal;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class BrewFragment extends Fragment implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {


    private static final String LIST_STATE_KEY = "key";
    private static final String BREW_RECIPE_LIST_KEY = "key2" ;
    private static final String BREW_RECIPE_PREFERENCE_KEY = "brewListKey";
    private OnFragmentInteractionListener mListener;
    private FloatingActionButton addBrewButton;
    private RecyclerView rv;
    private DBOperator mDBOperator;
    private BrewAdapter ba;
    private ArrayList<BrewRecipe> brewRecipeArrayList;
    private String sortByCurrent;
    private ItemTouchHelper itemTouchHelper;
    private ImageButton imageButton;
    LinearLayoutManager llm;

    public BrewFragment() {
        // Required empty public constructor
    }


    public static BrewFragment newInstance(String param1, String param2) {
        BrewFragment fragment = new BrewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    //TODO add date added field to brew, sort by date, add newest to top of last saved instance



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBOperator = new DBOperator(this.getContext());
        ArrayList<BrewRecipe> dbBrewList = mDBOperator.getBrewRecipes();
            Gson gson = new Gson();
            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this.getActivity().getApplicationContext());
            String json = appSharedPrefs.getString(BREW_RECIPE_PREFERENCE_KEY, "");
            if (json.isEmpty()) {
                Log.i("Shared Pref", "No shared preferences for brew list order");
            } else {
                Log.i("Shared Pref", "Loading shared preferences for brew list order");
                Type type = new TypeToken<List<BrewRecipe>>() {
                }.getType();
                brewRecipeArrayList = gson.fromJson(json, type);
            }


    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<BrewRecipe> dbBrewList = mDBOperator.getBrewRecipes();
        if (dbBrewList.size() != brewRecipeArrayList.size()){
            Log.i("Brew List", "Change in DB detected, reordering list");
            brewRecipeArrayList = dbBrewList;
            sortBrewsBy("By Date");
            ba.setBrewList(brewRecipeArrayList);
        }
    }

    //TODO HANDLE ARRAYLIST in onCreate instead of onResume

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ROAST LISTS RECYCLERVIEW + SORT



        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_brew, container, false);
        rv = rootView.findViewById(R.id.brew_recycler_view);
        llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
//in oncreate        mDBOperator = new DBOperator(this.getContext());
        ArrayList<BrewRecipe> dbBrewList = mDBOperator.getBrewRecipes();
        if (savedInstanceState != null && brewRecipeArrayList.size() == dbBrewList.size()) {
            Log.i("Brew Order", "Setting to last saved order");
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            rv.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            brewRecipeArrayList = savedInstanceState.getParcelableArrayList(BREW_RECIPE_LIST_KEY);
        }

        ba = new BrewAdapter(rootView.getContext(), brewRecipeArrayList);
        rv.setAdapter(ba);

        //handle drog drop
        ItemTouchHelper.Callback ithCallback = new ItemTouchHelper.Callback() {
            //and in your imlpementaion of
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // get the viewHolder's and target's positions in your adapter data, swap them
                Collections.swap(brewRecipeArrayList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                // and notify the adapter that its dataset has changed
                ba.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //TODO
            }

            //defines the enabled move directions in each state (idle, swiping, dragging).
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP );
            }
        };
        ItemTouchHelper ith = new ItemTouchHelper(ithCallback);
        ith.attachToRecyclerView(rv);

        imageButton = rootView.findViewById(R.id.sort_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortMenu();
            }
        });



        return rootView;

    }

    public void showSortMenu() {
        //creating a popup menu
        PopupMenu popup = new PopupMenu(getContext(), imageButton);
        //inflating menu from xml resource
        popup.inflate(R.menu.sort_menu);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sort_by_name:
                        sortBrewsBy("By Name");
                        return true;
                    case R.id.sort_by_method:
                        sortBrewsBy("By Method");
                        return true;
                    case R.id.sort_by_date:
                        sortBrewsBy("By Date");
                        return true;
                    default:
                        return false;
                }
            }
        });
        //displaying the popup
        popup.show();
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
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

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(ba.getBrewList());
        prefsEditor.putString(BREW_RECIPE_PREFERENCE_KEY, json);
        prefsEditor.apply();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable mListState = llm.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
        outState.putParcelableArrayList(BREW_RECIPE_LIST_KEY, ba.getBrewList());
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
            case "By Name":
                Collections.sort(brewRecipeArrayList, new Comparator<BrewRecipe>() {
                    @Override
                    public int compare(BrewRecipe o1, BrewRecipe o2) {
                        return o1.getName().compareToIgnoreCase(o2.getName());
                    }
                });
                break;
            case "By Method":
                Collections.sort(brewRecipeArrayList, new Comparator<BrewRecipe>() {
                    @Override
                    public int compare(BrewRecipe o1, BrewRecipe o2) {
                        return o1.getBrewMethod().compareToIgnoreCase(o2.getBrewMethod());
                    }
                });
                break;
            case "By Date":
                Collections.sort(brewRecipeArrayList, new Comparator<BrewRecipe>() {
                    @Override
                    public int compare(BrewRecipe o1, BrewRecipe o2) {
                        int compareResult = 0;
                        try {
                            Date first = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(o1.getDateAdded());
                            Date end = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(o2.getDateAdded());
                            compareResult =  end.compareTo(first);
                        }
                        catch (ParseException e) {Log.e("Parse Error", e.toString());}
                        return compareResult;
                    }
                });
                break;

        }
        ba.notifyDataSetChanged();

    }

    public class myMenuClickListener implements PopupMenu.OnMenuItemClickListener {
        String roastName;
        public myMenuClickListener(String roastName) {
            this.roastName = roastName;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return false;
        }
    }
}
