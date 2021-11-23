import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICart } from '../cart.model';

export type EntityResponseType = HttpResponse<ICart>;
export type EntityArrayResponseType = HttpResponse<ICart[]>;

@Injectable({ providedIn: 'root' })
export class CartService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cart/userCart');
  protected amountsGrossUrl = this.applicationConfigService.getEndpointFor(this.resourceUrl + '/amountsGross');
  protected amountOfCartGrossUrl = this.applicationConfigService.getEndpointFor(this.resourceUrl + '/amountOfCartGross');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  querytAllAmountsGrossOfCurrentLoggedUser(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICart[]>(this.amountsGrossUrl, { params: options, observe: 'response' });
  }

  querytAmountOfCartGrossOfCurrentLoggedUser(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICart[]>(this.amountOfCartGrossUrl, { params: options, observe: 'response' });
  }
}
