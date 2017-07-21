import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TimesheetComponent } from './timesheet.component';
import { TimesheetAdminComponent } from './timesheet-admin.component';
import { TimesheetDetailComponent } from './timesheet-detail.component';
import { TimesheetPopupComponent } from './timesheet-dialog.component';
import { TimesheetDeletePopupComponent } from './timesheet-delete-dialog.component';
import { TimesheetEntryComponent } from './timesheet-entry.component';

import { Principal } from '../../shared';

export const timesheetRoute: Routes = [
    {
        path: 'timesheet',
        component: TimesheetEntryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.timesheet.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'timesheet/:year/:week',
        component: TimesheetEntryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.timesheet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'timesheet-admin',
        component: TimesheetAdminComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'timesheetApp.timesheet.home.admin'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'timesheet/:id',
        component: TimesheetDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.timesheet.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const timesheetPopupRoute: Routes = [
    {
        path: 'timesheet-new',
        component: TimesheetPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.timesheet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'timesheet/:id/edit',
        component: TimesheetPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.timesheet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'timesheet/:id/delete',
        component: TimesheetDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.timesheet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
