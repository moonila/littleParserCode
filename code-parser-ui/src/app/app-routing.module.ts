import {NgModule} from "@angular/core";
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {DetailComponent} from "./detail/detail.component";

export const appRouteList: Routes = [
    {
        path: 'home',
        component: HomeComponent
    },
    {
       path: 'detail',
       component: DetailComponent
    },
    {
        path: '',
        redirectTo: 'home'
    }
];

@NgModule({
   exports: [
          RouterModule
      ],
      imports: [
          RouterModule.forRoot(appRouteList)
      ]

})
export class AppRoutingModule {
}
