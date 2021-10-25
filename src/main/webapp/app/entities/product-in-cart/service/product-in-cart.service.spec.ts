import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProductInCart, ProductInCart } from '../product-in-cart.model';

import { ProductInCartService } from './product-in-cart.service';

describe('ProductInCart Service', () => {
  let service: ProductInCartService;
  let httpMock: HttpTestingController;
  let elemDefault: IProductInCart;
  let expectedResult: IProductInCart | IProductInCart[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductInCartService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      category: 'AAAAAAA',
      name: 'AAAAAAA',
      quantity: 0,
      priceNet: 0,
      vat: 0,
      priceGross: 0,
      totalPriceNet: 0,
      totalPriceGross: 0,
      stock: 0,
      description: 'AAAAAAA',
      imageContentType: 'image/png',
      image: 'AAAAAAA',
      productId: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ProductInCart', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ProductInCart()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductInCart', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          category: 'BBBBBB',
          name: 'BBBBBB',
          quantity: 1,
          priceNet: 1,
          vat: 1,
          priceGross: 1,
          totalPriceNet: 1,
          totalPriceGross: 1,
          stock: 1,
          description: 'BBBBBB',
          image: 'BBBBBB',
          productId: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductInCart', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          category: 'BBBBBB',
          name: 'BBBBBB',
          quantity: 1,
          priceNet: 1,
          vat: 1,
          priceGross: 1,
          totalPriceNet: 1,
          totalPriceGross: 1,
          stock: 1,
          description: 'BBBBBB',
          image: 'BBBBBB',
          productId: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.queryUserCart().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ProductInCart', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
