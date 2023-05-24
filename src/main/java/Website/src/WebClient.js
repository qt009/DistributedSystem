

const backendIp = "172.20.2.1";
const express = require('express')
const app = express()
const bodyParser = require('body-parser')
const cors = require('cors')
const backendPort = 8002;


app.use( bodyParser.json() );       // to support JSON-encoded bodies

app.use(bodyParser.urlencoded({     // to support URL-encoded bodies
    extended: true}));
app.use(cors())

//You can use this to check if your server is working
app.get('/', (req, res)=>{
    res.send("Welcome to WebClient backend")
})




//Route that handles login logic
app.post('/connectToBank', (req, res) =>{
    console.log("Bank IP: " + req.body.bankIP)
    console.log("Bank port: " + req.body.bankPort)
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


const ws = new net.Socket();

ws.onopen = function () {
    console.log('WebSocket connection opened.');
};

ws.onmessage = function (event) {
    console.log('Received message from WebSocket:', event.data);
};

ws.onclose = function () {
    console.log('WebSocket connection closed.');
};

ws.onerror = function (error) {
    console.log('WebSocket error:', error);
};


tcpSocket.connect(bankPort, bankIp, () => {
    console.log('Connected to server');
    tcpSocket.write("CONNECT" + " " + "Hello"+"\n");
    // rl.setPrompt('Enter message to send to server: ');
    for(let i = 0; i < 6; i++){
        const millisecondsToWait = 500;
        setTimeout(function() {
            tcpSocket.write("POST" + " " + "Add:"+i+"\n");
        }, millisecondsToWait);
    }
});

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
    console.log(`Message from Bank: ${data}`);
    const serverMessage = data.toString();
    console.log('Bank says: ' + serverMessage);
    //rl.prompt();
});

tcpSocket.on('close', () => {
    console.log('Connection closed');
    process.exit(0);
});

tcpSocket.on('error', (err) => {
    console.log('Error connecting to server: ' + err.message);
    process.exit(1);
});
// Create a TCP socket connection to the Java server
/*const tcpSocket = net.createConnection({ host: bankIp, port: bankPort }, function () {
    console.log('Connected to Java server.');
    tcpSocket.write('Hello, Java server!');
    // Handle the closure of the TCP connection
    tcpSocket.on('close', function () {
        console.log('TCP connection closed.');
    });

    // Pass data received from the TCP server to the console
    tcpSocket.on('data', function (data) {
        console.log('Received data from Java server:', data.toString());
    });

    // Send data to the TCP server
    tcpSocket.write('Hello, Java server!');
});

// Handle connection errors
tcpSocket.on('error', function (error) {
    console.error('Error connecting to Java server:', error);
});

// Handle the closure of the TCP connection
tcpSocket.on('close', function () {
    console.log('TCP connection closed.');
});*/
