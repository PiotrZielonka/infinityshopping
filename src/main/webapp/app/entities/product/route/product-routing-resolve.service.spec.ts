jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IProduct, Product } from '../product.model';
import { ProductService } from '../service/product.service';

import { ProductRoutingResolveService } from './product-routing-resolve.service';

describe('Product routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ProductRoutingResolveService;
  let service: ProductService;
  let resultProduct: IProduct | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ProductRoutingResolveService);
    service = TestBed.inject(ProductService);
    resultProduct = undefined;
  });

  describe('resolve', () => {
    it('should return IProduct returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProduct = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProduct).toEqual({ id: 123 });
    });

    it('should return new IProduct if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProduct = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultProduct).toEqual(new Product());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Product })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProduct = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProduct).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
