import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ShipmentCartService } from '../service/shipment-cart.service';

import { ShipmentCartComponent } from './shipment-cart.component';
import { ShipmentCart } from '../shipment-cart.model';

describe('Component Tests', () => {
  describe('ShipmentCart Management Component', () => {
    let comp: ShipmentCartComponent;
    let fixture: ComponentFixture<ShipmentCartComponent>;
    let service: ShipmentCartService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ShipmentCartComponent],
      })
        .overrideTemplate(ShipmentCartComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ShipmentCartComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ShipmentCartService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'queryShipmentCartOfCurrentLoggedUser').mockReturnValue(
        of(
          new HttpResponse({
            body: (new ShipmentCart(123)),
            headers,
          })
        )
      );
    });

    it('Should call load a shipmentCart of currentLoggedUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.queryShipmentCartOfCurrentLoggedUser).toHaveBeenCalled();
      expect(comp.shipmentCart).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
