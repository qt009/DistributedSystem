package MOM.src;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface Subscriber {
    void subscribe(String topic) throws MqttException;
    void unsubscribe(String topic) throws MqttException;
    void disconnect() throws MqttException;
}
