package app.gamadrone;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import app.gamadrone.R;

public class MainActivity extends AppCompatActivity {

    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    public static int SELECT_DISCOVERED_DEVICE = 3;
    public static TextView statusMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusMessage = (TextView) findViewById(R.id.statusMessage);
    }

    public void turnOn(View view) {

        BluetoothAdapter bluetooth;
        bluetooth = BluetoothAdapter.getDefaultAdapter();

        if (bluetooth == null){
            statusMessage.setText("Bluetooth nao funcional");
        } else {
            statusMessage.setText("Bluetooth funcional");
        }

        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        Intent search = new Intent(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);

    }

}
