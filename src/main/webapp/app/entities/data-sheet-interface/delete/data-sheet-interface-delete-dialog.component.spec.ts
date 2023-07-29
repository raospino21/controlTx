jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { DataSheetInterfaceService } from '../service/data-sheet-interface.service';

import { DataSheetInterfaceDeleteDialogComponent } from './data-sheet-interface-delete-dialog.component';

describe('DataSheetInterface Management Delete Component', () => {
  let comp: DataSheetInterfaceDeleteDialogComponent;
  let fixture: ComponentFixture<DataSheetInterfaceDeleteDialogComponent>;
  let service: DataSheetInterfaceService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DataSheetInterfaceDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(DataSheetInterfaceDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DataSheetInterfaceDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DataSheetInterfaceService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
