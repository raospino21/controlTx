import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InterfaceBoardDetailComponent } from './interface-board-detail.component';

describe('InterfaceBoard Management Detail Component', () => {
  let comp: InterfaceBoardDetailComponent;
  let fixture: ComponentFixture<InterfaceBoardDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InterfaceBoardDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ interfaceBoard: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InterfaceBoardDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InterfaceBoardDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load interfaceBoard on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.interfaceBoard).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
