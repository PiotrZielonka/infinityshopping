jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ShipmentCartService } from '../service/shipment-cart.service';
import { IShipmentCart, ShipmentCart } from '../shipment-cart.model';

import { ShipmentCartUpdateComponent } from './shipment-cart-update.component';

describe('Component Tests', () => {
  describe('ShipmentCart Management Update Component', () => {
    let comp: ShipmentCartUpdateComponent;
    let fixture: ComponentFixture<ShipmentCartUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let shipmentCartService: ShipmentCartService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ShipmentCartUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ShipmentCartUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ShipmentCartUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      shipmentCartService = TestBed.inject(ShipmentCartService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const shipmentCart: IShipmentCart = { id: 456 };

        activatedRoute.data = of({ shipmentCart });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(shipmentCart));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ShipmentCart>>();
        const shipmentCart = { id: 123 };
        jest.spyOn(shipmentCartService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ shipmentCart });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: shipmentCart }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(shipmentCartService.update).toHaveBeenCalledWith(shipmentCart);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ShipmentCart>>();
        const shipmentCart = { id: 123 };
        jest.spyOn(shipmentCartService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ shipmentCart });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(shipmentCartService.update).toHaveBeenCalledWith(shipmentCart);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
