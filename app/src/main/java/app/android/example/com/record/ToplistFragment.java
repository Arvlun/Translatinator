package app.android.example.com.record;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import app.android.example.com.record.db.TransDBcontract;

/**
 * Created by Arvid on 2016-11-28.
 */

//TODO: GÖR SÅ ATT DEN UPPDATERAS NÄR MAN TAR BORT OCH LÄGGER TILL MED EN GÅNG
//TODO: FIXA så att den öppnar nytt fragment med översättning möjligheter när man trycker på den istället för delete
//TODO: Fixa nån vettig knapp för delete på nåt sätt ---

public class ToplistFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PHRASELIST_LOADERID = 0;
    private PhraseAdapter phraseAdapter;

    public ToplistFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Uri phraseUri = TransDBcontract.PhrasesDefs.CONTENT_URI;
        //TODO: ändra så att det inte är massa null som skickas in --
        //Cursor cursor = getActivity().getContentResolver().query(phraseUri, null, null, null, null);

        phraseAdapter = new PhraseAdapter(getActivity(), null, 0);

        View rView = inflater.inflate(R.layout.fragment_toplist, container, false);
        Button navTrans = (Button) rView.findViewById(R.id.transbutton);
        Button savePhrase = (Button) rView.findViewById(R.id.savePhraseButton);
        final EditText phraseToSave = (EditText) rView.findViewById(R.id.textPhrase);

        navTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Button", "Trans Button");
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, new TranslatorFragment());
                ft.commit();
            }
        });

        savePhrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Button", "SAVE phrase Button" + phraseToSave.getText());
                //String tempPhrase = (String) phraseToSave.getText();
                addPhrase(phraseToSave.getText().toString());
            }
        });

        ListView listView = (ListView) rView.findViewById(R.id.listview_forecast);
        listView.setAdapter(phraseAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                deletePhrase(id);
            }
        });

        return rView;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//    }

    private long addPhrase(String phrase) {

        long phraseId;

        //Lägg till senare för att hindra användaren från att lägga till flera av samma fras;
        //Cursor phraseCursor;

        ContentValues phraseValues = new ContentValues();

        phraseValues.put(TransDBcontract.PhrasesDefs.PHRASE_COL, phrase);
        Context lContext;
        if (this.isAdded()) {
            lContext = getActivity();
        } else {
            Log.v("CONTEXT ERR", "fragment not added");
            return -1;
        }


        Uri insertedUri = lContext.getContentResolver().insert(
                TransDBcontract.PhrasesDefs.CONTENT_URI,
                phraseValues

        );

        phraseId = ContentUris.parseId(insertedUri);

        return phraseId;
    }

    //TODO: Fundera på hur man kan ta bort flytta från UIt? Om det är nödvändigt
    public long deletePhrase(int id) {

        Context lContext;
        if (this.isAdded()) {
            lContext = getActivity();
        } else {
            Log.v("CONTEXT ERR", "fragment not added");
            return -1;
        }

        String[] whereArgs = new String[] {""+id};

        int deletedId = lContext.getContentResolver().delete(
                TransDBcontract.PhrasesDefs.CONTENT_URI,
                "_id=?",
                whereArgs
        );

        return deletedId;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(PHRASELIST_LOADERID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri phraseUri = TransDBcontract.PhrasesDefs.CONTENT_URI;
        //TODO: ändra så att det inte är massa null som skickas in --
        CursorLoader cursorLoader = new CursorLoader(getActivity(), phraseUri, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        phraseAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        phraseAdapter.swapCursor(null);
    }
}
