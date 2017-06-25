import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TimesheetFeedbackModule } from './feedback/feedback.module';
import { TimesheetEntryCategoryModule } from './entry-category/entry-category.module';
import { TimesheetEntryModule } from './entry/entry.module';
import { TimesheetTimesheetModule } from './timesheet/timesheet.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        TimesheetFeedbackModule,
        TimesheetEntryCategoryModule,
        TimesheetEntryModule,
        TimesheetTimesheetModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TimesheetEntityModule {}
