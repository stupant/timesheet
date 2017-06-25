import * as moment from 'moment';
export class Timesheet {
    today = new Date();
    dow = ['sun', 'mon', 'tue', 'wed', 'thu', 'fri', 'sat'];
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
    ) {
        this.hasDate(new Date());
    }
    hasDate(d: Date) {
        this.today = d;
        this.year = d.getFullYear();
        this.week = moment(d.toString()).week();
    }
    setDateByWeek(w: number, y: number) {
        this.today = this.getDate(w, y);
        this.week = w;
        this.year = y;
    }
    getDate(w: number, y: number): Date {
        return moment().week(w).year(y).toDate();
    }
    getWeekNumber(d: Date): number {
        return moment(d.toString).week();
    }
}
