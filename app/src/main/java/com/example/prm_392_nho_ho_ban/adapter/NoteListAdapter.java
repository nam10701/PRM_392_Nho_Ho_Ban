package com.example.prm_392_nho_ho_ban.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.bean.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.WordViewHolder> {

    private final ArrayList<Note> noteList;
    LayoutInflater mInflater;
    Context context;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm");


    public NoteListAdapter(Context context, ArrayList<Note> noteList) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.noteList = noteList;

    }

    @SuppressLint("NotifyDataSetChanged")
    public void update(ArrayList<Note> datas){
        noteList.clear();
        noteList.addAll(datas);
        notifyDataSetChanged();
        Log.i("update","updateNe");
    }

    @NonNull
    @Override
    public NoteListAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(
                R.layout.note_item_recycleview, parent, false);
        return new WordViewHolder(mItemView, this);

    }

    @Override
    public void onBindViewHolder(@NonNull NoteListAdapter.WordViewHolder holder, int position) {
        Note mCurrent = noteList.get(position);
        String title = mCurrent.getTitle();
        String content = mCurrent.getContent();

        holder.tvNoteTitle.setText(title);
        holder.tvNoteContent.setText(content);
        holder.tvDate.setText(sdf.format(new Date(mCurrent.getDate().getSeconds()*1000)));

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final NoteListAdapter mAdapter;
        protected TextView tvNoteTitle;
        protected TextView tvNoteContent;
        protected TextView tvDate;
        public WordViewHolder(@NonNull View itemView, NoteListAdapter adapter) {
            super(itemView);
            tvNoteTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvNoteContent = itemView.findViewById(R.id.tvNoteContent);
            tvDate = itemView.findViewById(R.id.tvDate);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, tvNoteTitle.getText().toString() + " Click ", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(context,NoteDetailActivity.class);
//            intent.putExtra("label",wordItemView.getText().toString());
//            context.startActivity(intent);
        }
    }
}
