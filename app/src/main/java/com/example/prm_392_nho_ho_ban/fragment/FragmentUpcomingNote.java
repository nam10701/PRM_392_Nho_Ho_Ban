package com.example.prm_392_nho_ho_ban.fragment;

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

import java.util.ArrayList;
//sd
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentUpcomingNote#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUpcomingNote extends Fragment {
    private RecyclerView noteRecyclerView;
    private RecyclerView pinRecyclerView;
    private NoteListAdapter noteListAdapter;
    private NoteListAdapter pinListAdapter;
    private TextView tvMes3;
    private NoteDAO n = new NoteDAO();
    public FragmentUpcomingNote() {
       super(R.layout.fragment_upcoming_note);
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentUpcomingNote newInstance(String param1, String param2) {
        FragmentUpcomingNote fragment = new FragmentUpcomingNote();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void bindingUI(View view) {
        tvMes3 = view.findViewById(R.id.tvMes3);
        noteRecyclerView = view.findViewById(R.id.noteListRecyclerView);
        pinRecyclerView = view.findViewById(R.id.PinListRecyclerView);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_note, container, false);
        bindingUI(view);
        showUpcomingNote();
        return view;
    }

    private void showUpcomingNote() {
        n.getAllUpcomingNoteCallBack(new NoteDAO.FirebaseCallBack()  {
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
                    tvMes3.setVisibility(View.VISIBLE);
                }
            }
        }, User.USER);
    }
    public void updateAdapter(){
        n.getAllUpcomingNoteCallBack(new NoteDAO.FirebaseCallBack() {
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
        },User.USER);

    }
    }