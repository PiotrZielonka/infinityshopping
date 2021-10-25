import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductInCartComponent } from '../list/product-in-cart.component';
import { ProductInCartDetailComponent } from '../detail/product-in-cart-detail.component';
import { ProductInCartUpdateComponent } from '../update/product-in-cart-update.component';
import { ProductInCartRoutingResolveService } from './product-in-cart-routing-resolve.service';
import { Authority } from 'app/config/authority.constants';

const productInCartRoute: Routes = [
  {
    path: '',
    component: ProductInCartComponent,
    data: {
      authorities: [Authority.USER]
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductInCartDetailComponent,
    resolve: {
      productInCart: ProductInCartRoutingResolveService,
    },
    data: {
      authorities: [Authority.USER]
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductInCartUpdateComponent,
    resolve: {
      productInCart: ProductInCartRoutingResolveService,
    },
    data: {
      authorities: [Authority.USER]
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductInCartUpdateComponent,
    resolve: {
      productInCart: ProductInCartRoutingResolveService,
    },
    data: {
      authorities: [Authority.USER]
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productInCartRoute)],
  exports: [RouterModule],
})
export class ProductInCartRoutingModule {}
