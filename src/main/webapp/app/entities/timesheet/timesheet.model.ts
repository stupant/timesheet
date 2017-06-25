import { BaseEntity } from './../../shared';

export class Timesheet implements BaseEntity {
    constructor(
        public id?: string,
        public user?: string,
        public week?: number,
        public year?: number,
        public submitAt?: any,
        public updatedAt?: any,
        public updatedBy?: string,
        public summary?: any,
        public totalHours?: number,
        public status?: number,
    ) {
    }
}
