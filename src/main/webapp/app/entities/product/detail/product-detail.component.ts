import { Component, ElementRef, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { IProduct } from '../product.model';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ProductInCartService } from 'app/entities/product-in-cart/service/product-in-cart.service';
import { FormBuilder } from '@angular/forms';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IProductInCart, ProductInCart } from 'app/entities/product-in-cart/product-in-cart.model';
import { ICart } from 'app/entities/cart/cart.model';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-product-detail',
  templateUrl: './product-detail.component.html',
})
export class ProductDetailComponent implements OnInit {
  product!: IProduct;
  isSaving = false;

  editForm = this.fb.group({
    category: [],
    name: [],
    quantity: [],
    priceNet: [],
    vat: [],
    priceGross: [],
    stock: [],
    description: [],
    image: [],
    imageContentType: [],
    productId: []
  });

  constructor(
    private accountService: AccountService,
    private router: Router,
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected productInCartService: ProductInCartService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
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

  previousState(): void {
    window.history.back();
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  save(): void {
    this.isSaving = true;

    if (this.isAuthenticated()) {
      const productInCart = this.createFromForm();
      if (productInCart.id !== undefined) {
        this.subscribeToSaveResponse(this.productInCartService.update(productInCart));
      } else {
        this.subscribeToSaveResponse(this.productInCartService.create(productInCart));
      }
    } else {
      this.login();
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
      category: productInCart.category,
      name: productInCart.name,
      quantity: productInCart.quantity,
      priceNet: productInCart.priceNet,
      vat: productInCart.vat,
      priceGross: productInCart.priceGross,
      stock: productInCart.stock,
      description: productInCart.description,
      image: productInCart.image,
      imageContentType: productInCart.imageContentType,
      productId: productInCart.productId
    });

  }

  protected createFromForm(): IProductInCart {
    return {
      ...new ProductInCart(),
      category: this.editForm.get(['category'])!.value,
      name: this.editForm.get(['name'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      priceNet: this.editForm.get(['priceNet'])!.value,
      vat: this.editForm.get(['vat'])!.value,
      priceGross: this.editForm.get(['priceGross'])!.value,
      stock: this.editForm.get(['stock'])!.value,
      description: this.editForm.get(['description'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      productId: this.editForm.get(['productId'])!.value
    };
  }
}
