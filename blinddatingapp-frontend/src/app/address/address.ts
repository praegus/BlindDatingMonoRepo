import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-address',
  imports: [ReactiveFormsModule, CommonModule],
  standalone: true,
  templateUrl: './address.html',
  styleUrl: './address.css'
})
export class Address {
  @Input() formGroup!: FormGroup;
  @Input() valid: boolean = false;
}
