export class Entry {
    public today = new Date();
    constructor(
        public id?: string,
        public user?: string,
        public hour?: number,
        public notes?: any,
        public category?: string,
        public day?: any,
    ) {
        this.hour = 1;
    }
}
