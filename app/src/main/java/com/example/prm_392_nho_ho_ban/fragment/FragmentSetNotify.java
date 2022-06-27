package com.example.prm_392_nho_ho_ban.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.example.prm_392_nho_ho_ban.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSetNotify#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSetNotify extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private TextView noteRemindDate;
    private TextView noteRemindTime;
    private Button remindConfirm;

    private DatePickerDialog.OnDateSetListener setListener;

    private String remindDate;
    private String remindTime;

    private int nHour, nMinute;

    private Calendar calendar = Calendar.getInstance();
    private int nYear = calendar.get(Calendar.YEAR);
    private int nMonth = calendar.get(Calendar.MONTH);
    private int nDay = calendar.get(Calendar.DAY_OF_MONTH);

    private int lastSelectedHour = -1;
    private int lastSelectedMinute = -1;

    private void bindingView(View view) {
        noteRemindDate = view.findViewById(R.id.noteRemindDate);
        noteRemindTime = view.findViewById(R.id.noteRemindTime);
        remindConfirm = view.findViewById(R.id.btnConfirm);
    }

    public interface OnBtnSaveClickListener {
        void onClick(String date, String time);
    }

    private OnBtnSaveClickListener callback;

    public void setOnBtnSaveClickListener(OnBtnSaveClickListener callback) {
        this.callback = callback;
    }

    private void bindingAction() {
        noteRemindTime.setOnClickListener(this::onTimeSelect);
        noteRemindDate.setOnClickListener(this::onDateSelect);
        remindConfirm.setOnClickListener(this::onBtnConfirmClick);
    }

    private void onDateSelect(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext()
                , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                nMonth = month;
                String date = day+"/"+(month+1)+"/"+year;
                noteRemindDate.setText(date);
            }
        },nYear,nMonth,nDay);
        datePickerDialog.show();
    }

    private void onTimeSelect(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        nHour = hourOfDay;
                        nMinute = minute;
                        String time = nHour + ":" + nMinute;
                        SimpleDateFormat f24Hour = new SimpleDateFormat("HH:mm");
                        try {
                            Date date = f24Hour.parse(time);
                            noteRemindTime.setText(f24Hour.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, 24, 0, true
        );
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.updateTime(nHour,nMinute);
        timePickerDialog.show();
    }

    private void onBtnConfirmClick(View view) {
        if(!noteRemindDate.getText().toString().isEmpty() &&
        !noteRemindTime.getText().toString().isEmpty()) {
            remindDate = noteRemindDate.getText().toString();
            remindTime = noteRemindTime.getText().toString();
            if(this.callback!=null) {
                this.callback.onClick(remindDate, remindTime);
                closeFragment();
            }
        } else closeFragment();

    }

    private void closeFragment() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(this)
                .commit();
        requireActivity().getSupportFragmentManager().popBackStack();
    }


    public FragmentSetNotify() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSetNotify.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSetNotify newInstance(String param1, String param2) {
        FragmentSetNotify fragment = new FragmentSetNotify();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_notify, container, false);
        bindingView(view);
        bindingAction();
        return view;
    }
}