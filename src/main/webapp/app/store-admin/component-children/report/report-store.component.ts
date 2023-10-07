import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'jhi-report',
  templateUrl: './report-store.component.html',
  styleUrls: ['./report-store.component.scss'],
})
export class ReportStoreComponent implements OnInit {
  constructor(protected activatedRoute: ActivatedRoute, protected router: Router) {}

  ngOnInit(): void {}

  public filter(): void {}
}
