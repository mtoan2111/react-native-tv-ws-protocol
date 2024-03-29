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
import com.neovisionaries.ws.client.WebSocketState;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

public class TvWsProtocolModule extends ReactContextBaseJavaModule {
    private static ReactApplicationContext reactcontext;
    private WebSocket ws = null;
    public TvWsProtocolModule(ReactApplicationContext context){
        super(context);
        reactcontext = context;
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
            if (options != null){
                if (options.hasKey("rejectUnauthorized")){
                    if (!options.getBoolean("rejectUnauthorized")){
                    SSLContext context = NaiveSSLContext.getInstance("SSL");
                    factory.setSSLContext(context);
                    factory.setVerifyHostname(false);
                    }
                }
            }

            if (uri == ""){
                uri ="ws://localhost:8080";
            }
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
                    params.putString("message", "connected");
                    sendEvent("connect", params);
                }
            });
            this.ws.addListener(new WebSocketAdapter(){
                @Override
                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                    WritableMap params = Arguments.createMap();
                    params.putString("message", "disconnected");
                    sendEvent("disconnect", params);
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
            this.ws.connect();
        } catch (NoSuchAlgorithmException e) {
            WritableMap params = Arguments.createMap();
            params.putString("error", e.getMessage());
            sendEvent("error", params);
        } catch (IOException e) {
            WritableMap params = Arguments.createMap();
            params.putString("error", e.getMessage());
            sendEvent("error", params);
        } catch (WebSocketException e) {
            WritableMap params = Arguments.createMap();
            params.putString("error", e.getMessage());
            sendEvent("error", params);
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
    public void isConnected(Promise promise){
        if (this.ws.getState() == WebSocketState.OPEN){
            WritableMap param = Arguments.createMap();
            param.putBoolean("message", true);
            promise.resolve(param);
        }else {
            WritableMap param = Arguments.createMap();
            param.putBoolean("message", true);
            promise.reject("error", "web socket has been disconnected");
        }
    }

    @ReactMethod
    public void close(){
        this.ws.disconnect();
    }
}
