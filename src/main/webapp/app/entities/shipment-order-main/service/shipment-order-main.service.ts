import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IShipmentOrderMain, getShipmentOrderMainIdentifier } from '../shipment-order-main.model';

export type EntityResponseType = HttpResponse<IShipmentOrderMain>;
export type EntityArrayResponseType = HttpResponse<IShipmentOrderMain[]>;

@Injectable({ providedIn: 'root' })
export class ShipmentOrderMainService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/shipment-order-main');
  protected getShipmentOrderMainByIdOrderMainUrl = this.applicationConfigService.getEndpointFor(this.resourceUrl + '/orderDetails');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}
  
  update(shipmentOrderMain: IShipmentOrderMain): Observable<EntityResponseType> {
    return this.http.put<IShipmentOrderMain>(
      `${this.resourceUrl}/${getShipmentOrderMainIdentifier(shipmentOrderMain) as number}`,
      shipmentOrderMain,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IShipmentOrderMain>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  queryShipmentOrderMainByIdOrderMain(id: number): Observable<EntityResponseType> {
    return this.http.get<IShipmentOrderMain>(`${this.getShipmentOrderMainByIdOrderMainUrl}/${id}`, { observe: 'response' });
  }
}
