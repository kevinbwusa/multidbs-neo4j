import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAsset, Asset } from '../asset.model';
import { AssetService } from '../service/asset.service';

@Injectable({ providedIn: 'root' })
export class AssetRoutingResolveService implements Resolve<IAsset> {
  constructor(protected service: AssetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAsset> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((asset: HttpResponse<Asset>) => {
          if (asset.body) {
            return of(asset.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Asset());
  }
}
