import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { Feedback } from './feedback.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class FeedbackService {

    private resourceUrl = 'api/feedbacks';

    entity: Feedback = new Feedback();
    entities: Feedback[];

    constructor(private http: Http, private dateUtils: JhiDateUtils) {
      this.entity = new Feedback();
      this.entities = [];
    }

    create(feedback: Feedback): Observable<Feedback> {
        const copy = this.convert(feedback);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(feedback: Feedback): Observable<Feedback> {
        const copy = this.convert(feedback);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: string): Observable<Feedback> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
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
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.time = this.dateUtils
            .convertDateTimeFromServer(entity.time);
    }

    private convert(feedback: Feedback): Feedback {
        const copy: Feedback = Object.assign({}, feedback);

        copy.time = this.dateUtils.toDate(feedback.time);
        return copy;
    }
}
