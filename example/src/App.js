import * as React from 'react';
import { StyleSheet, View, Text, Button } from 'react-native';
import TvWsProtocol from 'react-native-tv-ws-protocol';

export default class App extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    TvWsProtocol.onmessage = (data) => {
      console.log(data);
    };
    TvWsProtocol.create('', {
      security: true,
      rejectUnauthorized: true,
      appName: 'SamsungWebSocketModule',
    });
  }

  onOpenApp = () => {
    TvWsProtocol.send(
      JSON.stringify({
        method: 'ms.channel.emit',
        params: {
          data: { action_type: 'DEEP_LINK', appId: '111299001912' },
          event: 'ed.apps.launch',
          to: 'host',
        },
      })
    );
  };

  render() {
    return (
      <View style={{ paddingVertical: 20 }}>
        <Button onPress={this.onOpenApp} title="Send" />
      </View>
    );
  }
}

exports.modu;
