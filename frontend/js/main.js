var ws = null;
var serverip = '192.168.1.150:8080';
ws = new SockJS("http://"+serverip+"/echo");
ws.onopen = function () {
    print('Connected');
};


var pathtest = null;
var currentline = null;
ws.onmessage = function (event) {
    print('Received: ' + event.data);
    var message = event.data;

    if(message == 'stop'){
        currentline = null;
    } else if(currentline == null){
        //new line starts
        //currentline = $('<polyline points="1,1 10,10" style="fill:none;stroke:black;stroke-width:3" />');
        xmlns = "http://www.w3.org/2000/svg";
        currentline = document.createElementNS(xmlns,"polyline");
        //console.log(currentline);
        //currentline = $('#realtimepaintingarea polyline');
        $('#realtimepaintingarea').get(0).appendChild(currentline);
        currentline = $('#realtimepaintingarea polyline').eq($('#realtimepaintingarea polyline').length-1);
        currentline.css({'fill':'none', 'stroke':'black', 'stroke-width':3});
        pathtest = message + ' ';
        currentline.attr('points',pathtest);
    } else {
        pathtest += message +' ';
        currentline.attr('points',pathtest);
        //$('#realtimepaintingarea').attr('preserveAspectRatio', 'xMinYMin meet')
        //$('#realtimepaintingarea').attr('viewBox', '0 0 200 200')
    }
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


function print(message) {
    var messages = document.getElementById('message');
    messages.value += message + "\n";
}


///MAP creation

function initialize() {

    // Create an array of styles.
    var styles = [
        {
            stylers: [
                { hue: "#00ffe6" },
                { saturation: -20 }
            ]
        },{
            featureType: "road",
            elementType: "geometry",
            stylers: [
                { lightness: 100 },
                { visibility: "simplified" }
            ]
        },{
            featureType: "road",
            elementType: "labels",
            stylers: [
                { visibility: "off" }
            ]
        }
    ];
    var styledMap = new google.maps.StyledMapType(styles,
        {name: "Styled Map"});

    var mapOptions = {
        zoom: 11,
        center: new google.maps.LatLng(55.6468, 37.581),
        mapTypeControlOptions: {
            mapTypeIds: [google.maps.MapTypeId.ROADMAP, 'map_style']
        }
    };
    var map = new google.maps.Map(document.getElementById('map-canvas'),mapOptions);
    map.mapTypes.set('map_style', styledMap);
    map.setMapTypeId('map_style');
}