import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ShipmentCartComponent } from '../list/shipment-cart.component';
import { ShipmentCartUpdateComponent } from '../update/shipment-cart-update.component';
import { ShipmentCartRoutingResolveService } from './shipment-cart-routing-resolve.service';

const shipmentCartRoute: Routes = [
  {
    path: '',
    component: ShipmentCartComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ShipmentCartUpdateComponent,
    resolve: {
      shipmentCart: ShipmentCartRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(shipmentCartRoute)],
  exports: [RouterModule],
})
export class ShipmentCartRoutingModule {}
