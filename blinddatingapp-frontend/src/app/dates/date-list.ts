import { Component, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { RomanticDate } from '../generated-sources/openapi';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-date-list',
  templateUrl: './date-list.html',
  imports: [CommonModule, MatCardModule, MatListModule, MatIconModule],
  styleUrls: ['./date-list.css']
})
export class DateList {
  @Input() dates: RomanticDate[] = [];
Array: any;

  dateTimeMs(dateTime : string | undefined): number {
    return Number(dateTime) * 1000
  }
}