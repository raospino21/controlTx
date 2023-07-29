import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDataSheetInterface } from '../data-sheet-interface.model';

@Component({
  selector: 'jhi-data-sheet-interface-detail',
  templateUrl: './data-sheet-interface-detail.component.html',
})
export class DataSheetInterfaceDetailComponent implements OnInit {
  dataSheetInterface: IDataSheetInterface | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dataSheetInterface }) => {
      this.dataSheetInterface = dataSheetInterface;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
