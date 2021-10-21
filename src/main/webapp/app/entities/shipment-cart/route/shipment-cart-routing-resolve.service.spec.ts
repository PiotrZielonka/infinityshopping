jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IShipmentCart, ShipmentCart } from '../shipment-cart.model';
import { ShipmentCartService } from '../service/shipment-cart.service';

import { ShipmentCartRoutingResolveService } from './shipment-cart-routing-resolve.service';

describe('ShipmentCart routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ShipmentCartRoutingResolveService;
  let service: ShipmentCartService;
  let resultShipmentCart: IShipmentCart | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ShipmentCartRoutingResolveService);
    service = TestBed.inject(ShipmentCartService);
    resultShipmentCart = undefined;
  });

  describe('resolve', () => {
    it('should return IShipmentCart returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultShipmentCart = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultShipmentCart).toEqual({ id: 123 });
    });

    it('should return new IShipmentCart if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultShipmentCart = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultShipmentCart).toEqual(new ShipmentCart());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ShipmentCart })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultShipmentCart = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultShipmentCart).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
