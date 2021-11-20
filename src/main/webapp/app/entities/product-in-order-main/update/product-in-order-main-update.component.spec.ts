jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProductInOrderMainService } from '../service/product-in-order-main.service';
import { IProductInOrderMain, ProductInOrderMain } from '../product-in-order-main.model';
import { OrderMainService } from 'app/entities/order-main/service/order-main.service';

import { ProductInOrderMainUpdateComponent } from './product-in-order-main-update.component';

describe('Component Tests', () => {
  describe('ProductInOrderMain Management Update Component', () => {
    let comp: ProductInOrderMainUpdateComponent;
    let fixture: ComponentFixture<ProductInOrderMainUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let productInOrderMainService: ProductInOrderMainService;
    let orderMainService: OrderMainService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProductInOrderMainUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProductInOrderMainUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductInOrderMainUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      productInOrderMainService = TestBed.inject(ProductInOrderMainService);
      orderMainService = TestBed.inject(OrderMainService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const productInOrderMain: IProductInOrderMain = { id: 456 };

        activatedRoute.data = of({ productInOrderMain });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(productInOrderMain));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ProductInOrderMain>>();
        const productInOrderMain = { id: 123 };
        jest.spyOn(productInOrderMainService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ productInOrderMain });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: productInOrderMain }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(productInOrderMainService.update).toHaveBeenCalledWith(productInOrderMain);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ProductInOrderMain>>();
        const productInOrderMain = { id: 123 };
        jest.spyOn(productInOrderMainService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ productInOrderMain });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(productInOrderMainService.update).toHaveBeenCalledWith(productInOrderMain);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackOrderMainById', () => {
        it('Should return tracked OrderMain primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrderMainById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
