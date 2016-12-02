package app.android.example.com.record;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import app.android.example.com.record.db.TransDBcontract;

/**
 * Created by Arvid on 2016-12-02.
 */

public class PhraseAdapter extends CursorAdapter {

    public PhraseAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup vGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_phrase, vGroup, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView textView = (TextView) view.findViewById(R.id.list_item_phrase_textview);

        //Ändra och skrev egen funktion om vi ändrar så att det är mer saker i databasen
        int phraseid = cursor.getColumnIndex(TransDBcontract.PhrasesDefs.PHRASE_COL);
        String phrase = cursor.getString(phraseid);

        textView.setText(phrase);
    }
}
