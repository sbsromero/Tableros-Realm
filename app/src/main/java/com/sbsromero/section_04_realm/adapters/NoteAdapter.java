package com.sbsromero.section_04_realm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sbsromero.section_04_realm.R;
import com.sbsromero.section_04_realm.models.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by USERPC on 11/11/2017.
 */

public class NoteAdapter extends BaseAdapter {

    private Context context;
    private List<Note> notes;
    private int layout;

    public NoteAdapter(Context context, List<Note> notes, int layout) {
        this.context = context;
        this.notes = notes;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Note getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder vh;
        if(view == null){
            view = LayoutInflater.from(context).inflate(layout,null);
            vh = new ViewHolder();
            vh.description = (TextView) view.findViewById(R.id.textViewNoteDescription);
            vh.createdAt = (TextView) view.findViewById(R.id.textViewNoteCreatedAt);
            view.setTag(vh);
        }else{
            vh = (ViewHolder) view.getTag();
        }
        Note note = notes.get(i);
        vh.description.setText(note.getDescription());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(note.getCreateAt());
        vh.createdAt.setText(date);
        return view;
    }

    public class ViewHolder {
        TextView description;
        TextView createdAt;
    }
}
