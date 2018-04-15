package com.jadem.androidpitscout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niraq on 3/11/2018.
 */

public class ColorCycleClass {

    private Runnable runnable;
    private boolean stopThread, isRed;
    private boolean canRun = false;
    private Integer id;
    private Spannable wordToSpan;
    private Context context;
    private Thread thread;
    private List<Integer> colorList;
    private List<Spannable> spannableList;

    public ColorCycleClass(Context context, TextView textView) {
        this.context = context;
        id = textView.getId();
        wordToSpan = new SpannableString(textView.getText());
        colorWheel();
        setSpannableList(textView.getText().toString());
        initializeRunnable();
        if(id != null) canRun = true;
    }

    //TODO: Use AsyncTask
    public void startCycle() {
        initializeRunnable();
        stopCycle();
        if(!canRun) return;
        stopThread = false;
        thread = null;
        thread = new Thread(runnable);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    public void setView(TextView textView) {
        id = textView.getId();
        wordToSpan = new SpannableString(textView.toString());
        setSpannableList(textView.getText().toString());
        if(context != null) canRun = true;
    }

    public void setText(String string) {
        wordToSpan = new SpannableString(string);
        setSpannableList(string);
    }

    //TODO: Test if this is necessary (EditText extends TextView).
    public void setView(EditText editText) {
        id = editText.getId();
        wordToSpan = new SpannableString(editText.toString());
        setSpannableList(editText.getText().toString());
        if(context != null) canRun = true;
    }

    public boolean isRunning() {
        if(thread != null) {
            return thread.isAlive();
        } else {
            return false;
        }
    }

    public void stopCycle() {
        stopThread = true;
    }

    private void colorWheel() {
        double freq = 0.3;
        colorList = new ArrayList<Integer>();

        //TODO: Should this be 21 or 22?
        for(int position = 0; position < 22; position++) {
            int red = (int) Math.round(Math.sin(freq*position + 0) * 127 + 128);
            int green = (int) Math.round(Math.sin(freq*position + 2) * 127 + 128);
            int blue  = (int) Math.round(Math.sin(freq*position + 4) * 127 + 128);
            colorList.add(Color.argb(255, red, green, blue));
        }
    }

    private void setSpannableList(String string) {
        spannableList = new ArrayList<Spannable>();
        for(int stringNum = 0; stringNum < 22; stringNum++) {
            int colorPos = 0;
            Spannable wordToSpan = new SpannableString(string);
            for(int charNum = 0; charNum < wordToSpan.length(); charNum++) {
                wordToSpan.setSpan(new ForegroundColorSpan(colorList.get(colorPos)), charNum, charNum + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                colorPos++;
                if(colorPos >= 22) colorPos = 0;
            }
            spannableList.add(wordToSpan);
        }
    }

    private void initializeRunnable() {
        runnable = null;
        runnable = new Runnable() {

            @Override
            public void run() {
                canRun = false;
                int pos = 0;
                while (pos < 22 && !Thread.interrupted() && !stopThread) {
                    wordToSpan = spannableList.get(pos);
                    pos++;

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        canRun = true;
                        return;
                    }

                    ((Activity) context).runOnUiThread(new Runnable() // start actions in UI thread
                    {

                        @Override
                        public void run() {
                            TextView view = (TextView) ((Activity) context).findViewById(id);
                            view.setText(wordToSpan);
                        }
                    });
                }
                canRun = true;

                if(!stopThread) startCycle();
            }
        };
    }

}