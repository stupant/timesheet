import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Entry } from './entry.model';
import { EntryService } from './entry.service';
@Injectable()
export class EntryPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private entryService: EntryService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.entryService.find(id).subscribe((entry) => {
                if (entry.day) {
                    entry.day = {
                        year: entry.day.getFullYear(),
                        month: entry.day.getMonth() + 1,
                        day: entry.day.getDate()
                    };
                }
                this.entryModalRef(component, entry);
            });
        } else {
            return this.entryModalRef(component, new Entry());
        }
    }

    openEntity(component: Component): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        return this.entryModalRef(component, this.entryService.entity);
    }

    entryModalRef(component: Component, entry: Entry): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.entry = entry;
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
