package app.gamadrone;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import app.gamadrone.R;

public class MainActivity extends AppCompatActivity {

    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    public static int SELECT_DISCOVERED_DEVICE = 3;
    public static TextView statusMessage;
    BluetoothAdapter bluetooth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusMessage = (TextView) findViewById(R.id.statusMessage);
        bluetooth = BluetoothAdapter.getDefaultAdapter();
    }

    public void turnOn(View view) {

        if (bluetooth == null){
            Toast.makeText(getApplicationContext(), "Este dispositivo nao suporta Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Bluetooth ativado", Toast.LENGTH_SHORT).show();
        }

    }

    public void turnOff(View view) {
        if(bluetooth.isEnabled()){
            bluetooth.disable();
            Toast.makeText(getApplicationContext(), "Bluetooth desligado", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth já estava desligado", Toast.LENGTH_LONG).show();
        }
    }

}
