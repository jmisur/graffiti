var ws = null;
var serverip = '192.168.1.150:8080';
ws = new SockJS("http://"+serverip+"/echo");
ws.onopen = function () {
    print('Connected');
};


var pathtest = null;
var livegraf = '';
var currentline = null;
ws.onmessage = function (event) {
    print('Received: ' + event.data);
    var message = event.data;

    if(message == 'stop'){
        currentline = null;
    } else if(currentline == null){
    	currentline = '';
        //new line starts
        //currentline = $('<polyline points="1,1 10,10" style="fill:none;stroke:black;stroke-width:3" />');
//        xmlns = "http://www.w3.org/2000/svg";
//        currentline = document.createElementNS(xmlns,"polyline");
        //console.log(currentline);
        //currentline = $('#realtimepaintingarea polyline');
//        $('#realtimepaintingarea').get(0).appendChild(currentline);
//        currentline = $('#realtimepaintingarea polyline').eq($('#realtimepaintingarea polyline').length-1);
//        currentline.css({'fill':'none', 'stroke':'black', 'stroke-width':5});
//        currentline.attr('transform', 'scale(0.3)')
//        pathtest = message + ' ';
        livegraf += 'M ' + message + ' ';
//        currentline.attr('points',pathtest);

        stepMarker(livegraf);
    } else {
//        pathtest += message +' ';
        livegraf += 'L ' + message + ' ';
//        currentline.attr('points',pathtest);
        
        stepMarker(livegraf);
    }
    //console.log(message);
};

function stepMarker(livegraf, marker){
    //livegraf = 'M 30.5 31 L 34.5 151 M 16.5 88 L 98.5 79 M 74.5 40 L 90.5 132 M 97.5 161 L 131.5 40 M 109.5 31 L 205.5 147 M 99.5 101 L 206.5 101 M 287.5 43 L 247.5 68 M 234.5 65 L 242.5 107 M 243.63547 109.068103 L 256.36453 150.931898 M 263.5 159 L 301.5 124 M 342.5 37 L 355.5 170 M 393.5 46 L 356.5 112 M 433.5 163 L 354.5 120 M 89.5 325 L 99.5 198 M 164.5 318 L 85.5 190 M 62.5 275 L 167.5 260 M 202.5 207 L 229.5 306 M 160.5 208 L 255.5 192 M 305.5 192 L 248.5 254 M 305.5 303 L 246.5 243 M 361.5 239 L 297.5 304 M 294.5 193 L 358.5 241 M 393.5 302 L 385.5 203 M 367.5 191 L 449.5 297 M 444.5 176 L 446.5 303 ';
//    console.log('22222'+livegraf);
    if(!marker){
        marker = currentMarker;
    }
    if (!livegraf) {
        livegraf = marker.getIcon().path;
    }
//    console.log('33333'+livegraf);
     var panorama = marker.getMap().getStreetView();

    if(panorama.getVisible()) {
    		 var distance = google.maps.geometry.spherical.computeDistanceBetween(panorama.getPosition(), marker.getPosition());
            if(distance==0)distance = 10;
             marker.setIcon({
    			 path: livegraf,
    			 scale: 3/distance,
    			 anchor: new google.maps.Point(0, 500),
    			 strokeWeight: 10
    		 });
    }
}

function recalculateDistance() {
    $('#tabnearest li, #tabpopular li').each(function(){
        stepMarker(null, $(this).data('marker'));
    });
}

ws.onclose = function () {
    print('Disconnected');
};

function send() {
    if (ws && currentId) {
//        console.log("SENDINGGG: " + currentId);
        ws.send(currentId);
    }
}


function print(message) {
  //  var messages = document.getElementById('message');
   // messages.value += message + "\n";
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