jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPaymentCart, PaymentCart } from '../payment-cart.model';
import { PaymentCartService } from '../service/payment-cart.service';

import { PaymentCartRoutingResolveService } from './payment-cart-routing-resolve.service';

describe('PaymentCart routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PaymentCartRoutingResolveService;
  let service: PaymentCartService;
  let resultPaymentCart: IPaymentCart | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PaymentCartRoutingResolveService);
    service = TestBed.inject(PaymentCartService);
    resultPaymentCart = undefined;
  });

  describe('resolve', () => {
    it('should return IPaymentCart returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPaymentCart = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPaymentCart).toEqual({ id: 123 });
    });

    it('should return new IPaymentCart if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPaymentCart = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPaymentCart).toEqual(new PaymentCart());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PaymentCart })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPaymentCart = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPaymentCart).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
