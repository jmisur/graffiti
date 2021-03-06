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

function stepMarker(livegraf, marker, image){
    //console.log(livegraf);
//    console.log(marker);
//    console.log(image);
    
    if(!marker){
        marker = currentMarker;
    }
    
    var useLivegraf = false;
    if (livegraf) useLivegraf = true;
    
    if (!livegraf && !image) {
    	if (marker.getIcon().path) {
    		useLivegraf = true;
    		livegraf = marker.getIcon().path;
    	} else {
    		image = marker.getIcon();
    	}
    }

//    console.log('33333'+livegraf);
     var panorama = marker.getMap().getStreetView();

    if(panorama.getVisible()) {
    		var distance = google.maps.geometry.spherical.computeDistanceBetween(panorama.getPosition(), marker.getPosition());
            if(distance==0) distance = 10;
            if(!useLivegraf){
//                console.log('image gefunden');
                marker.setIcon({
                    url: image,
                    scaledSize: new google.maps.Size(300*3/distance, 300*3/distance),
                    origin: new google.maps.Point(0, 0),
                    anchor: new google.maps.Point(0, 400/Math.sqrt(distance))
                  });
            }else{
//                console.log('path gefudnen');
                marker.setIcon({
                     path: livegraf,
                     scale: 3/distance,
                     anchor: new google.maps.Point(0, 500),
                     strokeWeight: 5
                 });
            }
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