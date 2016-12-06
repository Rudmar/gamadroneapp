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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    public static int SELECT_DISCOVERED_DEVICE = 3;
    BluetoothDevice mmDevice;
    BluetoothSocket mmSocket = null;
    public static TextView statusMessage;
    public static TextView velocity;
    public static TextView altura;
    public static TextView bateria;
    private BluetoothAdapter bluetooth;
    Set<BluetoothDevice> pairedDevices;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusMessage = (TextView) findViewById(R.id.statusMessage);
        velocity = (TextView) findViewById(R.id.velocidade);
        altura = (TextView) findViewById(R.id.alturaTexts);
        bateria = (TextView) findViewById(R.id.bateriaText);

        bluetooth = BluetoothAdapter.getDefaultAdapter();

        final Handler handler = new Handler();
        final Handler handler2 = new Handler();

        altura.setText("3 m");
        bateria.setText("40 %");

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Random generator = new Random();
                int vel = generator.nextInt(40 - 5) + 5;
                velocity.setText(vel + " KM/H");
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);

        Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();
        if(pairedDevices.size() > 0) {
            for(BluetoothDevice device : pairedDevices){
                if(device.getName().equals("ubuntu-0")){
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

    public void sendBtMsg(String msg2send){
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        try {
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            if (!mmSocket.isConnected()) {
                mmSocket.connect();
            }

            String send = msg2send;

            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(send.getBytes());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    public void tryBT(View view){
        sendBtMsg("Esta mensagem foi enviada do meu APP GamaDrone\n");
        sendBtMsg("Teste de mensagem android -> python 1");
        sendBtMsg("Teste de mensagem android -> python 2");
    }

}