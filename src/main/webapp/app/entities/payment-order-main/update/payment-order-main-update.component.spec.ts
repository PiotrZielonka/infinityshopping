jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PaymentOrderMainService } from '../service/payment-order-main.service';
import { IPaymentOrderMain, PaymentOrderMain } from '../payment-order-main.model';
import { IOrderMain } from 'app/entities/order-main/order-main.model';
import { OrderMainService } from 'app/entities/order-main/service/order-main.service';

import { PaymentOrderMainUpdateComponent } from './payment-order-main-update.component';

describe('Component Tests', () => {
  describe('PaymentOrderMain Management Update Component', () => {
    let comp: PaymentOrderMainUpdateComponent;
    let fixture: ComponentFixture<PaymentOrderMainUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let paymentOrderMainService: PaymentOrderMainService;
    let orderMainService: OrderMainService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PaymentOrderMainUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PaymentOrderMainUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentOrderMainUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      paymentOrderMainService = TestBed.inject(PaymentOrderMainService);
      orderMainService = TestBed.inject(OrderMainService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const paymentOrderMain: IPaymentOrderMain = { id: 456 };
        const orderMain: IOrderMain = { id: 46955 };
        paymentOrderMain.orderMain = orderMain;

        activatedRoute.data = of({ paymentOrderMain });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(paymentOrderMain));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentOrderMain>>();
        const paymentOrderMain = { id: 123 };
        jest.spyOn(paymentOrderMainService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentOrderMain });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: paymentOrderMain }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(paymentOrderMainService.update).toHaveBeenCalledWith(paymentOrderMain);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentOrderMain>>();
        const paymentOrderMain = { id: 123 };
        jest.spyOn(paymentOrderMainService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentOrderMain });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(paymentOrderMainService.update).toHaveBeenCalledWith(paymentOrderMain);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
