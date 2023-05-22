const WebSocket = require('ws');

// Create a WebSocket server
const wss = new WebSocket.Server({ port: 8080 });

// Handle WebSocket connection
wss.on('connection', function connection(ws) {
    console.log('WebSocket connection established.');

    // Create a TCP socket connection to the Java server
    const net = require('net');
    const socket = net.createConnection({ port: 8000 }, function() {
        console.log('Connected to Java server.');

        // Pass data received from the WebSocket to the Java server
        ws.on('message', function incoming(message) {
            console.log('Received message from WebSocket:', message);
            socket.write(message);
        });

        // Pass data received from the Java server to the WebSocket
        socket.on('data', function(data) {
            console.log('Received data from Java server:', data.toString());
            ws.send(data.toString());
        });

        // Handle the closure of the WebSocket connection
        ws.on('close', function close() {
            console.log('WebSocket connection closed.');
            socket.end();
        });
    });
});

const http = require('http');
const fs = require('fs');

const PORT=8080;

fs.readFile('./Browser.html', function (err, html) {

    if (err) throw err;

    http.createServer(function(request, response) {
        response.writeHeader(200, {"Content-Type": "text/html"});
        response.write(html);
        response.end();
    }).listen(PORT);
});