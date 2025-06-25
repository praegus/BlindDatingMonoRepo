import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Gender, HairColor, MusicGenre } from '../generated-sources/openapi';

@Component({
  selector: 'app-characteristics',
  imports: [ReactiveFormsModule, CommonModule],
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
