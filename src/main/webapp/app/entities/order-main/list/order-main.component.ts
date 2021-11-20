import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrderMain } from '../order-main.model';
import { OrderMainService } from '../service/order-main.service';

@Component({
  selector: 'jhi-order-main',
  templateUrl: './order-main.component.html',
})
export class OrderMainComponent implements OnInit {
  orderMains?: IOrderMain[];
  isLoading = false;

  constructor(protected orderMainService: OrderMainService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.orderMainService.queryOnlyUserOrderMains().subscribe(
      (res: HttpResponse<IOrderMain[]>) => {
        this.isLoading = false;
        this.orderMains = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IOrderMain): number {
    return item.id!;
  }
}
