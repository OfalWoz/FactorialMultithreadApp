package pl.ofalwoz.factorialmultithreadapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public BigInteger Iloczyn;
    public TextView showResult;
    public TextView insertNumber;
    public int Number, i, n;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertNumber = findViewById(R.id.insertnumber);
        showResult = findViewById(R.id.showresult);

        showResult.setText("");

        if(savedInstanceState != null){
            i = savedInstanceState.getInt("counter_state");
            Number = savedInstanceState.getInt("number");
        }
    }

    public void Count(View view) {
        final Handler handler = new Handler();

        Number = Integer.parseInt(insertNumber.getText().toString());

        Runnable runnable = new Runnable() { //obliczenia na jednym watku
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showResult.setText("Calculating.");
                    }
                });
                Iloczyn = BigInteger.valueOf(1);
                for (i=1; i<=Number; i++) {
                    Iloczyn = Iloczyn.multiply(BigInteger.valueOf(i));
                    Log.d(TAG, "Thread number: "+ Thread.currentThread().getName());
                }
                synchronized (this){
                    try{
                        wait( Number);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                Log.d(TAG,"Number: "+ Iloczyn);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showResult.setText(String.valueOf(Iloczyn));
                    }
                });
            }
        };

        Log.d(TAG, "Thred name 1 "+ Thread.currentThread().getName());
        if(Number <= 20) {
            Thread thread = new Thread(runnable);
            thread.start();
        } else {
            int n = Runtime.getRuntime().availableProcessors(); // liczba dostepnych watkow
            for (int i = 0; i < n; i++) { //obliczanie na kilku watkach
                Thread object = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showResult.setText("Calculating.");
                            }
                        });
                        Iloczyn = BigInteger.valueOf(1);
                        for (int j=1; j<=Number; j++) {
                            Iloczyn = Iloczyn.multiply(BigInteger.valueOf(j));
                            Log.d(TAG, "Thread number: "+ Thread.currentThread().getName());
                        }
                        synchronized (this){
                            try{
                                wait(Number);
                            } catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                        Log.d(TAG,"Number: "+ Iloczyn);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showResult.setText(String.valueOf(Iloczyn));
                            }
                        });
                    }
                });
                object.start();
            }
        }
    }
}