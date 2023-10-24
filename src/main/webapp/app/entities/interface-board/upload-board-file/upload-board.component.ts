import { Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { IReceptionOrder } from 'app/entities/reception-order/reception-order.model';
import { Subject } from 'rxjs';

@Component({
  selector: 'jhi-upload-board',
  templateUrl: './upload-board.component.html',
})
export class UploadBoardComponent implements OnDestroy, OnChanges, OnInit {
  stop$: Subject<any> = new Subject();
  @Input() public receptionOrder: IReceptionOrder | undefined;

  public fileName: string = '';
  public mac: string = '';
  public cantidadFilas: number = 0;
  public disableButtonUpload = false;
  public progress: number = 0;
  public message = '';
  public messageError = '';
  public messageBonusCreation = '';
  public progressCreationTasks = 0;
  public progressFinishedCreateBoard = 0;
  public quantityOfErrors = 0;
  public messageCreateBoardTasks = '';

  public currentFile: File | null = null;

  constructor() {
    console.log('------------ constructor component chlid');
    console.log('------------ providerLotNumber ' + this.receptionOrder?.providerLotNumber);
  }
  ngOnInit(): void {
    console.log('------------ ngOnInit');
    console.log('------------ngOnInit providerLotNumber ' + this.receptionOrder?.providerLotNumber);
  }

  ngOnDestroy(): void {
    this.stop$.complete();
  }
  ngOnChanges(changes: SimpleChanges): void {
    this.receptionOrder = changes?.receptionOrder?.currentValue;
    console.log('------------ ngOnChanges ' + this.receptionOrder?.providerLotNumber);
  }

  handleFileSelect(evt: any): void {
    const files = evt.target.files; // FileList object

    const file = files[0];
    this.fileName = file.name;
    this.currentFile = file;

    const reader = new FileReader();
    reader.readAsText(file);
    reader.onload = event => {
      const csv = event.target!['result']; // Content of CSV file
      this.extractData(csv); // Here you can call the above function.
    };
  }

  public upload(): void {
    this.disableButtonUpload = false;
  }

  public download(): void {}

  private extractData(data: any): void {
    this.messageError = '';
    const csvData = data;
    const allTextLines = csvData.split(/\r\n|\n/);
    const header = allTextLines[0].replace(/"/g, '');
    const headersSemiColon = header.split(';');
    const headersComma = header.split(',');

    if (headersSemiColon.length >= 2 || headersComma.length >= 2) {
      this.messageError = 'Solo se admite una columna con los ID de los jugadores';
      alert('Error: ' + this.messageError);
      throw new Error(this.messageError);
    }

    if (header.length < 1) {
      this.messageError = 'El formato del archivo es incorrecto';
      alert('Error: ' + this.messageError);
      throw new Error(this.messageError);
    }

    this.mac = header;
    this.cantidadFilas = allTextLines.length - 2;
  }
}
