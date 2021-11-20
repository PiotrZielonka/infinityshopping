import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductInOrderMainDetailComponent } from '../detail/product-in-order-main-detail.component';
import { ProductInOrderMainUpdateComponent } from '../update/product-in-order-main-update.component';
import { ProductInOrderMainRoutingResolveService } from './product-in-order-main-routing-resolve.service';

const productInOrderMainRoute: Routes = [
  {
    path: ':id/view',
    component: ProductInOrderMainDetailComponent,
    data: {
      authorities: [Authority.USER],
    },
    resolve: {
      productInOrderMain: ProductInOrderMainRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductInOrderMainUpdateComponent,
    data: {
      authorities: [Authority.ADMIN],
    },
    resolve: {
      productInOrderMain: ProductInOrderMainRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productInOrderMainRoute)],
  exports: [RouterModule],
})
export class ProductInOrderMainRoutingModule {}
