import React, { createContext, useState } from 'react';

export const GameContext = createContext();

export const GameProvider = ({ children }) => {
    const [player, setPlayer] = useState(null);
    const [rooms, setRooms] = useState({});
    const [currentRoom, setCurrentRoom] = useState(null);

    return (
        <GameContext.Provider value={{
            player, setPlayer,
            rooms, setRooms,
            currentRoom, setCurrentRoom,
        }}>
            {children}
        </GameContext.Provider>
    );
};
