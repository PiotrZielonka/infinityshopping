import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IProductInOrderMain, ProductInOrderMain } from '../product-in-order-main.model';
import { ProductInOrderMainService } from '../service/product-in-order-main.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IOrderMain } from 'app/entities/order-main/order-main.model';
import { OrderMainService } from 'app/entities/order-main/service/order-main.service';

@Component({
  selector: 'jhi-product-in-order-main-update',
  templateUrl: './product-in-order-main-update.component.html',
})
export class ProductInOrderMainUpdateComponent implements OnInit {
  isSaving = false;

  orderMainsSharedCollection: IOrderMain[] = [];

  editForm = this.fb.group({
    id: [],
    category: [],
    name: [],
    quantity: [],
    priceNet: [],
    vat: [],
    priceGross: [],
    totalPriceNet: [],
    totalPriceGross: [],
    stock: [],
    description: [],
    image: [],
    imageContentType: [],
    productId: [],
    orderMain: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected productInOrderMainService: ProductInOrderMainService,
    protected orderMainService: OrderMainService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productInOrderMain }) => {
      this.updateForm(productInOrderMain);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('infinityshoppingApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productInOrderMain = this.createFromForm();
    this.subscribeToSaveResponse(this.productInOrderMainService.update(productInOrderMain));
  }

  trackOrderMainById(index: number, item: IOrderMain): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductInOrderMain>>): void {
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

  protected updateForm(productInOrderMain: IProductInOrderMain): void {
    this.editForm.patchValue({
      id: productInOrderMain.id,
      category: productInOrderMain.category,
      name: productInOrderMain.name,
      quantity: productInOrderMain.quantity,
      priceNet: productInOrderMain.priceNet,
      vat: productInOrderMain.vat,
      priceGross: productInOrderMain.priceGross,
      totalPriceNet: productInOrderMain.totalPriceNet,
      totalPriceGross: productInOrderMain.totalPriceGross,
      stock: productInOrderMain.stock,
      description: productInOrderMain.description,
      image: productInOrderMain.image,
      imageContentType: productInOrderMain.imageContentType,
      productId: productInOrderMain.productId,
      orderMain: productInOrderMain.orderMain,
    });
  }

  protected createFromForm(): IProductInOrderMain {
    return {
      ...new ProductInOrderMain(),
      id: this.editForm.get(['id'])!.value,
      category: this.editForm.get(['category'])!.value,
      name: this.editForm.get(['name'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      priceNet: this.editForm.get(['priceNet'])!.value,
      vat: this.editForm.get(['vat'])!.value,
      priceGross: this.editForm.get(['priceGross'])!.value,
      totalPriceNet: this.editForm.get(['totalPriceNet'])!.value,
      totalPriceGross: this.editForm.get(['totalPriceGross'])!.value,
      stock: this.editForm.get(['stock'])!.value,
      description: this.editForm.get(['description'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      productId: this.editForm.get(['productId'])!.value,
      orderMain: this.editForm.get(['orderMain'])!.value,
    };
  }
}
