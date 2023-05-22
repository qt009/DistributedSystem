let socket;
function establishConnection() {
    socket = new WebSocket("ws://172.20.1.1:8000");

    socket.onopen = function () {
        console.log("Connected to Bank server");
        socket.send("Request from browser");
    };

    socket.onmessage = function (event) {
        console.log("Received response: " + event.data);
        // Process the response received from the bank server
    };

    socket.onclose = function () {
        console.log("Connection closed");
    };
    console.log("Connected to Bank server");
    fetch('http://172.20.1.1:8000')
        .then(response => {
            // Output the response status code to the console
            console.log('Response status code:', response.status);
        })
        .then(response => response.json())
        .then(data => {
            // Process the response from the Java application
            console.log(data);
        })
        .catch(error => {
            // Handle the error
            console.error(error);
        });
}

function pingURL() {

    // The custom URL entered by user
    const URL = $("#url").val();
    const settings = {

        // Defines the configurations
        // for the request
        cache: false,
        dataType: "jsonp",
        async: true,
        crossDomain: true,
        url: URL,
        method: "GET",
        headers: {
            accept: "application/json",
            "Access-Control-Allow-Origin": "*",
        },

        // Defines the response to be made
        // for certain status codes
        statusCode: {
            200: function (response) {
                console.log("Status 200: Page is up!");
            },
            400: function (response) {
                console.log("Status 400: Page is down.");
            },
            0: function (response) {
                console.log("Status 0: Page is down.");
            },
        },
    };

    // Sends the request and observes the response
    $.ajax(settings).done(function (response) {
        console.log(response);
    });
}

function totalValueRequest() {
    socket.addEventListener('open', function (event) {
        socket.send("Request total Value of the Bank");
    });

    socket.addEventListener('message', function (event) {
        document.getElementById('totalValue').value = event.data;
    });
}

function moneyAdd() {
    socket.addEventListener('open', function (event) {
        socket.send("+" . document.getElementById('changeMoney'))
    });
}

function moneySubtract() {
    socket.addEventListener('open', function (event) {
        socket.send("-" . document.getElementById('changeMoney'))
    });
}