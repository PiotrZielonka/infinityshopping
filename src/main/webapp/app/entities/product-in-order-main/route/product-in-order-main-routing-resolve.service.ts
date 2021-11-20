import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductInOrderMain, ProductInOrderMain } from '../product-in-order-main.model';
import { ProductInOrderMainService } from '../service/product-in-order-main.service';

@Injectable({ providedIn: 'root' })
export class ProductInOrderMainRoutingResolveService implements Resolve<IProductInOrderMain> {
  constructor(protected service: ProductInOrderMainService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductInOrderMain> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((productInOrderMain: HttpResponse<ProductInOrderMain>) => {
          if (productInOrderMain.body) {
            return of(productInOrderMain.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProductInOrderMain());
  }
}
