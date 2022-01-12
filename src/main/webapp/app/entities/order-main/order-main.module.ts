import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OrderMainComponent } from './list/order-main.component';
import { OrderMainDetailComponent } from './detail/order-main-detail.component';
import { OrderMainUpdateComponent } from './update/order-main-update.component';
import { OrderMainDeleteDialogComponent } from './delete/order-main-delete-dialog.component';
import { OrderMainRoutingModule } from './route/order-main-routing.module';
import { OrderMainManagementComponent } from './list/order-main-management.component';
import { OrderMainUpdateStatusComponent } from './update/order-main-update-status.component';
import { Ng2SearchPipeModule } from 'ng2-search-filter';

@NgModule({
  imports: [
    SharedModule,
    OrderMainRoutingModule,
    Ng2SearchPipeModule
  ],
  declarations: [
    OrderMainComponent,
    OrderMainManagementComponent,
    OrderMainDetailComponent,
    OrderMainUpdateComponent,
    OrderMainUpdateStatusComponent,
    OrderMainDeleteDialogComponent,
  ],
  entryComponents: [OrderMainDeleteDialogComponent],
})
export class OrderMainModule {}
