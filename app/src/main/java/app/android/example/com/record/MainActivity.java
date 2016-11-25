package app.android.example.com.record;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.container, new TranslatorFragment())
                    .commit();
        }
    }

    // För att sätta upp menyn med inställningar och sharefunktionen behöver skapa layouts för dessa tror jag
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


}
