import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Entry } from './entry.model';
import { EntryPopupService } from './entry-popup.service';
import { EntryService } from './entry.service';

import { EntryCategoryService } from '../entry-category';
import { ResponseWrapper, Principal, AccountService } from '../../shared';

@Component({
    selector: 'jhi-entry-dialog',
    templateUrl: './entry-dialog.component.html'
})
export class EntryDialogComponent implements OnInit {

    entry: Entry;
    authorities: any[];
    isSaving: boolean;
    dayDp: any;
    today = new Date();

    constructor(
        public entryService: EntryService,
        public category: EntryCategoryService,
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private account: AccountService,
        private principal: Principal,
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.category.query({ size: 1000 }).subscribe((res: ResponseWrapper) => this.category.entities = res.json, (err) => this.alertService.error(err));
        this.principal.identity().then((account) => {
            this.entry.user = account.email;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entry, field, isImage) {
        if (event.target.files && event.target.files[0]) {
            const file = event.target.files[0];
            if (isImage && !/^image\//.test(file.type)) {
                return;
            }
            this.dataUtils.toBase64(file, (base64Data) => {
                entry[field] = base64Data;
                entry[`${field}ContentType`] = file.type;
            });
        }
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.entry.id !== undefined) {
            this.subscribeToSaveResponse(
                this.entryService.update(this.entry), false);
        } else {
            this.subscribeToSaveResponse(
                this.entryService.create(this.entry), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Entry>, isCreated: boolean) {
        result.subscribe((res: Entry) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Entry, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'timesheetApp.entry.created'
                : 'timesheetApp.entry.updated',
            { param: result.id }, null);

        this.eventManager.broadcast({ name: 'entryListModification', content: 'OK' });
        this.isSaving = false;
        this.activeModal.dismiss(result);
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

@Component({
    selector: 'jhi-entry-popup',
    template: ''
})
export class EntryPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private entryPopupService: EntryPopupService
    ) { }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.modalRef = this.entryPopupService
                    .open(EntryDialogComponent, params['id']);
            } else {
                this.modalRef = this.entryPopupService
                    .open(EntryDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
