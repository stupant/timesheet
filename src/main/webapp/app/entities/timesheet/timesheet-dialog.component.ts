import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, DataUtils } from 'ng-jhipster';

import { Timesheet } from './timesheet.model';
import { TimesheetPopupService } from './timesheet-popup.service';
import { TimesheetService } from './timesheet.service';

@Component({
    selector: 'jhi-timesheet-dialog',
    templateUrl: './timesheet-dialog.component.html'
})
export class TimesheetDialogComponent implements OnInit {

    timesheet: Timesheet;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: DataUtils,
        private alertService: AlertService,
        private timesheetService: TimesheetService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, timesheet, field, isImage) {
        if (event.target.files && event.target.files[0]) {
            const file = event.target.files[0];
            if (isImage && !/^image\//.test(file.type)) {
                return;
            }
            this.dataUtils.toBase64(file, (base64Data) => {
                timesheet[field] = base64Data;
                timesheet[`${field}ContentType`] = file.type;
            });
        }
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.timesheet.id !== undefined) {
            this.subscribeToSaveResponse(
                this.timesheetService.update(this.timesheet), false);
        } else {
            this.subscribeToSaveResponse(
                this.timesheetService.create(this.timesheet), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Timesheet>, isCreated: boolean) {
        result.subscribe((res: Timesheet) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Timesheet, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'timesheetApp.timesheet.created'
            : 'timesheetApp.timesheet.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'timesheetListModification', content: 'OK'});
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
    selector: 'jhi-timesheet-popup',
    template: ''
})
export class TimesheetPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private timesheetPopupService: TimesheetPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.timesheetPopupService
                    .open(TimesheetDialogComponent, params['id']);
            } else {
                this.modalRef = this.timesheetPopupService
                    .open(TimesheetDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
