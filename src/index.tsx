import { NativeModules, NativeEventEmitter } from 'react-native';

class TvWsProtocol {
    TvWsProtocol: any;
    onmessage: any;
    onconnect: any;
    ondisconnect: any;
    onerror: any;
    nativeEvent: NativeEventEmitter;
    constructor() {
        try {
            this.TvWsProtocol = NativeModules.TvWsProtocol;
            this.nativeEvent = new NativeEventEmitter(this.TvWsProtocol);
            this.initListener();
        } catch (err) {
            this.nativeEvent = new NativeEventEmitter(null);
            this.error(err);
        }
    }

    initListener = () => {
        try {
            this.nativeEvent.addListener('message', (data) => {
                try {
                    if (typeof this.onmessage === 'function') {
                        this.onmessage(data);
                    }
                } catch (err) {
                    this.error(err);
                }
            });

            this.nativeEvent.addListener('error', (message) => {
                try {
                    if (typeof this.onerror === 'function') {
                        this.onerror(message);
                    }
                } catch (err) {
                    this.error(err);
                }
            });

            this.nativeEvent.addListener('disconnect', () => {
                try {
                    if (typeof this.ondisconnect === 'function') {
                        this.ondisconnect();
                    }
                } catch (err) {
                    this.error(err);
                }
            });

            this.nativeEvent.addListener('connect', () => {
                try {
                    if (typeof this.onconnect === 'function') {
                        this.onconnect();
                    }
                } catch (err) {
                    this.error(err);
                }
            });
        } catch (err) {
            this.error(err);
        }
    };

    create = (uri: string, options: any) => {
        try {
            this.TvWsProtocol.create(uri, options);
        } catch (err) {
            this.error(err);
        }
    };

    send = (message: string) => {
        try {
            this.TvWsProtocol.send(message);
        } catch (err) {
            this.error(err);
        }
    };

    isConnected = () => {
        try {
            return this.TvWsProtocol?.isConnected();
        } catch (err) {
            this.error(err);
        }
    };

    close = () => {
        try {
            this.TvWsProtocol.close();
        } catch (err) {
            this.error(err);
        }
    };

    error = (message) => {
        try {
            if (typeof this.onerror === 'function') {
                this.onerror(message);
            }
        } catch (err) {
            console.log(err);
        }
    };
}

export default new TvWsProtocol();
