import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductInCartComponent } from './list/product-in-cart.component';
import { ProductInCartDetailComponent } from './detail/product-in-cart-detail.component';
import { ProductInCartUpdateComponent } from './update/product-in-cart-update.component';
import { ProductInCartDeleteDialogComponent } from './delete/product-in-cart-delete-dialog.component';
import { ProductInCartRoutingModule } from './route/product-in-cart-routing.module';

@NgModule({
  imports: [SharedModule, ProductInCartRoutingModule],
  declarations: [ProductInCartComponent, ProductInCartDetailComponent, ProductInCartUpdateComponent, ProductInCartDeleteDialogComponent],
  entryComponents: [ProductInCartDeleteDialogComponent],
})
export class ProductInCartModule {}
