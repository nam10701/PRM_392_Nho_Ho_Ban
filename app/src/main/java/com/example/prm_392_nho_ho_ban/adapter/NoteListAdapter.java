package com.example.prm_392_nho_ho_ban.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.bean.Note;

import java.util.ArrayList;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.WordViewHolder> {

    private final ArrayList<Note> noteList;
    LayoutInflater mInflater;
    Context context;

    public NoteListAdapter(Context context, ArrayList<Note> noteList) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.noteList = noteList;
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
        String content = mCurrent.getTitle();
        holder.tvNoteTitle.setText(content);
        holder.tvNoteContent.setText(mCurrent.getContent());
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
        public WordViewHolder(@NonNull View itemView, NoteListAdapter adapter) {
            super(itemView);
            tvNoteTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvNoteContent = itemView.findViewById(R.id.tvNoteContent);
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
