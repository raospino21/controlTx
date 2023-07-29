import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataSheetInterfaceDetailComponent } from './data-sheet-interface-detail.component';

describe('DataSheetInterface Management Detail Component', () => {
  let comp: DataSheetInterfaceDetailComponent;
  let fixture: ComponentFixture<DataSheetInterfaceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DataSheetInterfaceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dataSheetInterface: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DataSheetInterfaceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DataSheetInterfaceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dataSheetInterface on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dataSheetInterface).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
