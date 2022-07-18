package com.handaebong.handaebong.Ulitility;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketConnection {
    private static SocketConnection sInstance;
    private static Socket mSocket;
    private Emitter.Listener onConnect;
    private Map<String, Emitter.Listener> eventList;
    private Emitter.Listener onDisconnect;
    private String event;

    public static SocketConnection getInstance(){
        if (sInstance == null) {
            sInstance = new SocketConnection();
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.upgrade = false;
            String[] transports = new String[]{"websocket"};
            opts.transports = transports;
            String url = "http://13.124.228.201:3000/";
            try {
                mSocket = IO.socket(url, opts);
                Log.i("ㅅㄷ", "ㅅㅅㅅㅅ");
            } catch (Exception e) {
                Log.i("ㅅㄷ", e+"ee");
                e.printStackTrace();
            }
        }
        return sInstance;
    }

    public void connect(){
        if (mSocket != null && mSocket.connected()) {
            Log.d("Socket Connecting is already","");
            mSocket.disconnect();
            mSocket.connect();
            return;
        }

        try {
            mSocket.connect();
            Log.d("Socket Connecting is connect","");
        } catch (Exception e){
            e.printStackTrace();
            Log.d("Socket Connecting is fail","");
        }
    }

    public SocketConnection on(){
        try {
            if (onConnect != null) {
                mSocket.on(Socket.EVENT_CONNECT, onConnect);
            }
            if (onDisconnect != null) {
                mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            }

            onEvent();

        } catch (Exception e) {
            Log.e(e.getMessage(),"");
        }

        return this;
    }

    public void add(String event, Emitter.Listener listener) {
        mSocket.on(event, listener);
    }

    private void onEvent() {
        for (Map.Entry<String, Emitter.Listener> entry : eventList.entrySet()) {
            mSocket.on(entry.getKey(), entry.getValue());
            Log.d("add " + entry.getKey(),"");
        }
    }

    public void emit(String event, JSONObject value) {
        Log.d("emit : " + event,"");
        mSocket.emit(event, value);
    }

    public void emit(String event, JSONObject value, Ack ack) {
        mSocket.emit(event, value, ack);
    }

    public static boolean isConnected() {
        if (mSocket == null) {
            return false;
        }

        if (mSocket.connected()) {
            return true;
        } else {
            return false;
        }
    }

    public SocketConnection setOnConnect(Emitter.Listener onConnect){
        this.onConnect = onConnect;
        return this;
    }

    public SocketConnection addEvent(String event, Emitter.Listener listener){
//        if (eventList == null) {
//            eventList = new HashMap<>();
//        }
//        eventList.put(event, listener);
        mSocket.on(event, listener);
        Log.i("test", "test_");
        return this;
    }

    public SocketConnection setOnDisconnect(Emitter.Listener onDisconnect){
        this.onDisconnect = onDisconnect;
        return this;
    }

    public void disconnect() {
        mSocket.disconnect();
    }

    public boolean login(String token) {
        JSONObject data = new JSONObject();
        try {
            data.put("token", token);
            this.mSocket.emit("login", data.toString());
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
