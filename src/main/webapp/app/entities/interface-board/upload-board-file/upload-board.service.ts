import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Subject } from 'rxjs/internal/Subject';

@Injectable({ providedIn: 'root' })
export class UploadBoardService {
  private layersSourceMacs$ = new BehaviorSubject<Macs>(macs);
  private layersSourceMacsWithErrors$ = new BehaviorSubject<MacsWithErrors>(macsWithErrors);

  layersMacs$(): Observable<Macs> {
    return this.layersSourceMacs$.asObservable();
  }

  layersMacsWithErrors$(): Observable<MacsWithErrors> {
    return this.layersSourceMacsWithErrors$.asObservable();
  }

  setMacs(macs: string[]): void {
    this.layersSourceMacs$.next({
      ...this.getMacsCurrentValue(),
      macs,
    });
  }

  setMacsWithErros(macsWithErrors: string[]): void {
    this.layersSourceMacsWithErrors$.next({
      ...this.getMacsWithErrorsCurrentValue(),
      macsWithErrors,
    });
  }

  private getMacsCurrentValue(): Macs {
    return this.layersSourceMacs$.getValue();
  }

  private getMacsWithErrorsCurrentValue(): MacsWithErrors {
    return this.layersSourceMacsWithErrors$.getValue();
  }
}

export interface Macs {
  macs: string[];
}

export interface MacsWithErrors {
  macsWithErrors: string[];
}

export const macs: Macs = {
  macs: [],
};

export const macsWithErrors: MacsWithErrors = {
  macsWithErrors: [],
};
