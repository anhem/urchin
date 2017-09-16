import {Actions} from "../constants";

const groupReducer = (state = {}, action) => {
    switch (action.type) {
        case Actions.Groups.GET_GROUPS:
            return {...state, isFetchingGroups: true};

        case Actions.Groups.GET_GROUPS_SUCCESS:
            return {...state, isFetchingGroups: false, groups: action.data};

        case Actions.Group.GET_GROUP:
            return {...state, isFetchingGroup: true};

        case Actions.Group.GET_GROUP_SUCCESS:
            return {...state, isFetchingGroup: false, group: action.data};

        case Actions.Group.SET_GROUP:
            return {...state, group: {...state.group, [action.data.field]: action.data.value}};

        case Actions.Group.SAVE_GROUP:
            return {...state, isSavingGroup: true};

        case Actions.Group.SAVE_GROUP_SUCCESS:
            return {...state, isSavingGroup: false};

        default:
            return state;
    }
};

export default groupReducer