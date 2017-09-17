import history from '../history'
import {Actions} from '../constants'
import {get, post, del} from './restClient'

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

export const getGroupData = (groupId) => (dispatch) => {
    dispatch({
        type: Group.GET_GROUP
    });
    get('/api/groups/' + groupId)
        .then(json => dispatch({
            type: Group.GET_GROUP_SUCCESS,
            data: json
        }))
};

export const setGroupData = (group) => (dispatch) => {
    dispatch({
        type: Group.SET_GROUP,
        data: group
    });
};

export const saveGroupData = (groupId, group) => (dispatch) => {
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
        }, json => (
            console.log('failed: ' + json)
        ))
};

export const deleteGroupData = (groupId) => (dispatch) => {
    dispatch({
        type: Group.DELETE_GROUP
    });
    del('/api/groups/' + groupId)
        .then(json => {
            dispatch({
                type: Group.DELETE_GROUP_SUCCESS,
                data: json
            });
            history.push('/groups')
        }, json => (
            console.log('failed: ' + json)
        ))
};