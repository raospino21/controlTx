import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'jhi-report',
  templateUrl: './report-commercial.component.html',
  styleUrls: ['./report-commercial.component.scss'],
})
export class ReportCommercialComponent implements OnInit {
  constructor(protected activatedRoute: ActivatedRoute, protected router: Router) {}

  ngOnInit(): void {}

  public filter(): void {}
}
