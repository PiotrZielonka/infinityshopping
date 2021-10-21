import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaymentCart } from '../payment-cart.model';
import { PaymentCartService } from '../service/payment-cart.service';

@Component({
  selector: 'jhi-payment-cart',
  templateUrl: './payment-cart.component.html',
})
export class PaymentCartComponent implements OnInit {
  paymentCart?: IPaymentCart;
  isLoading = false;

  constructor(protected paymentCartService: PaymentCartService, protected modalService: NgbModal) {}

  ngOnInit(): void {
    this.paymentCartService.queryPaymentCartOfCurrentLoggedUser().subscribe(
      (paymentCart: any) => {this.paymentCart = paymentCart.body;
    });
  }

  trackId(index: number, item: IPaymentCart): number {
    return item.id!;
  }
}
