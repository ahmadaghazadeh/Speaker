package ahmad.aghazadeh.speaker;

import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener,TextToSpeech.OnUtteranceCompletedListener {

    private TextToSpeech tts;
    private Button btnSpeak;
    private Button btnRest;
    private EditText numDelay;
    private EditText numDiv;
    CheckBox checkBox;
    EditText txtText;
    String[] items;
    int current =0;
    boolean flag=false;
    int delay;
    private static final String PREF_DELAY= "pref_delay";
    private static final String PREF_DIV= "pref_div";
    private static final String PREF_CONTEXT= "pref_context";
    private static final String PREF_CURRENT= "pref_current";
    private static final String PREF_CHECKED= "pref_checked";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts = new TextToSpeech(this, this);
        btnSpeak =   findViewById(R.id.btnStart);
        btnRest =   findViewById(R.id.btnRest);
        txtText =   findViewById(R.id.txtContent);
        numDelay =   findViewById(R.id.numDelay);
        numDiv =   findViewById(R.id.numDiv);
        checkBox =   findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceManager pm=new PreferenceManager(MainActivity.this);
                pm.set(PREF_CHECKED,isChecked);
            }
        });



        numDelay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                PreferenceManager pm=new PreferenceManager(MainActivity.this);
                int i=1;
                try {
                    i=Integer.parseInt(s.toString());
                } catch (Exception e) {
                }
                pm.set(PREF_DELAY,i);
            }
        });

        numDiv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                PreferenceManager pm=new PreferenceManager(MainActivity.this);
                int i=1;
                try {
                    i=Integer.parseInt(s.toString());
                } catch (Exception e) {
                }
                pm.set(PREF_DIV,i);
            }
        });

        txtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                PreferenceManager pm=new PreferenceManager(MainActivity.this);
                pm.set(PREF_CONTEXT,s.toString());
            }
        });
        PreferenceManager pm=new PreferenceManager(this);
        checkBox.setChecked(pm.get(PREF_CHECKED,true));
        String text = pm.get(PREF_CONTEXT,"");
        items=text.split("\n");
        txtText.setText(text, TextView.BufferType.SPANNABLE);
        setText();

        int delay=pm.get(PREF_DELAY,1);
        numDelay.setText(""+delay);
        numDiv.setText(""+pm.get(PREF_DIV,5));

        current=pm.get(PREF_CURRENT,0);

        if(current==items.length)
            current--;
        setTitle(current+" - "+items[current]);
        // button on click event
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                speakOut();
            }

        });
        btnRest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                current=0;
                PreferenceManager pm=new PreferenceManager(MainActivity.this);
                pm.set(PREF_CURRENT,current);
                setTitle(current+" - "+items[current]);
                setText();
            }

        });

    }

    private void setText() {
        try {
            Spannable s = txtText.getText();
            String str=txtText.getText().toString().toLowerCase();
            int start = str.indexOf(items[current].toLowerCase());
            int end = start + items[current].length();
            s.setSpan(new ForegroundColorSpan(0xFF000000), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new ForegroundColorSpan(0xFFEC407A), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            e.getMessage();
        }


    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            tts.setOnUtteranceCompletedListener(this);
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                btnSpeak.setEnabled(true);

            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut() {
        items=txtText.getText().toString().toLowerCase().split("\n");
        playTTS();
    }
    private void playTTS()
    {
        if(items.length>current)
        {
            flag=true;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,items[current]);
            tts.speak(items[current], TextToSpeech.QUEUE_FLUSH, params);
            setTitle(current+" - "+items[current] +"("+delay/1000.0+")");
            setText();
            current++;
            PreferenceManager pm=new PreferenceManager(MainActivity.this);
            pm.set(PREF_CURRENT,current);

        }else {
            Toast.makeText(this,"Finished",Toast.LENGTH_LONG).show();
            flag=false;
        }
        setReadOnly();
    }

    private void setReadOnly() {
        if(flag){
            btnSpeak.setText("Stop");
        }
        else{
            btnSpeak.setText("Start");
        }
        txtText.setEnabled(!flag);
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        try {
            delay=0;
            if(checkBox.isChecked())
            {
                if(current!=0)
                {
                    int div=Integer.parseInt( numDiv.getText().toString());
                    delay=(int)(items[current-1].length()/(div *1.0) *1000);
                }
            }
            delay+=Integer.parseInt(numDelay.getText().toString())*1000;
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            if(flag)
                               playTTS();
                            else
                              setReadOnly();
                        }
                    });
                }
            }, delay);

        } catch (Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }


}
