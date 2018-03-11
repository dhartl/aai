
var mymap = L.map('mapid').setView([47.069241, 15.438473], 13);
L.tileLayer('https://{s}.wien.gv.at/basemap/geolandbasemap/normal/google3857/{z}/{y}/{x}.png', {
    attribution: 'Tiles &copy; <a href="//www.basemap.at/">basemap.at</a> (STANDARD)',
    subdomains: ['maps1','maps2','maps3','maps4'],
    maxZoom: 18,
    id: 'basemap.normal',
}).addTo(mymap);
