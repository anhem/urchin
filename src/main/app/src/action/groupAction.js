import history from '../history'
import {Actions, ErrorCodes} from '../constants'
import {del, get, post} from './restClient'
import {notifyBackendError, notifySuccess} from "./notificationAction";

const {Groups} = Actions;
const {Group} = Actions;

export const getGroups = () => (dispatch) => {
    dispatch({
        type: Groups.GET_GROUPS
    });
    get('/api/groups')
        .then(json => dispatch({
            type: Groups.GET_GROUPS_SUCCESS,
            data: json
        }))
};

export const getGroup = (groupId) => (dispatch) => {
    dispatch({
        type: Group.GET_GROUP
    });
    get('/api/groups/' + groupId)
        .then(json => dispatch({
            type: Group.GET_GROUP_SUCCESS,
            data: json
        }))
};

export const setGroup = (group) => (dispatch) => {
    dispatch({
        type: Group.SET_GROUP,
        data: group
    });
};

export const createGroup = (group) => (dispatch) => {
    dispatch({
        type: Group.SAVE_GROUP
    });
    post('/api/groups/add', group)
        .then(json => {
                dispatch({
                    type: Group.SAVE_GROUP_SUCCESS,
                    data: json
                });
                history.push('/groups');
                notifySuccess("Success", "Group created")
            }, error => {
                if (error.errorCode === ErrorCodes.VALIDATION_ERROR) {
                    dispatch({
                        type: Group.SAVE_GROUP_VALIDATION_ERROR,
                        data: error
                    });
                } else {
                    notifyBackendError(error)
                }
            }
        )
};

export const deleteGroup = (groupId) => (dispatch) => {
    dispatch({
        type: Group.DELETE_GROUP
    });
    del('/api/groups/' + groupId)
        .then(json => {
            dispatch({
                type: Group.DELETE_GROUP_SUCCESS,
                data: json
            });
            history.push('/groups');
            notifySuccess("Success", "Group deleted")
        }, error => (
            notifyBackendError(error)
        ))
};

export const getUsersForGroup = (groupId) => (dispatch) => {
    dispatch({
        type: Group.GET_USERS_FOR_GROUP
    });
    get('/api/groups/' + groupId + "/users")
        .then(json => dispatch({
            type: Group.GET_USERS_FOR_GROUP_SUCCESS,
            data: json
        }))
};

export const addUser = (groupId, userId) => (dispatch) => {
    let body = {
        userId: userId,
        groupId: groupId
    };
    dispatch({
        type: Group.ADD_USER
    });
    post('/api/groups/user', body)
        .then(json => {
            dispatch({
                type: Group.ADD_USER_SUCCESS,
                data: json
            });
            notifySuccess("Success", "User added to group");
            dispatch(getUsersForGroup(groupId));
        }, error => (
            notifyBackendError(error)
        ))
};

export const removeUser = (groupId, userId) => (dispatch) => {
    dispatch({
        type: Group.REMOVE_USER
    });
    del('/api/groups/' + groupId + "/user/" + userId)
        .then(json => {
            dispatch({
                type: Group.REMOVE_USER_SUCCESS,
                data: json
            });
            notifySuccess("Success", "User was removed from group");
            dispatch(getUsersForGroup(groupId));
        }, error => (
            notifyBackendError(error)
        ))
};