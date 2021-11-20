import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IShipmentOrderMain, ShipmentOrderMain } from '../shipment-order-main.model';
import { ShipmentOrderMainService } from '../service/shipment-order-main.service';

@Injectable({ providedIn: 'root' })
export class ShipmentOrderMainRoutingResolveService implements Resolve<IShipmentOrderMain> {
  constructor(protected service: ShipmentOrderMainService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IShipmentOrderMain> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((shipmentOrderMain: HttpResponse<ShipmentOrderMain>) => {
          if (shipmentOrderMain.body) {
            return of(shipmentOrderMain.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ShipmentOrderMain());
  }
}
