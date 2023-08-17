import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReceptionOrder } from '../reception-order.model';

@Component({
  selector: 'jhi-reception-order-detail',
  templateUrl: './reception-order-detail.component.html',
})
export class ReceptionOrderDetailComponent implements OnInit {
  receptionOrder: IReceptionOrder | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ receptionOrder }) => {
      this.receptionOrder = receptionOrder;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
