import React, { useState, useEffect, useRef } from 'react';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { useRouter } from 'next/router';

export default function MatchNotifier({ data }: any) {

  const [message, setMessage] = useState('');
  const [name, setName] = useState('');
  const stompClientRef = useRef(null);
  const router = useRouter();

  useEffect(() => {
    var profileName =
      Array.isArray(router.query.name) ? router.query.name[0] :
        router.query.name!;
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
        stompClient.subscribe('/topic/matchings', (response) => {
          console.log('Received message:', response.body);
          var json = JSON.parse(response.body);
          var yourMatch = json.personA === profileName ? json.personB : json.personA;
          setMessage('You have been matched with ' + yourMatch);
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
  }, [router.isReady]);

  return (
    <div>
      <h3>Matches:</h3>
      { message ? 
      <div>
      <p>{message}</p>
      <button>Date</button>
      </div>
      : ''
      }
    </div>
  );
};
