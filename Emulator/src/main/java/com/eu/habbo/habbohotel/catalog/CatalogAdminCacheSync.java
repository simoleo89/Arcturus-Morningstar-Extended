package com.eu.habbo.habbohotel.catalog;

import com.eu.habbo.Emulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Keeps the in-memory catalog cache aligned with catalog admin DB mutations
 * (draft edits before publish reload).
 */
public final class CatalogAdminCacheSync {
    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogAdminCacheSync.class);

    private CatalogAdminCacheSync() {
    }

    public static void reparentPage(CatalogPage page, int newParentId, int newOrderNum, CatalogPageType pageType) {
        if (page == null) return;

        CatalogManager catalogManager = Emulator.getGameEnvironment().getCatalogManager();
        int oldParentId = page.getParentId();

        if (oldParentId != newParentId) {
            CatalogPage oldParent = catalogManager.getCatalogPage(oldParentId, pageType);
            if (oldParent != null) {
                oldParent.getChildPages().remove(page.getId());
            }

            CatalogPage newParent = catalogManager.getCatalogPage(newParentId, pageType);
            if (newParent != null) {
                newParent.addChildPage(page);
            }

            page.setParentId(newParentId);
        }

        page.setOrderNum(newOrderNum);
    }

    public static void refreshPageFlagsFromDb(int pageId, CatalogPageType pageType) {
        CatalogManager catalogManager = Emulator.getGameEnvironment().getCatalogManager();
        CatalogPage page = catalogManager.getCatalogPage(pageId, pageType);
        if (page == null) return;

        String tableName = (pageType == CatalogPageType.BUILDER) ? "catalog_pages_bc" : "catalog_pages";
        String sql = "SELECT visible, enabled FROM " + tableName + " WHERE id = ? LIMIT 1";

        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pageId);

            try (ResultSet set = statement.executeQuery()) {
                if (!set.next()) return;

                page.setVisible("1".equals(set.getString("visible")) || set.getBoolean("visible"));
                page.setEnabled("1".equals(set.getString("enabled")) || set.getBoolean("enabled"));
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to refresh catalog page flags for page {}", pageId, e);
        }
    }

    public static void applyPageSave(
            CatalogPage page,
            String caption,
            String captionSave,
            String layout,
            int iconImage,
            int minRank,
            boolean visible,
            boolean enabled,
            int orderNum,
            int parentId,
            String headline,
            String teaser,
            String textDetails,
            String textOne,
            CatalogPageType catalogMode,
            CatalogPageType pageType
    ) {
        if (page == null) return;

        if (page.getParentId() != parentId) {
            reparentPage(page, parentId, orderNum, pageType);
        } else {
            page.setOrderNum(orderNum);
        }

        page.setCaption(caption);
        page.setPageName(captionSave);
        page.setLayout(layout);
        page.setIconImage(iconImage);
        page.setRank(minRank);
        page.setVisible(visible);
        page.setEnabled(enabled);
        page.setHeaderImage(headline);
        page.setTeaserImage(teaser);
        page.setTextDetails(textDetails);
        page.setTextOne(textOne);

        if (pageType != CatalogPageType.BUILDER) {
            page.setCatalogPageType(catalogMode);
        }
    }

    public static void detachDeletedPage(CatalogPage page, CatalogPageType pageType) {
        if (page == null) return;

        CatalogManager catalogManager = Emulator.getGameEnvironment().getCatalogManager();
        CatalogPage parent = catalogManager.getCatalogPage(page.getParentId(), pageType);

        if (parent != null) {
            parent.getChildPages().remove(page.getId());
        }

        catalogManager.getCatalogPagesMap(pageType).remove(page.getId());
    }

    public static boolean reloadCatalogItem(int offerId, CatalogPageType pageType) {
        CatalogManager catalogManager = Emulator.getGameEnvironment().getCatalogManager();
        CatalogItem existing = catalogManager.getCatalogItem(offerId, pageType);
        int previousPageId = existing != null ? existing.getPageId() : -1;

        String sql = (pageType == CatalogPageType.BUILDER)
                ? "SELECT * FROM catalog_items_bc WHERE id = ? LIMIT 1"
                : "SELECT * FROM catalog_items WHERE id = ? LIMIT 1";

        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, offerId);

            try (ResultSet set = statement.executeQuery()) {
                if (!set.next()) {
                    removeCatalogItem(offerId, pageType, previousPageId);
                    return false;
                }

                if (existing != null) {
                    if (previousPageId != set.getInt("page_id")) {
                        CatalogPage oldPage = catalogManager.getCatalogPage(previousPageId, pageType);
                        if (oldPage != null) {
                            oldPage.getCatalogItems().remove(offerId);
                        }
                    }

                    existing.update(set);
                    attachItemToPage(existing, pageType);
                    return true;
                }

                if ("0".equals(set.getString("item_ids"))) {
                    return false;
                }

                CatalogItem created = new CatalogItem(set);
                attachItemToPage(created, pageType);
                registerOfferSearchIndex(created, pageType);
                return true;
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to reload catalog item {}", offerId, e);
            return false;
        }
    }

    public static void removeCatalogItem(int offerId, CatalogPageType pageType, int pageIdHint) {
        CatalogManager catalogManager = Emulator.getGameEnvironment().getCatalogManager();
        CatalogItem item = catalogManager.getCatalogItem(offerId, pageType);

        if (item != null) {
            int searchOfferId = item.getSearchOfferId();
            if (searchOfferId != -1) {
                if (pageType == CatalogPageType.BUILDER) {
                    catalogManager.buildersClubOfferDefs.remove(searchOfferId);
                } else {
                    catalogManager.offerDefs.remove(searchOfferId);
                }
            }
        }

        int pageId = item != null ? item.getPageId() : pageIdHint;

        if (pageId > -1) {
            CatalogPage page = catalogManager.getCatalogPage(pageId, pageType);
            if (page != null) {
                page.getCatalogItems().remove(offerId);
            }
        }
    }

    private static void attachItemToPage(CatalogItem item, CatalogPageType pageType) {
        CatalogManager catalogManager = Emulator.getGameEnvironment().getCatalogManager();
        CatalogPage page = catalogManager.getCatalogPage(item.getPageId(), pageType);

        if (page == null) return;

        page.getCatalogItems().put(item.getId(), item);
    }

    private static void registerOfferSearchIndex(CatalogItem item, CatalogPageType pageType) {
        CatalogManager catalogManager = Emulator.getGameEnvironment().getCatalogManager();
        CatalogPage page = catalogManager.getCatalogPage(item.getPageId(), pageType);

        if (page == null) return;

        int searchOfferId = item.getSearchOfferId();
        if (searchOfferId != -1) {
            page.addOfferId(searchOfferId);

            if (pageType == CatalogPageType.BUILDER) {
                catalogManager.buildersClubOfferDefs.put(searchOfferId, item.getId());
            } else {
                catalogManager.offerDefs.put(searchOfferId, item.getId());
            }
        }
    }
}
