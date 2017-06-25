import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { FeedbackComponent } from './feedback.component';
import { FeedbackDetailComponent } from './feedback-detail.component';
import { FeedbackPopupComponent } from './feedback-dialog.component';
import { FeedbackDeletePopupComponent } from './feedback-delete-dialog.component';

import { Principal } from '../../shared';

export const feedbackRoute: Routes = [
    {
        path: 'feedback',
        component: FeedbackComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.feedback.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'feedback/:id',
        component: FeedbackDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.feedback.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const feedbackPopupRoute: Routes = [
    {
        path: 'feedback-new',
        component: FeedbackPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.feedback.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'feedback/:id/edit',
        component: FeedbackPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.feedback.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'feedback/:id/delete',
        component: FeedbackDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.feedback.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
