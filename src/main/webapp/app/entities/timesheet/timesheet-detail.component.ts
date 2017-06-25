import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, DataUtils } from 'ng-jhipster';

import { Timesheet } from './timesheet.model';
import { TimesheetService } from './timesheet.service';
import { Entry, EntryService } from '../entry';
import { ResponseWrapper } from '../../shared';
import * as moment from 'moment';

@Component({
    selector: 'jhi-timesheet-detail',
    templateUrl: './timesheet-detail.component.html'
})
export class TimesheetDetailComponent implements OnInit, OnDestroy {

    timesheet: Timesheet;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    totalHours: number;
    entries: any = {};
    isSaving = false;

    constructor(
        public entry: EntryService,
        private eventManager: EventManager,
        private dataUtils: DataUtils,
        private timesheetService: TimesheetService,
        private route: ActivatedRoute
    ) {
        this.totalHours = 0;
        this.entries = [];
        this.timesheet = new Timesheet();
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTimesheets();
    }

    load(id) {
        this.entries = {};
        this.timesheetService.find(id).subscribe((timesheet: Timesheet) => {
            this.timesheet = Object.assign(new Timesheet(), timesheet);
            console.log(this.timesheet);
            this.entry.lookup(this.timesheet.user, this.timesheet.year, this.timesheet.week).subscribe(
                (res: ResponseWrapper) => {
                    this.entry.entities = res.json;
                    this.totalHours = 0;
                    res.json.forEach((i) => {
                        console.log(i);
                        const key = moment(i.day.toString()).format('YYYY-MM-DD');
                        if (!this.entries[key]) {
                            this.entries[key] = [];
                        }
                        this.entries[key].push(i);
                        this.totalHours += i.hour;
                    });
                    console.log(this.entries);
                }, (res: ResponseWrapper) => console.error(res.json)
            );
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTimesheets() {
        this.eventSubscriber = this.eventManager.subscribe(
            'timesheetListModification',
            (response) => this.load(this.timesheet.id)
        );
    }

    date(i: number) {
        return moment(this.timesheet.today.toString()).add(i, 'days').format('YYYY-MM-DD');
    }
}
