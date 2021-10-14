import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProduct } from '../product.model';
import { ProductService } from '../service/product.service';

@Component({
  templateUrl: './product-delete-dialog.component.html',
})
export class ProductDeleteDialogComponent {
  product?: IProduct;

  constructor(protected productService: ProductService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
