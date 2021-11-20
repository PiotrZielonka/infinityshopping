import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IShipmentOrderMain, ShipmentOrderMain } from '../shipment-order-main.model';
import { ShipmentOrderMainService } from '../service/shipment-order-main.service';
import { IOrderMain } from 'app/entities/order-main/order-main.model';
import { OrderMainService } from 'app/entities/order-main/service/order-main.service';

@Component({
  selector: 'jhi-shipment-order-main-update',
  templateUrl: './shipment-order-main-update.component.html',
})
export class ShipmentOrderMainUpdateComponent implements OnInit {
  isSaving = false;

  orderMainsCollection: IOrderMain[] = [];

  editForm = this.fb.group({
    id: [],
    firstName: [null, [Validators.required, Validators.maxLength(500)]],
    lastName: [null, [Validators.required, Validators.maxLength(500)]],
    street: [null, [Validators.required, Validators.maxLength(500)]],
    postalCode: [null, [Validators.required, Validators.maxLength(20)]],
    city: [null, [Validators.required, Validators.maxLength(500)]],
    country: [null, [Validators.required, Validators.maxLength(500)]],
    phoneToTheReceiver: [null, [Validators.required, Validators.maxLength(30)]],
    firm: [null, [Validators.maxLength(10000)]],
    taxNumber: [null, [Validators.maxLength(50)]],
    orderMain: [],
  });

  constructor(
    protected shipmentOrderMainService: ShipmentOrderMainService,
    protected orderMainService: OrderMainService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shipmentOrderMain }) => {
      this.updateForm(shipmentOrderMain);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const shipmentOrderMain = this.createFromForm();
    if (shipmentOrderMain.id !== undefined) {
      this.subscribeToSaveResponse(this.shipmentOrderMainService.update(shipmentOrderMain));
    } 
  }

  trackOrderMainById(index: number, item: IOrderMain): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShipmentOrderMain>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(shipmentOrderMain: IShipmentOrderMain): void {
    this.editForm.patchValue({
      id: shipmentOrderMain.id,
      firstName: shipmentOrderMain.firstName,
      lastName: shipmentOrderMain.lastName,
      street: shipmentOrderMain.street,
      postalCode: shipmentOrderMain.postalCode,
      city: shipmentOrderMain.city,
      country: shipmentOrderMain.country,
      phoneToTheReceiver: shipmentOrderMain.phoneToTheReceiver,
      firm: shipmentOrderMain.firm,
      taxNumber: shipmentOrderMain.taxNumber,
      orderMain: shipmentOrderMain.orderMain,
    });

  }


  protected createFromForm(): IShipmentOrderMain {
    return {
      ...new ShipmentOrderMain(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      street: this.editForm.get(['street'])!.value,
      postalCode: this.editForm.get(['postalCode'])!.value,
      city: this.editForm.get(['city'])!.value,
      country: this.editForm.get(['country'])!.value,
      phoneToTheReceiver: this.editForm.get(['phoneToTheReceiver'])!.value,
      firm: this.editForm.get(['firm'])!.value,
      taxNumber: this.editForm.get(['taxNumber'])!.value,
      orderMain: this.editForm.get(['orderMain'])!.value,
    };
  }
}
