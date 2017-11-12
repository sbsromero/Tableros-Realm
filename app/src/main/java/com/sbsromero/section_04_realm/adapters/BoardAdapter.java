package com.sbsromero.section_04_realm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sbsromero.section_04_realm.R;
import com.sbsromero.section_04_realm.models.Board;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by USERPC on 11/11/2017.
 */

public class BoardAdapter extends BaseAdapter {

    private Context context;
    private List<Board> list;
    private int layout;

    public BoardAdapter(Context context, List<Board> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Board getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder vh;
        if(view==null){
            view = LayoutInflater.from(context).inflate(layout,null);
            vh = new ViewHolder();
            vh.title = (TextView) view.findViewById(R.id.textViewBoardTitle);
            vh.notes = (TextView) view.findViewById(R.id.textViewBoardNotes);
            vh.createdAt = (TextView) view.findViewById(R.id.textViewBoardDate);

            view.setTag(vh);
        }
        else{
            vh = (ViewHolder) view.getTag();
        }
        Board board = list.get(i);
        vh.title.setText(board.getTitle());
        int numberNotes = board.getNotes().size();
        String textForNotes = (numberNotes == 1) ? numberNotes + " Note": numberNotes + " Notes";
        vh.notes.setText(textForNotes);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String createdAt = df.format(board.getCreateAt());
        vh.createdAt.setText(createdAt);
        return view;
    }

    public class ViewHolder{
        TextView title;
        TextView notes;
        TextView createdAt;
    }
}
