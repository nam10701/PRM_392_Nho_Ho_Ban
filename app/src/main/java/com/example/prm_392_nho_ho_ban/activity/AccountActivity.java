package com.example.prm_392_nho_ho_ban.activity;

import static com.example.prm_392_nho_ho_ban.MyApplication.dbRoom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.adapter.AccountListAdapter;
import com.example.prm_392_nho_ho_ban.adapter.NoteListAdapter;
import com.example.prm_392_nho_ho_ban.bean.User;
import com.example.prm_392_nho_ho_ban.dao.RoomNoteDAO;
import com.example.prm_392_nho_ho_ban.dao.RoomUserDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {
    private RecyclerView accountRecyclerView;
    private AccountListAdapter accountListAdapter;
    private ArrayList<User> userList;
    private RoomUserDAO roomUserDAO = dbRoom.createUserDAO();
    private Button btnAddAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        bindingUI();
        bindingAction();
        authorize();
    }
    private void bindingAction(){
        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
    private void bindingUI() {
        userList = (ArrayList<User>) roomUserDAO.getAllUser();
        btnAddAccount = findViewById(R.id.btnAddAccount);
        accountRecyclerView = findViewById(R.id.accountRecycler);
        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        accountRecyclerView.setLayoutManager(verticalLayoutManager);
        accountListAdapter = new AccountListAdapter(this,userList);
        accountRecyclerView.setAdapter(accountListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        authorize();

    }
    private void authorize() {
        if (User.USER == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}