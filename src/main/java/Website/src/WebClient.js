const WebSocket = require('ws');
const net = require('net');

// Create a WebSocket server
const wss = new WebSocket.Server({ port: 8080 });

// Create a TCP socket server
const tcpServer = net.createServer();

// Handle WebSocket connection
wss.on('connection', function connection(ws) {
    console.log('WebSocket connection established.');

    // Create a TCP socket connection to the Java server
    const tcpSocket = net.createConnection({ port: 8000 }, function () {
        console.log('Connected to Java server.');

        // Pass data received from the WebSocket to the TCP server
        ws.on('message', function incoming(message) {
            console.log('Received message from WebSocket:', message.toString());
            tcpSocket.write(message);
        });

        // Pass data received from the TCP server to the WebSocket
        tcpSocket.on('data', function (data) {
            console.log('Received data from Java server:', data.toString());
            ws.send(data.toString());
        });

        // Handle the closure of the WebSocket connection
        ws.on('close', function close() {
            console.log('WebSocket connection closed.');
            tcpSocket.end();
        });
    });
});

// Start the TCP server
tcpServer.listen(8000, function () {
    console.log('TCP server listening on port 8000');
});