import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ShipmentCartComponent } from './list/shipment-cart.component';
import { ShipmentCartUpdateComponent } from './update/shipment-cart-update.component';
import { ShipmentCartRoutingModule } from './route/shipment-cart-routing.module';

@NgModule({
  imports: [SharedModule, ShipmentCartRoutingModule],
  declarations: [ShipmentCartComponent, ShipmentCartUpdateComponent]
})
export class ShipmentCartModule {}
