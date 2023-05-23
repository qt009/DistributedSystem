const readline = require('readline');
const net = require('net');

const serverAddress = 'localhost';
const port = 8080;

const client = new net.Socket();
debugger;
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

client.connect(port, serverAddress, () => {
    console.log('Connected to server');
    // rl.setPrompt('Enter message to send to server: ');
    // rl.prompt();
});


rl.on('line', (input) => {

    if (input.trim() === 'DISCONNECT') {
        client.write(input + "\n");
        client.end();
        rl.close();
    } else {
        client.write("PUT" + " " + input+"\n");

        // rl.prompt();
    }
});

client.on('data', (data) => {
    console.log(`Server message Received: ${data}`);
    const serverMessage = data.toString();
    console.log('Server says: ' + serverMessage);
    // rl.prompt();
});

client.on('close', () => {
    console.log('Connection closed');
    process.exit(0);
});

client.on('error', (err) => {
    console.log('Error connecting to server: ' + err.message);
    process.exit(1);
});
