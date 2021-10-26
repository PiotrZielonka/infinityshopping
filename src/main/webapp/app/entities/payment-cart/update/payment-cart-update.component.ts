import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPaymentCart, PaymentCart } from '../payment-cart.model';
import { PaymentCartService } from '../service/payment-cart.service';
import { IPayment } from 'app/entities/payment/payment.model';
import { PaymentService } from 'app/entities/payment/service/payment.service';

@Component({
  selector: 'jhi-payment-cart-update',
  templateUrl: './payment-cart-update.component.html',
})
export class PaymentCartUpdateComponent implements OnInit {
  isSaving = false;
  payments?: IPayment[];
  isLoading = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    priceNet: [],
    vat: [],
    priceGross: [],
    cart: [],
  });



  constructor(
    protected paymentCartService: PaymentCartService,
    protected paymentService: PaymentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) { }

  loadAll(): void {
    this.isLoading = true;

    this.paymentService.query().subscribe(
      (res: HttpResponse<IPayment[]>) => {
        this.isLoading = false;
        this.payments = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentCart }) => {
      this.updateForm(paymentCart);
    });
    this.loadAll();
  }

  trackId(index: number, item: IPayment): number {
    return item.id!;
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paymentCart = this.createFromForm();
    if (paymentCart.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentCartService.update(paymentCart));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentCart>>): void {
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

  protected updateForm(paymentCart: IPaymentCart): void {
    this.editForm.patchValue({
      id: paymentCart.id,
      name: paymentCart.name,
      cart: paymentCart.cart,
    });
  }

  protected createFromForm(): IPaymentCart {
    return {
      ...new PaymentCart(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      cart: this.editForm.get(['cart'])!.value,
    };
  }
}
