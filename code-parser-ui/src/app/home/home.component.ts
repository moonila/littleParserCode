import {Component, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{

   selectedFile: File;
   productForm: FormGroup;
   result: string;
   fileName: string;

   constructor(
     private router: Router,
     private fb: FormBuilder,
     private http: HttpClient
   ) { }

   ngOnInit() {
     this.createForm();
   }

 createForm() {
    this.productForm = this.fb.group({
      name: ['', [Validators.required]]
    });
  }


  onFormSubmit() {
     console.log('start on form submit');
    const formData = new FormData();
    formData.append('file', this.selectedFile);
    this.fileName = this.selectedFile.name;
    this.xhrCall('/parser-ui/api/file-upload', formData)
     .subscribe(
          res => {
            console.log("the response ");
            this.result = res.response;
          },
          error => console.log("fails")
    )
    this.router.navigate(['detail'], { state: { fileName: this.fileName, result: this.result } } )
    console.log("end")
  }

  xhrCall(url, formData) {
      return Observable.create(function (observer) {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
          if (xhr.readyState == 4) {
            if (xhr.status == 200) {
              observer.next(xhr);
            } else {
              observer.error(xhr);
            }
          }
        };
        xhr.open("POST", url, false);
        xhr.send(formData);
      });
  }


      onFileSelected(event) {
        this.selectedFile = <File>event.target.files[0];
      }
    }
