import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Feedback } from './feedback.model';
import { FeedbackService } from './feedback.service';

@Injectable()
export class FeedbackPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private feedbackService: FeedbackService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.feedbackService.find(id).subscribe((feedback) => {
                feedback.time = this.datePipe
                    .transform(feedback.time, 'yyyy-MM-ddThh:mm');
                this.feedbackModalRef(component, feedback);
            });
        } else {
            return this.feedbackModalRef(component, new Feedback());
        }
    }

    feedbackModalRef(component: Component, feedback: Feedback): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.feedback = feedback;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
