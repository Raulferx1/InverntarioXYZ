package com.example.inverntarioxyz;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.w3c.dom.Text;

public class MQTT extends AppCompatActivity {
    String clienteID = "";
    static String MQTTHOST = "tcp://crudfirebases.cloud.shiftr.io:1883";
    static String MQTTUSER = "crudfirebases";
    static String MQTTPASS = "dQhQtMSojfLV7vH1";
    static String TOPIC = "LED";
    static String TOPIC_MSG_ON = "Encender";
    static String TOPIC_MSG_OFF = "Apagar";

    MqttAndroidClient cliente;
    MqttConnectOptions opciones;
    Boolean permisoPublicar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);


        connectBroker();

        Button btn_encendido = findViewById(R.id.btn_encender);
        btn_encendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensaje(TOPIC, TOPIC_MSG_ON);
            }
        });
        Button btn_apagado = findViewById(R.id.btn_apagar);
        btn_apagado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensaje(TOPIC, TOPIC_MSG_OFF);
            }
        });
    }

    private void enviarMensaje(String topic, String msg) {
        checkConnection();
        if (this.permisoPublicar) {
            try {
                int qos = 0;
                this.cliente.publish(topic, msg.getBytes(), qos, false);
                Toast.makeText(getBaseContext(), topic + " : " + msg, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void checkConnection() {
        if (this.cliente.isConnected()) {
            this.permisoPublicar = true;
        } else {
            this.permisoPublicar = false;
            connectBroker();
        }
    }

    private void connectBroker() {
        this.cliente = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, this.clienteID);
        this.opciones = new MqttConnectOptions();
        this.opciones.setUserName(MQTTUSER);
        this.opciones.setPassword(MQTTPASS.toCharArray());
        try {
            IMqttToken token = this.cliente.connect(opciones);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getBaseContext(), "Conectado", Toast.LENGTH_SHORT).show();
                    mensajearTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getBaseContext(), "Conexion Fallida", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void mensajearTopic() {
        try {
            this.cliente.subscribe(TOPIC, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    TextView txtinfo = findViewById(R.id.txtInfo);

                    if (topic.matches(TOPIC)) {
                        String msg = new String(message.getPayload());
                        if (msg.matches(TOPIC_MSG_ON)) {
                            txtinfo.setText(msg);
                            txtinfo.setBackgroundColor(GREEN);
                        }
                        if (msg.matches(TOPIC_MSG_OFF)) {
                            txtinfo.setText(msg);
                            txtinfo.setBackgroundColor(RED);
                        }
                    }
                }
            });
        } catch (MqttSecurityException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
