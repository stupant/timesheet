import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Timesheet } from './timesheet.model';
import { TimesheetService } from './timesheet.service';
@Injectable()
export class TimesheetPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private timesheetService: TimesheetService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.timesheetService.find(id).subscribe((timesheet) => {
                timesheet.submitAt = this.datePipe
                    .transform(timesheet.submitAt, 'yyyy-MM-ddThh:mm');
                timesheet.updatedAt = this.datePipe
                    .transform(timesheet.updatedAt, 'yyyy-MM-ddThh:mm');
                this.timesheetModalRef(component, timesheet);
            });
        } else {
            return this.timesheetModalRef(component, new Timesheet());
        }
    }

    timesheetModalRef(component: Component, timesheet: Timesheet): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.timesheet = timesheet;
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
