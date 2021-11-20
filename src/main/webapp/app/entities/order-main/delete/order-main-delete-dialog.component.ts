import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrderMain } from '../order-main.model';
import { OrderMainService } from '../service/order-main.service';

@Component({
  templateUrl: './order-main-delete-dialog.component.html',
})
export class OrderMainDeleteDialogComponent {
  orderMain?: IOrderMain;

  constructor(protected orderMainService: OrderMainService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orderMainService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
