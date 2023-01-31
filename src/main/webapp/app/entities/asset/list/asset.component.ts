import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAsset } from '../asset.model';
import { AssetService } from '../service/asset.service';
import { AssetDeleteDialogComponent } from '../delete/asset-delete-dialog.component';

@Component({
  selector: 'jhi-asset',
  templateUrl: './asset.component.html',
})
export class AssetComponent implements OnInit {
  assets?: IAsset[];
  isLoading = false;

  constructor(protected assetService: AssetService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.assetService.query().subscribe(
      (res: HttpResponse<IAsset[]>) => {
        this.isLoading = false;
        this.assets = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAsset): string {
    return item.id!;
  }

  delete(asset: IAsset): void {
    const modalRef = this.modalService.open(AssetDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.asset = asset;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
