import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IControlInterfaceBoard } from '../control-interface-board.model';

@Component({
  selector: 'jhi-control-interface-board-detail',
  templateUrl: './control-interface-board-detail.component.html',
})
export class ControlInterfaceBoardDetailComponent implements OnInit {
  controlInterfaceBoard: IControlInterfaceBoard | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ controlInterfaceBoard }) => {
      this.controlInterfaceBoard = controlInterfaceBoard;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
