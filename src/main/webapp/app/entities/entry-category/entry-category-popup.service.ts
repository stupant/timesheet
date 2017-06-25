import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EntryCategory } from './entry-category.model';
import { EntryCategoryService } from './entry-category.service';

@Injectable()
export class EntryCategoryPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private entryCategoryService: EntryCategoryService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.entryCategoryService.find(id).subscribe((entryCategory) => {
                this.entryCategoryModalRef(component, entryCategory);
            });
        } else {
            return this.entryCategoryModalRef(component, new EntryCategory());
        }
    }

    entryCategoryModalRef(component: Component, entryCategory: EntryCategory): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.entryCategory = entryCategory;
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
