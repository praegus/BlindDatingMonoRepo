import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-address',
  imports: [ReactiveFormsModule, CommonModule, MatFormFieldModule, MatInputModule, MatIconModule],
  standalone: true,
  templateUrl: './address.html',
  styleUrl: './address.css'
})
export class Address {
  @Input() formGroup!: FormGroup;
  @Input() valid: boolean = false;
}
