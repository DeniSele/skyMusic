package the.prothean.skymusic;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import the.prothean.entryactivity.R;

public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_activity);
    }

    public void onPlayerButton(View view) {}

    public void onShazamButton(View view) {}
}
