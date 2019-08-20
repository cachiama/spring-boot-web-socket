let stompClient = null;

const setConnected = (connected) => {
    $("#connect").prop("disabled", connected);
    $("#send").prop("disabled", !connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#table").show();
    } else {
        $("#table").hide();
    }
    $("#responses").html("");
};

let globalTimeSubscription;
const connect = () => {
    const socket = new SockJS('/websocket-example');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, (frame) => {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/personalised-time', (greeting) => showGreeting("/user/personalised-time " + JSON.parse(greeting.body).message));
        globalTimeSubscription = stompClient.subscribe('/topic/time', (message) => showGreeting("/topic/time " + JSON.parse(message.body).message));
    });
};

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
};

const sendName = () => {
    stompClient.subscribe('/topic/user', (greeting) => {
        alert("Name set " + (JSON.parse(greeting.body).success ? "successfully" : "failed") + ".")
        globalTimeSubscription && globalTimeSubscription.unsubscribe()
    });
    stompClient.send("/app/user", {}, JSON.stringify({'name': $("#name").val()}));
};

const showGreeting = (message) => $("#responses").append("<tr><td>" + message + "</td></tr>");

$(() => {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(connect);
    $("#disconnect").click(disconnect);
    $("#send").click(sendName);
});