import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { Timesheet } from './timesheet.model';
import { TimesheetPopupService } from './timesheet-popup.service';
import { TimesheetService } from './timesheet.service';

@Component({
    selector: 'jhi-timesheet-delete-dialog',
    templateUrl: './timesheet-delete-dialog.component.html'
})
export class TimesheetDeleteDialogComponent {

    timesheet: Timesheet;

    constructor(
        private timesheetService: TimesheetService,
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.timesheetService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'timesheetListModification',
                content: 'Deleted an timesheet'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('timesheetApp.timesheet.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-timesheet-delete-popup',
    template: ''
})
export class TimesheetDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private timesheetPopupService: TimesheetPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.timesheetPopupService
                .open(TimesheetDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
