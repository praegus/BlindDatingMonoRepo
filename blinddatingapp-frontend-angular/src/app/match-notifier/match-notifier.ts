import { Component, Input, OnInit } from '@angular/core';
import { WebsocketService } from './websocket.service';
import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-match-notifier',
  imports: [CommonModule],
  templateUrl: './match-notifier.html',
  styleUrl: './match-notifier.css'
})
export class MatchNotifier implements OnInit {
  @Input() username: string = '';

  constructor(private wsService: WebsocketService, private snackBar: MatSnackBar) { }

  ngOnInit() {
    this.wsService.connect(this.username);

    this.wsService.message$.subscribe(message => {
      if (message.type === 'match') {
        this.snackBar
          .open(`🎯 Match gevonden met ${message.matchWith}`, 'Accept!', { duration: 5000 })
          .onAction()
          .subscribe(() => {
            console.log('schaap', message.fullPayload)
            this.wsService.sendMessage(this.username, message.fullPayload);
          });
      } else if (message.type === 'approved') {
        this.snackBar.open(`💌 Date goedgekeurd met ${message.approvedBy}! Je date zal binnenkort in je profiel verschijnen!`, 'Sluiten', { duration: 5000 })
      }
    });
  }
}
