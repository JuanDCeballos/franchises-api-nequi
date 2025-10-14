package co.juan.nequi.api.constants;

import static co.juan.nequi.api.constants.ApiConstants.API_V1_BASE;

public final class FranchiseRoutes {

    private FranchiseRoutes() {
    }

    public static final String ID_FRANCHISE_PATH_VARIABLE = "idFranchise";

    private static final String FRANCHISE_BASE = API_V1_BASE + "/franchise";

    public static final String SAVE_FRANCHISE = FRANCHISE_BASE;
    public static final String FRANCHISE_BY_ID = FRANCHISE_BASE + "/{" + ID_FRANCHISE_PATH_VARIABLE + "}";
    public static final String TOP_STOCK_BY_FRANCHISE = FRANCHISE_BY_ID + "/top-stock";
    public static final String UPDATE_FRANCHISE_NAME = FRANCHISE_BY_ID + "/name";
}
