import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService, DataUtils } from 'ng-jhipster';

import { Timesheet } from './timesheet.model';
import { TimesheetService } from './timesheet.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { Entry, EntryService, EntryPopupService, EntryDialogComponent } from '../entry';
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
    totalHours: number;
    modalRef: NgbModalRef;
    entries: any = {};
    isSaving = false;

    constructor(
        public timesheetService: TimesheetService,
        public entry: EntryService,
        private entryPopupService: EntryPopupService,
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
        this.entries = {};
        this.timesheetService.lookup(this.timesheet.user, this.timesheet.year, this.timesheet.week).subscribe(
          (ts: ResponseWrapper) => this.timesheet = Object.assign(this.timesheet, ts.json),
          (err) => console.error(err)
        );
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
        this.principal.identity().then((account) => {
            this.currentAccount = account;
            this.timesheet.user = this.currentAccount.email;
            this.loadAll();
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
        this.eventSubscriber = this.eventManager.subscribe('entryListModification', (response) => this.reset());
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
    cur() {
        this.timesheet.hasDate(new Date());
        this.loadAll();
    }
    date(i: number) {
        return moment(this.timesheet.today.toString()).add(i, 'days').format('YYYY-MM-DD');
    }

    addEntry(d: string) {
        this.entry.entity = new Entry();
        const date = moment(d);
        this.entry.entity.day = { year: date.year(), month: date.month() + 1, day: date.date() };
        this.modalRef = this.entryPopupService.openEntity(EntryDialogComponent);
    }
    editEntry(e: Entry) {
        this.entry.entity = Object.assign(new Entry(), e);
        const date = moment(e.day);
        this.entry.entity.day = { year: date.year(), month: date.month() + 1, day: date.date() };
        console.log('Entry to edit', this.entry.entity);
        this.modalRef = this.entryPopupService.openEntity(EntryDialogComponent);
    }

    save() {
        this.isSaving = true;
        this.timesheetService.update(this.timesheet).subscribe(
            (result: Timesheet) => {
                this.timesheet = result;
                this.alertService.success('timesheetApp.timesheet.submited', { param: result.id }, null);
                this.eventManager.broadcast({ name: 'timesheetListModification', content: 'OK' });
                this.isSaving = false;
            },
            (err) => this.onSaveError(err)
        );
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
