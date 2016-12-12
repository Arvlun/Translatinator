package app.android.example.com.record;

import android.app.LoaderManager;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        final Cursor uCursor = cursor;
        TextView textView = (TextView) view.findViewById(R.id.list_item_phrase_textview);


        //Ändra och skrev egen funktion om vi ändrar så att det är mer saker i databasen
        int phraseColID = cursor.getColumnIndex(TransDBcontract.PhrasesDefs.PHRASE_COL);
        Button delButton = (Button) view.findViewById(R.id.list_item_removeButton);

        String phrase = cursor.getString(phraseColID);
        textView.setText(phrase);


        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        delButton.setTag(id);
        textView.setTag(id);

        delButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rowId = (Integer) v.getTag();
                Log.v("DELETE: ", ""+ rowId);
                long ret = deletePhrase(rowId, v.getContext());
                Log.v("DELETE: ", ""+ ret);
                if (ret > 0) {
                    notifyDataSetChanged();
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rowId = (Integer) v.getTag();
                Log.v("TEXTCLICK: ", ""+ rowId);
            }
        });
    }

    public long deletePhrase(int id, Context context) {

        String[] whereArgs = new String[] {""+id};

        int deletedId = context.getContentResolver().delete(
                TransDBcontract.PhrasesDefs.CONTENT_URI,
                "_id=?",
                whereArgs
        );

        return deletedId;
    }
}
