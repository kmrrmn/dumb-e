package com.rmn.dumb_e.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by rmn on 26-01-2017.
 */

public class StringToSpeech {

    static TextToSpeech textToSpeech;
    public static void Speak(Context context,String text){
        textToSpeech=new TextToSpeech(context
                , new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH, null);
    }
}
