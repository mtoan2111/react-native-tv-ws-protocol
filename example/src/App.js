import * as React from 'react';
import { StyleSheet, View, Text, Button } from 'react-native';
import TvWsProtocol from 'react-native-tv-ws-protocol';

export default class App extends React.Component {
    _TvWsProtocol;
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        this._TvWsProtocol = new TvWsProtocol();
        this._TvWsProtocol.onmessage = (data) => {
            console.log(data);
        };
        this._TvWsProtocol.onerror = (data) => {
            console.log(data);
        };
        this._TvWsProtocol.create('wss://192.168.0.125:8002/api/v2/channels/samsung.remote.control?name=S2JlZSBTbWFydCBTb2x1dGlvbnM=', {
            rejectUnauthorized: false,
        });
    }

    onOpenApp = () => {
        this._TvWsProtocol
            .isConnected()
            .then(() => {
                console.log('connected');
            })
            .catch(() => {
                console.log('disconnected');
            });
          
        this._TvWsProtocol.send(
            JSON.stringify({
                method: 'ms.channel.emit',
                params: {
                    data: { action_type: 'DEEP_LINK', appId: '111299001912' },
                    event: 'ed.apps.launch',
                    to: 'host',
                },
            }),
        );
    };

    render() {
        return (
            <View style={{ paddingVertical: 20 }}>
                <Button onPress={this.onOpenApp} title='Send' />
            </View>
        );
    }
}

exports.modu;
