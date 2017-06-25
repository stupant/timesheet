import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TimesheetSharedModule } from '../../shared';
import {
    EntryCategoryService,
    EntryCategoryPopupService,
    EntryCategoryComponent,
    EntryCategoryDetailComponent,
    EntryCategoryDialogComponent,
    EntryCategoryPopupComponent,
    EntryCategoryDeletePopupComponent,
    EntryCategoryDeleteDialogComponent,
    entryCategoryRoute,
    entryCategoryPopupRoute,
    EntryCategoryResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...entryCategoryRoute,
    ...entryCategoryPopupRoute,
];

@NgModule({
    imports: [
        TimesheetSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        EntryCategoryComponent,
        EntryCategoryDetailComponent,
        EntryCategoryDialogComponent,
        EntryCategoryDeleteDialogComponent,
        EntryCategoryPopupComponent,
        EntryCategoryDeletePopupComponent,
    ],
    entryComponents: [
        EntryCategoryComponent,
        EntryCategoryDialogComponent,
        EntryCategoryPopupComponent,
        EntryCategoryDeleteDialogComponent,
        EntryCategoryDeletePopupComponent,
    ],
    providers: [
        EntryCategoryService,
        EntryCategoryPopupService,
        EntryCategoryResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TimesheetEntryCategoryModule {}
