jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ShipmentOrderMainService } from '../service/shipment-order-main.service';
import { ShipmentOrderMain } from '../shipment-order-main.model';
import { OrderMainService } from 'app/entities/order-main/service/order-main.service';

import { ShipmentOrderMainUpdateComponent } from './shipment-order-main-update.component';

describe('Component Tests', () => {
  describe('ShipmentOrderMain Management Update Component', () => {
    let comp: ShipmentOrderMainUpdateComponent;
    let fixture: ComponentFixture<ShipmentOrderMainUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let shipmentOrderMainService: ShipmentOrderMainService;
    let orderMainService: OrderMainService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ShipmentOrderMainUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ShipmentOrderMainUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ShipmentOrderMainUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      shipmentOrderMainService = TestBed.inject(ShipmentOrderMainService);
      orderMainService = TestBed.inject(OrderMainService);

      comp = fixture.componentInstance;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ShipmentOrderMain>>();
        const shipmentOrderMain = { id: 123 };
        jest.spyOn(shipmentOrderMainService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ shipmentOrderMain });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: shipmentOrderMain }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(shipmentOrderMainService.update).toHaveBeenCalledWith(shipmentOrderMain);
        expect(comp.isSaving).toEqual(false);
      });
    
      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ShipmentOrderMain>>();
        const shipmentOrderMain = { id: 123 };
        jest.spyOn(shipmentOrderMainService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ shipmentOrderMain });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(shipmentOrderMainService.update).toHaveBeenCalledWith(shipmentOrderMain);
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
