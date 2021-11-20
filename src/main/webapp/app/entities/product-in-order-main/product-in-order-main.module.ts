import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductInOrderMainDetailComponent } from './detail/product-in-order-main-detail.component';
import { ProductInOrderMainUpdateComponent } from './update/product-in-order-main-update.component';
import { ProductInOrderMainDeleteDialogComponent } from './delete/product-in-order-main-delete-dialog.component';
import { ProductInOrderMainRoutingModule } from './route/product-in-order-main-routing.module';

@NgModule({
  imports: [SharedModule, ProductInOrderMainRoutingModule],
  declarations: [
    ProductInOrderMainDetailComponent,
    ProductInOrderMainUpdateComponent,
    ProductInOrderMainDeleteDialogComponent,
  ],
  entryComponents: [ProductInOrderMainDeleteDialogComponent],
})
export class ProductInOrderMainModule {}
