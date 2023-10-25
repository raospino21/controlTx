import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Subject } from 'rxjs/internal/Subject';

@Injectable({ providedIn: 'root' })
export class UploadBoardService {
  private layersSource$ = new BehaviorSubject<Layers>(layers);

  layers$(): Observable<Layers> {
    return this.layersSource$.asObservable();
  }

  setMacs(macs: string[]): void {
    this.layersSource$.next({
      ...this.getLayersCurrentValue(),
      macs,
    });
  }

  private getLayersCurrentValue(): Layers {
    return this.layersSource$.getValue();
  }
}

export interface Layers {
  macs: string[];
}

export const layers: Layers = {
  macs: [],
};
