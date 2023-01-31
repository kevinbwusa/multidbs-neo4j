package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Asset;
import com.mycompany.myapp.repository.AssetRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Asset}.
 */
@Service
public class AssetService {

    private final Logger log = LoggerFactory.getLogger(AssetService.class);

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    /**
     * Save a asset.
     *
     * @param asset the entity to save.
     * @return the persisted entity.
     */
    public Asset save(Asset asset) {
        log.debug("Request to save Asset : {}", asset);
        return assetRepository.save(asset);
    }

    /**
     * Partially update a asset.
     *
     * @param asset the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Asset> partialUpdate(Asset asset) {
        log.debug("Request to partially update Asset : {}", asset);

        return assetRepository
            .findById(asset.getId())
            .map(existingAsset -> {
                if (asset.getName() != null) {
                    existingAsset.setName(asset.getName());
                }

                return existingAsset;
            })
            .map(assetRepository::save);
    }

    /**
     * Get all the assets.
     *
     * @return the list of entities.
     */
    public List<Asset> findAll() {
        log.debug("Request to get all Assets");
        return assetRepository.findAll();
    }

    /**
     * Get one asset by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Asset> findOne(String id) {
        log.debug("Request to get Asset : {}", id);
        return assetRepository.findById(id);
    }

    /**
     * Delete the asset by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Asset : {}", id);
        assetRepository.deleteById(id);
    }
}
