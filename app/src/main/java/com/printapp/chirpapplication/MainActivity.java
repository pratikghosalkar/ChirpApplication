package com.printapp.chirpapplication;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.Random;

import io.chirp.connect.ChirpConnect;
import io.chirp.connect.interfaces.ConnectEventListener;
import io.chirp.connect.interfaces.ConnectSetConfigListener;
import io.chirp.connect.models.ChirpError;
import io.chirp.connect.models.ConnectState;


public class MainActivity extends AppCompatActivity {

    private ChirpConnect chirpConnect;
    private Context context;

    private static final int RESULT_REQUEST_RECORD_AUDIO = 1;

    EditText editTextMessageToBeSent;

    TextView status;
    TextView lastChirp;
    TextView versionView;

    Button startStopSdkBtn;
    Button startStopSendingBtn;

    Boolean startStopSdkBtnPressed = false;

    String APP_KEY = "B6BfB90E2FacBc20de7EA85E8";
    String APP_SECRET = "8132C2cFEd1a97DF7AC249eF54CE6ec149403ABAADEF099605";
    String APP_CONFIG = "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";

    String TAG = "ConnectDemoApp";

    View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parentLayout = findViewById(android.R.id.content);

        editTextMessageToBeSent = findViewById(R.id.editTextMessageToSend);
        status = (TextView) findViewById(R.id.stateValue);
        lastChirp = (TextView) findViewById(R.id.lastChirp);
        versionView = (TextView) findViewById(R.id.versionView);
        startStopSdkBtn = (Button) findViewById(R.id.startStopSdkBtn);
        startStopSendingBtn = (Button) findViewById(R.id.startStopSengingBtn);

        startStopSendingBtn.setAlpha(.4f);
        startStopSendingBtn.setClickable(false);
        startStopSdkBtn.setAlpha(.4f);
        startStopSdkBtn.setClickable(false);

        context = this;

        Log.v("Connect Version: ", ChirpConnect.getVersion());
        versionView.setText(ChirpConnect.getVersion());

        if (APP_KEY.equals("") || APP_SECRET.equals("")) {
            Log.e(TAG, "APP_KEY or APP_SECRET is not set. " +
                    "Please update with your APP_KEY/APP_SECRET from admin.chirp.io");
            return;
        }

        /**
         * Key and secret initialisation
         */
        chirpConnect = new ChirpConnect(this, APP_KEY, APP_SECRET);
        chirpConnect.setConfig(APP_CONFIG, new ConnectSetConfigListener() {

            @Override
            public void onSuccess() {

                //Set-up the connect callbacks
                chirpConnect.setListener(connectEventListener);
                //Enable Start/Stop button
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startStopSdkBtn.setAlpha(1f);
                        startStopSdkBtn.setClickable(true);
                    }
                });
            }

            @Override
            public void onError(ChirpError setConfigError) {
                Log.e("setConfigError", setConfigError.getMessage());
            }
        });

    }


    ConnectEventListener connectEventListener = new ConnectEventListener() {

        @Override
        public void onSending(byte[] data, byte channel) {
            /**
             * onSending is called when a send event begins.
             * The data argument contains the payload being sent.
             */
            String hexData = "null";
            if (data != null) {
                hexData = chirpConnect.payloadToHexString(data);
            }
            Log.v("connectdemoapp", "ConnectCallback: onSending: " + hexData + " on channel: " + channel);
            String string = new String(data, Charset.forName("UTF-8"));
            updateLastPayload(string);
        }

        @Override
        public void onSent(byte[] data, byte channel) {
            /**
             * onSent is called when a send event has completed.
             * The data argument contains the payload that was sent.
             */
            String hexData = "null";
            if (data != null) {
                hexData = chirpConnect.payloadToHexString(data);
            }
            String string = new String(data, Charset.forName("UTF-8"));
            updateLastPayload(string);
            Log.v("connectdemoapp", "ConnectCallback: onSent: " + hexData + " on channel: " + channel);
        }

        @Override
        public void onReceiving(byte channel) {
            /**
             * onReceiving is called when a receive event begins.
             * No data has yet been received.
             */
            Log.v("connectdemoapp", "ConnectCallback: onReceiving on channel: " + channel);
        }

        @Override
        public void onReceived(byte[] data, byte channel) {
            /**
             * onReceived is called when a receive event has completed.
             * If the payload was decoded successfully, it is passed in data.
             * Otherwise, data is null.
             */
            String hexData = "null";
            if (data != null) {
                hexData = chirpConnect.payloadToHexString(data);
                String string = new String(data, Charset.forName("UTF-8"));
//                Toast.makeText(context, "Data received : " + string, Toast.LENGTH_SHORT).show();
            }
            Log.v("connectdemoapp", "ConnectCallback: onReceived: " + hexData + " on channel: " + channel);
            String string = new String(data, Charset.forName("UTF-8"));
            updateLastPayload(string);
        }

        @Override
        public void onStateChanged(byte oldState, byte newState) {
            /**
             * onStateChanged is called when the SDK changes state.
             */
            ConnectState state = ConnectState.createConnectState(newState);
            Log.v("connectdemoapp", "ConnectCallback: onStateChanged " + oldState + " -> " + newState);
            if (state == ConnectState.ConnectNotCreated) {
                updateStatus("NotCreated");
            } else if (state == ConnectState.AudioStateStopped) {
                updateStatus("Stopped");
            } else if (state == ConnectState.AudioStatePaused) {
                updateStatus("Paused");
            } else if (state == ConnectState.AudioStateRunning) {
                updateStatus("Running");
            } else if (state == ConnectState.AudioStateSending) {
                updateStatus("Sending");
            } else if (state == ConnectState.AudioStateReceiving) {
                updateStatus("Receiving");
            } else {
                updateStatus(newState + "");
            }

        }

        @Override
        public void onSystemVolumeChanged(int oldVolume, int newVolume) {
            /**
             * onSystemVolumeChanged is called when the system volume is changed.
             */
//            Snackbar snackbar = Snackbar.with(MainActivity.this);
//            snackbar.text("System volume has been changed to: " + newVolume);
//            snackbar.show(MainActivity.this);
//            Snackbar.make(parentLayout, "System volume has been changed to: " + newVolume, Snackbar.LENGTH_LONG);
////            snackbar.setAction("CLOSE", new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////
////                }
////            })
//                    .setActionTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light))
//                    .show();
            Log.v("connectdemoapp", "System volume has been changed, notify user to increase the volume when sending data");
        }

    };

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RESULT_REQUEST_RECORD_AUDIO);
        } else {
            if (startStopSdkBtnPressed) startSdk();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RESULT_REQUEST_RECORD_AUDIO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (startStopSdkBtnPressed) stopSdk();
                }
                return;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        chirpConnect.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            chirpConnect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        stopSdk();
    }

    public void updateStatus(final String newStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                status.setText(newStatus);
            }
        });
    }

    public void updateLastPayload(final String newPayload) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lastChirp.setText(newPayload);
            }
        });
    }

    public void stopSdk() {
        ChirpError error = chirpConnect.stop();
        if (error.getCode() > 0) {
            Log.e("ConnectError: ", error.getMessage());
            return;
        }
        startStopSendingBtn.setAlpha(.4f);
        startStopSendingBtn.setClickable(false);
        startStopSdkBtn.setText("Start Sdk");
    }

    public void startSdk() {
        ChirpError error = chirpConnect.start();
        if (error.getCode() > 0) {
            Log.e("ConnectError: ", error.getMessage());
            return;
        }
        startStopSendingBtn.setAlpha(1f);
        startStopSendingBtn.setClickable(true);
        startStopSdkBtn.setText("Stop Sdk");
    }

    public void startStopSdk(View view) {
        /**
         * Start or stop the SDK.
         * Audio is only processed when the SDK is running.
         */
        startStopSdkBtnPressed = true;
        if (chirpConnect.getConnectState() == ConnectState.AudioStateStopped) {
            startSdk();
        } else {
            stopSdk();
        }
    }

    public void sendPayload(View view) {
        /**
         * A payload is a byte array dynamic size with a maximum size defined by the config string.
         *
         * Generate a random payload, and send it.
         */
        if (editTextMessageToBeSent.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Enter message to send", Toast.LENGTH_SHORT).show();
            return;
        }

        long maxPayloadLength = chirpConnect.getMaxPayloadLength();
        long size = (long) new Random().nextInt((int) maxPayloadLength) + 1;
        String string = "Hello World";
//        byte[] payload = string.getBytes(StandardCharsets.UTF_8);
//        String identifier = "Hello";
        String identifier = editTextMessageToBeSent.getText().toString().trim();
        byte[] payload = identifier.getBytes(Charset.forName("UTF-8"));
//        byte[] payload = chirpConnect.randomPayload(size);
        long maxSize = chirpConnect.getMaxPayloadLength();
        long payloadSize = payload.length;
        if (maxSize < payload.length) {
            Log.e("ConnectError: ", "Invalid Payload");
            return;
        }
        ChirpError error = chirpConnect.send(payload);
        if (error.getCode() > 0) {
            Log.e("ConnectError: ", error.getMessage());
            Toast.makeText(context, error.getMessage().substring(error.getMessage().indexOf(" ") + 1), Toast.LENGTH_SHORT).show();
        }
    }
}
