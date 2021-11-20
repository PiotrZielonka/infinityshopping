import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductInOrderMain } from '../product-in-order-main.model';
import { ProductInOrderMainService } from '../service/product-in-order-main.service';

@Component({
  templateUrl: './product-in-order-main-delete-dialog.component.html',
})
export class ProductInOrderMainDeleteDialogComponent {
  productInOrderMain?: IProductInOrderMain;

  constructor(protected productInOrderMainService: ProductInOrderMainService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productInOrderMainService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
