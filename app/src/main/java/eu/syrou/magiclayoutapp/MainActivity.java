package eu.syrou.magiclayoutapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button animateButton = (Button) findViewById(R.id.animatebutton);
        assert animateButton != null;
        animateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView5 = (TextView) findViewById(R.id.textview5);
                textView5.setVisibility(View.VISIBLE);
            }
        });
        Button restoreButton = (Button) findViewById(R.id.restorebutton);
        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView5 = (TextView) findViewById(R.id.textview5);
                textView5.setVisibility(View.GONE);
            }
        });
    }
}
