package com.example.prm_392_nho_ho_ban.fragment;

import static com.example.prm_392_nho_ho_ban.MyApplication.dbRoom;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.adapter.NoteListAdapter;
import com.example.prm_392_nho_ho_ban.bean.Note;
import com.example.prm_392_nho_ho_ban.bean.User;
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;
import com.example.prm_392_nho_ho_ban.dao.RoomNoteDAO;
import com.google.firebase.Timestamp;


import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTodayNote#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTodayNote extends Fragment {
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
    private Timestamp todayTS;
    private RecyclerView noteRecyclerView;
    private RecyclerView pinRecyclerView;
    private NoteListAdapter noteListAdapter;
    private NoteListAdapter pinListAdapter;
    private TextView tvMes2;
    private ArrayList<Note> pinList;
    private ArrayList<Note> unPinList;

    RoomNoteDAO roomNoteDAO = dbRoom.createNoteDAO();
    public FragmentTodayNote() {
       super(R.layout.fragment_all_note);
    }

    public static FragmentTodayNote newInstance(String param1, String param2) {
        FragmentTodayNote fragment = new FragmentTodayNote();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void bindingUI(View view) {
        tvMes2 = view.findViewById(R.id.tvMes2);
        noteRecyclerView = view.findViewById(R.id.noteListRecyclerView);
        pinRecyclerView = view.findViewById(R.id.PinListRecyclerView);

        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        pinRecyclerView.setLayoutManager(verticalLayoutManager);
        pinListAdapter = new NoteListAdapter(getActivity(),getPinNote(todayTS,todayTS));
        pinRecyclerView.setAdapter(pinListAdapter);

        LinearLayoutManager verticalLayoutManagerr
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        noteRecyclerView.setLayoutManager(verticalLayoutManagerr);
        noteListAdapter = new NoteListAdapter(getActivity(),getUnpinNote(todayTS,todayTS));
        noteRecyclerView.setAdapter(noteListAdapter);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            todayTS = new Timestamp(new Date());
        View view = inflater.inflate(R.layout.fragment_today_note, container, false);
        bindingUI(view);

        return view;
    }

    private ArrayList<Note> getPinNote(Timestamp startDate, Timestamp endDate){
        pinList = (ArrayList<Note>) roomNoteDAO.getAllPinByDay(startDate,endDate,true, User.USER.getUid());
        return pinList;
    }

    private ArrayList<Note> getUnpinNote(Timestamp startDate, Timestamp endDate){
        unPinList = (ArrayList<Note>) roomNoteDAO.getAllPinByDay(startDate,endDate,false,User.USER.getUid());
        return unPinList;
    }

    public void updateAdapter(){
                pinListAdapter.update(getPinNote(todayTS,todayTS));
                noteListAdapter.update(getUnpinNote(todayTS,todayTS));
                checkEmpty();
    }


    private void checkEmpty(){
        if(pinList.isEmpty()&&unPinList.isEmpty()){
            tvMes2.setVisibility(View.VISIBLE);
        }else{
            tvMes2.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
       updateAdapter();
    }
}