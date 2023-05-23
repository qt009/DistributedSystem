const net = require('net');

// Retrieve the Java server's IP address and port from environment variables
const bankIp = '172.20.1.1';
const bankPort = 8000;

// Create a TCP socket connection to the Java server
const tcpSocket = net.createConnection({ host: bankIp, port: bankPort }, function () {
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
});
