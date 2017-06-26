import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { EntryCategory } from './entry-category.model';
import { EntryCategoryPopupService } from './entry-category-popup.service';
import { EntryCategoryService } from './entry-category.service';

@Component({
    selector: 'jhi-entry-category-delete-dialog',
    templateUrl: './entry-category-delete-dialog.component.html'
})
export class EntryCategoryDeleteDialogComponent {

    entryCategory: EntryCategory;

    constructor(
        private entryCategoryService: EntryCategoryService,
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.entryCategoryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'entryCategoryListModification',
                content: 'Deleted an entryCategory'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('timesheetApp.entryCategory.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-entry-category-delete-popup',
    template: ''
})
export class EntryCategoryDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private entryCategoryPopupService: EntryCategoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.entryCategoryPopupService
                .open(EntryCategoryDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
