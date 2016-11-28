package app.android.example.com.record;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Arvid on 2016-11-28.
 */

public class ToplistFragment extends Fragment {

    private ArrayAdapter<String> topAdapter;

    public ToplistFragment () {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //TESTDATA
        String[] data = {
                "Hej jag gillar blå katter - 15",
                "Knåda degen - 13",
                "Bada bastu - 8",
                "Hoppa högt - 3"
        };
        List<String> testData = new ArrayList<String>(Arrays.asList(data));

        topAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.toplist_item,
                R.id.list_item_text,
                testData);

        View rView = inflater.inflate(R.layout.fragment_toplist, container, false);
        Button navTrans = (Button) rView.findViewById(R.id.transbutton);

        navTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Button", "Trans Button");
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, new TranslatorFragment());
                ft.commit();
            }
        });

        ListView listView = (ListView) rView.findViewById(R.id.listview_forecast);
        listView.setAdapter(topAdapter);

        return rView;
    }

}
