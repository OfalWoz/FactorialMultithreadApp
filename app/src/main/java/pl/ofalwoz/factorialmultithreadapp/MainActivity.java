package pl.ofalwoz.factorialmultithreadapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AtomicFile;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private static final Object LOCK = new Object();
    private static final String TAG = "MainActivity";
    private BigInteger Iloczyn;
    private TextView showResult;
    private TextView insertNumber;
    private Button countBtn;
    private volatile int Number, n;
    private volatile AtomicInteger i;
    private volatile boolean abort = true;

    private Handler mUiHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //zablokowane obracanie ekranu

        insertNumber = findViewById(R.id.insertnumber);
        showResult = findViewById(R.id.showresult);
        countBtn = findViewById(R.id.button);

        showResult.setText("");
        insertNumber.setText("0");

        countBtn.setOnClickListener(view -> {
            if (insertNumber.getText().toString().isEmpty() || insertNumber.getText().toString().contains(",") || insertNumber.getText().toString().contains(".") || insertNumber.getText().toString().contains("-") || insertNumber.getText().toString().contains(" ")) {
                showResult.setText("It must be positive number.");
            } else {
                Count();
            }
        });
    }
    public void Calculating(AtomicInteger Number){ //liczenie silni
        synchronized (LOCK) {
            Iloczyn = BigInteger.ONE;
            i = new AtomicInteger(1);
            while (i.get() <= Number.get()) {
                Iloczyn = Iloczyn.multiply(BigInteger.valueOf(i.get()));
                i.getAndIncrement();
                Log.d(TAG, "Thread: " + Thread.currentThread().getName() + " Number: " + i.get()); //wyswietla w logu w czasie rzeczywistym ktory watek jest na jakiej liczbie
            }
        }
    }

    public void Count() { //sprawdzanie czy licznba jest mniejsza niz 20 czy wieksza
        countBtn.setEnabled(false);
        Number = Integer.parseInt(insertNumber.getText().toString());
        Log.d(TAG, "Thred name 1 " + Thread.currentThread().getName());
        if (Number <= 20) {
            StartThread(Number, 1);
        } else {
            n = Runtime.getRuntime().availableProcessors(); // liczba dostepnych watkow
            for (int j = 0; j < n; j++) {
                StartThread(Number, n);
            }
        }
        mUiHandler.postDelayed(() -> {
            showResult.setText(String.valueOf(Number) + "! = " + String.valueOf(Iloczyn));
            countBtn.setEnabled(true);
        }, 2000);
    }

    @Override
    protected void onStop() {
        Log.d(TAG,"STOP");
        super.onStop();
        abort = false;
    }

    private void StartThread(int Number, int n) { //dzialanie na watkach
        abort = true;
        AtomicInteger atomicNumber = new AtomicInteger(Number);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showResult.setText("Calculating.");
                    }
                });
                for (int j = 0; j < n; j++) {
                    Calculating(atomicNumber);
                }
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) { }
                Log.d(TAG, "Wynik: " + Iloczyn + "\nLiczba watkow: " + n);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.interrupt();
    }
}