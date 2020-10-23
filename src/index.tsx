import { NativeModules } from 'react-native';

type TvWsProtocolType = {
  multiply(a: number, b: number): Promise<number>;
};

const { TvWsProtocol } = NativeModules;

export default TvWsProtocol as TvWsProtocolType;
