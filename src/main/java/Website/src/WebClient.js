const backendIp = "172.20.2.1";
const express = require('express')
const app = express()
const bodyParser = require('body-parser')
const cors = require('cors')
const backendPort = 8002;
let bankBalance = 0;

let startTime = 0;
let totalBytesReceived = 0;

app.use( bodyParser.json() );       // to support JSON-encoded bodies

app.use(bodyParser.urlencoded({     // to support URL-encoded bodies
    extended: true}));
app.use(cors())
const jsonParser = bodyParser.json();
//You can use this to check if your server is working
app.get('/', (req, res)=>{
    res.send("Welcome to WebClient backend")
})
// This sends JSON in response to a GET request at /api
app.get('/api', (req, res) => {
    startTime = Date.now();
    console.log("Start time: " + startTime);
    requestTotalBalanceFromBank();
    res.json({msg: "TotalBalance: " + bankBalance});
    bankBalance = 0;
});

// This displays the received message when a POST is sent to /api
app.post('/api', jsonParser, (req, res) => {
    console.log("Client message: "+req.body.msg);
    startTime = Date.now();
    console.log("Start time: " + startTime);
    if(req.body.msg.includes("Add")){
        let split = req.body.msg.split(":");
        tcpSocket.write("POST" + " " + "Add:"+split[1]+"\n");
    }else if(req.body.msg.includes("Sub")){
        let split = req.body.msg.split(":");
        tcpSocket.write("POST" + " " + "Sub:"+split[1]+"\n");
    }else if(req.body.msg.includes("TotalBalance")){
        requestTotalBalanceFromBank();
        res.json({msg: "TotalBalance: " + bankBalance});
        bankBalance = 0;
    }else if(req.body.msg.includes("DISCONNECT")) {
        tcpSocket.write("DISCONNECT" + "\n");
        tcpSocket.end();
        rl.close();
    }else{
        res.json({msg: "Unknown command"});
    }
});

app.post('/connectToBank', (req, res) =>{
    console.log("Bank IP: " + req.body.bankIP)
    console.log("Bank port: " + req.body.bankPort)

    const bankIp = req.body.bankIP;
    const bankPort = req.body.bankPort;
    const tcpSocket = new net.Socket();
    tcpSocket.connect(bankPort, bankIp, () => {
        console.log('Connected to server');
        tcpSocket.write("CONNECT" + " " + "Hello"+"\n");
        // rl.setPrompt('Enter message to send to server: ');
        for(let i = 0; i < 6; i++){
            const millisecondsToWait = 500;
            setTimeout(function() {
                tcpSocket.write("POST" + " " + "Add:"+randomIntFromInterval(100, 1000)+"\n");
            }, millisecondsToWait);
        }

    });
})


//Start your server on a specified port
app.listen(backendPort, backendIp, ()=>{
    console.log(`Server is running on ${backendIp}:${backendPort}`)
})

const bankIp = '172.20.1.1';
const bankPort = 8000;


const net = require('net');
const readline = require('readline');
const tcpSocket = new net.Socket();
debugger;
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});
tcpSocket.connect(bankPort, bankIp, () => {
    console.log('Connected to server');
    tcpSocket.write("CONNECT" + " " + "Hello"+"\n");
    // rl.setPrompt('Enter message to send to server: ');
    for(let i = 0; i < 6; i++){
        const millisecondsToWait = 500;
        setTimeout(function() {
            tcpSocket.write("POST" + " " + "Add:"+randomIntFromInterval(100, 1000)+"\n");
        }, millisecondsToWait);
    }
});
function randomIntFromInterval(min, max) {
    return Math.floor(Math.random() * (max - min + 1) + min)
}

function requestTotalBalanceFromBank(){
    tcpSocket.write("GET" + " " + "TotalBalance"+"\n");

}
rl.on('line', (input) => {

    if (input.trim() === 'DISCONNECT') {
        tcpSocket.write(input + "\n");
        tcpSocket.end();
        rl.close();
    } else {
        tcpSocket.write("POST" + " " + input+"\n");

        //rl.prompt();
    }
});
tcpSocket.on('data', (data) => {
    const endTime = Date.now();
    const rtt = endTime - startTime;

    totalBytesReceived += data.length;
    const throughputMbps = (totalBytesReceived * 8 / 1000000) /  (rtt / 1000);

    console.log('RTT: ', rtt, 'ms | Throughput: ', throughputMbps, 'Mbps');
    console.log(`Message from Bank: ${data}`);
    if(data.toString().includes("TotalBalance")){
        bankBalance = data.toString().split(":")[1];
        console.log("BankBalance is now: " + bankBalance);
    }
});

tcpSocket.on('close', () => {
    console.log('Connection closed');
    process.exit(0);
});

tcpSocket.on('error', (err) => {
    console.log('Error connecting to server: ' + err.message);
    process.exit(1);
});
