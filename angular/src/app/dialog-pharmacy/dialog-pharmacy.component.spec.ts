import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogPharmacyComponent } from './dialog-pharmacy.component';

describe('DialogPharmacyComponent', () => {
  let component: DialogPharmacyComponent;
  let fixture: ComponentFixture<DialogPharmacyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogPharmacyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogPharmacyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
