package co.juan.nequi.api.constants;

import static co.juan.nequi.api.constants.ApiConstants.API_V1_BASE;
import static co.juan.nequi.api.constants.ApiConstants.ID_BRANCH_PATH_VARIABLE;

public final class ProductRoutes {

    private ProductRoutes() {
    }

    public static final String ID_PRODUCT_PATH_VARIABLE = "idProduct";

    private static final String PRODUCT_BASE = API_V1_BASE + "/product";

    public static final String SAVE_PRODUCT = PRODUCT_BASE;
    public static final String PRODUCT_BY_ID = PRODUCT_BASE + "/{" + ID_PRODUCT_PATH_VARIABLE + "}";
    public static final String UPDATE_PRODUCT_NAME = PRODUCT_BY_ID + "/name";
    public static final String PRODUCT_IN_BRANCH = PRODUCT_BY_ID + "/branch/{" + ID_BRANCH_PATH_VARIABLE + "}";
    public static final String DELETE_PRODUCT_FROM_BRANCH = PRODUCT_IN_BRANCH + "/delete";
    public static final String UPDATE_PRODUCT_STOCK = PRODUCT_IN_BRANCH + "/stock";
}
