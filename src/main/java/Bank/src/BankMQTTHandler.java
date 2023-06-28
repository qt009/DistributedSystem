package Bank.src;

import MOM.src.Follower;
import MOM.src.Publisher;
import MOM.src.Subscriber;
import org.eclipse.paho.client.mqttv3.*;

import java.net.UnknownHostException;

public class BankMQTTHandler extends Thread implements Publisher, Subscriber, Follower {
    private final MqttClient client;
    private final Bank bank;
    private boolean isLeader = false;

    public BankMQTTHandler(String broker, String clientId, Bank bank) throws MqttException {
        System.out.println("Constructing Bank MQTT Handler with broker: " + broker + " and client id: " + clientId);
        this.bank = bank;
        client = new MqttClient(broker, clientId);
        client.connect();
    }

    @Override
    public void run() {
        try {
            subscribeToBankValues();
            publishBankValue(bank.getTotalValue());
            while (true) {
                if(bank.isBankValueUpdated()){
                    publishBankValue(bank.getTotalValue());
                    bank.setBankValueUpdated(false);
                }
            }
        } catch (MqttException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publish(String topic, String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        client.publish(topic, mqttMessage);
    }

    @Override
    public void subscribe(String topic) throws MqttException {
        client.subscribe(topic);
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                // Handle connection lost
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                // Handle incoming messages
                String bankId = topic.substring(topic.lastIndexOf('/') + 1);
                double value = Double.parseDouble(new String(message.getPayload()));
                System.out.println("Received bank value: " + bankId + " - " + value);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // Handle message delivery complete
            }
        });
    }

    @Override
    public void unsubscribe(String topic) throws MqttException {
        client.unsubscribe(topic);
    }

    @Override
    public void disconnect() throws MqttException {
        client.disconnect();
    }

    public void publishBankValue(double value) throws MqttException, UnknownHostException {
        String topic = "pub/bank-values/" + bank.getThisBankIP() + ":" + bank.getThisBankPortThrift() + "/" + bank.getTotalValue();
        publish(topic, Double.toString(value));
    }

    public void subscribeToBankValues() throws MqttException, UnknownHostException {
        String topic = "sub/bank-values/" + bank.getThisBankIP() + ":" + bank.getThisBankPortThrift();
        subscribe(topic);
    }

    public void unsubscribeFromBankValues() throws MqttException, UnknownHostException {
        String topic = "unsub/bank-values/" + bank.getThisBankIP() + ":" + bank.getThisBankPortThrift();
        unsubscribe(topic);
    }

    public void disconnectFromMQTTBroker() throws MqttException {
        disconnect();
    }

    @Override
    public void requestVote() {

    }

    @Override
    public void grantVote() {

    }
}

