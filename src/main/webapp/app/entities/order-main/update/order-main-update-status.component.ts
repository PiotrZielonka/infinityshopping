import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOrderMain, OrderMain } from '../order-main.model';
import { OrderMainService } from '../service/order-main.service';

@Component({
  selector: 'jhi-order-main-update-status',
  templateUrl: './order-main-update-status.component.html'
})
export class OrderMainUpdateStatusComponent implements OnInit {
  isSaving = false;
 
  editForm = this.fb.group({
    id: [],
    buyerLogin: [],
    buyerFirstName: [],
    buyerLastName: [],
    buyerEmail: [],
    buyerPhone: [],
    amountOfCartNet: [],
    amountOfCartGross: [],
    amountOfShipmentNet: [],
    amountOfShipmentGross: [],
    amountOfOrderNet: [],
    amountOfOrderGross: [],
    orderMainStatus: [],
    createTime: [],
    updateTime: [],
  });
  

  constructor(
    protected orderMainService: OrderMainService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
  ) {}

  ngOnInit(): void {   
    this.activatedRoute.data.subscribe(({ orderMain }) => {
      if (orderMain.id === undefined) {
        const today = dayjs().startOf('day');
        orderMain.createTime = today;
        orderMain.updateTime = today;
      }

      this.updateForm(orderMain);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orderMain = this.createFromForm();
    if (orderMain.id !== undefined) {
      this.subscribeToSaveResponse(this.orderMainService.partialUpdate(orderMain));
    } 
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderMain>>): void {
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

  protected updateForm(orderMain: IOrderMain): void {
    this.editForm.patchValue({
      id: orderMain.id,
      buyerLogin: orderMain.buyerLogin,
      buyerFirstName: orderMain.buyerFirstName,
      buyerLastName: orderMain.buyerLastName,
      buyerEmail: orderMain.buyerEmail,
      buyerPhone: orderMain.buyerPhone,
      amountOfCartNet: orderMain.amountOfCartNet,
      amountOfCartGross: orderMain.amountOfCartGross,
      amountOfShipmentNet: orderMain.amountOfShipmentNet,
      amountOfShipmentGross: orderMain.amountOfShipmentGross,
      amountOfOrderNet: orderMain.amountOfOrderNet,
      amountOfOrderGross: orderMain.amountOfOrderGross,
      orderMainStatus: orderMain.orderMainStatus,
      createTime: orderMain.createTime ? orderMain.createTime.format(DATE_TIME_FORMAT) : null,
      updateTime: orderMain.updateTime ? orderMain.updateTime.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IOrderMain {
    return {
      ...new OrderMain(),
      id: this.editForm.get(['id'])!.value,
      buyerLogin: this.editForm.get(['buyerLogin'])!.value,
      buyerFirstName: this.editForm.get(['buyerFirstName'])!.value,
      buyerLastName: this.editForm.get(['buyerLastName'])!.value,
      buyerEmail: this.editForm.get(['buyerEmail'])!.value,
      buyerPhone: this.editForm.get(['buyerPhone'])!.value,
      amountOfCartNet: this.editForm.get(['amountOfCartNet'])!.value,
      amountOfCartGross: this.editForm.get(['amountOfCartGross'])!.value,
      amountOfShipmentNet: this.editForm.get(['amountOfShipmentNet'])!.value,
      amountOfShipmentGross: this.editForm.get(['amountOfShipmentGross'])!.value,
      amountOfOrderNet: this.editForm.get(['amountOfOrderNet'])!.value,
      amountOfOrderGross: this.editForm.get(['amountOfOrderGross'])!.value,
      orderMainStatus: this.editForm.get(['orderMainStatus'])!.value,
      createTime: this.editForm.get(['createTime'])!.value ? dayjs(this.editForm.get(['createTime'])!.value, DATE_TIME_FORMAT) : undefined,
      updateTime: this.editForm.get(['updateTime'])!.value ? dayjs(this.editForm.get(['updateTime'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  } 
}
