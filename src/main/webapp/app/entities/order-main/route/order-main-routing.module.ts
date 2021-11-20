import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrderMainComponent } from '../list/order-main.component';
import { OrderMainDetailComponent } from '../detail/order-main-detail.component';
import { OrderMainUpdateComponent } from '../update/order-main-update.component';
import { OrderMainRoutingResolveService } from './order-main-routing-resolve.service';
import { OrderMainManagementComponent } from '../list/order-main-management.component';
import { OrderMainUpdateStatusComponent } from '../update/order-main-update-status.component';
import { Authority } from 'app/config/authority.constants';

const orderMainRoute: Routes = [
  {
    path: '',
    component: OrderMainComponent,
    data: {
      authorities: [Authority.USER],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'order-main-management',
    component: OrderMainManagementComponent,
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'infinityshoppingApp.orderMain.home.titleOrderManagement',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrderMainDetailComponent,
    data: {
      authorities: [Authority.USER],
    },
    resolve: {
      orderMain: OrderMainRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrderMainUpdateComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'infinityshoppingApp.orderMain.home.titleSubmitYourOrder',
    },
    resolve: {
      orderMain: OrderMainRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/editStatus',
    component: OrderMainUpdateStatusComponent,
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'infinityshoppingApp.orderMain.home.changeStatus',
    },
    resolve: {
      orderMain: OrderMainRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(orderMainRoute)],
  exports: [RouterModule],
})
export class OrderMainRoutingModule {}
