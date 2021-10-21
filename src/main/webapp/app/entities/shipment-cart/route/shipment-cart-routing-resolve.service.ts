import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IShipmentCart, ShipmentCart } from '../shipment-cart.model';
import { ShipmentCartService } from '../service/shipment-cart.service';

@Injectable({ providedIn: 'root' })
export class ShipmentCartRoutingResolveService implements Resolve<IShipmentCart> {
  constructor(protected service: ShipmentCartService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IShipmentCart> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((shipmentCart: HttpResponse<ShipmentCart>) => {
          if (shipmentCart.body) {
            return of(shipmentCart.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ShipmentCart());
  }
}
