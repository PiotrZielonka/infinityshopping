import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductInOrderMain, getProductInOrderMainIdentifier } from '../product-in-order-main.model';

export type EntityResponseType = HttpResponse<IProductInOrderMain>;
export type EntityArrayResponseType = HttpResponse<IProductInOrderMain[]>;

@Injectable({ providedIn: 'root' })
export class ProductInOrderMainService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-in-order-mains');
  protected resourceUrlById = this.applicationConfigService.getEndpointFor(this.resourceUrl + '/byId');
  protected getProductInOrderMainsByIdOrderMainUrl = this.applicationConfigService.getEndpointFor(this.resourceUrl + '/orderDetails');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  update(productInOrderMain: IProductInOrderMain): Observable<EntityResponseType> {
    return this.http.put<IProductInOrderMain>(
      `${this.resourceUrl}/${getProductInOrderMainIdentifier(productInOrderMain) as number}`,
      productInOrderMain,
      { observe: 'response' }
    );
  }
  
  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductInOrderMain>(`${this.resourceUrlById}/${id}`, { observe: 'response' });
  }

  queryProductInOrderMainByIdOrderMain(id: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProductInOrderMain[]>(`${this.getProductInOrderMainsByIdOrderMainUrl}/${id}`, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
