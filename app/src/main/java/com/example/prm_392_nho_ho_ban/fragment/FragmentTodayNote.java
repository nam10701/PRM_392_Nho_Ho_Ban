package com.example.prm_392_nho_ho_ban.fragment;

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
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;

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
    private Date today;
    private RecyclerView noteRecyclerView;
    private RecyclerView pinRecyclerView;
    private NoteListAdapter noteListAdapter;
    private NoteListAdapter pinListAdapter;
    private TextView tvMes2;
    private NoteDAO n = new NoteDAO();
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

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_note, container, false);
        bindingUI(view);

        try {
            today = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        showPinByDay(today,today);

        return view;
    }
    public void showPinByDay(Date startDate, Date endDate) {
        n.getAllPinByDayCallBack(new NoteDAO.FirebaseCallBack()  {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCallBack(ArrayList<Note> noteList) {
            }

            @Override
            public void onCallBack() {
            }

            @Override
            public void onCallBack(ArrayList<Note> noteList, ArrayList<Note> noteUnpinList) {
                LinearLayoutManager verticalLayoutManager
                        = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                pinRecyclerView.setLayoutManager(verticalLayoutManager);
                pinListAdapter = new NoteListAdapter(getActivity(),noteList);
                pinRecyclerView.setAdapter(pinListAdapter);

                LinearLayoutManager verticalLayoutManagerr
                        = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                noteRecyclerView.setLayoutManager(verticalLayoutManagerr);
                noteListAdapter = new NoteListAdapter(getActivity(),noteUnpinList);
                noteRecyclerView.setAdapter(noteListAdapter);
                if(noteList.isEmpty()&&noteUnpinList.isEmpty()){
                    tvMes2.setVisibility(View.VISIBLE);
                }
            }
        },startDate, endDate,true);
    }
    public void updateAdapter(){
        n.getAllPinByDayCallBack(new NoteDAO.FirebaseCallBack() {
            @Override
            public void onCallBack(ArrayList<Note> noteList) {
            }
            @Override
            public void onCallBack() {
            }
            @Override
            public void onCallBack(ArrayList<Note> noteList, ArrayList<Note> noteUnpinList) {
                pinListAdapter.update(noteList);
                noteListAdapter.update(noteUnpinList);
            }
        },today,today,true);

    }
}