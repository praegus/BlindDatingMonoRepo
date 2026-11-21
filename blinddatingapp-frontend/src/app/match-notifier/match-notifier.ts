import { Component, DestroyRef, inject, Input, OnDestroy, OnInit, signal, TemplateRef, ViewChild } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { WebsocketService } from './websocket.service';

@Component({
  selector: 'app-match-notifier',
  imports: [MatDialogModule],
  templateUrl: './match-notifier.html',
  styleUrl: './match-notifier.css'
})
export class MatchNotifier implements OnInit, OnDestroy {
  @Input() username = '';
  @ViewChild('invitation', { static: true }) invitation!: TemplateRef<unknown>;
  readonly matchWith = signal('');
  readonly secondsLeft = signal(5);
  private readonly destroyRef = inject(DestroyRef);
  private readonly dialog = inject(MatDialog);
  private dialogRef?: MatDialogRef<unknown>;
  private timer?: ReturnType<typeof setInterval>;
  private deadline = 0;
  private payload: string | undefined;

  constructor(private wsService: WebsocketService, private snackBar: MatSnackBar) {}

  ngOnInit() {
    this.wsService.message$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(message => {
      if (message.type === 'match') {
        this.closeInvitation();
        this.matchWith.set(message.matchWith);
        this.payload = message.fullPayload;
        this.deadline = Date.now() + 5000;
        this.secondsLeft.set(5);
        const ref = this.dialog.open(this.invitation, {
          width: '560px', maxWidth: 'calc(100vw - 32px)',
          ariaLabelledBy: 'match-invitation-title',
          ariaDescribedBy: 'match-invitation-description',
          autoFocus: '.accept-match', restoreFocus: true
        });
        this.dialogRef = ref;
        ref.beforeClosed().pipe(takeUntilDestroyed(this.destroyRef)).subscribe(() => {
          if (this.dialogRef === ref) this.clearInvitation();
        });
        this.timer = setInterval(() => {
          const remaining = this.deadline - Date.now();
          this.secondsLeft.set(Math.max(0, Math.ceil(remaining / 1000)));
          if (remaining <= 0) this.closeInvitation();
        }, 100);
      } else if (message.type === 'approved') {
        this.closeInvitation();
        this.snackBar.open(`Date approved with ${message.approvedBy}! Your date details will appear in your profile shortly.`, 'Close', { duration: 5000 });
      }
    });
    this.wsService.connect(this.username);
  }

  acceptMatch() {
    const payload = this.payload;
    const inTime = Date.now() < this.deadline;
    this.closeInvitation();
    if (payload !== undefined && inTime) this.wsService.sendMessage(this.username, payload);
  }

  private clearInvitation() {
    clearInterval(this.timer);
    this.timer = undefined;
    this.payload = undefined;
    this.dialogRef = undefined;
  }

  private closeInvitation() {
    const ref = this.dialogRef;
    this.clearInvitation();
    ref?.close();
  }

  ngOnDestroy() {
    this.closeInvitation();
  }
}
