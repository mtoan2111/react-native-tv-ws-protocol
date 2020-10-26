//
//  TvWsProtocol.swift
//  TvWsProtocol
//
//  Created by Nguyễn Mạnh Toàn on 10/26/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation
import Starscream

@objc(TvWsProtocol)
class TvWsProtocol: NSObject, WebSocketDelegate {
    
    var socket: WebSocket!
    var isConnected = false
    let server = WebSocketServer()
    
    @objc(create:options:)
    func create(uri: String, options: Dictionary) -> Void {
        var request = URLRequest(url: URL(string: "wss://192.168.0.2:8002")!)
        request.timeoutInterval = 5
        let pinner = FoundationSecurity(allowSelfSigned: true)
        let socket = WebSocket(request: request, certPinner: pinner)
        socket.delegate = self
        socket.connect()
    };
    
    func didReceive(event: WebSocketEvent, client: WebSocket) {
        switch event {
        case .connected(let headers):
            isConnected = true
            print("websocket is connected: \(headers)")
        case .disconnected(let reason, let code):
            isConnected = false
            print("websocket is disconnected: \(reason) with code: \(code))")
        case .text(let string):
            print("Receive data: \(string)")
        case .ping():
            break
        case .pong():
            break
        case .error(let error):
            isConnected = false
        default:
            break;
        }
    }
    
    func handleError(_ error: Error?) {
        if let e = error as? WSError{
            print("websocket encountered an error: \(e.message)")
        }else if let e = error {
            print("websocket encountered an error: \(e.localizedDescription)")
        }else {
            print("websocket encountered an error")
        }
    }
}
