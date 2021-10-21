import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IPaymentCart } from '../payment-cart.model';

export type EntityResponseType = HttpResponse<IPaymentCart>;

@Injectable({ providedIn: 'root' })
export class PaymentCartService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payment-cart');

  constructor(protected http: HttpClient, 
    protected applicationConfigService: ApplicationConfigService) {}

  update(paymentCart: IPaymentCart): Observable<EntityResponseType> {
    return this.http.put<IPaymentCart>(`${this.resourceUrl}`,
    paymentCart, {observe: 'response'});
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaymentCart>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  queryPaymentCartOfCurrentLoggedUser(): Observable<EntityResponseType> {
    return this.http.get<IPaymentCart>(`${this.resourceUrl}/userPaymentCart`, { observe: 'response' });
  }
}
