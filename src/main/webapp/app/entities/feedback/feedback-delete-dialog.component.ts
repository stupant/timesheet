import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { Feedback } from './feedback.model';
import { FeedbackPopupService } from './feedback-popup.service';
import { FeedbackService } from './feedback.service';

@Component({
    selector: 'jhi-feedback-delete-dialog',
    templateUrl: './feedback-delete-dialog.component.html'
})
export class FeedbackDeleteDialogComponent {

    feedback: Feedback;

    constructor(
        private feedbackService: FeedbackService,
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.feedbackService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'feedbackListModification',
                content: 'Deleted an feedback'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('timesheetApp.feedback.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-feedback-delete-popup',
    template: ''
})
export class FeedbackDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private feedbackPopupService: FeedbackPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.feedbackPopupService
                .open(FeedbackDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
