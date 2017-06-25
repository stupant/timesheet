import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { EntryCategory } from './entry-category.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class EntryCategoryService {

    private resourceUrl = 'api/entry-categories';

    constructor(private http: Http) { }

    create(entryCategory: EntryCategory): Observable<EntryCategory> {
        const copy = this.convert(entryCategory);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(entryCategory: EntryCategory): Observable<EntryCategory> {
        const copy = this.convert(entryCategory);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: string): Observable<EntryCategory> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: string): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(entryCategory: EntryCategory): EntryCategory {
        const copy: EntryCategory = Object.assign({}, entryCategory);
        return copy;
    }
}
