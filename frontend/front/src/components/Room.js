import React from 'react';
import Item from './Item';

const Room = ({ room }) => {
    return (
        <div>
            <h2>{room.name}</h2>
            <p>{room.description}</p>
            <div>
                <h3>Items in this room:</h3>
                {room.items.map(item => (
                    <Item key={item.name} item={item} />
                ))}
            </div>
        </div>
    );
};

export default Room;
