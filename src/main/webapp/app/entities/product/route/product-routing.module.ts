import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductComponent } from '../list/product.component';
import { ProductDetailComponent } from '../detail/product-detail.component';
import { ProductUpdateComponent } from '../update/product-update.component';
import { ProductRoutingResolveService } from './product-routing-resolve.service';
import { ProductManagementComponent } from '../list/product-management.component';
import { Authority } from 'app/config/authority.constants';

const productRoute: Routes = [
  {
    path: '',
    component: ProductComponent,
    data: {
      authorities: [],
      defaultSort: 'id,asc',
    },
    canActivate: [],
  },
  {
    path: 'product-management',
    component: ProductManagementComponent,
    data: {
      authorities: [Authority.ADMIN],
      defaultSort: 'id,asc',
      pageTitle: 'infinityshoppingApp.product.home.titleProductManagement',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductDetailComponent,
    resolve: {
      product: ProductRoutingResolveService,
    },
    data: {
      authorities: [],
    },
    canActivate: [],
  },
  {
    path: 'new',
    component: ProductUpdateComponent,
    resolve: {
      product: ProductRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductUpdateComponent,
    resolve: {
      product: ProductRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productRoute)],
  exports: [RouterModule],
})
export class ProductRoutingModule {}
