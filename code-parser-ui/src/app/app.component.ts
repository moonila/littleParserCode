import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  selectedFile: File;
  productForm: FormGroup;
  result: string;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient
  ) { }

  ngOnInit() {
    this.createForm();
  }

  createForm() {
    this.productForm = this.fb.group({
      name: ['', [Validators.required]],
      result: this.result
    });
  }


  onFormSubmit() {
     console.log('start on form submit');
    const formData = new FormData();
    formData.append('file', this.selectedFile);
    this.http.post<string>('/parser-ui/api/file-upload', formData)
         .subscribe(data => {
                console.log('a log');
                console.log(data);
                this.result = JSON.stringify(data)
                },
                error => { console.error(error)});
    console.log('result', this.result);
  }

  onFileSelected(event) {
    this.selectedFile = <File>event.target.files[0];
  }

}
