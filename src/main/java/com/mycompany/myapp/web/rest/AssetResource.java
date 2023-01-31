package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Asset;
import com.mycompany.myapp.repository.AssetRepository;
import com.mycompany.myapp.service.AssetService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Asset}.
 */
@RestController
@RequestMapping("/api")
public class AssetResource {

    private final Logger log = LoggerFactory.getLogger(AssetResource.class);

    private static final String ENTITY_NAME = "asset";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetService assetService;

    private final AssetRepository assetRepository;

    public AssetResource(AssetService assetService, AssetRepository assetRepository) {
        this.assetService = assetService;
        this.assetRepository = assetRepository;
    }

    /**
     * {@code POST  /assets} : Create a new asset.
     *
     * @param asset the asset to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new asset, or with status {@code 400 (Bad Request)} if the asset has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assets")
    public ResponseEntity<Asset> createAsset(@RequestBody Asset asset) throws URISyntaxException {
        log.debug("REST request to save Asset : {}", asset);
        if (asset.getId() != null) {
            throw new BadRequestAlertException("A new asset cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Asset result = assetService.save(asset);
        return ResponseEntity
            .created(new URI("/api/assets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /assets/:id} : Updates an existing asset.
     *
     * @param id the id of the asset to save.
     * @param asset the asset to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated asset,
     * or with status {@code 400 (Bad Request)} if the asset is not valid,
     * or with status {@code 500 (Internal Server Error)} if the asset couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/assets/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable(value = "id", required = false) final String id, @RequestBody Asset asset)
        throws URISyntaxException {
        log.debug("REST request to update Asset : {}, {}", id, asset);
        if (asset.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, asset.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Asset result = assetService.save(asset);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, asset.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /assets/:id} : Partial updates given fields of an existing asset, field will ignore if it is null
     *
     * @param id the id of the asset to save.
     * @param asset the asset to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated asset,
     * or with status {@code 400 (Bad Request)} if the asset is not valid,
     * or with status {@code 404 (Not Found)} if the asset is not found,
     * or with status {@code 500 (Internal Server Error)} if the asset couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/assets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Asset> partialUpdateAsset(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Asset asset
    ) throws URISyntaxException {
        log.debug("REST request to partial update Asset partially : {}, {}", id, asset);
        if (asset.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, asset.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Asset> result = assetService.partialUpdate(asset);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, asset.getId()));
    }

    /**
     * {@code GET  /assets} : get all the assets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assets in body.
     */
    @GetMapping("/assets")
    public List<Asset> getAllAssets() {
        log.debug("REST request to get all Assets");
        return assetService.findAll();
    }

    /**
     * {@code GET  /assets/:id} : get the "id" asset.
     *
     * @param id the id of the asset to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the asset, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assets/{id}")
    public ResponseEntity<Asset> getAsset(@PathVariable String id) {
        log.debug("REST request to get Asset : {}", id);
        Optional<Asset> asset = assetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(asset);
    }

    /**
     * {@code DELETE  /assets/:id} : delete the "id" asset.
     *
     * @param id the id of the asset to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assets/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable String id) {
        log.debug("REST request to delete Asset : {}", id);
        assetService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
