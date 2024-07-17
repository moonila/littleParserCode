import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {Observable} from 'rxjs';
import {ParserService} from "../services/parser.service";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

    selectedFile: File;
    productForm: FormGroup;
    result: string;
    fileName: string;
    errorMessage:'';

    constructor(
        private router: Router,
        private fb: FormBuilder,
        private parserService: ParserService
    ) {
    }

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
        this.parserService.postFile(formData).subscribe({
                next: (data) => {
                    this.result = data;
                    this.router.navigate(['/detail'], {state: {fileName: this.fileName, result: data}});
                },
                error: (error) => {
                    this.errorMessage = error;
                },
                complete: () => {
                    console.log('file uploaded.');
                }
            }
        );
    }

    onFileSelected(event) {
        this.selectedFile = <File>event.target.files[0];
    }
}
