package app.gamadrone;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    public static int SELECT_DISCOVERED_DEVICE = 3;
    BluetoothDevice mmDevice;
    BluetoothSocket mmSocket = null;
    public static TextView statusMessage;
    private BluetoothAdapter bluetooth;
    Set<BluetoothDevice> pairedDevices;
    final byte delimiter = 33;
    int readBufferPosition = 0;

    final Handler handler = new Handler();

    public void sendBtMsg(String msg2send){
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        try {
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            if (!mmSocket.isConnected()){
                mmSocket.connect();
                statusMessage.setText("Socket conectado");
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void tryBT(View view){
        sendBtMsg("WTF!");
        statusMessage.setText("Mensagem enviada com sucesso");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Handler handler = new Handler();

        statusMessage = (TextView) findViewById(R.id.statusMessage);

        bluetooth = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("ubuntu-0")) //Note, you will need to change this to match the name of your device
                {
                    Log.e("RASPBERRY",device.getName());
                    mmDevice = device;
                    String name;
                    name = device.getName();
                    statusMessage.setText("Emparelhado com o dispositivo " + name);
                    break;
                }
            }
        }


    }

    public void turnOn(View view) {

        if (bluetooth == null){
            Toast.makeText(getApplicationContext(), "Este dispositivo nao suporta Bluetooth", Toast.LENGTH_SHORT).show();
            statusMessage.setText("Por algum motivo, o bluetooth\nnao esta funcionando");
        } else {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            statusMessage.setText("Bluetooth ativado");

            Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(getVisible, 0);
            statusMessage.setText("Bluetooth visivel para\noutros dispositivos");

        }

    }

    public void turnOff(View view) {
        if (bluetooth == null){
            Toast.makeText(getApplicationContext(), "Este dispositivo nao suporta Bluetooth", Toast.LENGTH_SHORT).show();
            statusMessage.setText("Por algum motivo, o bluetooth\nnao esta funcionando");
        }
        else if(bluetooth.isEnabled() == true){
            bluetooth.disable();
            Toast.makeText(getApplicationContext(), "Bluetooth desligado", Toast.LENGTH_LONG).show();
            statusMessage.setText("Bluetooth desligado");
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth já estava desligado", Toast.LENGTH_LONG).show();
            statusMessage.setText("Bluetooth ja estava desligado");
        }
    }

    public void list(View view){

        pairedDevices = bluetooth.getBondedDevices();

        ArrayList list = new ArrayList();

        for (BluetoothDevice bt : pairedDevices) list.add(bt.getName());
        String address = mmDevice.getAddress();
        Toast.makeText(getApplicationContext(), "Bluetooth conectato ao endereco " + address, Toast.LENGTH_LONG).show();
        Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("ubuntu-0")) //Note, you will need to change this to match the name of your device
                {
                    Log.e("RASPBERRY",device.getName());
                    mmDevice = device;
                    String name;
                    name = device.getName();
                    statusMessage.setText("Emparelhado com o dispositivo " + name);
                    break;
                }
            }
        }

    }

}