import React from 'react';
import {shallow} from 'enzyme'
import toJson from 'enzyme-to-json';
import {FolderListContainer} from "./FolderListContainer";

describe('FolderListContainer', () => {

    let props = {
        getFolders: jest.fn(),
    };

    it('match snapshot', () => {
        expect(toJson(shallow(<FolderListContainer {...props}/>))).toMatchSnapshot();
    });
});
