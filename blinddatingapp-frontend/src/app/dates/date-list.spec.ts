import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DateList } from './date-list';

describe('Dates', () => {
  let component: DateList;
  let fixture: ComponentFixture<DateList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: []
    })
    .compileComponents();

    fixture = TestBed.createComponent(DateList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
