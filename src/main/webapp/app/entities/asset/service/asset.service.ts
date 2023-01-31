import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAsset, getAssetIdentifier } from '../asset.model';

export type EntityResponseType = HttpResponse<IAsset>;
export type EntityArrayResponseType = HttpResponse<IAsset[]>;

@Injectable({ providedIn: 'root' })
export class AssetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/assets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(asset: IAsset): Observable<EntityResponseType> {
    return this.http.post<IAsset>(this.resourceUrl, asset, { observe: 'response' });
  }

  update(asset: IAsset): Observable<EntityResponseType> {
    return this.http.put<IAsset>(`${this.resourceUrl}/${getAssetIdentifier(asset) as string}`, asset, { observe: 'response' });
  }

  partialUpdate(asset: IAsset): Observable<EntityResponseType> {
    return this.http.patch<IAsset>(`${this.resourceUrl}/${getAssetIdentifier(asset) as string}`, asset, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IAsset>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAsset[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAssetToCollectionIfMissing(assetCollection: IAsset[], ...assetsToCheck: (IAsset | null | undefined)[]): IAsset[] {
    const assets: IAsset[] = assetsToCheck.filter(isPresent);
    if (assets.length > 0) {
      const assetCollectionIdentifiers = assetCollection.map(assetItem => getAssetIdentifier(assetItem)!);
      const assetsToAdd = assets.filter(assetItem => {
        const assetIdentifier = getAssetIdentifier(assetItem);
        if (assetIdentifier == null || assetCollectionIdentifiers.includes(assetIdentifier)) {
          return false;
        }
        assetCollectionIdentifiers.push(assetIdentifier);
        return true;
      });
      return [...assetsToAdd, ...assetCollection];
    }
    return assetCollection;
  }
}
