package pl.ofalwoz.factorialmultithreadapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {

    BigInteger result;
    TextView showResult;
    TextView insertNumber;
    int Number, i, n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertNumber = findViewById(R.id.insertnumber);
        showResult = findViewById(R.id.showresult);

        showResult.setText("");
    }

    public void Count(View view) {
        Number = Integer.parseInt(insertNumber.getText().toString());
        BigInteger iloczyn = BigInteger.valueOf(1);
        for (int i=1; i<=Number; i++) {
           iloczyn = iloczyn.multiply(BigInteger.valueOf(i));
        }
        showResult.setText(String.valueOf(iloczyn));
    }
}