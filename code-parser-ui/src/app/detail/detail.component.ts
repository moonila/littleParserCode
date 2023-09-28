import {Component, OnInit} from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';


@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent implements OnInit{
   result: string;
   fileName: string;
   stats: string;


   constructor(private activatedRoute: ActivatedRoute,
    private router: Router,
    private http: HttpClient
   ) { }

   ngOnInit() {
    const state = history.state
    this.fileName = state.fileName
    var jsonObject : any = JSON.parse(state.result)
    this.result = JSON.stringify(jsonObject.nodeBean, null, 2)
    this.stats = JSON.stringify(jsonObject.kindList)
   }

   goHome(event) {
      this.router.navigate(['home'])
   }
}
