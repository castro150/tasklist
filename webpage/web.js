const express = require('express');
const path = require('path');

const app = express();

app.use(express.static(__dirname + '/dist/webpage'));

app.get('/*', function(req, res) {
    res.sendFile(path.join(__dirname+'/dist/webpage/index.html'));
});

let port = process.env.PORT || 9000;
app.listen(port);
console.log('Listening on port ' + port);
