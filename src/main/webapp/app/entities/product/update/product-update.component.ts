import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProduct, Product } from '../product.model';
import { ProductService } from '../service/product.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ProductCategoryEnum } from 'app/entities/enumerations/product-category-enum.model';

@Component({
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;
  productCategoryEnumValues = Object.keys(ProductCategoryEnum);

  editForm = this.fb.group({
    id: [],
    productCategoryEnum: [null, [Validators.required]],
    name: [null, [Validators.required, Validators.minLength(0), Validators.maxLength(5000)]],
    quantity: [],
    priceNet: [null, [Validators.required, Validators.min(0), Validators.max(1000000)]],
    vat: [null, [Validators.required, Validators.min(5), Validators.max(100)]],
    priceGross: [],
    stock: [null, [Validators.required, Validators.min(0), Validators.max(1000000)]],
    description: [null, [Validators.required, Validators.minLength(0), Validators.maxLength(10000)]],
    createTime: [],
    updateTime: [],
    image: [null, [Validators.required]],
    imageContentType: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected productService: ProductService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      if (product.id === undefined) {
        const today = dayjs().startOf('day');
        product.createTime = today;
        product.updateTime = today;
      }

      this.updateForm(product);
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
    const product = this.createFromForm();
    if (product.id !== undefined) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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

  protected updateForm(product: IProduct): void {
    this.editForm.patchValue({
      id: product.id,
      productCategoryEnum: product.productCategoryEnum,
      name: product.name,
      quantity: product.quantity,
      priceNet: product.priceNet,
      vat: product.vat,
      priceGross: product.priceGross,
      stock: product.stock,
      description: product.description,
      createTime: product.createTime ? product.createTime.format(DATE_TIME_FORMAT) : null,
      updateTime: product.updateTime ? product.updateTime.format(DATE_TIME_FORMAT) : null,
      image: product.image,
      imageContentType: product.imageContentType,
    });
  }

  protected createFromForm(): IProduct {
    return {
      ...new Product(),
      id: this.editForm.get(['id'])!.value,
      productCategoryEnum: this.editForm.get(['productCategoryEnum'])!.value,
      name: this.editForm.get(['name'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      priceNet: this.editForm.get(['priceNet'])!.value,
      vat: this.editForm.get(['vat'])!.value,
      priceGross: this.editForm.get(['priceGross'])!.value,
      stock: this.editForm.get(['stock'])!.value,
      description: this.editForm.get(['description'])!.value,
      createTime: this.editForm.get(['createTime'])!.value ? dayjs(this.editForm.get(['createTime'])!.value, DATE_TIME_FORMAT) : undefined,
      updateTime: this.editForm.get(['updateTime'])!.value ? dayjs(this.editForm.get(['updateTime'])!.value, DATE_TIME_FORMAT) : undefined,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
    };
  }
}
