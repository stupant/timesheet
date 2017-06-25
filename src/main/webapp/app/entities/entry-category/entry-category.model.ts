import { BaseEntity } from './../../shared';

export class EntryCategory implements BaseEntity {
    constructor(
        public id?: string,
        public name?: string,
    ) {
    }
}
