jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IProductInOrderMain, ProductInOrderMain } from '../product-in-order-main.model';
import { ProductInOrderMainService } from '../service/product-in-order-main.service';

import { ProductInOrderMainRoutingResolveService } from './product-in-order-main-routing-resolve.service';

describe('ProductInOrderMain routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ProductInOrderMainRoutingResolveService;
  let service: ProductInOrderMainService;
  let resultProductInOrderMain: IProductInOrderMain | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ProductInOrderMainRoutingResolveService);
    service = TestBed.inject(ProductInOrderMainService);
    resultProductInOrderMain = undefined;
  });

  describe('resolve', () => {
    it('should return IProductInOrderMain returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductInOrderMain = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProductInOrderMain).toEqual({ id: 123 });
    });

    it('should return new IProductInOrderMain if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductInOrderMain = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultProductInOrderMain).toEqual(new ProductInOrderMain());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ProductInOrderMain })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductInOrderMain = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProductInOrderMain).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
