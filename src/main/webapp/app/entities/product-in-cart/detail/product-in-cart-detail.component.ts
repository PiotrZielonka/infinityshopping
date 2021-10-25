import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductInCart } from '../product-in-cart.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-product-in-cart-detail',
  templateUrl: './product-in-cart-detail.component.html',
})
export class ProductInCartDetailComponent implements OnInit {
  productInCart: IProductInCart | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productInCart }) => {
      this.productInCart = productInCart;
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
}
