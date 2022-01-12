import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrderMain } from '../order-main.model';
import { OrderMainService } from '../service/order-main.service';
import { OrderMainDeleteDialogComponent } from '../delete/order-main-delete-dialog.component';

@Component({
  selector: 'jhi-order-main-order-main-management',
  templateUrl: './order-main-management.component.html',
  styleUrls: ['./order-main-management.scss'],
})
export class OrderMainManagementComponent implements OnInit {
  orderMains?: IOrderMain[];
  isLoading = false;

  searchOrderMain!: any;

  constructor(protected orderMainService: OrderMainService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.orderMainService.query().subscribe(
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
  

  delete(orderMain: IOrderMain): void {
    const modalRef = this.modalService.open(OrderMainDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.orderMain = orderMain;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
