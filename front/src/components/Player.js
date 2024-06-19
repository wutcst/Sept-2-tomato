import React from 'react';

const Player = ({ player }) => {
    return (
        <div>
            <h2>{player.name}</h2>
            <p>Current weight: {player.currentWeight}/{player.maxWeight}</p>
            <div>
                <h3>Items carried:</h3>
                {player.items.map(item => (
                    <div key={item.name}>{item.name} - {item.weight}</div>
                ))}
            </div>
        </div>
    );
};

export default Player;
