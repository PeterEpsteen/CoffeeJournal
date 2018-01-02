package com.example.peter.coffeejournal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.plus.PlusOneButton;
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

public class RoastFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ROAST_PREFERNCE_KEY = "roast_key";
    private static final String LIST_STATE_KEY_ROAST = "key12342";
    private static final String ROAST_LIST_KEY = "key2234" ;
    // The request code must be 0 or greater.
    // The URL to +1.  Must be a valid URL.
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PlusOneButton mPlusOneButton;
    private ListView lv;
    private RoastAdapter roastAdapter;
    private DBOperator dbOperator;
    private ArrayList<Roast> roastArrayList;
    private RecyclerView rvRoast;
    private LinearLayoutManager linearLayoutManager;
    private SharedPreferences appSharedPrefs;
    private ImageButton sortByButton, addButton;


    private OnFragmentInteractionListener mListener;

    public RoastFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlusOneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RoastFragment newInstance(String param1, String param2) {
        RoastFragment fragment = new RoastFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getView() != null) {
            Log.i("SetVisible", "Set user visible called on Roast Fragment");
            new LoadRoasts("checkDBChanged").execute();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        roastArrayList = new ArrayList<Roast>();
        roastAdapter = new RoastAdapter(getContext(), roastArrayList);
        dbOperator = new DBOperator(getContext());
        new LoadRoasts("initialize").execute();
    }


    //TODO come back to right fragment after editing/adding
    //TODO editing doesnt work and beans display -1 under weight when none is entered

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.roast_fragment, container, false);
        rvRoast = view.findViewById(R.id.roast_recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvRoast.setLayoutManager(linearLayoutManager);
        rvRoast.setAdapter(roastAdapter);
        if (savedInstanceState != null) {
            Log.i("Brew Order", "Setting to last saved order");
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(LIST_STATE_KEY_ROAST);
            rvRoast.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            roastArrayList = savedInstanceState.getParcelableArrayList(ROAST_LIST_KEY);
            roastAdapter = new RoastAdapter(view.getContext(), roastArrayList);
        }
        rvRoast.setAdapter(roastAdapter);


        initializeTouchHelper();

        sortByButton = view.findViewById(R.id.sort_button);
        sortByButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortMenu();
            }
        });
        addButton = view.findViewById(R.id.add_roast_top_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddRoast.class);
                startActivityForResult(intent, 1);

            }
        });

        //Find the +1 button

        return view;
    }

    public void showSortMenu() {
        //creating a popup menu
        PopupMenu popup = new PopupMenu(getContext(), sortByButton);
        //inflating menu from xml resource
        popup.inflate(R.menu.sort_roast_menu);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sort_by_name:
                        Log.i("Sort", "Sort name clicked");
                        new LoadRoasts("sortName").execute();
                        return true;
                    case R.id.sort_by_date:
                        Log.i("Sort", "Sort date clicked");
                        new LoadRoasts("sortDate").execute();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == AddRoast.ROAST_DB_CHANGED) {
            Log.i("ResultRoast", "Roast result called db changed");
            new LoadRoasts("checkDBChanged").execute();
        }
    }

    public void initializeTouchHelper() {
        ItemTouchHelper.Callback touchHelper = new ItemTouchHelper.Callback() {

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.DOWN | ItemTouchHelper.UP);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Collections.swap(roastArrayList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                roastAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelper);
        itemTouchHelper.attachToRecyclerView(rvRoast);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable mListState = linearLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY_ROAST, mListState);
        outState.putParcelableArrayList(ROAST_LIST_KEY, roastAdapter.getRoastList());
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction2(uri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(roastAdapter.getRoastList());
        prefsEditor.putString(ROAST_PREFERNCE_KEY, json);
        prefsEditor.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
        void onFragmentInteraction2(Uri uri);
    }

    private class LoadRoasts extends AsyncTask<Void, Void, ArrayList<Roast>> {

        private DBOperator dbOperator;
        private SharedPreferences appSharedPrefs;
        private String methodString;
        boolean refreshSort = false;

        public LoadRoasts(String methodString) {
            this.methodString = methodString;
            dbOperator = new DBOperator(getContext());
            appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        }

        protected void initialize() {
            ArrayList<Roast> roasts = new ArrayList<Roast>();
            roasts = dbOperator.getRoasts();
            Gson gson = new Gson();
            appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
            String json = appSharedPrefs.getString(ROAST_PREFERNCE_KEY, "");
            if(json.isEmpty()){
                Log.i("Shared Pref", "No shared preferences for roast list order");
            }
            else {
                Log.i("Shared Pref", "Loading shared preferences for roast list order");
                Type type = new TypeToken<List<Roast>>(){}.getType();
                roastArrayList = gson.fromJson(json, type);
            }
            if (roastArrayList.size() != roasts.size()) {
                roastArrayList = roasts;
            }
        }

        protected void checkDBChanged() {
            Log.i("Load Roasts", "Checking if db changed...");
            ArrayList<Roast> dbRoasts = dbOperator.getRoasts();
            if (dbRoasts.size() != roastArrayList.size()) {
                roastArrayList = dbRoasts;
                sortBy("Date");
            }
        }

        protected void sortBy(String sortBy) {
            Log.i("Sort", "Sort by called in brew fragment");
            switch (sortBy) {
                case "Name":
                    Collections.sort(roastArrayList, new Comparator<Roast>() {
                        @Override
                        public int compare(Roast o1, Roast o2) {
                            return o1.getName().compareToIgnoreCase(o2.getName());
                        }
                    });
                    break;

                case "Date":
                    Collections.sort(roastArrayList, new Comparator<Roast>() {
                        @Override
                        public int compare(Roast o1, Roast o2) {
                            int compareResult = 0;
                            try {
                                Date first = new SimpleDateFormat("MMM d yyyy h:mm a").parse(o1.getDate());
                                Date end = new SimpleDateFormat("MMM d yyyy h:mm a").parse(o2.getDate());
                                compareResult =  end.compareTo(first);
                            }
                            catch (ParseException e) {Log.e("Parse Error", e.toString());}
                            return compareResult;
                        }
                    });
                    break;

            }
        }

        ArrayList<Roast> refreshDB() {
            refreshSort = true;
            roastArrayList = dbOperator.getRoasts();
            return dbOperator.getRoasts();
        }


        @Override
        protected ArrayList<Roast> doInBackground(Void... voids) {
            switch (methodString) {
                case "initialize":
                    initialize();
                    break;
                case "sortName":
                    sortBy("Name");
                    break;
                case "sortDate":
                    sortBy("Date");
                    break;
                case "checkDBChanged":
                    checkDBChanged();
                    break;
                case "refreshDB":
                    refreshDB();
                    break;
            }
            return roastArrayList;

        }

        @Override
        protected void onPostExecute(ArrayList<Roast> roasts) {
            super.onPostExecute(roasts);
            roastAdapter.setRoastList(roastArrayList);
            roastAdapter.notifyDataSetChanged();
            if(refreshSort) {
                methodString = "sortDate";
                execute();
            }
        }
    }

}


//TODO Handle config changes, preferences, and whatnot