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


   constructor(private activatedRoute: ActivatedRoute,
    private router: Router,
    private http: HttpClient
   ) { }

   ngOnInit() {
    const state = history.state
    this.fileName = state.fileName
    this.result = state.result
   }
}
