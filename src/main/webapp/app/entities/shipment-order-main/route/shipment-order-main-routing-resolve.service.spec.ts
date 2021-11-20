jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IShipmentOrderMain, ShipmentOrderMain } from '../shipment-order-main.model';
import { ShipmentOrderMainService } from '../service/shipment-order-main.service';

import { ShipmentOrderMainRoutingResolveService } from './shipment-order-main-routing-resolve.service';

describe('ShipmentOrderMain routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ShipmentOrderMainRoutingResolveService;
  let service: ShipmentOrderMainService;
  let resultShipmentOrderMain: IShipmentOrderMain | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ShipmentOrderMainRoutingResolveService);
    service = TestBed.inject(ShipmentOrderMainService);
    resultShipmentOrderMain = undefined;
  });

  describe('resolve', () => {
    it('should return IShipmentOrderMain returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultShipmentOrderMain = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultShipmentOrderMain).toEqual({ id: 123 });
    });

    it('should return new IShipmentOrderMain if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultShipmentOrderMain = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultShipmentOrderMain).toEqual(new ShipmentOrderMain());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ShipmentOrderMain })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultShipmentOrderMain = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultShipmentOrderMain).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
