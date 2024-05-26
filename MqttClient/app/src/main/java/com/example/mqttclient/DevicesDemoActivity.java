package com.example.mqttclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mqttclient.mqtt.MqttService;
import com.example.mqttclient.protocol.AirConditioningMessage;
import com.example.mqttclient.protocol.BoolMessage;
import com.example.mqttclient.protocol.FloatMessage;
import com.example.mqttclient.protocol.IntMessage;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.HashMap;
import java.util.Map;

public class DevicesDemoActivity extends AppCompatActivity implements MqttService.MqttEventCallBack, CompoundButton.OnCheckedChangeListener {

    private TextView connectState, temperatureValue, humidityValue, pmValue, corbanValue,gasValue,waterValue,sunValue,peopleStatus, doorStatus;
    private EditText airCconditioningValue;
    private MqttService.MqttBinder mqttBinder;
    private String TAG = "MainActivity";
    private Switch parlourLightSwitch, curtain_switch, fan_socket_switch, air_conditioning_switch,socketStatus,pumpStatus;
    private Map<String, Integer> subscribeTopics = new HashMap<>();

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mqttBinder = (MqttService.MqttBinder) iBinder;
            mqttBinder.setMqttEventCallback(DevicesDemoActivity.this);
            if (mqttBinder.isConnected()) {
                connectState.setText("已连接");
                subscribeTopics();
            } else {
                connectState.setText("未连接");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_demo);

        connectState = findViewById(R.id.dev_connect_state);

        Intent mqttServiceIntent = new Intent(this, MqttService.class);
        bindService(mqttServiceIntent, connection, Context.BIND_AUTO_CREATE);

        temperatureValue = findViewById(R.id.temperature_value);

        humidityValue = findViewById(R.id.humidity_value);
        pmValue = findViewById(R.id.pm_value);
        gasValue = findViewById(R.id.gas_value);
        doorStatus = findViewById(R.id.door_status);

        corbanValue=findViewById(R.id.co2_value);
        waterValue=findViewById(R.id.water_value);
        sunValue=findViewById(R.id.sun_value);
        peopleStatus=findViewById(R.id.people_status);


        airCconditioningValue = findViewById(R.id.air_conditioning_value);
        parlourLightSwitch = findViewById(R.id.parlour_light_switch);
        parlourLightSwitch.setOnCheckedChangeListener(this);
        curtain_switch = findViewById(R.id.curtain_switch);
        curtain_switch.setOnCheckedChangeListener(this);
        fan_socket_switch = findViewById(R.id.fan_socket_switch);
        fan_socket_switch.setOnCheckedChangeListener(this);
        air_conditioning_switch = findViewById(R.id.air_conditioning_switch);
        air_conditioning_switch.setOnCheckedChangeListener(this);
        socketStatus=findViewById(R.id.socket_switch);
        socketStatus.setOnCheckedChangeListener(this);
        pumpStatus=findViewById(R.id.pump_switch);
        pumpStatus.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.parlour_light_switch:
                try {
                    if (compoundButton.isChecked()) {
                        mqttBinder.publishMessage("/test/light1",
                                new Gson().toJson(new BoolMessage(true)));
                    } else {
                        mqttBinder.publishMessage("/test/light1",
                                new Gson().toJson(new BoolMessage(false)));
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.socket_switch:
                try {
                    if (compoundButton.isChecked()) {
                        mqttBinder.publishMessage("/test/socket1",
                                new Gson().toJson(new BoolMessage(true)));
                    } else {
                        mqttBinder.publishMessage("/test/socket1",
                                new Gson().toJson(new BoolMessage(false)));
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.pump_switch:
                try {
                    if (compoundButton.isChecked()) {
                        mqttBinder.publishMessage("/test/pump1",
                                new Gson().toJson(new BoolMessage(true)));
                    } else {
                        mqttBinder.publishMessage("/test/pump1",
                                new Gson().toJson(new BoolMessage(false)));
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.curtain_switch:
                try {
                    if (compoundButton.isChecked()) {
                        mqttBinder.publishMessage("/test/curtain1",
                                new Gson().toJson(new BoolMessage(true)));
                    } else {
                        mqttBinder.publishMessage("/test/curtain1",
                                new Gson().toJson(new BoolMessage(false)));
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.fan_socket_switch:
                try {
                    if (compoundButton.isChecked()) {
                        mqttBinder.publishMessage("/test/fan1",
                                new Gson().toJson(new BoolMessage(true)));
                    } else {
                        mqttBinder.publishMessage("/test/fan1",
                                new Gson().toJson(new BoolMessage(false)));
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.air_conditioning_switch:
                try {
                    if (compoundButton.isChecked()) {
                        String json = new Gson().toJson(new AirConditioningMessage(true,
                                Float.parseFloat(airCconditioningValue.getText().toString())));
                        Log.d("json",json);
                        mqttBinder.publishMessage("/test/airConditioning",json);
                    } else {
                        String json = new Gson().toJson(new AirConditioningMessage(false,
                                Float.parseFloat(airCconditioningValue.getText().toString())));
                        Log.d("json",json);
                        mqttBinder.publishMessage("/test/airConditioning",json);
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    void subscribeTopics() {
        try {
            subscribeTopics.put("/test/temp",1);
            subscribeTopics.put("/test/hum", 2);
            subscribeTopics.put("/test/pm",3);
            subscribeTopics.put("/test/gas",4);
            subscribeTopics.put("/test/door",5);
            subscribeTopics.put("/test/co2",6);
            subscribeTopics.put("/test/waterTower",7);
            subscribeTopics.put("/test/illuminance",8);
            subscribeTopics.put("/test/human",9);
            subscribeTopics.put("/test/socket1",10);
            subscribeTopics.put("/test/waterPump",11);

            for(Map.Entry<String, Integer> entry : subscribeTopics.entrySet()){
                mqttBinder.subscribe(entry.getKey());
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    void unSubscribeTopics() {
        try {
            for(Map.Entry<String, Integer> entry : subscribeTopics.entrySet()){
                mqttBinder.unSubscribe(entry.getKey());
            }
            subscribeTopics.clear();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectSuccess() {
        subscribeTopics();
        connectState.setText("已连接");
    }

    @Override
    public void onConnectError(String error) {
        Log.d(TAG, "onConnectError: " + error);
        connectState.setText("未连接");
        subscribeTopics.clear();
    }

    @Override
    public void onDeliveryComplete() {
        Log.d(TAG, "publish ok");
    }

    @Override
    public void onMqttMessage(String topic, String message) {
        Log.d("onMqttMessage", "topic:"+topic+ "message length:"+ message.length() + ", message:"+message);
        Gson gson = new Gson();
        switch (subscribeTopics.get(topic)){
            case 1:
                temperatureValue.setText(String.valueOf(gson.fromJson(message.trim(), FloatMessage.class).value));
                break;

            case 2:
                humidityValue.setText(String.valueOf(gson.fromJson(message.trim(), IntMessage.class).value));
                break;

            case 3:
                pmValue.setText(String.valueOf(gson.fromJson(message.trim(), IntMessage.class).value));
                break;

            case 4:
                gasValue.setText(String.valueOf(gson.fromJson(message.trim(), IntMessage.class).value));
                break;

            case 5:
                String status = gson.fromJson(message.trim(), BoolMessage.class).value ?"开":"关";
                doorStatus.setText(status);
                break;
            case 6:
                corbanValue.setText(String.valueOf(gson.fromJson(message.trim(), IntMessage.class).value));
                break;
            case 7:
                waterValue.setText(String.valueOf(gson.fromJson(message.trim(), IntMessage.class).value));
                break;
            case 8:
                sunValue.setText(String.valueOf(gson.fromJson(message.trim(), IntMessage.class).value));
                break;
            case 9:
                String status_people = gson.fromJson(message.trim(), BoolMessage.class).value ?"有":"无";
                peopleStatus.setText(status_people);
                break;
            case 10:
                String status_socket = gson.fromJson(message.trim(), BoolMessage.class).value ?"开":"关";
                socketStatus.setText(status_socket);
                break;
            case 11:
                String status_pump = gson.fromJson(message.trim(), BoolMessage.class).value ?"开":"关";
                pumpStatus.setText(status_pump);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mqttBinder.isConnected()) {
            connectState.setText("已连接");
            subscribeTopics();
        } else {
            connectState.setText("未连接");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unSubscribeTopics();
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }

}
