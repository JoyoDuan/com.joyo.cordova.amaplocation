var exec = require('cordova/exec');

//auto create
// exports.coolMethod = function (arg0, success, error) {
//     exec(success, error, 'AMapLocation', 'coolMethod', [arg0]);
// };

function AMapLocation(){}

AMapLocation.prototype.getLocation = function (successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'AMapLocation', 'getLocation', []);
}
module.exports = new AMapLocation();
