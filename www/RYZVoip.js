cordova.define("cordova-plugin-x-voip.RYZVoip", function(require, exports, module) {
var exec = require('cordova/exec');

module.exports = {

    connect: function(onSuccess, onError, imToken) {

        exec(onSuccess, onError, "RYZVoip", "connect", [imToken]);
    },
    direct: function(onSuccess, onError, phones) {

        exec(onSuccess, onError, "RYZVoip", "direct", [phones]);
    },
   endCall: function(onSuccess, onError, callId) {

        exec(onSuccess, onError, "RYZVoip", "endCall", [callId]);
    },
    KeyboardInput: function(onSuccess, onError, params) {

        exec(onSuccess, onError, "RYZVoip", "KeyboardInput", [params]);
    }
}

});
