package com.example.prm_392_nho_ho_ban.fragment;

import static com.example.prm_392_nho_ho_ban.MyApplication.dbRoom;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prm_392_nho_ho_ban.AppDatabase;
import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.activity.WelcomeActivity;
import com.example.prm_392_nho_ho_ban.adapter.NoteListAdapter;
import com.example.prm_392_nho_ho_ban.bean.Note;
import com.example.prm_392_nho_ho_ban.bean.User;
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;
import com.example.prm_392_nho_ho_ban.dao.RoomNoteDAO;
import com.google.firebase.Timestamp;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAllNote#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NulldateFragment extends Fragment {
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
    private Date today;
    private RecyclerView noteRecyclerView;
    private RecyclerView pinRecyclerView;
    private NoteListAdapter noteListAdapter;
    private NoteListAdapter pinListAdapter;
    private NoteDAO n = new NoteDAO();
    private TextView tvMes0;
    private ArrayList<Note> pinList;
    private ArrayList<Note> unPinList;
    RoomNoteDAO roomNoteDAO = dbRoom.createNoteDAO();
    public NulldateFragment() {
        super(R.layout.fragment_all_note);
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentAllNote newInstance() {
        FragmentAllNote fragment = new FragmentAllNote();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void bindingUI(View view) {
        getPinNote();
        getUnpinNote();

        tvMes0 = view.findViewById(R.id.tvMes0);
        noteRecyclerView = view.findViewById(R.id.noteListRecyclerView);
        pinRecyclerView = view.findViewById(R.id.PinListRecyclerView);

        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        pinRecyclerView.setLayoutManager(verticalLayoutManager);
        pinListAdapter = new NoteListAdapter(getActivity(),pinList);
        pinRecyclerView.setAdapter(pinListAdapter);

        LinearLayoutManager verticalLayoutManagerr
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        noteRecyclerView.setLayoutManager(verticalLayoutManagerr);
        noteListAdapter = new NoteListAdapter(getActivity(),unPinList);
        noteRecyclerView.setAdapter(noteListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_nulldate, container, false);
        bindingUI(view);
        return view;
    }

    private void getPinNote(){
        pinList = (ArrayList<Note>) roomNoteDAO.getAllNotRemindNote(User.USER.getUid(),true,true);
    }

    private void getUnpinNote(){
        unPinList = (ArrayList<Note>) roomNoteDAO.getAllNotRemindNote(User.USER.getUid(),false, true);
    }

    public void updateAdapter(){
        getPinNote();
        getUnpinNote();
        Log.i("UPDATEE", "updateAdapter: " + unPinList.size());
        if(pinListAdapter!=null){
            pinListAdapter.update(pinList);}
        if(noteListAdapter!=null){
            noteListAdapter.update(unPinList);}
        checkEmpty();
    }

    private void checkEmpty(){
        if(pinList.isEmpty()&&unPinList.isEmpty()){
            tvMes0.setVisibility(View.VISIBLE);
        }else{
            tvMes0.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        updateAdapter();
    }
}