const net = require('net');
const readline = require('readline');
const worker_threads = require("worker_threads");

// Retrieve the Java server's IP address and port from environment variables
const bankIp = '172.20.1.1';
const bankPort = 8000;

const browserIp = '172.20.2.0';
const browserPort = 8080;


const tcpSocket = new net.Socket();
const frontEndSocket = new net.Socket();
debugger;
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

frontEndSocket.connect(bankPort, bankIp, () => {
    console.log('Connected to frontEnd.');
    frontEndSocket.on('close', function () {
        console.log('TCP connection with frontEnd closed.');
    });

    // Pass data received from the TCP server to the console
    frontEndSocket.on('data', function (data) {
        console.log('Received data from Browser:', data.toString());
    });
});
tcpSocket.connect(bankPort, bankIp, () => {
    console.log('Connected to server');
    tcpSocket.write("CONNECT" + " " + "Hello"+"\n");
    // rl.setPrompt('Enter message to send to server: ');
    for(let i = 0; i < 1000; i++){
        const millisecondsToWait = 500;
        setTimeout(function() {
            tcpSocket.write("POST" + " " + "Hello "+i+"\n");
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
