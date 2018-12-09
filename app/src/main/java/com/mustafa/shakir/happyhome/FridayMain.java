package com.mustafa.shakir.happyhome;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FridayMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //Button forward, back, left, right, stop, speakButton;
    String address = null;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friday_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                //this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.addDrawerListener(toggle);
        //toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Intent newint = getIntent();
        address = newint.getStringExtra(device_list.EXTRA_ADDRESS); //receive the address of the bluetooth device
        //forward = (Button) findViewById(R.id.go);
        //back = (Button) findViewById(R.id.back);
        //left = (Button) findViewById(R.id.left);
        //right = (Button) findViewById(R.id.right);
        //stop = (Button) findViewById(R.id.stop);
        //speakButton = (Button) findViewById(R.id.voice);
        new FridayMain.ConnectBT().execute(); //Call the class to connect
/*
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goForward();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnLeft();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnRight();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ploxstop();
            }
        });
        */
        /*speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startVoiceRecognitionActivity();
            }
       // });
       */
        //voiceinputbuttons();

        // Check to see if a recognition activity is present
// if running on AVD virtual device it will give this message. The mic
// required only works on an actual android device
        /*PackageManager pm = getPackageManager();
        List activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            speakButton.setOnClickListener((View.OnClickListener) this);
        } else {
            speakButton.setEnabled(false);
            speakButton.setText("Recognizer not present");
        }
        */
    }

    //public void voiceinputbuttons() {
        //speakButton = (Button) findViewById(R.id.voice);
    //}
    public void vibrationboi(){
        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(200);
    }
    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Your wish is my Command");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it
            // could have heard
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            // matches is the result of voice input. It is a list of what the
            // user possibly said.
            // Using an if statement for the keyword you want to use allows the
            // use of any activity if keywords match
            // it is possible to set up multiple keywords to use the same
            // activity so more than one word will allow the user
            // to use the activity (makes it so the user doesn't have to
            // memorize words from a list)
            // to use an activity from the voice input information simply use
            // the following format;
            // if (matches.contains("keyword here") { startActivity(new
            // Intent("name.of.manifest.ACTIVITY")

            if (matches.contains("go")) {
                goForward();
            }
            if (matches.contains("back")) {
                goBack();
            }

            if (matches.contains("left")) {
                turnLeft();
            }
            if (matches.contains("right")) {
                turnRight();
            }

            if (matches.contains("turn on")) {
                debugA();
            }
            if (matches.contains("turn off")) {
                debugB();
            }
        }
    }

    private void Disconnect() {
        if (btSocket != null) //If the btSocket is busy
        {
            try {
                btSocket.close(); //close connection
            } catch (IOException e) {
                msg("Error");
            }
        }
        finish(); //return to the first layout

    }

    public void goForward() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("f".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }
    public void debugA() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("a".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }
    public void debugB() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("b".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    public void turnLeft() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("l".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    public void turnRight() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("r".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    public void ploxstop() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("s".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    public void goBack() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("g".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.friday_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.remote) {
            RemoteBoi remoteBoi = new RemoteBoi();
            getSupportFragmentManager().beginTransaction().replace(R.id.boi,remoteBoi,remoteBoi.getTag()).commit();// Handle the camera action
        } else if (id == R.id.voiceNav) {
            VoiceFrag voiceFrag = new VoiceFrag();
            getSupportFragmentManager().beginTransaction().replace(R.id.boi,voiceFrag,voiceFrag.getTag()).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(FridayMain.this, "Turning Liberty into License", "Please Wait");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                msg("Connected.");
                TextView greet = (TextView)findViewById(R.id.frst) ;
                TextView inf = (TextView)findViewById(R.id.scnd);
                greet.setVisibility(View.VISIBLE);
                inf.setVisibility(View.VISIBLE);
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}
