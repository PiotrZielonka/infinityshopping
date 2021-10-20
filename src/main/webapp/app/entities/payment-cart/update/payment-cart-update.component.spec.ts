jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PaymentCartService } from '../service/payment-cart.service';
import { IPaymentCart, PaymentCart } from '../payment-cart.model';

import { PaymentCartUpdateComponent } from './payment-cart-update.component';

describe('Component Tests', () => {
  describe('PaymentCart Management Update Component', () => {
    let comp: PaymentCartUpdateComponent;
    let fixture: ComponentFixture<PaymentCartUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let paymentCartService: PaymentCartService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PaymentCartUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PaymentCartUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentCartUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      paymentCartService = TestBed.inject(PaymentCartService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const paymentCart: IPaymentCart = { id: 456 };

        activatedRoute.data = of({ paymentCart });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(paymentCart));
      });
    });
    
    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentCart>>();
        const paymentCart = { id: 123 };
        jest.spyOn(paymentCartService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentCart });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: paymentCart }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(paymentCartService.update).toHaveBeenCalledWith(paymentCart);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentCart>>();
        const paymentCart = { id: 123 };
        jest.spyOn(paymentCartService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentCart });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(paymentCartService.update).toHaveBeenCalledWith(paymentCart);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
