import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { EntryCategoryComponent } from './entry-category.component';
import { EntryCategoryDetailComponent } from './entry-category-detail.component';
import { EntryCategoryPopupComponent } from './entry-category-dialog.component';
import { EntryCategoryDeletePopupComponent } from './entry-category-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class EntryCategoryResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const entryCategoryRoute: Routes = [
    {
        path: 'entry-category',
        component: EntryCategoryComponent,
        resolve: {
            'pagingParams': EntryCategoryResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.entryCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'entry-category/:id',
        component: EntryCategoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.entryCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const entryCategoryPopupRoute: Routes = [
    {
        path: 'entry-category-new',
        component: EntryCategoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.entryCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'entry-category/:id/edit',
        component: EntryCategoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.entryCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'entry-category/:id/delete',
        component: EntryCategoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.entryCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
