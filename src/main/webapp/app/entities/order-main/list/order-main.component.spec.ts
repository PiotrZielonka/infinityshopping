import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OrderMainService } from '../service/order-main.service';

import { OrderMainComponent } from './order-main.component';

describe('Component Tests', () => {
  describe('OrderMain Management Component', () => {
    let comp: OrderMainComponent;
    let fixture: ComponentFixture<OrderMainComponent>;
    let service: OrderMainService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrderMainComponent],
      })
        .overrideTemplate(OrderMainComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrderMainComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(OrderMainService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'queryOnlyUserOrderMains').mockReturnValue(
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
      expect(service.queryOnlyUserOrderMains).toHaveBeenCalled();
      expect(comp.orderMains?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
