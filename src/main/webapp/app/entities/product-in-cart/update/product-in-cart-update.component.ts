import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IProductInCart, ProductInCart } from '../product-in-cart.model';
import { ProductInCartService } from '../service/product-in-cart.service';
import { EventManager } from 'app/core/util/event-manager.service';
import { DataUtils } from 'app/core/util/data-util.service';
import { ICart } from 'app/entities/cart/cart.model';

@Component({
  selector: 'jhi-product-in-cart-update',
  templateUrl: './product-in-cart-update.component.html',
})
export class ProductInCartUpdateComponent implements OnInit {
  isSaving = false;

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
    cart: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected productInCartService: ProductInCartService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productInCart }) => {
      this.updateForm(productInCart);

    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productInCart = this.createFromForm();
    if (productInCart.id !== undefined) {
      this.subscribeToSaveResponse(this.productInCartService.update(productInCart));
    } else {
      this.subscribeToSaveResponse(this.productInCartService.create(productInCart));
    }
  }

  trackCartById(index: number, item: ICart): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductInCart>>): void {
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

  protected updateForm(productInCart: IProductInCart): void {
    this.editForm.patchValue({
      id: productInCart.id,
      category: productInCart.category,
      name: productInCart.name,
      quantity: productInCart.quantity,
      priceNet: productInCart.priceNet,
      vat: productInCart.vat,
      priceGross: productInCart.priceGross,
      totalPriceNet: productInCart.totalPriceNet,
      totalPriceGross: productInCart.totalPriceGross,
      stock: productInCart.stock,
      description: productInCart.description,
      image: productInCart.image,
      imageContentType: productInCart.imageContentType,
      productId: productInCart.productId,
      cart: productInCart.cart,
    });

  }

  protected createFromForm(): IProductInCart {
    return {
      ...new ProductInCart(),
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
      cart: this.editForm.get(['cart'])!.value,
    };
  }
  
}
