import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Gender, HairColor, MusicGenre } from '../generated-sources/openapi';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio'

@Component({
  selector: 'app-characteristics',
  imports: [ReactiveFormsModule, CommonModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatRadioModule],
  standalone: true,
  templateUrl: './characteristics.html',
  styleUrl: './characteristics.css'
})
export class Characteristics {
  @Input() formGroup!: FormGroup;
  @Input() title: string = 'Default Title';

  genders = Object.values(Gender);
  hairColors = Object.values(HairColor);
  musicGenres = Object.values(MusicGenre);
}
