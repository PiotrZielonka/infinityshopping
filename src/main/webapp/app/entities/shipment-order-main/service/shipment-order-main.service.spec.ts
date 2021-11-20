import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IShipmentOrderMain } from '../shipment-order-main.model';

import { ShipmentOrderMainService } from './shipment-order-main.service';

describe('ShipmentOrderMain Service', () => {
  let service: ShipmentOrderMainService;
  let httpMock: HttpTestingController;
  let elemDefault: IShipmentOrderMain;
  let expectedResult: IShipmentOrderMain | IShipmentOrderMain[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ShipmentOrderMainService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      firstName: 'AAAAAAA',
      lastName: 'AAAAAAA',
      street: 'AAAAAAA',
      postalCode: 'AAAAAAA',
      city: 'AAAAAAA',
      country: 'AAAAAAA',
      phoneToTheReceiver: 'AAAAAAA',
      firm: 'AAAAAAA',
      taxNumber: 'AAAAAAA',
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

    it('should find a ShipmentOrderMain By id OrderMain', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.queryShipmentOrderMainByIdOrderMain(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });
    
    it('should update a ShipmentOrderMain', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          street: 'BBBBBB',
          postalCode: 'BBBBBB',
          city: 'BBBBBB',
          country: 'BBBBBB',
          phoneToTheReceiver: 'BBBBBB',
          firm: 'BBBBBB',
          taxNumber: 'BBBBBB',
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
