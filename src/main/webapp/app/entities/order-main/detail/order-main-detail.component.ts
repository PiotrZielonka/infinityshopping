import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DataUtils } from 'app/core/util/data-util.service';
import { IPaymentOrderMain } from 'app/entities/payment-order-main/payment-order-main.model';
import { PaymentOrderMainService } from 'app/entities/payment-order-main/service/payment-order-main.service';
import { ProductInOrderMainDeleteDialogComponent } from 'app/entities/product-in-order-main/delete/product-in-order-main-delete-dialog.component';
import { IProductInOrderMain } from 'app/entities/product-in-order-main/product-in-order-main.model';
import { ProductInOrderMainService } from 'app/entities/product-in-order-main/service/product-in-order-main.service';
import { ShipmentOrderMainService } from 'app/entities/shipment-order-main/service/shipment-order-main.service';
import { IShipmentOrderMain } from 'app/entities/shipment-order-main/shipment-order-main.model';

import { IOrderMain } from '../order-main.model';

@Component({
  selector: 'jhi-order-main-detail',
  templateUrl: './order-main-detail.component.html',
})
export class OrderMainDetailComponent implements OnInit {
  productInOrderMains?: IProductInOrderMain[];
  orderMain: IOrderMain | null = null;
  paymentOrderMain?: IPaymentOrderMain;
  shipmentOrderMain?: IShipmentOrderMain;
  id!: number;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected productInOrderMainService: ProductInOrderMainService,
    protected paymentOrderMainService: PaymentOrderMainService,
    protected shipmentOrderMainService: ShipmentOrderMainService,
    protected modalService: NgbModal) {}

  ngOnInit(): void {
    this.id = this.activatedRoute.snapshot.params['id'];

    this.productInOrderMainService.queryProductInOrderMainByIdOrderMain(this.id).subscribe(
      (res: HttpResponse<IProductInOrderMain[]>) => {this.productInOrderMains = res.body ?? [];
      },
    );
    this.paymentOrderMainService.queryPaymentOrderMainByIdOrderMain(this.id).subscribe(
      (paymentOrderMain: any) => {this.paymentOrderMain = paymentOrderMain.body;
    });
    this.shipmentOrderMainService.queryShipmentOrderMainByIdOrderMain(this.id).subscribe(
      (shipmentOrderMain: any) => {this.shipmentOrderMain = shipmentOrderMain.body;
    });
    
    this.activatedRoute.data.subscribe(({ orderMain }) => {
      this.orderMain = orderMain;
    });
  }

  trackId(index: number, item: IProductInOrderMain): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  deleteProductInOrderMain(productInOrderMain: IProductInOrderMain): void {
    const modalRef = this.modalService.open(ProductInOrderMainDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.productInOrderMain = productInOrderMain;
  }
}
