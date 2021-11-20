import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OrderMainService } from '../service/order-main.service';

import { OrderMainManagementComponent } from './order-main-management.component';

describe('Component Tests', () => {
  describe('OrderMain Management Component', () => {
    let comp: OrderMainManagementComponent;
    let fixture: ComponentFixture<OrderMainManagementComponent>;
    let service: OrderMainService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrderMainManagementComponent],
      })
        .overrideTemplate(OrderMainManagementComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrderMainManagementComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(OrderMainService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
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
      expect(service.query).toHaveBeenCalled();
      expect(comp.orderMains?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
