import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrderMain, OrderMain } from '../order-main.model';
import { OrderMainService } from '../service/order-main.service';

@Injectable({ providedIn: 'root' })
export class OrderMainRoutingResolveService implements Resolve<IOrderMain> {
  constructor(protected service: OrderMainService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrderMain> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((orderMain: HttpResponse<OrderMain>) => {
          if (orderMain.body) {
            return of(orderMain.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrderMain());
  }
}
