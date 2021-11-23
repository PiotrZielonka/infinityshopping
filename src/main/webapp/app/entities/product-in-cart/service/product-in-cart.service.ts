import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductInCart, getProductInCartIdentifier } from '../product-in-cart.model';

export type EntityResponseType = HttpResponse<IProductInCart>;
export type EntityArrayResponseType = HttpResponse<IProductInCart[]>;

@Injectable({ providedIn: 'root' })
export class ProductInCartService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-in-carts');
  protected userCartUrl = this.applicationConfigService.getEndpointFor(this.resourceUrl + '/userCart');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productInCart: IProductInCart): Observable<EntityResponseType> {
    return this.http.post<IProductInCart>(this.resourceUrl, productInCart, { observe: 'response' });
  }

  update(productInCart: IProductInCart): Observable<EntityResponseType> {
    return this.http.put<IProductInCart>(`${this.resourceUrl}/${getProductInCartIdentifier(productInCart) as number}`, productInCart, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductInCart>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  queryUserCart(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductInCart[]>(`${this.userCartUrl}`, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
