import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProduct, getProductIdentifier } from '../product.model';

export type EntityResponseType = HttpResponse<IProduct>;
export type EntityArrayResponseType = HttpResponse<IProduct[]>;

@Injectable({ providedIn: 'root' })
export class ProductService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/products');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(product: IProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(product);
    return this.http
      .post<IProduct>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(product: IProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(product);
    return this.http
      .put<IProduct>(`${this.resourceUrl}/${getProductIdentifier(product) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProduct>(`${this.resourceUrl}/byid/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProduct[]>(`${this.resourceUrl}/all`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryAllProductsOnlyWithImageNamePriceGross(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProduct[]>(`${this.resourceUrl}/all/imageNamePriceGross`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(product: IProduct): IProduct {
    return Object.assign({}, product, {
      createTime: product.createTime?.isValid() ? product.createTime.toJSON() : undefined,
      updateTime: product.updateTime?.isValid() ? product.updateTime.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createTime = res.body.createTime ? dayjs(res.body.createTime) : undefined;
      res.body.updateTime = res.body.updateTime ? dayjs(res.body.updateTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((product: IProduct) => {
        product.createTime = product.createTime ? dayjs(product.createTime) : undefined;
        product.updateTime = product.updateTime ? dayjs(product.updateTime) : undefined;
      });
    }
    return res;
  }
}
