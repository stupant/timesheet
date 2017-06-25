import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Feedback } from './feedback.model';
import { FeedbackPopupService } from './feedback-popup.service';
import { FeedbackService } from './feedback.service';

@Component({
    selector: 'jhi-feedback-dialog',
    templateUrl: './feedback-dialog.component.html'
})
export class FeedbackDialogComponent implements OnInit {

    feedback: Feedback;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private alertService: JhiAlertService,
        private feedbackService: FeedbackService,
        private eventManager: JhiEventManager
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

    setFileData(event, feedback, field, isImage) {
        if (event && event.target.files && event.target.files[0]) {
            const file = event.target.files[0];
            if (isImage && !/^image\//.test(file.type)) {
                return;
            }
            this.dataUtils.toBase64(file, (base64Data) => {
                feedback[field] = base64Data;
                feedback[`${field}ContentType`] = file.type;
            });
        }
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.feedback.id !== undefined) {
            this.subscribeToSaveResponse(
                this.feedbackService.update(this.feedback), false);
        } else {
            this.subscribeToSaveResponse(
                this.feedbackService.create(this.feedback), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Feedback>, isCreated: boolean) {
        result.subscribe((res: Feedback) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Feedback, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'timesheetApp.feedback.created'
            : 'timesheetApp.feedback.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'feedbackListModification', content: 'OK'});
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
    selector: 'jhi-feedback-popup',
    template: ''
})
export class FeedbackPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private feedbackPopupService: FeedbackPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.feedbackPopupService
                    .open(FeedbackDialogComponent, params['id']);
            } else {
                this.modalRef = this.feedbackPopupService
                    .open(FeedbackDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
