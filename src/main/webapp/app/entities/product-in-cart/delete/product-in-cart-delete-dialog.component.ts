import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductInCart } from '../product-in-cart.model';
import { ProductInCartService } from '../service/product-in-cart.service';

@Component({
  templateUrl: './product-in-cart-delete-dialog.component.html',
})
export class ProductInCartDeleteDialogComponent {
  productInCart?: IProductInCart;

  constructor(protected productInCartService: ProductInCartService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productInCartService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
