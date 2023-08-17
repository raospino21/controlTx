import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReceptionOrderDetailComponent } from './reception-order-detail.component';

describe('ReceptionOrder Management Detail Component', () => {
  let comp: ReceptionOrderDetailComponent;
  let fixture: ComponentFixture<ReceptionOrderDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReceptionOrderDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ receptionOrder: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ReceptionOrderDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReceptionOrderDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load receptionOrder on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.receptionOrder).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
