import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IShipmentCart, ShipmentCart } from '../shipment-cart.model';
import { ShipmentCartService } from '../service/shipment-cart.service';

@Component({
  selector: 'jhi-shipment-cart-update',
  templateUrl: './shipment-cart-update.component.html',
})
export class ShipmentCartUpdateComponent implements OnInit {
  isSaving = false;

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
    cart: [],
  });

  constructor(
    protected shipmentCartService: ShipmentCartService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shipmentCart }) => {
      this.updateForm(shipmentCart);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const shipmentCart = this.createFromForm();
    if (shipmentCart.id !== undefined) {
      this.subscribeToSaveResponse(this.shipmentCartService.update(shipmentCart));
    } 
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShipmentCart>>): void {
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

  protected updateForm(shipmentCart: IShipmentCart): void {
    this.editForm.patchValue({
      id: shipmentCart.id,
      firstName: shipmentCart.firstName,
      lastName: shipmentCart.lastName,
      street: shipmentCart.street,
      postalCode: shipmentCart.postalCode,
      city: shipmentCart.city,
      country: shipmentCart.country,
      phoneToTheReceiver: shipmentCart.phoneToTheReceiver,
      firm: shipmentCart.firm,
      taxNumber: shipmentCart.taxNumber,
      cart: shipmentCart.cart,
    });

  }

  protected createFromForm(): IShipmentCart {
    return {
      ...new ShipmentCart(),
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
      cart: this.editForm.get(['cart'])!.value,
    };
  }
}
