import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { OrderMainStatusEnum } from 'app/entities/enumerations/order-main-status-enum.model';
import { IOrderMain, OrderMain } from '../order-main.model';

import { OrderMainService } from './order-main.service';

describe('OrderMain Service', () => {
  let service: OrderMainService;
  let httpMock: HttpTestingController;
  let elemDefault: IOrderMain;
  let expectedResult: IOrderMain | IOrderMain[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrderMainService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      buyerLogin: 'AAAAAAA',
      buyerFirstName: 'AAAAAAA',
      buyerLastName: 'AAAAAAA',
      buyerEmail: 'AAAAAAA',
      buyerPhone: 'AAAAAAA',
      amountOfCartNet: 0,
      amountOfCartGross: 0,
      amountOfShipmentNet: 0,
      amountOfShipmentGross: 0,
      amountOfOrderNet: 0,
      amountOfOrderGross: 0,
      orderMainStatus: OrderMainStatusEnum.WaitingForBankTransfer,
      createTime: currentDate,
      updateTime: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          createTime: currentDate.format(DATE_TIME_FORMAT),
          updateTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a OrderMain', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createTime: currentDate.format(DATE_TIME_FORMAT),
          updateTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createTime: currentDate,
          updateTime: currentDate,
        },
        returnedFromService
      );

      service.create(new OrderMain()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderMain', () => {
      const patchObject = Object.assign(
        {
          orderMainStatus: OrderMainStatusEnum.Delivered,
        },
        new OrderMain()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          createTime: currentDate,
          updateTime: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderMain', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          buyerLogin: 'BBBBBB',
          buyerFirstName: 'BBBBBB',
          buyerLastName: 'BBBBBB',
          buyerEmail: 'BBBBBB',
          buyerPhone: 'BBBBBB',
          amountOfCartNet: 1,
          amountOfCartGross: 1,
          amountOfShipmentNet: 1,
          amountOfShipmentGross: 1,
          amountOfOrderNet: 1,
          amountOfOrderGross: 1,
          orderMainStatus: 'BBBBBB',
          createTime: currentDate.format(DATE_TIME_FORMAT),
          updateTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createTime: currentDate,
          updateTime: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a OrderMain', () => {
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
