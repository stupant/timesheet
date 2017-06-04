import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService, DataUtils } from 'ng-jhipster';

import { Timesheet } from './timesheet.model';
import { TimesheetService } from './timesheet.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

import { Entry, EntryService } from '../entry';
import * as moment from 'moment';

@Component({
    selector: 'jhi-timesheet',
    templateUrl: './timesheet.component.html'
})
export class TimesheetComponent implements OnInit, OnDestroy {

    timesheet: Timesheet = new Timesheet();
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    queryCount: any;
    reverse: any;
    totalItems: number;
    dow = ['sun', 'mon', 'tue', 'wed', 'thu', 'fri', 'sat'];

    constructor(
        public timesheetService: TimesheetService,
        public entry: EntryService,
        private alertService: AlertService,
        private dataUtils: DataUtils,
        private eventManager: EventManager,
        private parseLinks: ParseLinks,
        private principal: Principal,
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
    }

    loadAll() {
        this.entry.query({
            query: this.timesheet.year + ',' + this.timesheet.week,
            page: this.page,
            size: 10000,
            sort: this.sort()
        }).subscribe(
            (res: ResponseWrapper) => this.entry.entities = res.json,
            (res: ResponseWrapper) => console.error(res.json)
            );
    }

    reset() {
        this.page = 0;
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInTimesheets();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Timesheet) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    registerChangeInTimesheets() {
        this.eventSubscriber = this.eventManager.subscribe('timesheetListModification', (response) => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    prev() {
        this.timesheet.hasDate(moment(this.timesheet.today.toString()).subtract(7, 'days').toDate());
        this.loadAll();
    }
    next() {
        this.timesheet.hasDate(moment(this.timesheet.today.toString()).add(7, 'days').toDate());
        this.loadAll();
    }

    date(i: number) {
        return moment(this.timesheet.today.toString()).add(i, 'days').format('YYYY-MM-DD');
    }

    addEntry() {
        console.log('Todo: Create a new entry here');
    }
}
