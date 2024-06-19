import React from 'react';

const Item = ({ item }) => {
    return (
        <div>
            <p>{item.name} - {item.description} (Weight: {item.weight})</p>
        </div>
    );
};

export default Item;
