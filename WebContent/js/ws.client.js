/* A pausable event forwarder that can be used to pause and
 * resume handling of events (e.g. when we need to wait
 * for a Image's load event before we can process further
 * WebSocket messages).
 * The object's callFunction(func) should be called from an
 * event handler and give the function to handle the event as
 * argument.
 * Call pauseProcessing() to suspend event forwarding and
 * resumeProcessing() to resume it.
 */
function PausableEventForwarder() {

    var pauseProcessing = false;
    // Queue for buffering functions to be called.
    var functionQueue = [];

    this.callFunction = function(func) {
        // If message processing is paused, we push it
        // into the queue - otherwise we process it directly.
        if (pauseProcessing) {
            functionQueue.push(func);
        } else {
            func();
        }
    };

    this.pauseProcessing = function() {
        pauseProcessing = true;
    };

    this.resumeProcessing = function() {
        pauseProcessing = false;

        // Process all queued functions until some handler calls
        // pauseProcessing() again.
        while (functionQueue.length > 0 && !pauseProcessing) {
            var func = functionQueue.pop();
            func();
        }
    };
}


/**
 *使用web socket连接服务器
 *host-相对路径
 *return socket对象
**/
function connectWS(host) {
	if (!host) {
		return null;
	}

	host = (window.location.protocol == "https:"? "wss://" : "ws://") + window.location.host + host;
    var socket = new WebSocket(host);

    socket.onopen = function () {
    	onWSOpen();
    };

    socket.onclose = function () {
    	onWSClose(message);
    };

    socket.onerror = function (evt) {
    	onWSError(evt);
    };

    socket.onmessage = function(message) {
    	onWSMessage(message);
    };

    return socket;
}

function onWSOpen() {
}

function onWSClose() {
}

function onWSError(evt) {
}

function onWSMessage(message) {
}