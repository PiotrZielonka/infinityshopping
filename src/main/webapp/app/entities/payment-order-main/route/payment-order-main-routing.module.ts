import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaymentOrderMainUpdateComponent } from '../update/payment-order-main-update.component';
import { PaymentOrderMainRoutingResolveService } from './payment-order-main-routing-resolve.service';

const paymentOrderMainRoute: Routes = [
  {
    path: ':id/edit',
    component: PaymentOrderMainUpdateComponent,
    resolve: {
      paymentOrderMain: PaymentOrderMainRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paymentOrderMainRoute)],
  exports: [RouterModule],
})
export class PaymentOrderMainRoutingModule {}
