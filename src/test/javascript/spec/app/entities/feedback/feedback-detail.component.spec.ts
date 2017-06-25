import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TimesheetTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { FeedbackDetailComponent } from '../../../../../../main/webapp/app/entities/feedback/feedback-detail.component';
import { FeedbackService } from '../../../../../../main/webapp/app/entities/feedback/feedback.service';
import { Feedback } from '../../../../../../main/webapp/app/entities/feedback/feedback.model';

describe('Component Tests', () => {

    describe('Feedback Management Detail Component', () => {
        let comp: FeedbackDetailComponent;
        let fixture: ComponentFixture<FeedbackDetailComponent>;
        let service: FeedbackService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TimesheetTestModule],
                declarations: [FeedbackDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    FeedbackService,
                    JhiEventManager
                ]
            }).overrideTemplate(FeedbackDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FeedbackDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FeedbackService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Feedback('aaa')));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.feedback).toEqual(jasmine.objectContaining({id:'aaa'}));
            });
        });
    });

});
