export interface IAsset {
  id?: string;
  name?: string | null;
}

export class Asset implements IAsset {
  constructor(public id?: string, public name?: string | null) {}
}

export function getAssetIdentifier(asset: IAsset): string | undefined {
  return asset.id;
}
