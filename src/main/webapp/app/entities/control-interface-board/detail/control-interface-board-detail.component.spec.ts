import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ControlInterfaceBoardDetailComponent } from './control-interface-board-detail.component';

describe('ControlInterfaceBoard Management Detail Component', () => {
  let comp: ControlInterfaceBoardDetailComponent;
  let fixture: ComponentFixture<ControlInterfaceBoardDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ControlInterfaceBoardDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ controlInterfaceBoard: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ControlInterfaceBoardDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ControlInterfaceBoardDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load controlInterfaceBoard on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.controlInterfaceBoard).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
