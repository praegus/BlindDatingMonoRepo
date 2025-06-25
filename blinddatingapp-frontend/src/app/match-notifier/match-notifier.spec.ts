import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatchNotifier } from './match-notifier';

describe('MatchNotifier', () => {
  let component: MatchNotifier;
  let fixture: ComponentFixture<MatchNotifier>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MatchNotifier]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MatchNotifier);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
