import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductComponent } from './list/product.component';
import { ProductDetailComponent } from './detail/product-detail.component';
import { ProductUpdateComponent } from './update/product-update.component';
import { ProductDeleteDialogComponent } from './delete/product-delete-dialog.component';
import { ProductRoutingModule } from './route/product-routing.module';
import { ProductManagementComponent } from './list/product-management.component';
import { SearchProductPipe } from 'app/shared/pipe/search-product.pipe';

@NgModule({
  imports: [SharedModule, ProductRoutingModule],
  declarations: [
    ProductComponent,
    ProductManagementComponent,
    ProductDetailComponent,
    ProductUpdateComponent,
    ProductDeleteDialogComponent,
    SearchProductPipe,
  ],
  entryComponents: [ProductDeleteDialogComponent],
})
export class ProductModule {}
