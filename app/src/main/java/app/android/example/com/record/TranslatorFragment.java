package app.android.example.com.record;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Arvid on 2016-11-24.
 */

//TODO: fixa lutnings grejen, krånglar med tts (se onResume, onPause) eller shurdown efter den sagt ord och sen skapa en ny när man trycker på knappen;
// Activity app.android.example.com.record.MainActivity has leaked ServiceConnection android.speech.tts.TextToSpeech$Connection@da76a4c that was originally bound here

public class TranslatorFragment extends Fragment {

    private TextView txtSpeechInput;
    private TextView txtSpeechOutput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public TextToSpeech t1;
    private ImageButton b1;

    public TranslatorFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View transView = inflater.inflate(R.layout.fragment_translator, container, false);

        txtSpeechInput = (TextView) transView.findViewById(R.id.txtSpeechInput);
        txtSpeechOutput = (TextView) transView.findViewById(R.id.txtSpeechOutput);
        btnSpeak = (ImageButton) transView.findViewById(R.id.btnSpeak);
        b1 = (ImageButton) transView.findViewById(R.id.btnTalk);

        t1=new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.FRANCE);
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = txtSpeechInput.getText().toString();
                //Toast.makeText(getActivity(), toSpeak,Toast.LENGTH_SHORT).show();
                //String trans = "Say hello to my little friend";
                new GetTranslationTask().execute(toSpeak);
                //t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        // hide the action bar
        //getActionBar().hide();

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });


        return transView;
    }

    @Override
    public void onPause() {
        t1.shutdown();
        super.onPause();
    }

    @Override
    public void onResume() {
        t1=new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.FRANCE);
                }
            }
        });
        super.onResume();
    }

    public void speakWords(String speech) {

        // speak straight away
        t1.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()); //sätter språk till telefonens defualt?
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == Activity.RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    txtSpeechOutput.setText("");

                }
                break;
            }

        }
    }

    class GetTranslationTask extends AsyncTask<String, Void, String> {

        private String getTransText(String json) throws JSONException {
            final String T_TEXT = "text";
            try {
                JSONObject forecastJson = new JSONObject(json);
                JSONArray textObject = forecastJson.getJSONArray(T_TEXT);
                String translation = textObject.getString(0);
                if (translation != null) {
                    Log.v("Transplation: ", translation);
                    return translation;
                } else {
                    return "snett";
                }
            } catch (JSONException e) {
                Log.e("JSONERROR", e.getMessage(), e);
                e.printStackTrace();
                return "Error JSON";
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "..";
            //String res = "testing";
            if (params.length == 0) {
                return "";
            }

            String trans = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr;
            String format = "plain";
            String lang = "sv-fr";
            String apikey = "trnsl.1.1.20161120T095352Z.d04c8868138af811.50da8dc61d350802f9cf417f1718d51451f6023c";

            try {
                final String BASE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
                final String TEXT_PARAM = "text";
                final String LANG_PARAM = "lang";
                final String FORMAT_PARAM = "format";
                final String KEY_PARAM = "key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(KEY_PARAM, apikey)
                        .appendQueryParameter(TEXT_PARAM, trans)
                        .appendQueryParameter(LANG_PARAM, lang)
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v("URL:", "URL: " + url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                forecastJsonStr = buffer.toString();
                Log.v("FECTHED", "TXTJSON:" + forecastJsonStr);
                return getTransText(forecastJsonStr);
                // VIEWTEXT
            } catch (IOException e) {
                Log.e("ERROR", "Error ", e);
                return null;
            } catch (JSONException e) {
                Log.e("ERROR", e.getMessage(), e);
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("ERROR", "Error closing stream", e);
                    }
                }
            }
            return res;
        }

        protected void onPostExecute(String res) {
            //dosomething with res
            txtSpeechOutput.setText(res);
            t1.speak(res, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
