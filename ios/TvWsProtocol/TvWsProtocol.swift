//
//  TvWsProtocol.swift
//  TvWsProtocol
//
//  Created by Nguyễn Mạnh Toàn on 10/27/20.
//
import Foundation

@objc(TvWsProtocol)
class TvWsProtocol: RCTEventEmitter, WebSocketDelegate {
    var socket: WebSocket!
    var isConnected = false
    
    override func supportedEvents() -> [String]! {
        return ["message", "connect", "disconnect", "error"]
    }
    
    func didReceive(event: WebSocketEvent, client: WebSocket) {
        switch event {
            case .connected(let headers):
                isConnected = true
                print("websocket is connected: \(headers)")
                sendEvent(withName: "connect", body: "socket connected")
            case .disconnected(let reason, let code):
                isConnected = false
                print("websocket is disconnected: \(reason) with code: \(code)")
                sendEvent(withName: "disconnect", body: "socket disconnected")
            case .text(let string):
                print("Received text: \(string)")
                sendEvent(withName: "message", body: string)
            case .binary(let data):
                print("Received data: \(data.count)")
            case .ping(_):
                break
            case .pong(_):
                break
            case .viabilityChanged(_):
                break
            case .reconnectSuggested(_):
                break
            case .cancelled:
                isConnected = false
            case .error(let error):
                isConnected = false
                handleError(error)
            }
    }
    
    @objc(create:options:)
    func create(uri: String, options: Dictionary<String, Any>) {
        var request = URLRequest(url: URL(string: "http://localhost:8080")!)
        request.timeoutInterval = 5
        let pinned = FoundationSecurity(allowSelfSigned: true)
        socket = WebSocket(request: request, certPinner: pinned)
        socket.delegate = self
        socket.connect()
    }
    
    @objc(send:)
    func send(message: String) {
        print("socket send message: \(message)")
        socket.write(string: message)
    }
    
    @objc(close)
    func close() {
        socket.disconnect()
    }
    
    func handleError(_ error: Error?) {
        if let e = error as? WSError {
            print("websocket encountered an error: \(e.message)")
            sendEvent(withName: "error", body: e.message)
        } else if let e = error {
            print("websocket encountered an error: \(e.localizedDescription)")
            sendEvent(withName: "error", body: e.localizedDescription)
        } else {
            print("websocket encountered an error")
            sendEvent(withName: "error", body: "unknown error")
       }
   }
}