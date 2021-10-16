jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';

import { ProductService } from '../service/product.service';

import { ProductComponent } from './product.component';

describe('Component Tests', () => {
  describe('Product Management Component', () => {
    let comp: ProductComponent;
    let fixture: ComponentFixture<ProductComponent>;
    let service: ProductService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProductComponent],
        providers: [
          Router,
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({
                defaultSort: 'id,asc',
              }),
              queryParamMap: of(
                jest.requireActual('@angular/router').convertToParamMap({
                  page: '1',
                  sort: 'id,desc',
                })
              ),
            },
          },
        ],
      })
        .overrideTemplate(ProductComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ProductService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'queryAllProductsOnlyWithImageNamePriceGross').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.queryAllProductsOnlyWithImageNamePriceGross).toHaveBeenCalled();
    });

    it('should load a page', () => {
      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.queryAllProductsOnlyWithImageNamePriceGross).toHaveBeenCalled();
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.queryAllProductsOnlyWithImageNamePriceGross).toHaveBeenCalledWith(expect.objectContaining({ sort: ['id,desc'] }));
    });

    it('should calculate the sort attribute for a name attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.queryAllProductsOnlyWithImageNamePriceGross).toHaveBeenLastCalledWith(
        expect.objectContaining({ sort: ['name,desc', 'id'] })
      );
    });

    it('should calculate the sort attribute for a price gross attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'priceGross';

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.queryAllProductsOnlyWithImageNamePriceGross).toHaveBeenLastCalledWith(
        expect.objectContaining({ sort: ['priceGross,desc', 'id'] })
      );
    });
  });
});
