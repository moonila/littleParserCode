import {NgModule} from "@angular/core";
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {DetailComponent} from "./detail/detail.component";

export const appRouteList: Routes = [
    { path: '', component: HomeComponent },
    { path: 'home', component: HomeComponent },
    { path: 'detail', component: DetailComponent }
];

@NgModule({
   exports: [
        RouterModule
      ],
    imports: [
        RouterModule.forRoot(appRouteList, {scrollPositionRestoration: 'enabled'})
    ]

})
export class AppRoutingModule {
}
