import {Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'app-detail',
    templateUrl: './detail.component.html',
    styleUrls: ['./detail.component.css']
})
export class DetailComponent implements OnInit {
    result: string;
    fileName: string;
    statistics: any;
    show: boolean = false;
    showJsonButton: any = 'Show Json';

    constructor(private router: Router) {
    }

    ngOnInit() {
        const state = history.state;
        this.fileName = state.fileName;
        this.result = JSON.stringify(state.result.nodeBean, null, 2);
        this.statistics = state.result.kindList;
    }

    goHome(event) {
        this.router.navigate(['home'])
    }

    toggle() {
        this.show = !this.show;

        // Change the name of the button.
        if (this.show)
            this.showJsonButton = "Hide";
        else
            this.showJsonButton = "Show Json";
    }
}
