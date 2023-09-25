import {TestBed, async, inject, fakeAsync, tick} from "@angular/core/testing";
import {AppModule} from "./app.module";
import {AppRoutingModule} from "./app-routing.module";
import {AppComponent} from "./app.component"
import {Router} from "@angular/router";
import {Location} from "@angular/common";
import {TEST_BASE_HREF_PROVIDER} from "./app-module.spec";

describe('AppRoutingModule', () => {
  let appRoutingModule: AppRoutingModule;
  let location: Location;
  let router: Router;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
          imports: [ AppModule ],
          providers: [TEST_BASE_HREF_PROVIDER]
        })
        .compileComponents();

    router = TestBed.get(Router);
    location = TestBed.get(Location);
    appRoutingModule = new AppRoutingModule();
    router.initialNavigation();
  }));


  it('should create an instance', () => {
    expect(appRoutingModule).toBeTruthy();
  });
});
