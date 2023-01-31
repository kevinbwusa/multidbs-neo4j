jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAsset, Asset } from '../asset.model';
import { AssetService } from '../service/asset.service';

import { AssetRoutingResolveService } from './asset-routing-resolve.service';

describe('Asset routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AssetRoutingResolveService;
  let service: AssetService;
  let resultAsset: IAsset | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AssetRoutingResolveService);
    service = TestBed.inject(AssetService);
    resultAsset = undefined;
  });

  describe('resolve', () => {
    it('should return IAsset returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAsset = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultAsset).toEqual({ id: 'ABC' });
    });

    it('should return new IAsset if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAsset = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAsset).toEqual(new Asset());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Asset })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAsset = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultAsset).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
