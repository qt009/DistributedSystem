function getMsg() {
    // Creates a promise object for retrieving the desired data
    fetch("http://172.20.2.1:8002/api")
        // When received, exposes the JSON component
        .then((response) => {
            return response.json();
        })
        // Displays the message on the page
        .then((json) => {
            document.getElementById("msg").innerHTML = "Server message: " + json.msg;
        });
}
// Sends message to server via POST
function postMsg() {
    const data = {msg: document.getElementById("msg-box").value};
    // Creates a promise object for sending the desired data
    fetch("http://172.20.2.1:8002/api",{
        method: "POST",
        // Format of the body must match the Content-Type
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    });
}