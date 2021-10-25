jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IProductInCart, ProductInCart } from '../product-in-cart.model';
import { ProductInCartService } from '../service/product-in-cart.service';

import { ProductInCartRoutingResolveService } from './product-in-cart-routing-resolve.service';

describe('ProductInCart routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ProductInCartRoutingResolveService;
  let service: ProductInCartService;
  let resultProductInCart: IProductInCart | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ProductInCartRoutingResolveService);
    service = TestBed.inject(ProductInCartService);
    resultProductInCart = undefined;
  });

  describe('resolve', () => {
    it('should return IProductInCart returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductInCart = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProductInCart).toEqual({ id: 123 });
    });

    it('should return new IProductInCart if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductInCart = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultProductInCart).toEqual(new ProductInCart());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ProductInCart })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductInCart = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProductInCart).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
