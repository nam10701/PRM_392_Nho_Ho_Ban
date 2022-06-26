package com.example.prm_392_nho_ho_ban.fragment;

import static com.example.prm_392_nho_ho_ban.MyApplication.dbRoom;

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

import java.util.ArrayList;
import java.util.Date;
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
    private Timestamp today;
    private ArrayList<Note> pinList;
    private ArrayList<Note> unPinList;
    RoomNoteDAO roomNoteDAO = dbRoom.createNoteDAO();
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

        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        pinRecyclerView.setLayoutManager(verticalLayoutManager);
        pinListAdapter = new NoteListAdapter(getActivity(),getPinNote(today));
        pinRecyclerView.setAdapter(pinListAdapter);

        LinearLayoutManager verticalLayoutManagerr
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        noteRecyclerView.setLayoutManager(verticalLayoutManagerr);
        noteListAdapter = new NoteListAdapter(getActivity(),getUnpinNote(today));
        noteRecyclerView.setAdapter(noteListAdapter);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_note, container, false);
        today = new Timestamp(new Date());
        bindingUI(view);
        return view;
    }

    private ArrayList<Note> getPinNote(Timestamp today){
        pinList = (ArrayList<Note>) roomNoteDAO.getAllUpcomingNote(today,true, User.USER.getUid());
        return pinList;
    }

    private ArrayList<Note> getUnpinNote(Timestamp today){
        unPinList = (ArrayList<Note>) roomNoteDAO.getAllUpcomingNote(today,false,User.USER.getUid());
        return unPinList;
    }

    public void updateAdapter(){
        pinListAdapter.update(getPinNote(today));
        noteListAdapter.update(getUnpinNote(today));
        checkEmpty();
    }

    private void checkEmpty(){
        if(pinList.isEmpty()&&unPinList.isEmpty()){
            tvMes3.setVisibility(View.VISIBLE);
        }else{
            tvMes3.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAdapter();
    }
}