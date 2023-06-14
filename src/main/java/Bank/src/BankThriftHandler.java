package Bank.src;

import Thrift.src.BankService;
import Thrift.src.LoanRequest;
import Thrift.src.LoanResponse;
import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class BankThriftHandler extends Thread implements BankService.Iface {
    private final Bank bank;
    private final Map<String, Integer> friendlyBanks = new HashMap<>();

    private TServer server;

    public BankThriftHandler(Bank bank) throws UnknownHostException {
        this.bank = bank;
        setUpFriendlyBanks();
        System.out.println("Bank Thrift Handler constructed");
    }

    public Bank getBank() {
        return bank;
    }

    @Override
    public void run() {
        startServer();
    }


    public void startServer(){
        System.out.println("Starting RPC Server");
        String tmp = System.getenv("THIS_BANK_PORT_THRIFT");
        if(tmp != null) {
            int rpcPort = Integer.parseInt(tmp);
            TServerTransport transport = null;
            try {
                transport = new TServerSocket(rpcPort);
                server = new TSimpleServer(
                        new TServer.Args(transport).processor(new BankService.Processor<>(this)));
                server.serve();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public LoanResponse requestLoan(LoanRequest request) throws TException {
        if(this.bank.getReserves()> request.getAmount()) {
            System.out.println("Set Portfolio in RPC Money" + request.getAmount());
            this.bank.setReserves(this.bank.getReserves()- request.getAmount());
            return LoanResponse.APPROVED;
        }
        return LoanResponse.REJECTED;
    }


    private void setUpFriendlyBanks() throws UnknownHostException {
        InetAddress ip = InetAddress.getLocalHost();
        String thisBankIP = ip.getHostAddress();

        if (thisBankIP.equals("172.20.1.1")) {
            friendlyBanks.put("172.20.1.2", 7002);
            friendlyBanks.put("172.20.1.3", 7003);
        }
        if (thisBankIP.equals("172.20.1.2")) {
            friendlyBanks.put("172.20.1.1", 7001);
            friendlyBanks.put("172.20.1.3", 7003);
        }
        if (thisBankIP.equals("172.20.1.3")) {
            friendlyBanks.put("172.20.1.1", 7001);
            friendlyBanks.put("172.20.1.3", 7003);
        }
    }

    public Map<String, Integer> getFriendlyBanks() {
        return friendlyBanks;
    }
}
