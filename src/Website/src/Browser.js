var socket;

function establishConnection() {
    socket = new WebSocket("ws://localhost:9000");

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
    socket.onopen = function () {
        socket.send("Request Total Value");
    };
    socket.onmessage = function (event) {
        document.getElementById('totalValue').setDocument(event.data);
    };
}

function moneyAdd() {
    socket.onopen = function () {
        socket.send("+" . document.getElementById('changeMoney'));
    };
}

function moneySubtract() {
    socket.onopen = function () {
        socket.send("-" . document.getElementById('changeMoney'));
    }
}