import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IShipmentCart } from '../shipment-cart.model';
import { ShipmentCartService } from '../service/shipment-cart.service';

@Component({
  selector: 'jhi-shipment-cart',
  templateUrl: './shipment-cart.component.html',
})
export class ShipmentCartComponent implements OnInit {
  shipmentCart?: IShipmentCart;
  isLoading = false;

  constructor(protected shipmentCartService: ShipmentCartService, 
    protected modalService: NgbModal) {}

  ngOnInit(): void {
    this.shipmentCartService.queryShipmentCartOfCurrentLoggedUser().subscribe(
      (shipmentCart: any) => {this.shipmentCart = shipmentCart.body;
    });
  }
}
