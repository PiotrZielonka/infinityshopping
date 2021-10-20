import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { IPaymentCart, PaymentCart } from '../payment-cart.model';
import { PaymentCartService } from './payment-cart.service';

describe('PaymentCart Service', () => {
  let service: PaymentCartService;
  let httpMock: HttpTestingController;
  let elemDefault: IPaymentCart;
  let expectedResult: IPaymentCart | IPaymentCart[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PaymentCartService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = new PaymentCart (
      0,
      'AAAAAAA',
      0,
      0,
      0,
    );
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should update a PaymentCart', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          priceNet: 1,
          vat: 1,
          priceGross: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a PaymentCart of currentUser', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.queryPaymentCartOfCurrentLoggedUser().subscribe(resp => expectedResult = resp.body);

      const expected = Object.assign({}, returnedFromService);      

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
