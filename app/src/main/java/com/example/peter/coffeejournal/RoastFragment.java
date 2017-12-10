package com.example.peter.coffeejournal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.plus.PlusOneButton;

import java.util.ArrayList;
import java.util.List;

public class RoastFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    // The URL to +1.  Must be a valid URL.
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PlusOneButton mPlusOneButton;
    ListView lv;
    RoastAdapter roastAdapter;
    DBOperator dbOperator;
    ArrayList<Roast> roastArrayList;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.roast_fragment, container, false);
        lv = view.findViewById(R.id.roast_list_view);
        dbOperator = new DBOperator(this.getContext());
        roastArrayList = dbOperator.getRoasts();
//        if(roastArrayList.size()>0) {
//            noRoasts.setText("");
//        }
        roastAdapter = new RoastAdapter(view.getContext(), roastArrayList);
        lv.setAdapter(roastAdapter);
        lv.setOnItemClickListener(this);
        registerForContextMenu(lv);
        Log.i("Brew", roastArrayList.toString());

        //Find the +1 button

        return view;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tv = view.findViewById(R.id.roast_name_text_view);
        String roastName = tv.getText().toString();
        TextView tv1 = view.findViewById(R.id.date_added_text_view);
        String date = tv1.getText().toString();
        Intent myIntent = new Intent(view.getContext(), RoastActivity.class);
        myIntent.putExtra("Name", roastName);
        myIntent.putExtra("Date", date);
        startActivity(myIntent);
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
            TextView tv = info.targetView.findViewById(R.id.roast_name_text_view);
            String roastName = tv.getText().toString();
            switch (item.getItemId()) {
                case R.id.menu_edit:
                    return true;
                case R.id.menu_delete:
                    Log.i("Roast", "Deleting roast: " + roastName);
                    dbOperator.deleteRoast(roastName);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(this).attach(this).commit();
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
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

}
