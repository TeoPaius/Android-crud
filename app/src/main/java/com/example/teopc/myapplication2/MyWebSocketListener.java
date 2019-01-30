package com.example.teopc.myapplication2;

import android.util.Log;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MyWebSocketListener extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
//        webSocket.send("Hello, it's SSaurel !");
//        webSocket.send("What's up ?");
//        webSocket.send(ByteString.decodeHex("deadbeef"));
//        webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
    }
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.d("socket","Receiving : " + text);
    }
    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        Log.d("socket","Receiving bytes : " + bytes.hex());
    }
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        Log.d("socket","Closing : " + code + " / " + reason);
    }
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.d("socket","Error : " + t.getMessage());
    }
}
