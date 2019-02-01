const express = require('express');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
var https = require('https');
var fs = require('fs')
var path = require('path');

const indexRouter = require('./src/routes/index');
const partiesRouter = require('./src/routes/partie');

const app = express();

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

app.use('/', indexRouter);
app.use('/parties', partiesRouter);

app.use(express.static(path.resolve(__dirname, '../../TennisBetWeb')));

const generateur = require('./src/generateur');
generateur.demarrer();

var options = {
      key: fs.readFileSync('certificates/privateKey.key'),
      cert: fs.readFileSync('certificates/certificate.crt')
};
https.createServer(options, app).listen(8000);

module.exports = app;
