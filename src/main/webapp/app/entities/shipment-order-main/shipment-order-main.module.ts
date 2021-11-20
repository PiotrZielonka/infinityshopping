import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ShipmentOrderMainUpdateComponent } from './update/shipment-order-main-update.component';
import { ShipmentOrderMainRoutingModule } from './route/shipment-order-main-routing.module';

@NgModule({
  imports: [SharedModule, ShipmentOrderMainRoutingModule],
  declarations: [
    ShipmentOrderMainUpdateComponent,
  ]
})
export class ShipmentOrderMainModule {}
