import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ProductInCartService } from '../service/product-in-cart.service';

import { ProductInCartComponent } from './product-in-cart.component';

describe('Component Tests', () => {
  describe('ProductInCart Management Component', () => {
    let comp: ProductInCartComponent;
    let fixture: ComponentFixture<ProductInCartComponent>;
    let service: ProductInCartService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProductInCartComponent],
      })
        .overrideTemplate(ProductInCartComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductInCartComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ProductInCartService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'queryUserCart').mockReturnValue(
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
      expect(service.queryUserCart).toHaveBeenCalled();
      expect(comp.productInCarts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
