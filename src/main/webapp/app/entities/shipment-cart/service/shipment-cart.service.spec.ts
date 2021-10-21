import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { IShipmentCart } from '../shipment-cart.model';
import { ShipmentCartService } from './shipment-cart.service';

describe('ShipmentCart Service', () => {
  let service: ShipmentCartService;
  let httpMock: HttpTestingController;
  let elemDefault: IShipmentCart;
  let expectedResult: IShipmentCart | IShipmentCart[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ShipmentCartService);
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

    it('should update a ShipmentCart', () => {
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

   
    it('should return a ShipmentCart of currentUser', () => {
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

      service.queryShipmentCartOfCurrentLoggedUser().subscribe(
        resp => (expectedResult = resp.body));

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
