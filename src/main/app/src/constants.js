export const Actions = {
    Auth: {
        LOGIN: "LOGIN",
        LOGIN_SUCCESS: "LOGIN_SUCCESS",
        LOGIN_INVALID: "LOGIN_INVALID",
        LOGIN_FAILED: "LOGIN_FAILED",
        LOGOUT_SUCCESS: "LOGOUT_SUCCESS",
        PRECONDITION_FAILED: "PRECONDITION_FAILED"
    },
    Users: {
        GET_USERS: "GET_USERS",
        GET_USERS_SUCCESS: "GET_USERS_SUCCESS"
    },
    User: {
        GET_USER: "GET_USER",
        GET_USER_SUCCESS: "GET_USER_SUCCESS",
        SET_USER: "SET_USER",
        SAVE_USER: "SAVE_USER",
        SAVE_USER_SUCCESS: "SAVE_USER_SUCCESS",
        SAVE_USER_VALIDATION_ERROR: "SAVE_USER_VALIDATION_ERROR",
        DELETE_USER: "DELETE_USER",
        DELETE_USER_SUCCESS: "DELETE_USER_SUCCESS",
        GET_GROUPS_FOR_USER: "GET_GROUPS_FOR_USER",
        GET_GROUPS_FOR_USER_SUCCESS: "GET_GROUPS_FOR_USER_SUCCESS",
        ADD_GROUP: "ADD_GROUP_TO_USER",
        ADD_GROUP_SUCCESS: "ADD_GROUP_TO_USER_SUCCESS",
        REMOVE_GROUP: "REMOVE_GROUP",
        REMOVE_GROUP_SUCCESS: "REMOVE_GROUP_SUCCESS",
    },
    Groups: {
        GET_GROUPS: "GET_GROUPS",
        GET_GROUPS_SUCCESS: "GET_GROUPS_SUCCESS"
    },
    Group: {
        GET_GROUP: "GET_GROUP",
        GET_GROUP_SUCCESS: "GET_GROUP_SUCCESS",
        SET_GROUP: "SET_GROUP",
        SAVE_GROUP: "SAVE_GROUP",
        SAVE_GROUP_SUCCESS: "SAVE_GROUP_SUCCESS",
        SAVE_GROUP_VALIDATION_ERROR: "SAVE_GROUP_VALIDATION_ERROR",
        DELETE_GROUP: "DELETE_GROUP",
        DELETE_GROUP_SUCCESS: "DELETE_GROUP_SUCCESS",
        GET_USERS_FOR_GROUP: "GET_USERS_FOR_GROUP",
        GET_USERS_FOR_GROUP_SUCCESS: "GET_USERS_FOR_GROUP_SUCCESS",
        ADD_USER: "ADD_USER_TO_GROUP",
        ADD_USER_SUCCESS: "ADD_USER_TO_GROUP_SUCCESS",
        REMOVE_USER: "REMOVE_USER",
        REMOVE_USER_SUCCESS: "REMOVE_USER_SUCCESS",
    },
    Folders: {
        GET_FOLDERS: "GET_FOLDERS",
        GET_FOLDERS_SUCCESS: "GET_FOLDERS_SUCCESS"
    },
    Folder: {
        GET_FOLDER: "GET_FOLDER",
        GET_FOLDER_SUCCESS: "GET_FOLDER_SUCCESS",
        SET_FOLDER: "SET_FOLDER",
        SAVE_FOLDER: "SAVE_FOLDER",
        SAVE_FOLDER_SUCCESS: "SAVE_FOLDER_SUCCESS",
        SAVE_FOLDER_VALIDATION_ERROR: "SAVE_FOLDER_VALIDATION_ERROR",
        DELETE_FOLDER: "DELETE_FOLDER",
        DELETE_FOLDER_SUCCESS: "DELETE_FOLDER_SUCCESS",
        SET_CONFIRM_NEW_ENCRYPTED_FOLDER: "SET_CONFIRM_NEW_ENCRYPTED_FOLDER",
        CONFIRM_NEW_ENCRYPTED_FOLDER: "CONFIRM_NEW_ENCRYPTED_FOLDER",
        CONFIRM_NEW_ENCRYPTED_FOLDER_SUCCESS: "CONFIRM_NEW_ENCRYPTED_FOLDER_SUCCESS",
    },
    ACL: {
        GET_ACL: "GET_ACL",
        GET_ACL_SUCCESS: "GET_ACL_SUCCESS",
        UPDATE_GROUP_ACL: "UPDATE_GROUP_ACL",
        UPDATE_GROUP_ACL_SUCCESS: "UPDATE_GROUP_ACL_SUCCESS",
        UPDATE_USER_ACL: "UPDATE_USER_ACL",
        UPDATE_USER_ACL_SUCCESS: "UPDATE_USER_ACL_SUCCESS"
    },
    Admin: {
        SET_ADMIN: "SET_ADMIN",
        SETUP_ADMIN: "SETUP_ADMIN",
        SETUP_ADMIN_SUCCESS: "SETUP_ADMIN_SUCCESS",
        SETUP_ADMIN_VALIDATION_ERROR: "SETUP_ADMIN_VALIDATION_ERROR"
    },
    Site: {
        TOGGLE_NAVIGATION: "TOGGLE_NAVIGATION",
    }
};

export const ErrorCodes = {
    VALIDATION_ERROR: 'VALIDATION_ERROR'
};
