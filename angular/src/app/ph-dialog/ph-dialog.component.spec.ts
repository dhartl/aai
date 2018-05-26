import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PhDialogComponent } from './ph-dialog.component';

describe('PhDialogComponent', () => {
  let component: PhDialogComponent;
  let fixture: ComponentFixture<PhDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PhDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PhDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
