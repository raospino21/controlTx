import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInterfaceBoard } from '../interface-board.model';

@Component({
  selector: 'jhi-interface-board-detail',
  templateUrl: './interface-board-detail.component.html',
})
export class InterfaceBoardDetailComponent implements OnInit {
  interfaceBoard: IInterfaceBoard | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interfaceBoard }) => {
      this.interfaceBoard = interfaceBoard;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
