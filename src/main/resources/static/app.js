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
        globalTimeSubscription = stompClient.subscribe('/topic/time', (message) => showGreeting(`/topic/time ${JSON.parse(message.body).message}`));
    });
};

const disconnect = () => {
    stompClient && stompClient.disconnect();
    setConnected(false);
    console.log("Disconnected");
};

const sendName = () => {
    stompClient.subscribe('/user/username', (greeting) => {
        const {success} = JSON.parse(greeting.body);
        alert(`Name set ${success ? "successfully" : "failed"}.`);
        if (success) {
            stompClient.subscribe('/user/personalised-time', (greeting) => showGreeting(`/user/personalised-time ${JSON.parse(greeting.body).message}`));
            globalTimeSubscription && globalTimeSubscription.unsubscribe();
        }
    });
    stompClient.send("/app/username", {}, JSON.stringify({'name': $("#name").val()}));
};

const showGreeting = (message) => $("#responses").append("<tr><td>" + message + "</td></tr>");

$(() => {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(connect);
    $("#disconnect").click(disconnect);
    $("#send").click(sendName);
});