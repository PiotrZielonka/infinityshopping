import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICart, Cart } from '../cart.model';

import { CartService } from './cart.service';

describe('Cart Service', () => {
  let service: CartService;
  let httpMock: HttpTestingController;
  let elemDefault: ICart;
  let expectedResult: ICart | ICart[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CartService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      amountOfCartNet: 0,
      amountOfCartGross: 0,
      amountOfShipmentNet: 0,
      amountOfShipmentGross: 0,
      amountOfOrderNet: 0,
      amountOfOrderGross: 0,
    };
  });

  describe('Service methods', () => {
    it('should return all amounts gross of currentLoggedUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          amountOfCartGross: 1,
          amountOfShipmentGross: 1,
          amountOfOrderGross: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.querytAllAmountsGrossOfCurrentLoggedUser().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should return amount of cart gross of currentLoggedUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 2,
          amountOfCartGross: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.querytAllAmountsGrossOfCurrentLoggedUser().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
