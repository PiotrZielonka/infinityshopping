import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaymentCartComponent } from './list/payment-cart.component';
import { PaymentCartUpdateComponent } from './update/payment-cart-update.component';
import { PaymentCartRoutingModule } from './route/payment-cart-routing.module';

@NgModule({
  imports: [SharedModule, PaymentCartRoutingModule],
  declarations: [PaymentCartComponent, PaymentCartUpdateComponent]
})
export class PaymentCartModule {}
