import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TimesheetSharedModule } from '../../shared';
import {
    TimesheetService,
    TimesheetPopupService,
    TimesheetComponent,
    TimesheetDetailComponent,
    TimesheetDialogComponent,
    TimesheetPopupComponent,
    TimesheetDeletePopupComponent,
    TimesheetDeleteDialogComponent,
    timesheetRoute,
    timesheetPopupRoute,
    TimesheetAdminComponent,
} from './';

const ENTITY_STATES = [
    ...timesheetRoute,
    ...timesheetPopupRoute,
];

@NgModule({
    imports: [
        TimesheetSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TimesheetComponent,
        TimesheetDetailComponent,
        TimesheetDialogComponent,
        TimesheetDeleteDialogComponent,
        TimesheetPopupComponent,
        TimesheetDeletePopupComponent,
        TimesheetAdminComponent,
    ],
    entryComponents: [
        TimesheetComponent,
        TimesheetAdminComponent,
        TimesheetDialogComponent,
        TimesheetPopupComponent,
        TimesheetDeleteDialogComponent,
        TimesheetDeletePopupComponent,
    ],
    providers: [
        TimesheetService,
        TimesheetPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TimesheetTimesheetModule {}
