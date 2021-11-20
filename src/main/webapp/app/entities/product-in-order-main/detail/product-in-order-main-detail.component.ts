import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductInOrderMain } from '../product-in-order-main.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-product-in-order-main-detail',
  templateUrl: './product-in-order-main-detail.component.html',
})
export class ProductInOrderMainDetailComponent implements OnInit {
  productInOrderMain: IProductInOrderMain | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productInOrderMain }) => {
      this.productInOrderMain = productInOrderMain;
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
