import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ShipmentOrderMainUpdateComponent } from '../update/shipment-order-main-update.component';
import { ShipmentOrderMainRoutingResolveService } from './shipment-order-main-routing-resolve.service';

const shipmentOrderMainRoute: Routes = [
  {
    path: ':id/edit',
    component: ShipmentOrderMainUpdateComponent,
    resolve: {
      shipmentOrderMain: ShipmentOrderMainRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(shipmentOrderMainRoute)],
  exports: [RouterModule],
})
export class ShipmentOrderMainRoutingModule {}
