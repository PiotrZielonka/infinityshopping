import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaymentCart, PaymentCart } from '../payment-cart.model';
import { PaymentCartService } from '../service/payment-cart.service';

@Injectable({ providedIn: 'root' })
export class PaymentCartRoutingResolveService implements Resolve<IPaymentCart> {
  constructor(protected service: PaymentCartService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentCart> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((paymentCart: HttpResponse<PaymentCart>) => {
          if (paymentCart.body) {
            return of(paymentCart.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PaymentCart());
  }
}
