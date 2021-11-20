import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrderMain, getOrderMainIdentifier } from '../order-main.model';

export type EntityResponseType = HttpResponse<IOrderMain>;
export type EntityArrayResponseType = HttpResponse<IOrderMain[]>;

@Injectable({ providedIn: 'root' })
export class OrderMainService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/order-mains');
  protected deleteOrderMain = this.applicationConfigService.getEndpointFor(this.resourceUrl + '/delete');
  protected editOrderMainStatus = this.applicationConfigService.getEndpointFor(this.resourceUrl + '/editStatus');
  protected getAllOrderMainByCurrentUserLogin = this.applicationConfigService.getEndpointFor(this.resourceUrl + '/currentUser');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(orderMain: IOrderMain): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderMain);
    return this.http
      .post<IOrderMain>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(orderMain: IOrderMain): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderMain);
    return this.http
      .patch<IOrderMain>(`${this.editOrderMainStatus}/${getOrderMainIdentifier(orderMain) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOrderMain>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrderMain[]>(`${this.resourceUrl}/all`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryOnlyUserOrderMains(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrderMain[]>(this.getAllOrderMainByCurrentUserLogin, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.deleteOrderMain}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(orderMain: IOrderMain): IOrderMain {
    return Object.assign({}, orderMain, {
      createTime: orderMain.createTime?.isValid() ? orderMain.createTime.toJSON() : undefined,
      updateTime: orderMain.updateTime?.isValid() ? orderMain.updateTime.toJSON() : undefined,
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
      res.body.forEach((orderMain: IOrderMain) => {
        orderMain.createTime = orderMain.createTime ? dayjs(orderMain.createTime) : undefined;
        orderMain.updateTime = orderMain.updateTime ? dayjs(orderMain.updateTime) : undefined;
      });
    }
    return res;
  }
}
