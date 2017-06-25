import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TimesheetSharedModule } from '../../shared';
import {
    FeedbackService,
    FeedbackPopupService,
    FeedbackComponent,
    FeedbackDetailComponent,
    FeedbackDialogComponent,
    FeedbackPopupComponent,
    FeedbackDeletePopupComponent,
    FeedbackDeleteDialogComponent,
    feedbackRoute,
    feedbackPopupRoute,
} from './';

const ENTITY_STATES = [
    ...feedbackRoute,
    ...feedbackPopupRoute,
];

@NgModule({
    imports: [
        TimesheetSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        FeedbackComponent,
        FeedbackDetailComponent,
        FeedbackDialogComponent,
        FeedbackDeleteDialogComponent,
        FeedbackPopupComponent,
        FeedbackDeletePopupComponent,
    ],
    entryComponents: [
        FeedbackComponent,
        FeedbackDialogComponent,
        FeedbackPopupComponent,
        FeedbackDeleteDialogComponent,
        FeedbackDeletePopupComponent,
    ],
    providers: [
        FeedbackService,
        FeedbackPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TimesheetFeedbackModule {}
