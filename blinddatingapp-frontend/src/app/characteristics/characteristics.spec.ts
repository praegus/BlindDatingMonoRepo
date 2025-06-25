import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Characteristics } from './characteristics';

describe('Characteristics', () => {
  let component: Characteristics;
  let fixture: ComponentFixture<Characteristics>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Characteristics]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Characteristics);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
