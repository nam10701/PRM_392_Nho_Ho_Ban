package com.example.prm_392_nho_ho_ban.fragment;

import static com.example.prm_392_nho_ho_ban.MyApplication.dbRoom;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm_392_nho_ho_ban.AppDatabase;
import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.activity.WelcomeActivity;
import com.example.prm_392_nho_ho_ban.adapter.NoteListAdapter;
import com.example.prm_392_nho_ho_ban.bean.Note;
import com.example.prm_392_nho_ho_ban.bean.User;
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;
import com.example.prm_392_nho_ho_ban.dao.RoomNoteDAO;
import com.google.firebase.Timestamp;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAllNote#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAllNote extends Fragment {
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
    private Date today;
    private RecyclerView noteRecyclerView;
    private RecyclerView pinRecyclerView;
    private NoteListAdapter noteListAdapter;
    private NoteListAdapter pinListAdapter;
    private NoteDAO n = new NoteDAO();
    private TextView tvMes1;
    private ArrayList<Note> pinList;
    private ArrayList<Note> unPinList;
    private ArrayList<Note> emptyList = new ArrayList<>();
    RoomNoteDAO roomNoteDAO = dbRoom.createNoteDAO();
    private SearchView searchView = null;

    public FragmentAllNote() {
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
        setHasOptionsMenu(true);
    }

    private void bindingUI(View view) {
        getPinNote();
        getUnpinNote();
        tvMes1 = view.findViewById(R.id.tvMes1);
        noteRecyclerView = view.findViewById(R.id.noteListRecyclerView);
        pinRecyclerView = view.findViewById(R.id.PinListRecyclerView);

        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        pinRecyclerView.setLayoutManager(verticalLayoutManager);
        pinListAdapter = new NoteListAdapter(getActivity(), pinList);
        pinRecyclerView.setAdapter(pinListAdapter);

        LinearLayoutManager verticalLayoutManagerr
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        noteRecyclerView.setLayoutManager(verticalLayoutManagerr);
        noteListAdapter = new NoteListAdapter(getActivity(), unPinList);
        noteRecyclerView.setAdapter(noteListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_note, container, false);
        bindingUI(view);
        return view;
    }

    private void getPinNote() {
        pinList = (ArrayList<Note>) roomNoteDAO.getAllPin(true, User.USER.getUid(), true);

    }

    private void getUnpinNote() {
        unPinList = (ArrayList<Note>) roomNoteDAO.getAllPin(false, User.USER.getUid(), true);

    }

    public void updateAdapter() {
        getPinNote();
        getUnpinNote();
        if (pinListAdapter != null) {
            pinListAdapter.update(pinList);
        }
        if (noteListAdapter != null) {
            noteListAdapter.update(unPinList);
        }
        checkEmpty();
    }

    public NoteListAdapter getPinAdapter() {

        return pinListAdapter;
    }

    public NoteListAdapter getNoteAdapter() {

        return pinListAdapter;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = new SearchView(((WelcomeActivity) getActivity()).getSupportActionBar().getThemedContext());
        if (menuItem != null) {
            searchView = (SearchView) menuItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryHint("Type to search Note");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!newText.equals("")) {
                        pinList = emptyList;
                        unPinList = searchNote(newText);
                        pinListAdapter.update(pinList);
                        noteListAdapter.update(unPinList);
                        Log.d("tuan", "onQueryTextChange: " + unPinList.size());

                    } else {
                        updateAdapter();
                    }
                    return false;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }


    private ArrayList<Note> searchNote(String newText) {
        ArrayList<Note> myNote = (ArrayList<Note>) roomNoteDAO.searchNote(User.USER.getUid(), newText,true);
        return myNote;
    }

    private void checkEmpty() {
        if (tvMes1 != null) {
            if (pinList.isEmpty() && unPinList.isEmpty()) {
                tvMes1.setVisibility(View.VISIBLE);
            } else {
                tvMes1.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAdapter();
    }
}