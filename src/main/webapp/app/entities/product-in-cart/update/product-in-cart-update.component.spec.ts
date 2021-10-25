jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProductInCartService } from '../service/product-in-cart.service';
import { ProductInCart } from '../product-in-cart.model';
import { CartService } from 'app/entities/cart/service/cart.service';

import { ProductInCartUpdateComponent } from './product-in-cart-update.component';

describe('Component Tests', () => {
  describe('ProductInCart Management Update Component', () => {
    let comp: ProductInCartUpdateComponent;
    let fixture: ComponentFixture<ProductInCartUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let productInCartService: ProductInCartService;
    let cartService: CartService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProductInCartUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProductInCartUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductInCartUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      productInCartService = TestBed.inject(ProductInCartService);
      cartService = TestBed.inject(CartService);

      comp = fixture.componentInstance;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ProductInCart>>();
        const productInCart = { id: 123 };
        jest.spyOn(productInCartService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ productInCart });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: productInCart }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(productInCartService.update).toHaveBeenCalledWith(productInCart);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ProductInCart>>();
        const productInCart = new ProductInCart();
        jest.spyOn(productInCartService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ productInCart });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: productInCart }));
        saveSubject.complete();

        // THEN
        expect(productInCartService.create).toHaveBeenCalledWith(productInCart);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ProductInCart>>();
        const productInCart = { id: 123 };
        jest.spyOn(productInCartService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ productInCart });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(productInCartService.update).toHaveBeenCalledWith(productInCart);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCartById', () => {
        it('Should return tracked Cart primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCartById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
