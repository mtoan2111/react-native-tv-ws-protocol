package com.reactnativetvwsprotocol;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

public class TvWsProtocolModule extends ReactContextBaseJavaModule {
    private static ReactApplicationContext reactcontext;
    private String appName = "SamsungWebSocketModule";
    private String appName_b64;
    private WebSocket ws = null;
    public TvWsProtocolModule(ReactApplicationContext context){
        super(context);
        reactcontext = context;
        byte[] data = new byte[0];
        try {
            data = this.appName.getBytes("UTF-8");
            this.appName_b64 = Base64.encodeToString(data, Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public String getName() {
        return "TvWsProtocol";
    }

    private void sendEvent(String eventName, WritableMap params){
        reactcontext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
    }

    @ReactMethod
    public void create(String uri, ReadableMap options, Promise promise){
        WebSocketFactory factory = new WebSocketFactory();
        try {
            SSLContext context = NaiveSSLContext.getInstance("SSL");
            factory.setSSLContext(context);
            factory.setVerifyHostname(false);
            uri ="wss://192.168.0.2:8002/api/v2/channels/samsung.remote.control?name=" + this.appName_b64;
            this.ws = factory.createSocket(uri, 3000);
            this.ws.addListener(new WebSocketAdapter(){
                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                    WritableMap params = Arguments.createMap();
                    params.putString("message", text);
                    sendEvent("message", params);
                }
            });
            this.ws.addListener(new WebSocketAdapter(){
                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    WritableMap params = Arguments.createMap();
                    for (int i = 0; i < headers.size(); i++){
                       Log.d("DEBUG_WS", String.valueOf(headers.get(i)));
                    }
                    params.putString("message", "connected");
                    sendEvent("message", params);
                }
            });
            this.ws.addListener(new WebSocketAdapter(){
                @Override
                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                    WritableMap params = Arguments.createMap();
                    params.putString("message", "disconnected");
                    sendEvent("message", params);
                }
            });
            this.ws.addListener(new WebSocketAdapter(){
                @Override
                public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
                      WritableMap params = Arguments.createMap();
                      params.putString("error", cause.getMessage());
                      sendEvent("error", params);
                }
            });
            this.ws.connectAsynchronously();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void send(String message){
        try {
            byte[] data = message.getBytes("UTF-8");
            this.ws.sendText(message);
        } catch (UnsupportedEncodingException e) {
            WritableMap params = Arguments.createMap();
            params.putString("error", e.getMessage());
            sendEvent("error", params);
        }
    }

    @ReactMethod
    public void close(){
        this.ws.disconnect();
    }
}
