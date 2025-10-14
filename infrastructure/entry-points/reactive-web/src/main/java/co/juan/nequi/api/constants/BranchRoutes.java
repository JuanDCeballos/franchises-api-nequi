package co.juan.nequi.api.constants;

import static co.juan.nequi.api.constants.ApiConstants.API_V1_BASE;
import static co.juan.nequi.api.constants.ApiConstants.ID_BRANCH_PATH_VARIABLE;

public final class BranchRoutes {

    private BranchRoutes() {
    }

    private static final String BRANCH_BASE = API_V1_BASE + "/branch";

    public static final String SAVE_BRANCH = BRANCH_BASE;
    public static final String BRANCH_BY_ID = BRANCH_BASE + "/{" + ID_BRANCH_PATH_VARIABLE + "}";
    public static final String UPDATE_BRANCH_NAME = BRANCH_BY_ID + "/name";
}
