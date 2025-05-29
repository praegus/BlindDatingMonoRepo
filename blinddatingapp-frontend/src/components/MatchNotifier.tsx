import React, { useState, useEffect, useRef } from 'react';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { useRouter } from 'next/router';

export default function MatchNotifier({ data }: any) {

  const [message, setMessage] = useState('');
  const [match, setMatch] = useState('');
  const [username, setUsername] = useState('');
  const stompClientRef = useRef({});
  const router = useRouter();

  useEffect(() => {
    setUsername(Array.isArray(router.query.name) ? router.query.name[0] :router.query.name!);
    if (username == undefined || username == '') {
      return;
    }
    const socket = new SockJS('http://localhost:9082/ws');
    const stompClient = new Client({
      webSocketFactory: () => socket,
      // debug: function (str) {
      //     console.log('debug: ', str);
      // },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onConnect: () => {
        console.log('Connected to WebSocket');
        console.log('schaap2', username)
        stompClient.subscribe(`/topic/matchings/${username}`, (response) => {
          console.log('Received message:', response.body);
          var json = JSON.parse(response.body);
          setMatch(response.body)
          var yourMatch = json.personA === username ? json.personB : json.personA;
          setMessage('You have been matched with ' + yourMatch);
        });
        stompClient.subscribe(`/topic/approved/${username}`, (response) => {
          console.log('Date approved:', response.body);
          var json = JSON.parse(response.body);
          setMatch('')
          var yourMatch = json.personA === username ? json.personB : json.personA;
          setMessage(yourMatch + ' has accepted your date! Details will follow in your profile soon');
        });
      },
      onWebSocketError: (error) => {
        console.error('Error with websocket', error);
      },
      onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      },
    });

    stompClient.activate();
    stompClientRef.current = stompClient;
  }, [router.isReady, username]);

  const sendMessage = () => {
      const stompClient = stompClientRef.current as Client;
      if (stompClient && stompClient.connected) {
          console.log('Sending message:', match, username);
          stompClient.publish({
              destination: `/app/accept/${username}`,
              body: match,
          });
      } else {
          console.error('Stomp client is not connected');
      }
  };

  return (
    <div>
      <h3>Matches:</h3>
      { message ? <p>{message}</p> : '' }
      { match !== '' ? <button onClick={sendMessage}>Date!</button> : ''}
    </div>
  );
};
