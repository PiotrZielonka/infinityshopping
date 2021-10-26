import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaymentCartUpdateComponent } from './update/payment-cart-update.component';
import { PaymentCartRoutingModule } from './route/payment-cart-routing.module';

@NgModule({
  imports: [SharedModule, PaymentCartRoutingModule],
  declarations: [PaymentCartUpdateComponent]
})
export class PaymentCartModule {}
