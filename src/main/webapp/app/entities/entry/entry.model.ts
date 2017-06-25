import { BaseEntity } from './../../shared';

export class Entry implements BaseEntity {
    constructor(
        public id?: string,
        public user?: string,
        public hour?: number,
        public notes?: any,
        public category?: string,
        public day?: any,
    ) {
    }
}
