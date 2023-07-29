import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ControlInterfaceBoardFormService } from './control-interface-board-form.service';
import { ControlInterfaceBoardService } from '../service/control-interface-board.service';
import { IControlInterfaceBoard } from '../control-interface-board.model';
import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { IInterfaceBoard } from 'app/entities/interface-board/interface-board.model';
import { InterfaceBoardService } from 'app/entities/interface-board/service/interface-board.service';

import { ControlInterfaceBoardUpdateComponent } from './control-interface-board-update.component';

describe('ControlInterfaceBoard Management Update Component', () => {
  let comp: ControlInterfaceBoardUpdateComponent;
  let fixture: ComponentFixture<ControlInterfaceBoardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let controlInterfaceBoardFormService: ControlInterfaceBoardFormService;
  let controlInterfaceBoardService: ControlInterfaceBoardService;
  let contractService: ContractService;
  let interfaceBoardService: InterfaceBoardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ControlInterfaceBoardUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ControlInterfaceBoardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ControlInterfaceBoardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    controlInterfaceBoardFormService = TestBed.inject(ControlInterfaceBoardFormService);
    controlInterfaceBoardService = TestBed.inject(ControlInterfaceBoardService);
    contractService = TestBed.inject(ContractService);
    interfaceBoardService = TestBed.inject(InterfaceBoardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Contract query and add missing value', () => {
      const controlInterfaceBoard: IControlInterfaceBoard = { id: 456 };
      const contract: IContract = { id: 25970 };
      controlInterfaceBoard.contract = contract;

      const contractCollection: IContract[] = [{ id: 57797 }];
      jest.spyOn(contractService, 'query').mockReturnValue(of(new HttpResponse({ body: contractCollection })));
      const additionalContracts = [contract];
      const expectedCollection: IContract[] = [...additionalContracts, ...contractCollection];
      jest.spyOn(contractService, 'addContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ controlInterfaceBoard });
      comp.ngOnInit();

      expect(contractService.query).toHaveBeenCalled();
      expect(contractService.addContractToCollectionIfMissing).toHaveBeenCalledWith(
        contractCollection,
        ...additionalContracts.map(expect.objectContaining)
      );
      expect(comp.contractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call InterfaceBoard query and add missing value', () => {
      const controlInterfaceBoard: IControlInterfaceBoard = { id: 456 };
      const interfaceBoard: IInterfaceBoard = { id: 53081 };
      controlInterfaceBoard.interfaceBoard = interfaceBoard;

      const interfaceBoardCollection: IInterfaceBoard[] = [{ id: 32094 }];
      jest.spyOn(interfaceBoardService, 'query').mockReturnValue(of(new HttpResponse({ body: interfaceBoardCollection })));
      const additionalInterfaceBoards = [interfaceBoard];
      const expectedCollection: IInterfaceBoard[] = [...additionalInterfaceBoards, ...interfaceBoardCollection];
      jest.spyOn(interfaceBoardService, 'addInterfaceBoardToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ controlInterfaceBoard });
      comp.ngOnInit();

      expect(interfaceBoardService.query).toHaveBeenCalled();
      expect(interfaceBoardService.addInterfaceBoardToCollectionIfMissing).toHaveBeenCalledWith(
        interfaceBoardCollection,
        ...additionalInterfaceBoards.map(expect.objectContaining)
      );
      expect(comp.interfaceBoardsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const controlInterfaceBoard: IControlInterfaceBoard = { id: 456 };
      const contract: IContract = { id: 37484 };
      controlInterfaceBoard.contract = contract;
      const interfaceBoard: IInterfaceBoard = { id: 8370 };
      controlInterfaceBoard.interfaceBoard = interfaceBoard;

      activatedRoute.data = of({ controlInterfaceBoard });
      comp.ngOnInit();

      expect(comp.contractsSharedCollection).toContain(contract);
      expect(comp.interfaceBoardsSharedCollection).toContain(interfaceBoard);
      expect(comp.controlInterfaceBoard).toEqual(controlInterfaceBoard);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IControlInterfaceBoard>>();
      const controlInterfaceBoard = { id: 123 };
      jest.spyOn(controlInterfaceBoardFormService, 'getControlInterfaceBoard').mockReturnValue(controlInterfaceBoard);
      jest.spyOn(controlInterfaceBoardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ controlInterfaceBoard });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: controlInterfaceBoard }));
      saveSubject.complete();

      // THEN
      expect(controlInterfaceBoardFormService.getControlInterfaceBoard).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(controlInterfaceBoardService.update).toHaveBeenCalledWith(expect.objectContaining(controlInterfaceBoard));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IControlInterfaceBoard>>();
      const controlInterfaceBoard = { id: 123 };
      jest.spyOn(controlInterfaceBoardFormService, 'getControlInterfaceBoard').mockReturnValue({ id: null });
      jest.spyOn(controlInterfaceBoardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ controlInterfaceBoard: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: controlInterfaceBoard }));
      saveSubject.complete();

      // THEN
      expect(controlInterfaceBoardFormService.getControlInterfaceBoard).toHaveBeenCalled();
      expect(controlInterfaceBoardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IControlInterfaceBoard>>();
      const controlInterfaceBoard = { id: 123 };
      jest.spyOn(controlInterfaceBoardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ controlInterfaceBoard });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(controlInterfaceBoardService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContract', () => {
      it('Should forward to contractService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(contractService, 'compareContract');
        comp.compareContract(entity, entity2);
        expect(contractService.compareContract).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareInterfaceBoard', () => {
      it('Should forward to interfaceBoardService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(interfaceBoardService, 'compareInterfaceBoard');
        comp.compareInterfaceBoard(entity, entity2);
        expect(interfaceBoardService.compareInterfaceBoard).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
