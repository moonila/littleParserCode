import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ParserService {

    constructor(private httpClient: HttpClient) {
    }

    postFile(formData: FormData): Observable<any> {
        return this.httpClient.post<any>(`/parser-ui/api/file-upload`, formData).pipe(
            catchError(this.handleError)
        );
    }

    private handleError(error: HttpErrorResponse) {
        let errorMessage = error.error.message;
        return throwError(errorMessage);
    };
}