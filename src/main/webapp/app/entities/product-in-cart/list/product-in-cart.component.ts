import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductInCart } from '../product-in-cart.model';
import { ProductInCartService } from '../service/product-in-cart.service';
import { ProductInCartDeleteDialogComponent } from '../delete/product-in-cart-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { CartService } from 'app/entities/cart/service/cart.service';
import { ICart } from 'app/entities/cart/cart.model';

@Component({
  selector: 'jhi-product-in-cart',
  templateUrl: './product-in-cart.component.html',
  styleUrls: ['./product-in-cart.scss'],
})
export class ProductInCartComponent implements OnInit {
  productInCarts?: IProductInCart[];
  cart?: ICart;
  isLoading = false;

  constructor(
    protected productInCartService: ProductInCartService,
    protected cartService: CartService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.productInCartService.queryUserCart().subscribe(
      (res: HttpResponse<IProductInCart[]>) => {
        this.isLoading = false;
        this.productInCarts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );

    this.cartService.querytAmountOfCartGrossOfCurrentLoggedUser().subscribe(
      (cart: any) => {this.cart = cart.body;
    });

  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IProductInCart): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(productInCart: IProductInCart): void {
    const modalRef = this.modalService.open(ProductInCartDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.productInCart = productInCart;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
