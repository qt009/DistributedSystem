package Bank.src;

import MOM.src.Follower;
import MOM.src.Publisher;
import MOM.src.Subscriber;
import org.eclipse.paho.client.mqttv3.*;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class BankMQTTHandler extends Thread implements Publisher, Subscriber, Follower {
    private final MqttClient client;
    private final Bank bank;
    private boolean isLeader = false;

    private Map<String, Integer> votes = new HashMap<>();

    private double bailAmount = 0;

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
            subscribeToVotingSession();
            subscribeToVoteGrantingSession();
            subscribeToBailingSession();
            subscribeToBailGrantingSession();
            publishBankValue(bank.getTotalValue());

//            requestVote();
//            Thread.sleep(2000);
//            checkVotes();

            if(bank.getThisBankIP().equals("172.20.1.2")){
                isLeader = true;
            }

            while (true) {
                if(bank.isBankValueUpdated()){
                    publishBankValue(bank.getTotalValue());
                    bank.setBankValueUpdated(false);
                    displayAssociatedBanksWithValues();
                }
//                if (hasThisBankTheHighestValue()){
//                    requestVote();
//                    Thread.sleep(2000);
//                    checkVotes();
//                }
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
                System.out.println("Received subscribed message: " + topic + " - " + new String(message.getPayload()));

                switch (topic) {
                    case "bank-values/": {
                        // bank-values/
                        String[] bankInfo = message.toString().split("/");
                        String bankAddress = bankInfo[0];
                        String bankTotalValue = bankInfo[1];

                        System.out.println("Received subscribed bank value: " + bankAddress + " - " + bankTotalValue);
                        if (!isBankAddressOfThisBank(bankAddress))
                            bank.getAssociatedBanks().put(bankAddress, Double.parseDouble(bankTotalValue));
                        break;
                    }
                    case "start-voting/": {
                        grantVote();
                        break;
                    }
                    case "vote-granted/": {
                        String[] bankInfo = message.toString().split("/");
                        String bankAddress = bankInfo[0];
                        String bankValue = bankInfo[1];

                        System.out.println("Received voted bank value: " + bankAddress + " - " + bankValue);
                        if (!isBankAddressOfThisBank(bankAddress))
                            if(votes.containsKey(bankAddress))
                                votes.put(bankAddress, votes.get(bankAddress) + 1);
                            else
                                votes.put(bankAddress, 1);
                        break;
                    }
                    case "bail-out/": {
                        if(isLeader){
                            String[] bankInfo = message.toString().split("/");
                            String bankAddress = bankInfo[0];
                            String bailAmount = bankInfo[1];

                            System.out.println("Received to bail value: " + bankAddress + " - " + bailAmount);
                            publish("bail-out-granted/", bailAmount);
                        }
                    }
                    case "bail-out-granted":

                        String bailAmount = message.toString();
                        System.out.println("Received bail-out-granted message: " + bailAmount);

                        setBailAmount(Double.parseDouble(bailAmount) / (bank.getAssociatedBanks().size()));

                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // Handle message delivery complete
            }
        });
    }
    private boolean isBankAddressOfThisBank(String bankAddress) throws UnknownHostException {
        return bankAddress.equals(bank.getThisBankIP() + ":" + bank.getThisBankPortThrift());
    }

    public synchronized double getBailAmount() {
        return bailAmount;
    }

    public synchronized void setBailAmount(double bailAmount) {
        this.bailAmount = bailAmount;
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
        String topic = "bank-values/";
        publish(topic, bank.getThisBankIP() + ":" + bank.getThisBankPortThrift() + "/" + value);
    }

    public void subscribeToBankValues() throws MqttException, UnknownHostException {
        String topic = "bank-values/";
        subscribe(topic);
    }

    public void unsubscribeFromBankValues() throws MqttException, UnknownHostException {
        String topic = "bank-values/";
        unsubscribe(topic);
    }

    public void disconnectFromMQTTBroker() throws MqttException {
        disconnect();
    }

    @Override
    public void requestVote() throws MqttException {
        publish("start-voting/", "vote-request");
    }

    @Override
    public void grantVote() throws UnknownHostException, MqttException {
        String thisBankAddress = bank.getThisBankIP() + ":" + bank.getThisBankPortThrift();
        double bankValue = bank.getTotalValue();

        String bankAddressWithHighestValue = thisBankAddress;
        double highestValue = bankValue;

        for(String bankAddress : bank.getAssociatedBanks().keySet()){
            if(bank.getAssociatedBanks().get(bankAddress) > highestValue){
                highestValue = bank.getAssociatedBanks().get(bankAddress);
                bankAddressWithHighestValue = bankAddress;
            }
        }

        publish("vote-granted/", bankAddressWithHighestValue + "/" + highestValue);
    }

    public void subscribeToVotingSession() throws MqttException, UnknownHostException {
        String topic = "start-voting/";
        subscribe(topic);
    }

    public void subscribeToVoteGrantingSession() throws MqttException, UnknownHostException {
        String topic = "vote-granted/";
        subscribe(topic);
    }

    public void subscribeToBailingSession() throws MqttException, UnknownHostException {
        String topic = "bail-out/";
        subscribe(topic);
    }

    public void subscribeToBailGrantingSession() throws MqttException, UnknownHostException {
        String topic = "bail-out-granted/";
        subscribe(topic);
    }

    private void displayAssociatedBanksWithValues(){
        System.out.println("Associated Banks with Values:");
        for(String bankAddress : bank.getAssociatedBanks().keySet()){
            System.out.println(bankAddress + " - " + bank.getAssociatedBanks().get(bankAddress));
        }
    }

    private void checkVotes() throws UnknownHostException {
        //get bank address with the highest vote in votes
        String bankAddressWithHighestVote = "";
        int highestVote = 0;
        for(String bankAddress : votes.keySet()){
            if(votes.get(bankAddress) > highestVote){
                highestVote = votes.get(bankAddress);
                bankAddressWithHighestVote = bankAddress;
            }
        }

        //check if this bank is the bank with the highest vote
        if(bankAddressWithHighestVote.equals(bank.getThisBankIP() + ":" + bank.getThisBankPortThrift())){
            isLeader = true;
            System.out.println("This bank is the leader");
        }
        else{
            isLeader = false;
        }
    }

    private boolean hasThisBankTheHighestValue(){
        double thisBankValue = bank.getTotalValue();
        for(String bankAddress : bank.getAssociatedBanks().keySet()){
            if(bank.getAssociatedBanks().get(bankAddress) > thisBankValue)
                return false;
        }
        return true;
    }
}

