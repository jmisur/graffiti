var ws = null;

ws = new SockJS("http://192.168.1.150:8080/echo");
ws.onopen = function () {
    print('Connected');
};
ws.onmessage = function (event) {
    print('Received: ' + event.data);
    var message = event.data;
    if(pathtest==null)var pathtest = $('#realtimepaintingarea polyline').attr('points');
    $('#realtimepaintingarea polyline').attr('points',pathtest+' '+message);
    console.log(message);
};
ws.onclose = function () {
    print('Disconnected');
};

function send() {
    if (ws != null) {
        var message = document.getElementById('text').value;
        print('Sent: ' + message);
        ws.send(message);
    }
}
var pathtest = null;

function print(message) {
    var messages = document.getElementById('message');
    messages.value += message + "\n";
}
