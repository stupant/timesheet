import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TimesheetTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { EntryCategoryDetailComponent } from '../../../../../../main/webapp/app/entities/entry-category/entry-category-detail.component';
import { EntryCategoryService } from '../../../../../../main/webapp/app/entities/entry-category/entry-category.service';
import { EntryCategory } from '../../../../../../main/webapp/app/entities/entry-category/entry-category.model';

describe('Component Tests', () => {

    describe('EntryCategory Management Detail Component', () => {
        let comp: EntryCategoryDetailComponent;
        let fixture: ComponentFixture<EntryCategoryDetailComponent>;
        let service: EntryCategoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TimesheetTestModule],
                declarations: [EntryCategoryDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    EntryCategoryService,
                    JhiEventManager
                ]
            }).overrideTemplate(EntryCategoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EntryCategoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EntryCategoryService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new EntryCategory('aaa')));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.entryCategory).toEqual(jasmine.objectContaining({id:'aaa'}));
            });
        });
    });

});
