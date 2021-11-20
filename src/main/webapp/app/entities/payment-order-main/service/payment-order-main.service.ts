import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IPaymentOrderMain, getPaymentOrderMainIdentifier } from '../payment-order-main.model';

export type EntityResponseType = HttpResponse<IPaymentOrderMain>;
export type EntityArrayResponseType = HttpResponse<IPaymentOrderMain[]>;

@Injectable({ providedIn: 'root' })
export class PaymentOrderMainService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payment-order-main');
  protected getPaymentOrderMainByIdOrderMainUrl = this.applicationConfigService.getEndpointFor(this.resourceUrl + '/orderDetails');
  protected editPaymentOrderMain = this.applicationConfigService.getEndpointFor(this.resourceUrl + '/edit');
  
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  update(paymentOrderMain: IPaymentOrderMain): Observable<EntityResponseType> {
    return this.http.put<IPaymentOrderMain>(
      `${this.editPaymentOrderMain}/${getPaymentOrderMainIdentifier(paymentOrderMain) as number}`, paymentOrderMain, 
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaymentOrderMain>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  queryPaymentOrderMainByIdOrderMain(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaymentOrderMain>(`${this.getPaymentOrderMainByIdOrderMainUrl}/${id}`, { observe: 'response' });
  }
}
