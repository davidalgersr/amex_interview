let ws;

function connect() {

	ws = new WebSocket("ws://localhost:8080/messages");

	ws.onmessage = function (e) {
	    console.log(e);
	    printMessage(e.data);
	};

	ws.onerror = function(error) {
	    console.error("WebSocket error: ", error);
	};

	ws.onclose = function(event) {
	    console.log("WebSocket connection closed:", event);
	}
}

function printMessage(data) {
	let messages = document.getElementById("messageArea");
	let messageData = JSON.parse(data);
	let newMessage = document.createElement("div");
	newMessage.className = "incoming-message";
	newMessage.innerHTML = messageData.value;
	messages.appendChild(newMessage);
}