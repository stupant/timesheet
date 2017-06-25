import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { TimesheetTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TimesheetDetailComponent } from '../../../../../../main/webapp/app/entities/timesheet/timesheet-detail.component';
import { TimesheetService } from '../../../../../../main/webapp/app/entities/timesheet/timesheet.service';
import { Timesheet } from '../../../../../../main/webapp/app/entities/timesheet/timesheet.model';

describe('Component Tests', () => {

    describe('Timesheet Management Detail Component', () => {
        let comp: TimesheetDetailComponent;
        let fixture: ComponentFixture<TimesheetDetailComponent>;
        let service: TimesheetService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TimesheetTestModule],
                declarations: [TimesheetDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TimesheetService,
                    EventManager
                ]
            }).overrideTemplate(TimesheetDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TimesheetDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TimesheetService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Timesheet('aaa')));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.timesheet).toEqual(jasmine.objectContaining({id:'aaa'}));
            });
        });
    });

});
