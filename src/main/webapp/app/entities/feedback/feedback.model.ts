import { BaseEntity } from './../../shared';

export class Feedback implements BaseEntity {
    constructor(
        public id?: string,
        public email?: string,
        public comment?: any,
        public time?: any,
    ) {
    }
}
