import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaymentOrderMainUpdateComponent } from './update/payment-order-main-update.component';
import { PaymentOrderMainRoutingModule } from './route/payment-order-main-routing.module';

@NgModule({
  imports: [SharedModule, PaymentOrderMainRoutingModule],
  declarations: [
    PaymentOrderMainUpdateComponent,
  ]
})
export class PaymentOrderMainModule {}
