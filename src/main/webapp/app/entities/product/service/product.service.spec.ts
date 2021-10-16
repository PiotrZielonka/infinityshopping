import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ProductCategoryEnum } from 'app/entities/enumerations/product-category-enum.model';
import { IProduct, Product } from '../product.model';

import { ProductService } from './product.service';

describe('Product Service', () => {
  let service: ProductService;
  let httpMock: HttpTestingController;
  let elemDefault: IProduct;
  let expectedResult: IProduct | IProduct[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      productCategoryEnum: ProductCategoryEnum.Vitamins,
      name: 'AAAAAAA',
      quantity: 0,
      priceNet: 0,
      vat: 0,
      priceGross: 0,
      stock: 0,
      description: 'AAAAAAA',
      createTime: currentDate,
      updateTime: currentDate,
      imageContentType: 'image/png',
      image: 'AAAAAAA',
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

    it('should create a Product', () => {
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

      service.create(new Product()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Product', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          productCategoryEnum: 'BBBBBB',
          name: 'BBBBBB',
          quantity: 1,
          priceNet: 1,
          vat: 1,
          priceGross: 1,
          stock: 1,
          description: 'BBBBBB',
          createTime: currentDate.format(DATE_TIME_FORMAT),
          updateTime: currentDate.format(DATE_TIME_FORMAT),
          image: 'BBBBBB',
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

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of all products', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          productCategoryEnum: 'BBBBBB',
          name: 'BBBBBB',
          quantity: 1,
          priceNet: 1,
          vat: 1,
          priceGross: 1,
          stock: 1,
          description: 'BBBBBB',
          createTime: currentDate.format(DATE_TIME_FORMAT),
          updateTime: currentDate.format(DATE_TIME_FORMAT),
          image: 'BBBBBB',
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

    it('should return a list of all product only with image name priceGross', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          priceGross: 1,
          image: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.queryAllProductsOnlyWithImageNamePriceGross().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Product', () => {
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
