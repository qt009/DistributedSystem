package MOM.src;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.net.UnknownHostException;

public interface Follower {
    void requestVote() throws MqttException;
    void grantVote() throws UnknownHostException, MqttException;
}
