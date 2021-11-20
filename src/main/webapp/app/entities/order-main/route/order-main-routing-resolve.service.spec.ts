jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOrderMain, OrderMain } from '../order-main.model';
import { OrderMainService } from '../service/order-main.service';

import { OrderMainRoutingResolveService } from './order-main-routing-resolve.service';

describe('OrderMain routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: OrderMainRoutingResolveService;
  let service: OrderMainService;
  let resultOrderMain: IOrderMain | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(OrderMainRoutingResolveService);
    service = TestBed.inject(OrderMainService);
    resultOrderMain = undefined;
  });

  describe('resolve', () => {
    it('should return IOrderMain returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrderMain = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOrderMain).toEqual({ id: 123 });
    });

    it('should return new IOrderMain if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrderMain = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultOrderMain).toEqual(new OrderMain());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OrderMain })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrderMain = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOrderMain).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
