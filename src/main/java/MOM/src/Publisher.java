package MOM.src;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface Publisher {
    void publish(String topic, String message) throws MqttException;
}
