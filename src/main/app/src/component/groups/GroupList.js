import React, {Component} from "react";

class GroupList extends Component {

    render() {
        let groups = this.props.groups || [];
        let groupItems = groups.map((item, index) =>
            <li key={index.toString()}>
                <a href={`/groups/${item.groupId}`}>{item.groupName}</a>
            </li>
        );
        return (
            <div>
                <h2>grouplist</h2>
                <ul>
                    {groupItems}
                </ul>
                <a href="/groups/0">new group</a>
            </div>
        )
    }
}

export default GroupList