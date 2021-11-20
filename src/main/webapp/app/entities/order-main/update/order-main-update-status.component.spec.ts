jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrderMainService } from '../service/order-main.service';
import { IOrderMain, OrderMain } from '../order-main.model';

import { OrderMainUpdateStatusComponent } from './order-main-update-status.component';

describe('Component Tests', () => {
  describe('OrderMain Update Status Component', () => {
    let comp: OrderMainUpdateStatusComponent;
    let fixture: ComponentFixture<OrderMainUpdateStatusComponent>;
    let activatedRoute: ActivatedRoute;
    let orderMainService: OrderMainService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrderMainUpdateStatusComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrderMainUpdateStatusComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrderMainUpdateStatusComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      orderMainService = TestBed.inject(OrderMainService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should partialUpdate editForm', () => {
        const orderMain: IOrderMain = { id: 456 };

        activatedRoute.data = of({ orderMain });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(orderMain));
      });
    });

    describe('save', () => {
      it('Should call partialUpdate service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrderMain>>();
        const orderMain = { id: 123 };
        jest.spyOn(orderMainService, 'partialUpdate').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ orderMain });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: orderMain }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(orderMainService.partialUpdate).toHaveBeenCalledWith(orderMain);
        expect(comp.isSaving).toEqual(false);
      });
 

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrderMain>>();
        const orderMain = { id: 123 };
        jest.spyOn(orderMainService, 'partialUpdate').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ orderMain });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(orderMainService.partialUpdate).toHaveBeenCalledWith(orderMain);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
