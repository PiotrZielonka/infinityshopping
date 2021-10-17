import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPayment } from '../payment.model';
import { PaymentService } from '../service/payment.service';
import { PaymentDeleteDialogComponent } from '../delete/payment-delete-dialog.component';

@Component({
  selector: 'jhi-payment-payment-management',
  templateUrl: './payment-management.component.html',
})
export class PaymentManagementComponent implements OnInit {
  payments?: IPayment[];
  isLoading = false;

  constructor(protected paymentService: PaymentService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.paymentService.query().subscribe(
      (res: HttpResponse<IPayment[]>) => {
        this.isLoading = false;
        this.payments = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPayment): number {
    return item.id!;
  }

  delete(payment: IPayment): void {
    const modalRef = this.modalService.open(PaymentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.payment = payment;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
