jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPaymentOrderMain, PaymentOrderMain } from '../payment-order-main.model';
import { PaymentOrderMainService } from '../service/payment-order-main.service';

import { PaymentOrderMainRoutingResolveService } from './payment-order-main-routing-resolve.service';

describe('PaymentOrderMain routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PaymentOrderMainRoutingResolveService;
  let service: PaymentOrderMainService;
  let resultPaymentOrderMain: IPaymentOrderMain | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PaymentOrderMainRoutingResolveService);
    service = TestBed.inject(PaymentOrderMainService);
    resultPaymentOrderMain = undefined;
  });

  describe('resolve', () => {
    it('should return IPaymentOrderMain returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPaymentOrderMain = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPaymentOrderMain).toEqual({ id: 123 });
    });

    it('should return new IPaymentOrderMain if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPaymentOrderMain = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPaymentOrderMain).toEqual(new PaymentOrderMain());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PaymentOrderMain })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPaymentOrderMain = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPaymentOrderMain).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
