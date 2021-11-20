import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPaymentOrderMain } from '../payment-order-main.model';

import { PaymentOrderMainService } from './payment-order-main.service';

describe('PaymentOrderMain Service', () => {
  let service: PaymentOrderMainService;
  let httpMock: HttpTestingController;
  let elemDefault: IPaymentOrderMain;
  let expectedResult: IPaymentOrderMain | IPaymentOrderMain[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PaymentOrderMainService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      priceNet: 0,
      vat: 0,
      priceGross: 0,
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


    it('should find a PaymentOrderMain By id OrderMain', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.queryPaymentOrderMainByIdOrderMain(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should update a PaymentOrderMain', () => {
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
  });

  afterEach(() => {
    httpMock.verify();
  });
});
