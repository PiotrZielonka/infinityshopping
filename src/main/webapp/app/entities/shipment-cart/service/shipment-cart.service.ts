import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IShipmentCart } from '../shipment-cart.model';

export type EntityResponseType = HttpResponse<IShipmentCart>;

@Injectable({ providedIn: 'root' })
export class ShipmentCartService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/shipment-cart');

  constructor(protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService) {}

  update(shipmentCart: IShipmentCart): Observable<EntityResponseType> {
    return this.http.put<IShipmentCart>(`${this.resourceUrl}`, 
    shipmentCart, {observe: 'response',});
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IShipmentCart>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  queryShipmentCartOfCurrentLoggedUser(): Observable<EntityResponseType> {
    return this.http.get<IShipmentCart>(`${this.resourceUrl}/userShipmentCart`, { observe: 'response' });
  }
}
