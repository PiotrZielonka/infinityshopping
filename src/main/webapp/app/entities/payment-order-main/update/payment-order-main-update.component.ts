import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPaymentOrderMain, PaymentOrderMain } from '../payment-order-main.model';
import { PaymentOrderMainService } from '../service/payment-order-main.service';
import { IOrderMain } from 'app/entities/order-main/order-main.model';
import { IPayment } from 'app/entities/payment/payment.model';
import { PaymentService } from 'app/entities/payment/service/payment.service';

@Component({
  selector: 'jhi-payment-order-main-update',
  templateUrl: './payment-order-main-update.component.html',
})
export class PaymentOrderMainUpdateComponent implements OnInit {
  isSaving = false;
  payments?: IPayment[];
  isLoading = false;

  orderMainsCollection: IOrderMain[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    priceNet: [],
    vat: [],
    priceGross: [],
    orderMain: [],
  });

  constructor(
    protected paymentOrderMainService: PaymentOrderMainService,
    protected paymentService: PaymentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.paymentService.queryAllPaymentsOnlyWithNamePriceGross().subscribe(
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
    this.activatedRoute.data.subscribe(({ paymentOrderMain }) => {
      this.updateForm(paymentOrderMain);
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
    const paymentOrderMain = this.createFromForm();
    if (paymentOrderMain.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentOrderMainService.update(paymentOrderMain));    
    }
  }  

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentOrderMain>>): void {
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

  protected updateForm(paymentOrderMain: IPaymentOrderMain): void {
    this.editForm.patchValue({
      id: paymentOrderMain.id,
      name: paymentOrderMain.name,
      orderMain: paymentOrderMain.orderMain,
    });
  }

  protected createFromForm(): IPaymentOrderMain {
    return {
      ...new PaymentOrderMain(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      orderMain: this.editForm.get(['orderMain'])!.value,
    };
  }
}
