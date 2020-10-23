import { NativeModules, NativeEventEmitter } from 'react-native';

class TvWsProtocol {
  TvWsProtocol: any;
  onmessage: any;
  onconnect: any;
  onerror: any;
  nativeEvent: NativeEventEmitter;
  constructor() {
    this.TvWsProtocol = NativeModules.TvWsProtocol;
    this.nativeEvent = new NativeEventEmitter(this.TvWsProtocol);
    this.initListener();
  }

  initListener = () => {
    this.nativeEvent.addListener('message', (data) => {
      if (typeof this.onmessage === 'function') {
        this.onmessage(data);
      }
    });
  };

  create = (uri: String, options: any) => {
    this.TvWsProtocol.create(uri, options);
  };

  send = (message: String) => {
    this.TvWsProtocol.send(message);
  };

  close = () => {
    this.TvWsProtocol.close();
  };
}

export default new TvWsProtocol();