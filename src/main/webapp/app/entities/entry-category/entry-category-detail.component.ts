import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { EntryCategory } from './entry-category.model';
import { EntryCategoryService } from './entry-category.service';

@Component({
    selector: 'jhi-entry-category-detail',
    templateUrl: './entry-category-detail.component.html'
})
export class EntryCategoryDetailComponent implements OnInit, OnDestroy {

    entryCategory: EntryCategory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private entryCategoryService: EntryCategoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEntryCategories();
    }

    load(id) {
        this.entryCategoryService.find(id).subscribe((entryCategory) => {
            this.entryCategory = entryCategory;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEntryCategories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'entryCategoryListModification',
            (response) => this.load(this.entryCategory.id)
        );
    }
}
