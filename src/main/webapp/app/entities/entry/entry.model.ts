export class Entry {
    constructor(
        public id?: string,
        public user?: string,
        public hour?: number,
        public notes?: any,
        public category?: string,
    ) {
    }
}