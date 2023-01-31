import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAsset } from '../asset.model';
import { AssetService } from '../service/asset.service';

@Component({
  templateUrl: './asset-delete-dialog.component.html',
})
export class AssetDeleteDialogComponent {
  asset?: IAsset;

  constructor(protected assetService: AssetService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.assetService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
