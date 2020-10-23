import { NativeModules, NativeEventEmitter } from 'react-native';

let onmessage: any;
const { TvWsProtocol } = NativeModules;
const nativeEvent = new NativeEventEmitter(TvWsProtocol);

nativeEvent.addListener("message", (data) => {
  console.log(data);
  if (typeof onmessage === 'function'){
    onmessage(data);
  }
})

type TvWsProtocolType = {
  onmessage: any;
  onconnect: any;
  onerror: any;
  create(uri: String, options: any): void;
  send(message: String): void;
  close(): void;
};


export default TvWsProtocol as TvWsProtocolType;
