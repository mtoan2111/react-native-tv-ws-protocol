# react-native-tv-ws-protocol

This package is a wrapped websocket protocol, also supports self-certificate bypass

## Installation

```sh
npm install react-native-tv-ws-protocol --save
```

## Features

- Conforms to all of the base [Autobahn test suite](https://crossbar.io/autobahn/).
- Nonblocking. Everything happens in the background, thanks to GCD.
- TLS/WSS support.
- Compression Extensions support ([RFC 7692](https://tools.ietf.org/html/rfc7692))


## Usage

### import module
```js
import TvWsProtocol from "react-native-tv-ws-protocol";
```

### Connect to the WebSocket Server

Once imported, you can open a connection to your WebSocket server.

```js
    TvWsProtocol.create('http://localhost:8080');
```

After you are connected, there is either a pack of events that you can use for process WebSocket events.

### Receiving data from a WebSocket
```js
    TvWsProtocol.onmessage = (message) => {
      //...
    };
```

### Writing to a WebSocket

### write a string frame

```js
    TvWsProtocol.send(
      JSON.stringify({
        method: 'test',
        params: {
          data: { action_type: 'DEEP_LINK', appId: '111299001912' },
          event: 'ed.apps.launch',
          to: 'host',
        },
      })
    );
```

### disconnect

The disconnect method does what you would expect and closes the socket.

```js
    TvWsProtocol.close()
```

### SSL Pinning

SSL Pinning is also supported in this module.


Allow Self-signed certificates:

```js
    TvWsProtocol.create('http://localhost:8080', {
      rejectUnauthorized: false,
    });
```

### Some event you might need
```js
    TvWsProtocol.onconnect = () => {
      //...
    };
```

```js
    TvWsProtocol.ondisconnect = () => {
      //...
    };
```

```js
    TvWsProtocol.onerror = (error) => {
      //...
    };
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
