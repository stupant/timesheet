import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { EntryCategory } from './entry-category.model';
import { EntryCategoryPopupService } from './entry-category-popup.service';
import { EntryCategoryService } from './entry-category.service';

@Component({
    selector: 'jhi-entry-category-dialog',
    templateUrl: './entry-category-dialog.component.html'
})
export class EntryCategoryDialogComponent implements OnInit {

    entryCategory: EntryCategory;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private entryCategoryService: EntryCategoryService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.entryCategory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.entryCategoryService.update(this.entryCategory), false);
        } else {
            this.subscribeToSaveResponse(
                this.entryCategoryService.create(this.entryCategory), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<EntryCategory>, isCreated: boolean) {
        result.subscribe((res: EntryCategory) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: EntryCategory, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'timesheetApp.entryCategory.created'
            : 'timesheetApp.entryCategory.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'entryCategoryListModification', content: 'OK'});
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
    selector: 'jhi-entry-category-popup',
    template: ''
})
export class EntryCategoryPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private entryCategoryPopupService: EntryCategoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.entryCategoryPopupService
                    .open(EntryCategoryDialogComponent, params['id']);
            } else {
                this.modalRef = this.entryCategoryPopupService
                    .open(EntryCategoryDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
