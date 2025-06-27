import { Injectable } from '@angular/core';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { Subject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class WebsocketService {

  private stompClient: any;
  private messageSubject = new Subject<any>();

  public message$ = this.messageSubject.asObservable();

  connect(username: string): void {
    const socket = new SockJS('http://localhost:9082/ws');
    this.stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onConnect: () => {
        console.log('Connected to WebSocket');
        this.stompClient.subscribe(`/topic/matchings/${username}`, (response: any) => {
          console.log('Received message:', response.body);
          var parts = response.body.split(":")
          var yourMatch = parts[0] === username ? parts[1] : parts[0];
          this.messageSubject.next({
            type: 'match',
            matchWith: yourMatch,
            fullPayload: response.body
          });
        });
        this.stompClient.subscribe(`/topic/approved/${username}`, (response: any) => {
          console.log('Date approved:', response.body);
          var parts = response.body.split(":")
          var yourMatch = parts[0] === username ? parts[1] : parts[0];
          this.messageSubject.next({
            type: 'approved',
            approvedBy: yourMatch,
            fullPayload: response.body
          });
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
    this.stompClient.activate();
  }

  sendMessage(username: string, match: string): void {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: `/app/accept/${username}`,
        body: match
      });
    } else {
      console.error('Stomp client is not connected');
    }
  }




  // private stompClient: Client | undefined;

  // connect(): void {
  //   this.stompClient = new Client({
  //     webSocketFactory: () => new SockJS('http://localhost:9082/ws'),
  //     reconnectDelay: 5000
  //   });

  //   this.stompClient.onConnect = () => {
  //     console.log('Connected via WebSocket');
  //   };

  //   this.stompClient.activate();
  // }

  // subscribeMatching(username: string) {
  //   this.stompClient!.subscribe(`/ topic / matchings / ${ username } `, (message: IMessage) => {
  //     console.log('Received message:', message.body);
  //   });
  // }

  // subscribeApprovals(username: string) {
  //   this.stompClient!.subscribe(`/ topic / approved / ${ username } `, (message: IMessage) => {
  //     console.log('Received message:', message.body);
  //   });
  // }

  // sendMessage(destination: string, body: string): void {
  //   if (this.stompClient && this.stompClient.connected) {
  //     this.stompClient.publish({ destination, body });
  //   }
  // }

  // disconnect(): void {
  //   this.stompClient?.deactivate();
  // }
}