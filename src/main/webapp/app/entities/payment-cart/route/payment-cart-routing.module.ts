import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaymentCartUpdateComponent } from '../update/payment-cart-update.component';
import { PaymentCartRoutingResolveService } from './payment-cart-routing-resolve.service';

const paymentCartRoute: Routes = [
  {
    path: ':id/edit',
    component: PaymentCartUpdateComponent,
    resolve: {
      paymentCart: PaymentCartRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paymentCartRoute)],
  exports: [RouterModule],
})
export class PaymentCartRoutingModule {}
